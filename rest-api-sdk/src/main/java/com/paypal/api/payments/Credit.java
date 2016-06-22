package com.paypal.api.payments;

import com.paypal.base.rest.PayPalModel;


public class Credit  extends PayPalModel {

	/**
	 * Unique identifier of credit resource.
	 */
	private String id;

	/**
	 * specifies type of credit
	 */
	private String type;


	/**
	 * Default Constructor
	 */
	public Credit() {
	}

	/**
	 * Parameterized Constructor
	 */
	public Credit(String type) {
		this.type = type;
	}


	/**
	 * Setter for id
	 */
	public Credit setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Getter for id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Getter for type
	 */
	public String getType() {
		return this.type;
	}

}
