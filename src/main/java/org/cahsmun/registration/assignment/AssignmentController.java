package org.cahsmun.registration.assignment;

import lombok.extern.slf4j.Slf4j;
import org.cahsmun.registration.delegate.Delegate;
import org.cahsmun.registration.delegate.DelegateRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@CrossOrigin()
@RestController
@Slf4j
public class AssignmentController {

    @Resource
    AssignmentRepository assignmentRepository;

    @Resource
    DelegateRepository delegateRepository;

    /*
    Methods to have:
     - Basic CRUD
     - create records by bulk category (for creating the matrix)
     - delete records by bulk category (for editing the matrix)
     - assign to delegate
     */

    @GetMapping("/assignments") // READ
    public List<Assignment> retrieveAllAssignment() {
        return StreamSupport.stream(assignmentRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /*
    @GetMapping("/assignments/{committee}")
    public List<Assignment> retrieveByCommittee(@RequestParam String committee) { // was gonna do backend, but dynamic routing would be hard so I wouldn't use it...
    }*/

    @PutMapping("/assignments/{assignment_id}") // UPDATE -- specific, pre-existing assignment combo
    public Assignment updateAssignment(@PathVariable long assignment_id, @Valid @RequestBody Assignment assignment) {
        Assignment assignmentFromDB = assignmentRepository.findById(assignment_id)
                .orElseThrow(() -> new ResourceNotFoundException("Broken front-end code -- Check axios request + DB. Requested Assignment with ID: " + assignment_id)); // passed ID is dictated by the front-end
        assignmentFromDB.setCommittee(assignment.getCommittee());
        assignmentFromDB.setCountry(assignment.getCountry());
        assignmentFromDB.setDelegate(assignment.getDelegate());
        return assignmentRepository.save(assignmentFromDB);
    }

    @DeleteMapping("/assignments/{assignment_id}") // DELETE -- delete specific assignment combo
    public void delete(@PathVariable long assignment_id) {
        assignmentRepository.findById(assignment_id)
                .orElseThrow(() -> new ResourceNotFoundException("Broken front-end code -- Check axios request + DB. Requested Assignment with ID: " + assignment_id));
        assignmentRepository.deleteById(assignment_id);
    }

    @PutMapping("/assign/{assignment_id}/{delegate_id}")
    public Assignment assignDelegate(@PathVariable long assignment_id, @PathVariable long delegate_id) {
        Assignment assignment = assignmentRepository.findById(assignment_id).orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + assignment_id));
        Delegate delegate = delegateRepository.findById(delegate_id).orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id));

        assignment.setDelegate(delegate); // join table
        delegate.setAssignment_id(assignment.getAssignment_id());

        delegateRepository.save(delegate);

        return assignmentRepository.save(assignment);
    }
}
