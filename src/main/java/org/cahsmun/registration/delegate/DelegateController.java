package org.cahsmun.registration.delegate;

import com.jayway.jsonpath.JsonPath;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.cahsmun.registration.authentication.UserExistException;
import org.cahsmun.registration.user.UserRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.cahsmun.registration.user.User;
import org.cahsmun.registration.user.UserServiceImpl;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin() // Will be for eventually the front end when it calls on the data
@RestController
@Slf4j
public class DelegateController {

    private final String SENDGRID_API="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";

    @Resource
    DelegateRepository delegateRepository;

    @Resource
    UserServiceImpl userService;

    @Resource
    UserRepository userRepository;

    // CRUD Methods
    @GetMapping("/delegates") // Returns list of all delegates; will be used by DA
    public List<Delegate> retrieveAllDelegates() {
        return StreamSupport.stream(delegateRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/delegates/{delegate_id}") // Returning a specific delegate
    public Delegate findById(@PathVariable long delegate_id) {
        return delegateRepository.findById(delegate_id)
                .orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id));
    }

    @GetMapping("/delegates/email/{email}") // Returning a specific delegate
    public Delegate findByEmail(@PathVariable String email) {
        return delegateRepository.findByEmail(email);
    }

    @GetMapping("/delegates/delegation/{delegationId}") // Returning a specific delegate
    public List<Delegate> retrieveAllDelegatesFromDelegation(@PathVariable long delegationId) {
        return StreamSupport.stream(delegateRepository.findByDelegationId(delegationId).spliterator(), false)
                .collect(Collectors.toList());
    }


    @PostMapping("/registration")
    public Delegate createDelegate(@Valid @RequestBody RegistrationInfo registrationInfo) throws UserExistException, IOException {
        User user = userService.save(new User(registrationInfo)); // returns User object that has just been made


        Mail mail = new Mail();
        Personalization personalization = new Personalization();
        mail.setFrom(new Email("it@cahsmun.org"));
        mail.setTemplateId("d-66a3d695111747af80148e83bcf6c328");

        personalization.addDynamicTemplateData("full_name", registrationInfo.getName());
        personalization.addDynamicTemplateData("login_email", registrationInfo.getEmail());
        personalization.addDynamicTemplateData("login_passcode", registrationInfo.getPassword());

        personalization.addTo(new Email(registrationInfo.getEmail()));
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



        if(user != null) {
            return delegateRepository.save(new Delegate(registrationInfo));
        } else throw new UserExistException("User is empty. Something went wrong.");
    }

    @PutMapping("/delegates/{delegate_id}") // updates
    public Delegate updateDelegate(@PathVariable long delegate_id, @Valid @RequestBody Delegate delegate) {
        Delegate delegateFromDB = delegateRepository.findById(delegate_id)
                .orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id)); // gets from database the Delegate (w/ the specific id), and then set the new value from the value derived from the request

        // delegateFromDB.setEmail(delegate.getEmail()); // email shouldn't be able to be changed
        // delegateFromDB.setPassword(delegate.getPassword()); // password changes in the User class -- OR, password also shouldn't be able to be changed -- If wanting change for either email or password, delete and create new registration.
        delegateFromDB.setName(delegate.getName());
        delegateFromDB.setAge(delegate.getAge());
        delegateFromDB.setSchool(delegate.getSchool());
        delegateFromDB.setGender(delegate.getGender());
        delegateFromDB.setPhone_number(delegate.getPhone_number());
        delegateFromDB.setDate_of_birth(delegate.getDate_of_birth());
        delegateFromDB.setGrade(delegate.getGrade());
        delegateFromDB.setAddress(delegate.getAddress());
        delegateFromDB.setCity(delegate.getCity());
        delegateFromDB.setProvince(delegate.getProvince());
        delegateFromDB.setEc_name(delegate.getEc_name());
        delegateFromDB.setEc_phone_number(delegate.getEc_phone_number());
        delegateFromDB.setEc_relationship(delegate.getEc_relationship());
        delegateFromDB.setPast_experience(delegate.getPast_experience());
        delegateFromDB.setPref_first_comm(delegate.getPref_first_comm());
        delegateFromDB.setPref_first_country(delegate.getPref_first_country());
        delegateFromDB.setPref_second_comm(delegate.getPref_second_comm());
        delegateFromDB.setPref_second_country(delegate.getPref_second_country());
        delegateFromDB.setPref_third_comm(delegate.getPref_third_comm());
        delegateFromDB.setPref_third_country(delegate.getPref_third_country());
        delegateFromDB.setAssignment_id(delegate.getAssignment_id());
        delegateFromDB.setPayment_status(delegate.getPayment_status());
        delegateFromDB.setWaiver_link(delegate.getWaiver_link());
        delegateFromDB.setWaiver(delegate.getWaiver());
        delegateFromDB.setDelegationId(delegate.getDelegationId());
        return delegateRepository.save(delegateFromDB);
    }

    @PutMapping("/registration/head") // updates
    public Delegate registerHeadDelegate(@Valid @RequestBody Delegate delegate) {

        Delegate delegateFromDB = delegateRepository.findByEmail(delegate.getEmail());

        // delegateFromDB.setEmail(delegate.getEmail()); // email shouldn't be able to be changed
        // delegateFromDB.setPassword(delegate.getPassword()); // password changes in the User class -- OR, password also shouldn't be able to be changed -- If wanting change for either email or password, delete and create new registration.
        delegateFromDB.setName(delegate.getName());
        delegateFromDB.setAge(delegate.getAge());
        delegateFromDB.setSchool(delegate.getSchool());
        delegateFromDB.setGender(delegate.getGender());
        delegateFromDB.setPhone_number(delegate.getPhone_number());
        delegateFromDB.setDate_of_birth(delegate.getDate_of_birth());
        delegateFromDB.setGrade(delegate.getGrade());
        delegateFromDB.setAddress(delegate.getAddress());
        delegateFromDB.setCity(delegate.getCity());
        delegateFromDB.setProvince(delegate.getProvince());
        delegateFromDB.setEc_name(delegate.getEc_name());
        delegateFromDB.setEc_phone_number(delegate.getEc_phone_number());
        delegateFromDB.setEc_relationship(delegate.getEc_relationship());
        delegateFromDB.setPast_experience(delegate.getPast_experience());
        delegateFromDB.setPref_first_comm(delegate.getPref_first_comm());
        delegateFromDB.setPref_first_country(delegate.getPref_first_country());
        delegateFromDB.setPref_second_comm(delegate.getPref_second_comm());
        delegateFromDB.setPref_second_country(delegate.getPref_second_country());
        delegateFromDB.setPref_third_comm(delegate.getPref_third_comm());
        delegateFromDB.setPref_third_country(delegate.getPref_third_country());
        delegateFromDB.setAssignment_id(delegate.getAssignment_id());
        delegateFromDB.setPayment_status(delegate.getPayment_status());
        delegateFromDB.setWaiver_link(delegate.getWaiver_link());
        delegateFromDB.setWaiver(delegate.getWaiver());
        delegateFromDB.setDelegationId(delegate.getDelegationId());

        return delegateRepository.save(delegateFromDB);
    }

    @DeleteMapping("/delegates/{delegate_id}")
    public void delete(@PathVariable long delegate_id) {
        delegateRepository.findById(delegate_id)
                .orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id));
        delegateRepository.deleteById(delegate_id);
    }

}
