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

    @GetMapping("/secretariat")
    public List<Secretariat> retrieveAllSecretariats() {
        return StreamSupport.stream(secretariatRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/secretariat/{secretariat_id}")
    public void delete(@PathVariable long delegate_id) {
        secretariatRepository.findById(delegate_id)
                .orElseThrow(() -> new ResourceNotFoundException("Secretariat not found with ID: " + delegate_id));
        secretariatRepository.deleteById(delegate_id);
    }
}
