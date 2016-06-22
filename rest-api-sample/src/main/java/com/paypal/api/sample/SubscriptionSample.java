package com.paypal.api.sample;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.paypal.api.payments.Patch;
import com.paypal.api.payments.Plan;
import com.paypal.base.rest.PayPalRESTException;

public class SubscriptionSample extends SampleBase<Plan> {

	public SubscriptionSample() throws PayPalRESTException,
			JsonSyntaxException, JsonIOException, FileNotFoundException {
		super(new Plan());
	}

	/**
	 * Create a plan.
	 *
	 * https://developer.paypal.com/webapps/developer/docs/api/#create-a-plan
	 *
	 * @return newly created Plan instance
	 * @throws PayPalRESTException
	 */
	public Plan create() throws PayPalRESTException, IOException {
		// populate Plan object that we are going to play with
		super.instance = super.load("billingplan_create.json", Plan.class);
		super.instance = super.instance.create(accessToken);
		return super.instance;
	}

	/**
	 * Update a plan
	 *
	 * https://developer.paypal.com/webapps/developer/docs/api/#update-a-plan
	 *
	 * @return updated Plan instance
	 * @throws PayPalRESTException
	 */
	public Plan update() throws PayPalRESTException, IOException {
		Patch[] patch = super.load("billingplan_update.json", Patch[].class);
		super.instance.update(super.accessToken, Arrays.asList(patch));
		return super.instance;
	}

	/**
	 * Retrieve a plan
	 *
	 * https://developer.paypal.com/webapps/developer/docs/api/#retrieve-a-plan
	 *
	 * @return the retrieved plan
	 * @throws PayPalRESTException
	 */
	public Plan retrieve() throws PayPalRESTException {
		return Plan.get(super.accessToken, super.instance.getId());
	}

	/**
	 * Main method that calls all methods above in a flow.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SubscriptionSample subscriptionSample = new SubscriptionSample();

			Plan plan = subscriptionSample.create();
			System.out.println("create response:\n" + Plan.getLastResponse());
			plan = subscriptionSample.update();
			System.out.println("plan updated");
			plan = subscriptionSample.retrieve();
			System.out.println("retrieve response:\n" + Plan.getLastResponse());
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
