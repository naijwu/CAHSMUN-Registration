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
    private int secretariat_id;

    @Column(name="full_name")
    private String full_name;

    @Column(name="position")
    private String postion;

    @CreationTimestamp
    @Column(name="last_updated")
    @Setter(AccessLevel.NONE)
    private Date last_updated;
}
