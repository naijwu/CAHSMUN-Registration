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

        type = JsonPath.read(emailInfoJson, "$.type");

        if (type == "SRC") {
            // send email to registrant about School registration, sent by DA
            subject_email = "CAHSMUN 2021 School Registration Confirmation";

            to_email = JsonPath.read(emailInfoJson, "$.to_email");
            from_email = "jaewuchun@gmail.com"; // will be DA -- me for testing purposes (need to test reply)

            full_name = JsonPath.read(emailInfoJson, "$.full_name");
            login_email = JsonPath.read(emailInfoJson, "$.login_email");
            login_passcode = JsonPath.read(emailInfoJson, "$.login_passcode");

            school_name = JsonPath.read(emailInfoJson, "$.school_name");
            school_address = JsonPath.read(emailInfoJson, "$.school_address");
            school_city = JsonPath.read(emailInfoJson, "$.school_city");
            school_province = JsonPath.read(emailInfoJson, "$.school_province");
            school_postal = JsonPath.read(emailInfoJson, "$.school_postal");


        } else if (type == "DRC") {
            // send email to registrant about Delegate registration, sent by DA
            subject_email = "CAHSMUN 2021 Delegate Registration Confirmation";

            to_email = JsonPath.read(emailInfoJson, "$.to_email");
            from_email = "jaewuchun@gmail.com";

            login_email = JsonPath.read(emailInfoJson, "$.login_email");
            login_passcode = JsonPath.read(emailInfoJson, "$.login_passcode");


        } else if (type == "SRN") {
            // send email to DA
            subject_email = "[CRS] School Registration Notification";

            to_email = "jaewuchun@gmail.com"; // will be DA -- me for testing purposes
            from_email = "it@cahsmun.org";

            full_name = JsonPath.read(emailInfoJson, "$.full_name");
            login_email = JsonPath.read(emailInfoJson, "$.login_email");
            login_passcode = JsonPath.read(emailInfoJson, "$.login_passcode");

            school_name = JsonPath.read(emailInfoJson, "$.school_name");
        } else if (type == "TEST") {

            from_email = "it@cahsmun.org";
            subject_email = "Sendgrid Test";
            to_email = JsonPath.read(emailInfoJson, "$.to_email");
        }




        // JsonObject json = new com.google.gson.JsonObject();

        to_email = "jaewuchun@gmail.com";

        Mail mail = new Mail();
        mail.setFrom(new Email("it@cahsmun.org"));
        mail.setTemplateId("d-b877b83734b24152b02ae61e6b8b64fa");

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("full_name", "Testing Templates");
        personalization.addTo(new Email(to_email));

        /*

        Personalization personalization = new Personalization();

        json.addProperty("email", "jaewuchun@gmail.com");
        json.addProperty("full_name", "Example Name");

        personalization.addCustomArg("dynamic_template_data", json.toString());
        */

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
