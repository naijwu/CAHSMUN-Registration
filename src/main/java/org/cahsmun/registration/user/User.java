package org.cahsmun.registration.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cahsmun.registration.delegate.RegistrationInfo;
import org.cahsmun.registration.role.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "user", schema = "public")
public class User implements Serializable {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String username;

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
        this.email = registrationInfo.getEmail();
        this.username = registrationInfo.getName();
        this.password = registrationInfo.getPassword();
        this.enabled = true;
    }
}
