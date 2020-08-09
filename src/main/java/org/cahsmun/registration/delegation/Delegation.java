package org.cahsmun.registration.delegation;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "delegation", schema = "public")
public class Delegation implements Serializable {

    @Id
    @Column(name="delegationId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long delegation_id;

    @Column(name="sponsor_id")
    private long sponsor_id;

    @Column(name="name")
    private String name;

    @CreationTimestamp
    @Column(name="last_update")
    @Setter(AccessLevel.NONE)
    private Date last_update;
}
