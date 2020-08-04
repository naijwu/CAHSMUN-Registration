package org.cahsmun.registration.sponsor;

import lombok.Data;

import java.io.Serializable;

@Data
public class SponsorInfo implements Serializable {

    private String email;
    private String password;
    private String name;
    private String school;
    private String gender;
}
