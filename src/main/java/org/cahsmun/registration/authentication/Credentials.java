package org.cahsmun.registration.authentication;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Credentials implements Serializable {

    // Data transfer object

    private static final long serialVersionUID = 5926468583005150707L;

    private String username; // user email
    private String password;
    private Date datetime;

    public Credentials(String username, Date datetime) {
        this.username = username;
        this.datetime = datetime;
        this.password = "";
    }
}
