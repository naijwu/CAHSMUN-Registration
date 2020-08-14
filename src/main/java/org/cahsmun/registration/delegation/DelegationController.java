package org.cahsmun.registration.delegation;

import lombok.extern.slf4j.Slf4j;
import org.cahsmun.registration.authentication.UserExistException;
import org.cahsmun.registration.delegate.Delegate;
import org.cahsmun.registration.delegate.DelegateRepository;
import org.cahsmun.registration.role.Role;
import org.cahsmun.registration.sponsor.Sponsor;
import org.cahsmun.registration.sponsor.SponsorRepository;
import org.cahsmun.registration.user.User;
import org.cahsmun.registration.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin()
@RestController
@Slf4j
public class DelegationController {

    @Resource
    DelegationRepository delegationRepository;

    @Resource
    SponsorRepository sponsorRepository;

    @Resource
    UserRepository userRepository;

    @Resource
    DelegateRepository delegateRepository;

    @Bean
    public PasswordEncoder delegationPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @GetMapping("/delegations")
    public List<Delegation> retrieveAllDelegations() {
        return StreamSupport.stream(delegationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/delegations/{delegationId}")
    public Delegation findById(@PathVariable long delegation_id) {
        return delegationRepository.findById(delegation_id)
                .orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegation_id));
    }

    /*
    @PostMapping("/{sponsor_id}/delegations")
    public Delegation createDelegation(@PathVariable long sponsor_id, @Valid @RequestBody Delegation delegation) throws UserExistException {

        Sponsor sponsor = sponsorRepository.findById(sponsor_id).orElseThrow(() -> new ResourceNotFoundException("Sponsor not found with ID: " + sponsor_id));

        Delegation newDelegation = delegationRepository.save(delegation);

        sponsor.setDelegation_id(newDelegation.getDelegation_id());
        sponsorRepository.save(sponsor);

        return newDelegation;
    }
    */

    // Registering AS SPONSOR
    @PostMapping("/delegations/sponsor/register")
    public Delegation createDelegation(@Valid @RequestBody DelegationInfo delegationInfo) throws UserExistException {
        /*
            Three steps:
            1. Create user account (used for login)
            2. Create sponsor teacher (inputted in database)
            3. Create delegation w/ sponsor teacher information (sponsor_id) (inputted in database)
         */

        // STEP ONE
        User user = new User(delegationInfo);
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            throw new UserExistException(
                    "There is an account with that email address: " + user.getUsername());
        }

        user.setUsername(user.getUsername());
        user.setPassword(delegationPasswordEncoder().encode(user.getPassword()));

        if (user.getRoles().size() == 0) {
            List<Role> roles = new ArrayList<Role>();
            roles.add(new Role("SPONSOR"));
            user.setRoles(new HashSet<Role>(roles));
        } else {
            user.setRoles(user.getRoles());
        }
        userRepository.save(user);

        // STEP TWO
        sponsorRepository.save(new Sponsor(delegationInfo));
        Sponsor createdSponsor = sponsorRepository.findByEmail(delegationInfo.getEmail());

        // STEP THREE
        Delegation createdDelegation = delegationRepository.save(new Delegation(delegationInfo));
        createdDelegation.setHead_id(createdSponsor.getSponsor_id());

        if(user != null) {
            return createdDelegation;
        } else throw new UserExistException("User is empty. Something went wrong. Wouldn't like to be you... you know, needing to fix this issue and all...");
    }

    // Registering AS DELEGATE
    @PostMapping("/delegations/sponsor/register")
    public Delegation createDelegationAsDelegate(@Valid @RequestBody DelegationInfo delegationInfo) throws UserExistException {
        /*
            Three steps:
            1. Create user account (used for login)
            2. Create (partial) Delegate (just email and password)
            3. Create delegation w/ sponsor_teacher information (inputted in database)
         */

        // STEP ONE
        User user = new User(delegationInfo);
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            throw new UserExistException(
                    "There is an account with that email address: " + user.getUsername());
        }

        user.setUsername(user.getUsername());
        user.setPassword(delegationPasswordEncoder().encode(user.getPassword()));

        if (user.getRoles().size() == 0) {
            List<Role> roles = new ArrayList<Role>();
            roles.add(new Role("HEAD DELEGATE"));
            user.setRoles(new HashSet<Role>(roles));
        } else {
            user.setRoles(user.getRoles());
        }
        userRepository.save(user);

        // STEP TWO
        delegateRepository.save(new Delegate(delegationInfo));
        Delegate createdDelegate = delegateRepository.findByEmail(delegationInfo.getEmail());

        // STEP THREE
        Delegation createdDelegation = delegationRepository.save(new Delegation(delegationInfo));
        createdDelegation.setHead_id(createdDelegate.getDelegate_id()); // THIS WILL HAVE TO CONTINUE ON DELEGATE REGISTRATION

        if(user != null) {
            return createdDelegation;
        } else throw new UserExistException("User is empty. Something went wrong. Wouldn't like to be you... you know, needing to fix this issue and all...");
    }
}
