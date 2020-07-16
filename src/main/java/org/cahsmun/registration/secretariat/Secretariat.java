package org.cahsmun.registration.secretariat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data // Lombok specific annotation; makes it create getters and setters
@Table(name="secretariat", schema="public") // Mapping this entity to the delegate table
public class Secretariat {

    @Id
    @Column(name="secretariat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long secretariat_id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="name")
    private String name;

    @Column(name="position")
    private String position;

    @Column(name="school")
    private String school;

    @Column(name="phone_number")
    private String phone_number;

    @CreationTimestamp
    @Column(name="last_update")
    @Setter(AccessLevel.NONE)
    private Date last_update;
}
