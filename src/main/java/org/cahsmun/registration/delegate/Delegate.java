package org.cahsmun.registration.delegate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*; // Entity and table annotations
import java.util.Date;

@Entity
@Data // Lombok specific annotation; makes it create getters and setters
@Table(name="delegate", schema="public") // Mapping this entity to the delegate table
public class Delegate {

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

    @Column(name="assignment")
    private String assignment;

    @Column(name="haspaid") // IDK why i made this camel case (along with waiverLink) and everything else is underscored -- its a personality trait?
    private int has_paid;

    @Column(name="waiverLink")
    private String waiver_link;

    @Column(name="waiver")
    private byte[] waiver;

    @CreationTimestamp
    @Column(name = "last_update")
    @Setter(AccessLevel.NONE)
    private Date last_updated;
}
