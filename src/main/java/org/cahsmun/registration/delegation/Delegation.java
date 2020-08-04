package org.cahsmun.registration.delegation;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "delegation", schema = "public")
public class Delegation implements Serializable {

    @Id
    @Column(name="delegation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long delegation_id;

    @Column(name="sponsor_id")
    private long sponsor_id;

    @Column(name="name")
    private String name;

    @CreationTimestamp
    @Column(name="last_update")
    @Setter(AccessLevel.NONE)
    private String last_update;
}
