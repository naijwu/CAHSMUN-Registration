package org.cahsmun.registration.role;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "role", schema = "public")
public class Role implements Serializable, GrantedAuthority {


    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(name = "authority")
    private String authority;

    @CreationTimestamp
    @Column(name = "last_update")
    @Setter(AccessLevel.NONE)
    private Date last_updated;

    public Role() {
    }

    public Role(String role) {
        authority = role;
    }

//
//    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonIgnore
//    private Set<UserRole> userRoleSet = new HashSet<>();

//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users = new HashSet<>();

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Role role = (Role) o;
//        return user_id == role.user_id &&
//                Objects.equals(role_name, role.role_name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(user_id, role_name);
//    }
}
