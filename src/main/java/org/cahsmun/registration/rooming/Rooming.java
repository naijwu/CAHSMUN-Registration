package org.cahsmun.registration.rooming;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "rooming", schema = "public")
public class Rooming implements Serializable {

    @Id
    @Column(name="rooming_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rooming_id;

    @Column(name="delegation_id")
    private long delegation_id;

    @Column(name="sponsor_id")
    private long sponsor_id;

    @Column(name="room_gender")
    private String room_gender;

    @Column(name="delegate_one")
    private long delegate_one;

    @Column(name="delegate_two")
    private long delegate_two;

    @Column(name="delegate_three")
    private long delegate_three;

    @Column(name="delegate_four")
    private long delegate_four;

    @CreationTimestamp
    @Column(name="last_update")
    @Setter(AccessLevel.NONE)
    private Date last_update;

}
