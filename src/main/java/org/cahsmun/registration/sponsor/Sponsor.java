package org.cahsmun.registration.sponsor;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="sponsor", schema="public")
public class Sponsor {

    @Id
    @Column(name="sponsor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sponsor_id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="name")
    private String name;

    @Column(name="school")
    private String school;

    @CreationTimestamp
    @Column(name = "last_update")
    @Setter(AccessLevel.NONE)
    private Date last_update;
}
