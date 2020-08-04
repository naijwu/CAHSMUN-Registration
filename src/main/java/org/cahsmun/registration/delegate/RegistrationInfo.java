package org.cahsmun.registration.delegate;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RegistrationInfo implements Serializable {

    private String email;
    private String password;
    private String name;
    private int age;
    private String school;
    private String gender;
    private String phone_number;
    private Date date_of_birth;
    private int grade;
    private String address;
    private String city;
    private String province;
    private String ec_name;
    private String ec_phone_number;
    private String ec_relationship;
    private String past_experience;
    private String pref_first_comm;
    private String pref_first_country;
    private String pref_second_comm;
    private String pref_second_country;
    private String pref_third_comm;
    private String pref_third_country;
    private int payment_status;
    private String waiver_link;
    private byte[] waiver;
    private long assignment_id;
    private long delegation_id;
    private long rooming_id;
    private boolean enabled;
}