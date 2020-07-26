package org.cahsmun.registration.authentication;

import org.cahsmun.registration.user.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin()
@RestController
@Slf4j
@RequestMapping(value = "/token")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private org.cahsmun.registration.authentication.JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserServiceImpl userServiceImpl;


    /**
     * @param loginRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/generate-token", method = RequestMethod.POST)
    public JwtResponse createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
        // Step 1: Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );

        // Step 2: Load the user
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserPrincipal userPrincipal = userServiceImpl.loadUserPrincipalByEmail(loginRequest.getEmail());

        // Step 3: Create JWT
        log.info("createAuthenticationToken: " + userPrincipal.getUsername());
        return new JwtResponse(true, 200,
                "success",
                userPrincipal.getUsername(),
                jwtTokenUtil.generateToken(userPrincipal),
                userPrincipal.getFullName(),
                (Set) userPrincipal.getAuthorities()
        );
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public JwtResponse logout() throws AuthenticationException {
        return new JwtResponse(false, 200, "success", null, null, null, null);
    }
}
