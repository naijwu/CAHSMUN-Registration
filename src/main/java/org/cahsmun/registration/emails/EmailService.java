package org.cahsmun.registration.emails;

import com.sendgrid.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin()
@RestController
public class EmailService {

    private final String SENDGRID_API="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";

    @RequestMapping(
            value = "/sendmail",
            method = RequestMethod.POST,
            consumes = "application/json")
    public String sendEmail(@RequestBody String emailInfoJson) throws IOException {
        Email from = new Email("it@cahsmun.org");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("test@example.com");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(SENDGRID_API);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }

        return emailInfoJson;
    }
}
