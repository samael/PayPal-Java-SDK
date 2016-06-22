package com.paypal.api.sample;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.paypal.api.openidconnect.CreateFromRefreshTokenParameters;
import com.paypal.api.openidconnect.Tokeninfo;
import com.paypal.api.payments.Invoice;
import com.paypal.api.payments.Payment;
import com.paypal.base.ClientCredentials;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;

public class ThirdPartyInvoice extends SampleBase<Invoice> {

	private static final Logger log = LogManager.getLogger(ThirdPartyInvoice.class);

	/**
	 * Initialize and instantiate an Invoice object
	 * 
	 * @throws PayPalRESTException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 * @throws FileNotFoundException
	 */
	public ThirdPartyInvoice() throws PayPalRESTException, JsonSyntaxException, JsonIOException, FileNotFoundException {
		super(new Invoice());
	}

	/**
	 * Create an invoice.
	 * 
	 * https://developer.paypal.com/webapps/developer/docs/api/#create-an-
	 * invoice
	 * 
	 * @return newly created Invoice instance
	 * @throws PayPalRESTException
	 */
	public Invoice create(String refreshToken) throws PayPalRESTException, FileNotFoundException, IOException {
		Tokeninfo tokeninfo = null;
		log.info("creating third party invoice using refresh token " + refreshToken);

		// Setup the refresh token params. This will be used to get access token
		// from refresh token
		ClientCredentials credentials = PayPalResource.getCredential();
		CreateFromRefreshTokenParameters params = new CreateFromRefreshTokenParameters();
		params.setClientID(credentials.getClientID());
		params.setClientSecret(credentials.getClientSecret());

		// Create a token info object, and call `createFromRefreshToken`
		Tokeninfo info = new Tokeninfo();
		info.setRefreshToken(refreshToken);
		tokeninfo = info.createFromRefreshToken(params);

		// Set Access Token Appropriately
		tokeninfo.setAccessToken(tokeninfo.getTokenType() + " " + tokeninfo.getAccessToken());
		// Use this accessToken to make any calls to Invoice
		accessToken = tokeninfo.getAccessToken();

		System.out.println("Generated access token from auth code: " + tokeninfo.getAccessToken());

		// populate Invoice object that we are going to play with
		super.instance = load("invoice_create.json", Invoice.class);
		super.instance = super.instance.create(accessToken);
		return super.instance;
	}

	public Invoice send(Invoice invoice) throws PayPalRESTException {
		invoice.send(accessToken);
		return Invoice.get(accessToken, invoice.getId());
	}

	public static void main(String[] args) {
		try {
			String refreshToken = "J5yFACP3Y5dqdWCdN3o9lNYz0XyR01IHNMQn-E4r6Ss38rqbQ1C4rC6PSBhJvB_tte4WZsWe8ealMl-U_GMSz30dIkKaovgN41Xf8Sz0EGU55da6tST5I6sg3Rw";
			ThirdPartyInvoice fps = new ThirdPartyInvoice();
			// This will create an invoice for `developer@sample.com` merchant whose refresh token it is.
			Invoice invoice = fps.create(refreshToken);
			System.out.println(Invoice.getLastRequest());
			System.out.println(Invoice.getLastResponse());
			invoice = fps.send(invoice);
			System.out.println(Invoice.getLastRequest());
			System.out.println(Invoice.getLastResponse());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Payment.getLastRequest());
			System.out.println(Payment.getLastResponse());
		}
	}
}
