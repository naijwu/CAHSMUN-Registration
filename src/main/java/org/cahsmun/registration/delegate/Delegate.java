package org.cahsmun.registration.delegate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cahsmun.registration.delegation.DelegationInfo;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*; // Entity and table annotations
import java.io.Serializable;
import java.util.Date;

@Entity
@Data // Lombok specific annotation; makes it create getters and setters
@Table(name="delegate", schema="public") // Mapping this entity to the delegate table
@NoArgsConstructor
public class Delegate implements Serializable {

    // Note on convention: Variable naming scheme is not the typical camel case, as to match w/ column names in the database

    @Id
    @Column(name="delegate_id") // maps the variable to this column -- must be the column name of the database
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generates value for the primary key | strategy: how it gets its value from db
    private long delegate_id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="name")
    private String name;

    @Column(name="age")
    private int age;

    @Column(name="school")
    private String school;

    @Column(name="gender")
    private String gender;

    @Column(name="phone_number")
    private String phone_number;

    @Column(name="date_of_birth")
    private Date date_of_birth;

    @Column(name="grade")
    private int grade;

    @Column(name="address")
    private String address;

    @Column(name="city")
    private String city;

    @Column(name="province")
    private String province;

    @Column(name="ec_name")
    private String ec_name;

    @Column(name="ec_phone_number")
    private String ec_phone_number;

    @Column(name="ec_relationship")
    private String ec_relationship;

    @Column(name="past_experience")
    private String past_experience;

    @Column(name="pref_first_comm")
    private String pref_first_comm;

    @Column(name="pref_first_country")
    private String pref_first_country;

    @Column(name="pref_second_comm")
    private String pref_second_comm;

    @Column(name="pref_second_country")
    private String pref_second_country;

    @Column(name="pref_third_comm")
    private String pref_third_comm;

    @Column(name="pref_third_country")
    private String pref_third_country;

    @Column(name="payment_status")
    private int payment_status;

    @Column(name="waiver_link")
    private String waiver_link;

    @Column(name="waiver")
    private byte[] waiver;

    @Column(name="assignment_id")
    private long assignment_id;

    @Column(name="delegationid") // postgresql is not case sensitive? what?
    private long delegationId;

    @Column(name="is_head")
    private int is_head;

    @Column(name="payment_id")
    private String payment_id;

    // @Column(name="rooming_id")
    // private long rooming_id;

    @CreationTimestamp
    @Column(name="last_update")
    @Setter(AccessLevel.NONE)
    private Date last_update;

    public Delegate(RegistrationInfo registrationInfo) { // REGISTRATION VIA REGISTRATION FORM
        this.email = registrationInfo.getEmail();
        this.password = registrationInfo.getPassword();
        this.name = registrationInfo.getName();
        this.age = registrationInfo.getAge();
        this.school = registrationInfo.getSchool();
        this.gender = registrationInfo.getGender();
        this.phone_number = registrationInfo.getPhone_number();
        this.date_of_birth = registrationInfo.getDate_of_birth();
        this.grade = registrationInfo.getGrade();
        this.address = registrationInfo.getAddress();
        this.city = registrationInfo.getCity();
        this.province = registrationInfo.getProvince();
        this.ec_name = registrationInfo.getEc_name();
        this.ec_phone_number = registrationInfo.getEc_phone_number();
        this.ec_relationship = registrationInfo.getEc_relationship();
        this.past_experience = registrationInfo.getPast_experience();
        this.pref_first_comm = registrationInfo.getPref_first_comm();
        this.pref_first_country = registrationInfo.getPref_first_country();
        this.pref_second_comm = registrationInfo.getPref_second_comm();
        this.pref_second_country = registrationInfo.getPref_second_country();
        this.pref_third_comm = registrationInfo.getPref_third_comm();
        this.pref_third_country = registrationInfo.getPref_third_country();
        this.payment_status = registrationInfo.getPayment_status();
        this.waiver_link = registrationInfo.getWaiver_link();
        this.waiver = registrationInfo.getWaiver();
        this.assignment_id = registrationInfo.getAssignment_id();
        this.delegationId = registrationInfo.getDelegationId();
        this.is_head = 0;
        // this.rooming_id = registrationInfo.getRooming_id();
    }

    public Delegate(DelegationInfo delegationInfo) { // REGISTRATION VIA DELEGATION FORM (Will have to complete registration via registration form (PUT MAPPING)
        this.email = delegationInfo.getEmail();
        this.password = delegationInfo.getPassword();
        this.is_head = 1;
        this.name = delegationInfo.getName();
    }
}
