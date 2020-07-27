package org.cahsmun.registration.user;


import org.cahsmun.registration.authentication.UserExistException;
import org.cahsmun.registration.authentication.UserPrincipal;
import org.cahsmun.registration.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    @Resource
    UserRepository userRepository;


    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    /**
     * This method is used in AuthenticationController
     *
     * @param email
     * @return
     * @throws ResourceNotFoundException
     */
    public UserPrincipal loadUserPrincipalByEmail(String email) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(email);

        if (user == null) {
            throw new UsernameNotFoundException("User email cannot be found in the system.");
        }

        //String email, String fullName, String password, Collection<? extends GrantedAuthority> authorities
        return new UserPrincipal(email, user.getName(), "", getAuthority(user));
    }

    /**
     * Used in JwtRequestFilter
     *
     * @param email
     * @return
     * @throws ResourceNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(email);
        if (user == null) {
            throw new UsernameNotFoundException("User email cannot be found in the system.");
        }

        log.info("************************* " + user.getUsername() + " PW: " + user.getPassword());
        log.info("************ role: " + getAuthority(user));

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthority(user))
                .build();
    }

    private Set getAuthority(User user) {
        Set authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getAuthority()));
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        });
        return authorities;
    }

    /**
     * All users will be assigned "USER" role by default
     *
     * @param user
     * @return
     * @throws UserExistException
     */
    public User save(User user) throws UserExistException {

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            throw new UserExistException(
                    "There is an account with that email address: " + user.getUsername());
        }
        
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder().encode(user.getPassword()));

        if (user.getRoles().size() == 0) {
            List<Role> roles = new ArrayList<Role>();
            roles.add(new Role("USER"));
            user.setRoles(new HashSet<Role>(roles));
        } else {
            user.setRoles(user.getRoles());
        }

        return userRepository.save(user);
    }
}
