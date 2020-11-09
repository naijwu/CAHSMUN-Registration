package org.cahsmun.registration.assignment;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.cahsmun.registration.delegate.Delegate;
import org.cahsmun.registration.delegate.DelegateRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
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

    @GetMapping("/assignments/{assignment_id}") // Returning a specific delegate
    public Assignment findById(@PathVariable long assignment_id) {
        return assignmentRepository.findById(assignment_id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + assignment_id));
    }

    @PutMapping("/assignments/{assignment_id}") // UPDATE -- specific, pre-existing assignment combo
    public Assignment updateAssignment(@PathVariable long assignment_id, @Valid @RequestBody Assignment assignment) {
        Assignment assignmentFromDB = assignmentRepository.findById(assignment_id)
                .orElseThrow(() -> new ResourceNotFoundException("Broken front-end code -- Check axios request + DB. Requested Assignment with ID: " + assignment_id)); // passed ID is dictated by the front-end
        assignmentFromDB.setCommittee(assignment.getCommittee());
        assignmentFromDB.setCountry(assignment.getCountry());
        assignmentFromDB.setDelegate_id(assignment.getDelegate_id());
        return assignmentRepository.save(assignmentFromDB);
    }

    @DeleteMapping("/assignments/{assignment_id}") // DELETE -- delete specific assignment combo
    public void delete(@PathVariable long assignment_id) {
        assignmentRepository.findById(assignment_id)
                .orElseThrow(() -> new ResourceNotFoundException("Broken front-end code -- Check axios request + DB. Requested Assignment with ID: " + assignment_id));
        assignmentRepository.deleteById(assignment_id);
    }

    @PutMapping("/assign/{assignment_id}/{delegate_id}")
    public Assignment assignDelegate(@PathVariable long assignment_id, @PathVariable long delegate_id) throws IOException {
        Assignment assignment = assignmentRepository.findById(assignment_id).orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + assignment_id));
        Delegate delegate = delegateRepository.findById(delegate_id).orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id));

        assignment.setDelegate_id(delegate.getDelegate_id());
        delegate.setAssignment_id(assignment.getAssignment_id());



        Mail mail2 = new Mail();
        Personalization personalization2 = new Personalization();
        mail2.setFrom(new Email("delegates@cahsmun.org"));
        mail2.setTemplateId("d-0c25c8e32bfd4a96b339018a46a7230f");

        personalization2.addDynamicTemplateData("full_name", delegate.getName());
        personalization2.addDynamicTemplateData("committee", assignment.getCommittee());
        personalization2.addDynamicTemplateData("country", assignment.getCountry());

        personalization2.addTo(new Email(delegate.getEmail())); // TODO: For production, replace with delegates@cahsmun.org
        mail2.addPersonalization(personalization2);

        SendGrid sg2 = new SendGrid(SENDGRID_API);
        Request request2 = new Request();
        try {
            request2.setMethod(Method.POST);
            request2.setEndpoint("mail/send");
            request2.setBody(mail2.build());
            Response response = sg2.api(request2);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }



        delegateRepository.save(delegate);

        return assignmentRepository.save(assignment);
    }

    @PutMapping("/unassign/{assignment_id}/{delegate_id}")
    public Assignment unassignDelegate(@PathVariable long assignment_id, @PathVariable long delegate_id) {
        Assignment assignment = assignmentRepository.findById(assignment_id).orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + assignment_id));
        Delegate delegate = delegateRepository.findById(delegate_id).orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + delegate_id));

        assignment.setDelegate_id(0);
        delegate.setAssignment_id(0);

        delegateRepository.save(delegate);

        return assignmentRepository.save(assignment);
    }
}
