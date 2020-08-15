package org.cahsmun.registration.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185122326007488L;


    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);

    @Value("${jwt.signing.key.secret}")
    private String secret;

    @Value("${jwt.token.expiration.in.seconds}")
    private Long expiration;

    public String getUserEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = decodeJWT(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Decode JWT using our secret defined in application.properties
     *
     * @param token
     * @return
     */
    private Claims decodeJWT(String token) {
        Claims claims = Jwts.parser().
                setSigningKey(DatatypeConverter.parseBase64Binary(secret)).
                parseClaimsJws(token).getBody();
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(now);
    }

    /**
     * Generate Token for the user. Email is ID.
     * <p>
     * while creating the token -
     * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
     * 2. Sign the JWT using the HS256 algorithm and secret key.
     * 3. According to JWS Compact Serialization(https://bit.ly/321Ui4I)
     * compaction of the JWT to a URL-safe string
     * <p>
     * IMPORTANT: Spring Security role name prefix "ROLE_".
     * https://bit.ly/2N9uTC6
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {

        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        // claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        claims.put("role", userDetails.getAuthorities());

        return createJWT(claims, userDetails.getUsername());
    }


    private String createJWT(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        // We need a signing key, so we'll create one just for this example. Usually
        // the key would be read from your application configuration instead.
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signedKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://cahsmun.org")
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(signatureAlgorithm, signedKey)
                .compact();
    }


    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userEmailFromToken = getUserEmailFromToken(token);
        return (userEmailFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
