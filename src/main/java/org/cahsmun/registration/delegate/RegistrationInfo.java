package org.cahsmun.registration.delegate;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegistrationInfo implements Serializable {

    private String email;
    private String password;
    private String name;
    private String age;
    private String school;
    private boolean enabled;
}