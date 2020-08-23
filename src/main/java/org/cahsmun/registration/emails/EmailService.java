package org.cahsmun.registration.emails;

import com.sendgrid.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

public class EmailService {

    private final String SENDGRID_API="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";

    @RequestMapping(
            value = "/sendmail",
            method = RequestMethod.POST,
            consumes = "application/json")
    public String sendEmail(@RequestBody String emailInfoJson) throws IOException {
        Email from = new Email("test@example.com");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("test@example.com");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
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
