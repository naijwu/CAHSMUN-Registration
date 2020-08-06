package org.cahsmun.registration.sponsor;

import org.cahsmun.registration.authentication.UserExistException;
import org.cahsmun.registration.role.Role;
import org.cahsmun.registration.role.RoleRepository;
import org.cahsmun.registration.sponsor.SponsorInfo;
import org.cahsmun.registration.user.User;
import org.cahsmun.registration.user.UserRepository;
import org.cahsmun.registration.user.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin()
@RestController
public class SponsorController {


    @Resource
    SponsorRepository sponsorRepository;

    /*
    @Resource
    UserServiceImpl userService;
    */

    @Resource
    UserRepository userRepository;

    @Resource
    RoleRepository roleRepository;

    @Bean
    public PasswordEncoder sponsorPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @GetMapping("/sponsors")
    public List<Sponsor> retrieveAllSponsors() {
        return StreamSupport.stream(sponsorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @PutMapping("/sponsors/{sponsor_id}")
    public Sponsor updateSponsor(@PathVariable long sponsor_id, @Valid @RequestBody Sponsor sponsor) {
        Sponsor sponsorFromDB = sponsorRepository.findById(sponsor_id).orElseThrow(() -> new ResourceNotFoundException("Sponsor not found with ID " + sponsor_id));

        sponsorFromDB.setName(sponsor.getName());
        sponsorFromDB.setSchool(sponsor.getSchool());
        sponsorFromDB.setGender(sponsor.getGender());

        return sponsorRepository.save(sponsorFromDB);
    }

    @PostMapping("/sponsorregistration")
    public Sponsor createSponsor(@Valid @RequestBody SponsorInfo sponsorInfo) throws UserExistException {
        // User user = userService.save(new User(sponsorInfo)); // returns User object that has just been made

        User user = new User(sponsorInfo);

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            throw new UserExistException(
                    "There is an account with that email address: " + user.getUsername());
        }

        user.setUsername(user.getUsername());
        user.setPassword(sponsorPasswordEncoder().encode(user.getPassword()));

        if (user.getRoles().size() == 0) {
            List<Role> roles = new ArrayList<Role>();
            roles.add(new Role("SPONSOR"));
            user.setRoles(new HashSet<Role>(roles));
        } else {
            user.setRoles(user.getRoles());
        }

        userRepository.save(user);

        if(user != null) {
            return sponsorRepository.save(new Sponsor(sponsorInfo));
        } else throw new UserExistException("User is empty. Something went wrong.");
    }

    @GetMapping("/sponsors/{sponsor_id}") // Returning a specific delegate
    public Sponsor findById(@PathVariable long sponsor_id) {
        return sponsorRepository.findById(sponsor_id)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor not found with ID: " + sponsor_id));
    }

    @DeleteMapping("/sponsors/{sponsor_id}")
    public void delete(@PathVariable long sponsor_id) {
        sponsorRepository.findById(sponsor_id)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor not found with ID: " + sponsor_id));
        sponsorRepository.deleteById(sponsor_id);
    }
}
