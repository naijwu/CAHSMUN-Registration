package org.cahsmun.registration.sponsor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@CrossOrigin()
@RestController
public class SponsorController {


    @Resource
    SponsorRepository sponsorRepository;
}
