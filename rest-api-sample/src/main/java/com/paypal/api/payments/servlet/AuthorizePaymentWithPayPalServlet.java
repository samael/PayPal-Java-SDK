package com.paypal.api.payments.servlet;

import com.paypal.api.payments.*;
import com.paypal.api.payments.util.ResultPrinter;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.paypal.api.payments.util.SampleConstants.*;

public class AuthorizePaymentWithPayPalServlet extends HttpServlet {

    private static APIContext sApiContext = new APIContext(clientID, clientSecret, mode);

    private static final Logger LOGGER = Logger
            .getLogger(PaymentWithPayPalServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createAuthorization(req, resp);
        req.getRequestDispatcher("response.jsp").forward(req, resp);
    }

    /**
     * Creates a {@link Payment} from an {@link HttpServletRequest}, using the scheme host from the request as
     * the base for the redirectURL
     *
     * @param req a {@link HttpServletRequest} from which to grab redirect parameters
     * @return a pre-populated {@link Payment} with sample parameters
     */
     static Payment createPaymentForRequest(HttpServletRequest req) {
        Details details = new Details();
        details.setShipping("1");
        details.setSubtotal("5");
        details.setTax("1");

        // ###Amount
        // Let's you specify a payment amount.
        Amount amount = new Amount();
        amount.setCurrency("USD");
        // Total must be equal to sum of shipping, tax and subtotal.
        amount.setTotal("7");
        amount.setDetails(details);

        // ###Transaction
        // A transaction defines the contract of a
        // payment - what is the payment for and who
        // is fulfilling it. Transaction is created with
        // a `Payee` and `Amount` types
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction
                .setDescription("This is the payment transaction description.");

        // ### Items
        Item item = new Item();
        item.setName("Ground Coffee 40 oz").setQuantity("1").setCurrency("USD").setPrice("5");
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<Item>();
        items.add(item);
        itemList.setItems(items);

        transaction.setItemList(itemList);


        // The Payment creation API requires a list of
        // Transaction; add the created `Transaction`
        // to a List
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        // ###Payer
        // A resource representing a Payer that funds a payment
        // Payment Method
        // as 'paypal'
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // ###Payment
        // A Payment Resource; create one using
        // the above types and intent as 'sale'
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // ###Redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        String guid = UUID.randomUUID().toString().replaceAll("-", "");
        redirectUrls.setCancelUrl(req.getScheme() + "://"
                + req.getServerName() + ":" + req.getServerPort()
                + req.getContextPath() + "/paymentwithpaypal?guid=" + guid);
        redirectUrls.setReturnUrl(req.getScheme() + "://"
                + req.getServerName() + ":" + req.getServerPort()
                + req.getContextPath() + "/paymentwithpaypal?guid=" + guid);
        payment.setRedirectUrls(redirectUrls);

        return payment;
    }

    private void createAuthorization(HttpServletRequest req, HttpServletResponse resp) {
        // Create a payment by posting to the APIService
        // using a valid AccessToken
        // The return object contains the status;
        try {
            Payment createdPayment = createPaymentForRequest(req).create(sApiContext);
            LOGGER.info("Created payment with id = "
                    + createdPayment.getId() + " and status = "
                    + createdPayment.getState());
            // ###Payment Approval Url
            Iterator<Links> links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    req.setAttribute("redirectURL", link.getHref());
                }
            }
            ResultPrinter.addResult(req, resp, "Payment with PayPal", Payment.getLastRequest(), Payment.getLastResponse(), null);
            req.setAttribute("guid", createdPayment.getId());
        } catch (PayPalRESTException e) {
            ResultPrinter.addResult(req, resp, "Payment with PayPal", Payment.getLastRequest(), null, e.getMessage());
        }
    }
}
