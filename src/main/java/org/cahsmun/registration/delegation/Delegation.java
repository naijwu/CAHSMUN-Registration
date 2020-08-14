package org.cahsmun.registration.delegation;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "delegation", schema = "public")
@NoArgsConstructor
public class Delegation implements Serializable {

    @Id
    @Column(name="delegation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long delegation_id;

    @Column(name="head_id")
    private long head_id;

    @Column(name="name")
    private String name;

    @Column(name="admin_email")
    private String admin_email; // only filled in if person registering delegation is not a teacher/advisor, else on the front-end is filled in as the sponsor's account's email

    @Column(name="address_one")
    private String address_one;

    @Column(name="address_two")
    private String address_two;

    @Column(name="city")
    private String city;

    @Column(name="province")
    private String province;

    @Column(name="postal_code")
    private String postal_code;

    @Column(name="school_district")
    private String school_district;

    @Column(name="additional_information")
    private String additional_information;

    @Column(name="expected_size")
    private int expected_size;

    @Column(name="has_permission")
    private int has_permission; // using int instead of boolean to avoid any strict differences between spring and postgres

    @Column(name="sponsor_present")
    private int sponsor_present;

    @Column(name="enabled")
    private int enabled;

    @Column(name="registrant_position")
    private String registrant_position;

    @CreationTimestamp
    @Column(name="last_update")
    @Setter(AccessLevel.NONE)
    private Date last_update;


    public Delegation(DelegationInfo delegationInfo) {
        this.name = delegationInfo.getSchool_name();
        this.admin_email = delegationInfo.getAdmin_email();
        this.address_one = delegationInfo.getAddress_one();
        this.address_two = delegationInfo.getAddress_two();
        this.city = delegationInfo.getCity();
        this.province = delegationInfo.getProvince();
        this.postal_code = delegationInfo.getPostal_code();
        this.school_district = delegationInfo.getSchool_district();
        this.additional_information = delegationInfo.getAdditional_information();
        this.expected_size = delegationInfo.getExpected_size();
        this.has_permission = delegationInfo.getHas_permission(); // should always be true from the front end anyways... more like a EULA type beat
        this.sponsor_present = delegationInfo.getSponsor_present();
        this.enabled = 0; // always initially false -- has to be activated by DA to activate delegation (allow delegation name to show up on the delegates registration form)
        this.registrant_position = delegationInfo.getRegistrant_position();
    }
}
