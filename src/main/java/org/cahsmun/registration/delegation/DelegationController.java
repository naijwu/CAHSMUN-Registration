package org.cahsmun.registration.delegation;

import lombok.extern.slf4j.Slf4j;
import org.cahsmun.registration.authentication.UserExistException;
import org.cahsmun.registration.sponsor.Sponsor;
import org.cahsmun.registration.sponsor.SponsorRepository;
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

    @Resource
    SponsorRepository sponsorRepository;

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

    @PostMapping("/{sponsor_id}/delegations")
    public void createDelegation(@PathVariable long sponsor_id, @Valid @RequestBody Delegation delegation) throws UserExistException {

        Sponsor sponsor = sponsorRepository.findById(sponsor_id).orElseThrow(() -> new ResourceNotFoundException("Sponsor not found with ID: " + sponsor_id));

        Delegation newDelegation = delegationRepository.save(delegation);

        sponsor.setDelegation_id(newDelegation.getDelegation_id());
        sponsorRepository.save(sponsor);
    }
}
