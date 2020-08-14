package org.cahsmun.registration.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cahsmun.registration.delegate.RegistrationInfo;
import org.cahsmun.registration.delegation.DelegationInfo;
import org.cahsmun.registration.role.Role;
import org.cahsmun.registration.sponsor.SponsorInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "users", schema = "public")
public class User implements Serializable {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    // Email
    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name="enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "userrole", joinColumns = {
            @JoinColumn(name = "user_id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    public User(RegistrationInfo registrationInfo) {
        this.username = registrationInfo.getEmail();
        this.name = registrationInfo.getName();
        this.password = registrationInfo.getPassword();
        this.enabled = true;
    }

    /*
    public User(SponsorInfo sponsorInfo) {
        this.username = sponsorInfo.getEmail();
        this.name = sponsorInfo.getName();
        this.password = sponsorInfo.getPassword();
        this.enabled = true;
    }
    */

    public User(DelegationInfo delegationInfo) {
        this.username = delegationInfo.getEmail();
        this.name = delegationInfo.getName();
        this.password = delegationInfo.getPassword();
        this.enabled = true;
    }
}
