package org.cahsmun.registration.authentication;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Set;

@Data
public class JwtResponse implements Serializable {

    // the information that will be present/available for the front-end

    private static final long serialVersionUID = -8091879091924046844L;
    private int status;
    private String message;
    private String token;
    private String name;
    private String email;
    private boolean logged;
    private Set<? extends GrantedAuthority> roles;

    public JwtResponse(boolean logged, int status, String message, String email, String token, String name, Set<? extends GrantedAuthority> roles) {
        this.logged = logged;
        this.status = status;
        this.message = message;
        this.email = email;
        this.token = token;
        this.name = name;
        this.roles = roles;
    }
}
