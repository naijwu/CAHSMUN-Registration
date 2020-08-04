package org.cahsmun.registration.sponsor;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="sponsor", schema="public")
@NoArgsConstructor
public class Sponsor {

    @Id
    @Column(name="sponsor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sponsor_id;

    @Column(name="delegation_id")
    private long delegation_id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="name")
    private String name;

    @Column(name="school")
    private String school;

    @Column(name="gender")
    private String gender;

    @CreationTimestamp
    @Column(name = "last_update")
    @Setter(AccessLevel.NONE)
    private Date last_update;

    public Sponsor(SponsorInfo sponsorInfo) {
        this.email = sponsorInfo.getEmail();
        this.password = sponsorInfo.getPassword();
        this.name = sponsorInfo.getName();
        this.school = sponsorInfo.getSchool();
        this.gender = sponsorInfo.getGender();
    }
}
