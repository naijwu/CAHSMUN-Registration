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

    // TODO: Implement UPDATE feature with @PutMapping Annotation

    @DeleteMapping("/delegates/{delegate_id}")
    public void delete(@PathVariable long delegate_id) {
        delegateRepository.findById(delegate_id)
                .orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id));
        delegateRepository.deleteById(delegate_id);
    }

}
