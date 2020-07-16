package org.cahsmun.registration.secretariat;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin()
@RestController
public class SecretariatController {

    @Resource
    SecretariatRepository secretariatRepository;

    @GetMapping("/secretariats")
    public List<Secretariat> retrieveAllSecretariats() {
        return StreamSupport.stream(secretariatRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/secretariats/{secretariat_id}")
    public Secretariat findById(@PathVariable long secretariat_id) {
        return secretariatRepository.findById(secretariat_id)
                .orElseThrow(() -> new ResourceNotFoundException("Secretariat not found with ID: " + secretariat_id));
    }

    @DeleteMapping("/secretariats/{secretariat_id}")
    public void delete(@PathVariable long secretariat_id) {
        secretariatRepository.findById(secretariat_id)
                .orElseThrow(() -> new ResourceNotFoundException("Secretariat not found with ID: " + secretariat_id));
        secretariatRepository.deleteById(secretariat_id);
    }
}
