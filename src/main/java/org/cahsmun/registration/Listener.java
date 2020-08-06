package org.cahsmun.registration;

import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.cahsmun.registration.delegate.DelegateRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spark.Request;
import spark.Response;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin()
@RestController
public class Listener {

    @Resource
    DelegateRepository delegateRepository; // will be used to save payment information if payment successful

    /*
    public Object listener(Request request, Response response) throws IOException {
        return handler(request, response);
    }
    */

    @RequestMapping(value = "/listener", method = RequestMethod.POST)
    public Object handler(Request request, Response response) {

        // Set your secret key. Remember to switch to your live secret key in production!
        // See your keys here: https://dashboard.stripe.com/account/apikeys
        Stripe.apiKey = "pk_test_Z4lkXkyCLtsOVpfnVi95obk60051Kx9aKg";

        // You can find your endpoint's secret in your webhook settings
        String endpointSecret = "whsec_MffGhsa9kXv0s7sbxeKHQ6F54qlHZrpA";

        String payload = request.body();
        String sigHeader = request.headers("Stripe-Signature");
        Event event = null;

        try {
            /*
            event = Event.GSON.fromJson(payload, Event.class);
            */
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );

        } catch (JsonSyntaxException e) {
            // Invalid payload
            response.status(400);
            return "";
        }  catch (SignatureVerificationException e) {
            // Invalid signature
            response.status(400);
            return "";
        }/* catch (JsonSyntaxException e) {
            // Invalid payload
            response.status(400);
            return "";
        } */


        /*
        // Handle the checkout.session.completed event
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().get();

            // Fulfill the purchase...
            handleCheckoutSession(session);
        }
*/

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
            response.status(400);
        }

        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                System.out.println("PaymentIntent was successful!");
                break;
            case "payment_method.attached":
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                System.out.println("PaymentMethod was attached to a Customer!");
                break;
            // ... handle other event types
            default:
                // Unexpected event type
                response.status(400);
                return "";
        }


        response.status(200);
        return "";
    }

    public void handleCheckoutSession(Session session) {

        // Check payment success/failure,
        // save payment data information into the delegate

    }
}
