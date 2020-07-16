package org.cahsmun.registration.sponsor;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin()
@RestController
public class SponsorController {


    @Resource
    SponsorRepository sponsorRepository;

    @GetMapping("/sponsors")
    public List<Sponsor> retrieveAllSponsors() {
        return StreamSupport.stream(sponsorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
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
