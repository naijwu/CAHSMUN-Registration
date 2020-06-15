package org.cahsmun.registration.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

@CrossOrigin
@RestController
public class AuthenticationController {
    /*
        Test in postman: POST, Header: Content-Type, application/json, Body:
        {
	        "username" : "user",
	        "password" : "name"
        }
     */

    @Resource
    private AuthenticationManager authenticationManager;

    /* When data passed in from the front end, it takes the authentication and creates an authentication; results in authenticate success or fail,
     * and then its sent to SecurityContextHolder (Spring Security) as its authentication status, as well as the user that's been authenticated
     */
    @PostMapping("/login")
    public Credentials createAuthentication(@Valid @RequestBody Credentials credentials) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                // passing ID and password
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new Credentials(credentials.getUsername(), new Date());
    }
}
