package org.cahsmun.registration.delegation;

import lombok.Data;
import org.cahsmun.registration.role.Role;

import java.io.Serializable;

@Data
public class DelegationInfo implements Serializable {

    // all these values (except role) is taken in by the delegation registration form.

    // "School Information"
    private String school_name; // school_name isn't the name used in the Delegation class as well as the Delegation table (just 'name')
    private String address_one;
    private String address_two;
    private String city;
    private String province;
    private String postal_code;
    private String school_district;

    private int has_permission;

    private String registrant_position;

    // "Account Information"
    private String name;
    private String phone_number;
    private String email;
    private String password;

    // "Additional Information"
    private int sponsor_present;
    private int expected_size;
    private String admin_email;
    private String additional_information;

    // internal
    private int enabled;

    private Role role;
}
