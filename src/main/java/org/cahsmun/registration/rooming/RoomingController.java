package org.cahsmun.registration.rooming;

import lombok.extern.slf4j.Slf4j;
import org.cahsmun.registration.authentication.UserExistException;
import org.cahsmun.registration.delegate.Delegate;
import org.cahsmun.registration.delegate.DelegateRepository;
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

    @Resource
    DelegateRepository delegateRepository;

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

    @PutMapping("/rooms/{room_id}")
    public Rooming updateRooming(@PathVariable long room_id, @Valid @RequestBody Rooming rooming) {
        Rooming roomingFromDB = roomingRepository.findById(room_id).orElseThrow(() -> new ResourceNotFoundException("Room not found with ID " + room_id));

        roomingFromDB.setDelegation_id(rooming.getDelegation_id());
        roomingFromDB.setSponsor_id(rooming.getSponsor_id());
        roomingFromDB.setRoom_gender(rooming.getRoom_gender());
        roomingFromDB.setDelegate_one(rooming.getDelegate_one());
        roomingFromDB.setDelegate_two(rooming.getDelegate_two());
        roomingFromDB.setDelegate_three(rooming.getDelegate_three());
        roomingFromDB.setDelegate_four(rooming.getDelegate_four());

        return roomingRepository.save(roomingFromDB);
    }

    @PostMapping("/rooms")
    public Rooming createDelegation(@Valid @RequestBody Rooming rooming) throws UserExistException {
        return roomingRepository.save(rooming);
    }

    /*
    // removed rooming_id from delegate table

    @PostMapping("/room/bind/{room_id}/{room_slot}/{delegate_id}")
    public Rooming bindRooming(@PathVariable long room_id, @PathVariable long delegate_id, @PathVariable int room_slot) {
        Rooming rooming = roomingRepository.findById(room_id).orElseThrow(() -> new ResourceNotFoundException("Rooming not found with ID: " + room_id));
        Delegate delegate = delegateRepository.findById(delegate_id).orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id));

        switch (room_slot) {
            case 1:
                rooming.setDelegate_one(delegate.getDelegate_id());
                break;
            case 2:
                rooming.setDelegate_two(delegate.getDelegate_id());
                break;
            case 3:
                rooming.setDelegate_three(delegate.getDelegate_id());
                break;
            case 4:
                rooming.setDelegate_four(delegate.getDelegate_id());
                break;
        }

        delegate.setRooming_id(rooming.getRooming_id());

        delegateRepository.save(delegate);
        return roomingRepository.save(rooming);
    }

    @PostMapping("/room/unbind/{room_id}/{room_slot}/{delegate_id}")
    public Rooming unbindRooming(@PathVariable long room_id, @PathVariable long delegate_id, @PathVariable int room_slot) {
        Rooming rooming = roomingRepository.findById(room_id).orElseThrow(() -> new ResourceNotFoundException("Rooming not found with ID: " + room_id));
        Delegate delegate = delegateRepository.findById(delegate_id).orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id));

        switch (room_slot) {
            case 1:
                rooming.setDelegate_one(0);
                break;
            case 2:
                rooming.setDelegate_two(0);
                break;
            case 3:
                rooming.setDelegate_three(0);
                break;
            case 4:
                rooming.setDelegate_four(0);
                break;
        }

        delegate.setRooming_id(0);

        delegateRepository.save(delegate);
        return roomingRepository.save(rooming);
    }

    @RequestMapping(value="/test/room/unbind/{room_id}/{unbind_delegates}", method=RequestMethod.POST)
    public String test(@PathVariable long room_id, @PathVariable List<String> unbind_delegates) {
        for (String param : unbind_delegates) {
            System.out.println("Unbind delegate " + param + " from Room with ID " + room_id);
        }
        return "foo";
    } */
}
