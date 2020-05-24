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

    // Input Values -- What will be on the registration form

    @Column(name="full_name")
    private String full_name;

    @Column(name="email")
    private String email;

    @Column(name="date_of_birth")
    private Date date_of_birth;

    // TODO: Declare the variables for the rest of the columns


    // Internal Values

    @Column(name="assigned_country")
    private String assigned_country;

    @Column(name="assigned_committee")
    private String assigned_committee;

    @Column(name="has_paid")
    private boolean has_paid;

    @CreationTimestamp
    @Column(name = "last_update")
    @Setter(AccessLevel.NONE)
    private Date last_updated;
}
