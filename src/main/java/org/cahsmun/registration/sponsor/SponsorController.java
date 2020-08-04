package org.cahsmun.registration.sponsor;

import org.cahsmun.registration.authentication.UserExistException;
import org.cahsmun.registration.sponsor.SponsorInfo;
import org.cahsmun.registration.user.User;
import org.cahsmun.registration.user.UserServiceImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin()
@RestController
public class SponsorController {


    @Resource
    SponsorRepository sponsorRepository;

    @Resource
    UserServiceImpl userService;

    @GetMapping("/sponsors")
    public List<Sponsor> retrieveAllSponsors() {
        return StreamSupport.stream(sponsorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @PostMapping("/sponsors")
    public Sponsor createSponsor(@Valid @RequestBody SponsorInfo sponsorInfo) throws UserExistException {
        User user = userService.save(new User(sponsorInfo)); // returns User object that has just been made
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
