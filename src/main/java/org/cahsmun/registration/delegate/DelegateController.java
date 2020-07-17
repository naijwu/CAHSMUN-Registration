package org.cahsmun.registration.delegate;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin() // Will be for eventually the front end when it calls on the data
@RestController
public class DelegateController {

    @Resource
    DelegateRepository delegateRepository;

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

    @PostMapping("/delegates")
    public Delegate createDelegate(@Valid @RequestBody Delegate delegate) {
        return delegateRepository.save(delegate);
    }

    @PutMapping("/delegates/{delegate_id}") // updates
    public Delegate updateDelegate(@PathVariable long delegate_id, @Valid @RequestBody Delegate delegate) {
        Delegate delegateFromDB = delegateRepository.findById(delegate_id)
                .orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id)); // gets from database the Delegate (w/ the specific id), and then set the new value from the value derived from the request
        delegateFromDB.setEmail(delegate.getEmail());
        delegateFromDB.setPassword(delegate.getPassword());
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
        delegateFromDB.setAssignment(delegate.getAssignment());
        delegateFromDB.setHas_paid(delegate.getHas_paid());
        delegateFromDB.setWaiver_link(delegate.getWaiver_link());
        delegateFromDB.setWaiver(delegate.getWaiver());
        return delegateRepository.save(delegateFromDB);
    }

    @DeleteMapping("/delegates/{delegate_id}")
    public void delete(@PathVariable long delegate_id) {
        delegateRepository.findById(delegate_id)
                .orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id));
        delegateRepository.deleteById(delegate_id);
    }

}
