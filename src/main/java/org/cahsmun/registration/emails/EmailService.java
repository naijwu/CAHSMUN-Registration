package org.cahsmun.registration.emails;

import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin()
@RestController
public class EmailService {

    private final String SENDGRID_API="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";

    private String type; // DRC: Del Reg Confirm (To Registrant), SRC: School Reg Confirm (To Registrant), SRN: School Reg Notification (To DA)
    private String to_email; // To the one that just created the account/to DA
    private String from_email; // DA

    private String subject_email;

    private String full_name; // name of who to sending to

    // School registration content
    private String school_name;
    private String school_address;
    private String school_city;
    private String school_province;
    private String school_postal;

    // School + Delegate registrant content
    private String login_email;
    private String login_passcode; // For delegate registration, just tell them to log in to see their information)


    /* Request JSON Object Format:

        {
            "type": "..",
            "to_email": "..",
            "full_name": ".."
        }

     */


    @RequestMapping(
            value = "/sendmail",
            method = RequestMethod.POST,
            consumes = "application/json")
    public String sendEmail(@RequestBody String emailInfoJson) throws IOException {

        Mail mail = new Mail();
        Personalization personalization = new Personalization();

        type = JsonPath.read(emailInfoJson, "$.type");

        if (type.equals("SRC")) {
            mail.setTemplateId("d-b877b83734b24152b02ae61e6b8b64fa");
            // send email to registrant about School registration, sent by DA

            to_email = JsonPath.read(emailInfoJson, "$.to_email");
            mail.setFrom(new Email("delegates@cahsmun.org"));

            full_name = JsonPath.read(emailInfoJson, "$.full_name");
            login_email = JsonPath.read(emailInfoJson, "$.login_email");
            login_passcode = JsonPath.read(emailInfoJson, "$.login_passcode");

            school_name = JsonPath.read(emailInfoJson, "$.school_name");
            school_address = JsonPath.read(emailInfoJson, "$.school_address");
            school_city = JsonPath.read(emailInfoJson, "$.school_city");
            school_province = JsonPath.read(emailInfoJson, "$.school_province");
            school_postal = JsonPath.read(emailInfoJson, "$.school_postal");

            personalization.addDynamicTemplateData("full_name", full_name);
            personalization.addDynamicTemplateData("login_email", login_email);
            personalization.addDynamicTemplateData("login_passcode", login_passcode);

            personalization.addDynamicTemplateData("school_name", school_name);
            personalization.addDynamicTemplateData("school_address", school_address);
            personalization.addDynamicTemplateData("school_city", school_city);
            personalization.addDynamicTemplateData("school_province", school_province);
            personalization.addDynamicTemplateData("school_postal", school_postal);

            personalization.addTo(new Email(to_email));


        } else if (type.equals("DRC")) {
            mail.setTemplateId("d-66a3d695111747af80148e83bcf6c328");
            // send email to registrant about Delegate registration, sent by DA

            to_email = JsonPath.read(emailInfoJson, "$.to_email");
            mail.setFrom(new Email("delegates@cahsmun.org"));

            full_name = JsonPath.read(emailInfoJson, "$.full_name");
            login_email = JsonPath.read(emailInfoJson, "$.login_email");
            login_passcode = JsonPath.read(emailInfoJson, "$.login_passcode");

            personalization.addDynamicTemplateData("full_name", full_name);
            personalization.addDynamicTemplateData("login_email", login_email);
            personalization.addDynamicTemplateData("login_passcode", login_passcode);

            personalization.addTo(new Email(to_email));
        } else if (type.equals("SRN")) {
            mail.setTemplateId("d-a7dfc49a044f483eaa2014fec09fc275");
            // send email to DA

            to_email = "jaewuchun@gmail.com"; // will be DA -- me for testing purposes
            mail.setFrom(new Email("it@cahsmun.org"));

            full_name = JsonPath.read(emailInfoJson, "$.full_name");
            school_name = JsonPath.read(emailInfoJson, "$.school_name");

            personalization.addDynamicTemplateData("full_name", full_name);
            personalization.addDynamicTemplateData("school_name", school_name);

            mail.setSubject("[CRS] New School Registration: " + school_name);

            personalization.addTo(new Email(to_email));
        } else if (type.equals("TEST")) {

        }


        mail.addPersonalization(personalization);

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
