package org.cahsmun.registration.rooming;

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
public class RoomingController {

    @Resource
    RoomingRepository roomingRepository;

    @GetMapping("/rooms")
    public List<Rooming> retrieveAllRoomings() {
        return StreamSupport.stream(roomingRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/rooms/{room_id}")
    public Rooming findById(@PathVariable long room_id) {
        return roomingRepository.findById(room_id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + room_id));
    }

    @PostMapping("/rooms")
    public Rooming createDelegation(@Valid @RequestBody Rooming rooming) throws UserExistException {
        return roomingRepository.save(rooming);
    }
}
