package org.cahsmun.registration.delegation;

import lombok.extern.slf4j.Slf4j;
import org.cahsmun.registration.authentication.UserExistException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin()
@RestController
@Slf4j
public class DelegationController {

    @Resource
    DelegationRepository delegationRepository;

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

    @PostMapping("/delegations")
    public Delegation createDelegation(@Valid @RequestBody Delegation delegation) throws UserExistException {
        return delegationRepository.save(delegation);
    }
}
