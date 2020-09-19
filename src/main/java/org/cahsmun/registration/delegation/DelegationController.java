package org.cahsmun.registration.delegation;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin()
@RestController
@Slf4j
public class DelegationController {

    private final String SENDGRID_API="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";

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

    @GetMapping("/delegations/{delegation_id}")
    public Delegation findById(@PathVariable long delegation_id) {
        return delegationRepository.findById(delegation_id)
                .orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegation_id));
    }

    @PutMapping("/delegation/enable/{delegation_id}")
    public Delegation enableDelegation(@PathVariable long delegation_id) {
        Delegation delegationFromDB = delegationRepository.findById(delegation_id)
                .orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegation_id));

        delegationFromDB.setEnabled(1);

        return delegationRepository.save(delegationFromDB);
    }

    // TODO: (1) Delegation editing, (2) Delegation deleting

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
    public Delegation createDelegation(@Valid @RequestBody DelegationInfo delegationInfo) throws UserExistException, IOException {
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

        // STEPS 2 & 3
        Sponsor createdSponsor = sponsorRepository.save(new Sponsor(delegationInfo));
        Delegation createdDelegation = delegationRepository.save(new Delegation(delegationInfo));

        createdDelegation.setHead_id(createdSponsor.getSponsor_id());
        createdDelegation.setRegistrant_position("Sponsor Teacher/School Advisor");
        createdSponsor.setDelegation_id(createdDelegation.getDelegation_id());

        sponsorRepository.save(createdSponsor);
        delegationRepository.save(createdDelegation);



        Mail mail = new Mail();
        Personalization personalization = new Personalization();
        mail.setFrom(new Email("delegates@cahsmun.org"));
        mail.setTemplateId("d-b877b83734b24152b02ae61e6b8b64fa");

        personalization.addDynamicTemplateData("full_name", delegationInfo.getName());
        personalization.addDynamicTemplateData("login_email", delegationInfo.getEmail());
        personalization.addDynamicTemplateData("login_passcode", delegationInfo.getPassword());

        personalization.addDynamicTemplateData("school_name", delegationInfo.getSchool_name());
        personalization.addDynamicTemplateData("school_address", delegationInfo.getAddress_one());
        personalization.addDynamicTemplateData("school_city", delegationInfo.getCity());
        personalization.addDynamicTemplateData("school_province", delegationInfo.getProvince());
        personalization.addDynamicTemplateData("school_postal", delegationInfo.getPostal_code());

        personalization.addTo(new Email(delegationInfo.getEmail()));
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(SENDGRID_API);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }



        Mail mail2 = new Mail();
        Personalization personalization2 = new Personalization();
        mail.setFrom(new Email("it@cahsmun.org"));
        mail.setTemplateId("d-a7dfc49a044f483eaa2014fec09fc275");

        personalization2.addDynamicTemplateData("full_name", delegationInfo.getName());
        personalization2.addDynamicTemplateData("school_name", delegationInfo.getSchool_name());

        personalization2.addTo(new Email("it@cahsmun.org")); // TODO: For production, replace with delegates@cahsmun.org
        mail.addPersonalization(personalization);

        SendGrid sg2 = new SendGrid(SENDGRID_API);
        Request request2 = new Request();
        try {
            request2.setMethod(Method.POST);
            request2.setEndpoint("mail/send");
            request2.setBody(mail2.build());
            Response response = sg2.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }



        if(user != null) {
            return createdDelegation;
        } else throw new UserExistException("User is empty. Something went wrong. Wouldn't like to be you... you know, needing to fix this issue and all...");
    }

    // Registering AS DELEGATE
    @PostMapping("/delegations/delegate/register")
    public Delegation createDelegationAsDelegate(@Valid @RequestBody DelegationInfo delegationInfo) throws UserExistException, IOException {
        /*
            Three steps:
            1. Create user account (used for login)
            2. Create (partial) Delegate (just email, password, and name)
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

        // STEPS 2 & 3
        Delegate createdDelegate = delegateRepository.save(new Delegate(delegationInfo));
        Delegation createdDelegation = delegationRepository.save(new Delegation(delegationInfo));

        createdDelegation.setHead_id(createdDelegate.getDelegate_id());
        createdDelegation.setRegistrant_position("Head Delegate");
        createdDelegate.setDelegationId(createdDelegation.getDelegation_id());

        delegateRepository.save(createdDelegate);
        delegationRepository.save(createdDelegation);



        Mail mail = new Mail();
        Personalization personalization = new Personalization();
        mail.setFrom(new Email("delegates@cahsmun.org"));
        mail.setTemplateId("d-b877b83734b24152b02ae61e6b8b64fa");

        personalization.addDynamicTemplateData("full_name", delegationInfo.getName());
        personalization.addDynamicTemplateData("login_email", delegationInfo.getEmail());
        personalization.addDynamicTemplateData("login_passcode", delegationInfo.getPassword());

        personalization.addDynamicTemplateData("school_name", delegationInfo.getSchool_name());
        personalization.addDynamicTemplateData("school_address", delegationInfo.getAddress_one());
        personalization.addDynamicTemplateData("school_city", delegationInfo.getCity());
        personalization.addDynamicTemplateData("school_province", delegationInfo.getProvince());
        personalization.addDynamicTemplateData("school_postal", delegationInfo.getPostal_code());

        personalization.addTo(new Email(delegationInfo.getEmail()));
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(SENDGRID_API);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }



        Mail mail2 = new Mail();
        Personalization personalization2 = new Personalization();
        mail.setFrom(new Email("it@cahsmun.org"));
        mail.setTemplateId("d-a7dfc49a044f483eaa2014fec09fc275");

        personalization2.addDynamicTemplateData("full_name", delegationInfo.getName());
        personalization2.addDynamicTemplateData("school_name", delegationInfo.getSchool_name());

        personalization2.addTo(new Email("it@cahsmun.org")); // TODO: For production, replace with delegates@cahsmun.org
        mail.addPersonalization(personalization);

        SendGrid sg2 = new SendGrid(SENDGRID_API);
        Request request2 = new Request();
        try {
            request2.setMethod(Method.POST);
            request2.setEndpoint("mail/send");
            request2.setBody(mail2.build());
            Response response = sg2.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }



        if(user != null) {
            return createdDelegation;
        } else throw new UserExistException("User is empty. Something went wrong. Wouldn't like to be you... you know, needing to fix this issue and all...");
    }
}
