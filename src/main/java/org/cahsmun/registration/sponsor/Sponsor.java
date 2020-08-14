package org.cahsmun.registration.sponsor;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cahsmun.registration.delegation.DelegationInfo;
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

    @Column(name="delegationId")
    private long delegation_id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="name")
    private String name;

    @Column(name="phone_number")
    private String phone_number;

    @CreationTimestamp
    @Column(name = "last_update")
    @Setter(AccessLevel.NONE)
    private Date last_update;

    /*
    public Sponsor(SponsorInfo sponsorInfo) {
        this.email = sponsorInfo.getEmail();
        this.password = sponsorInfo.getPassword();
        this.name = sponsorInfo.getName();
    }
    */

    public Sponsor(DelegationInfo delegationInfo) {
        this.email = delegationInfo.getEmail();
        this.password = delegationInfo.getPassword();
        this.name = delegationInfo.getName();
        this.phone_number = delegationInfo.getPhone_number();
    }
}
