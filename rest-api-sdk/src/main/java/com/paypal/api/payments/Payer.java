package com.paypal.api.payments;

import com.paypal.base.rest.PayPalModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Payer  extends PayPalModel {

	/**
	 * Payment method being used - PayPal Wallet payment, Bank Direct Debit  or Direct Credit card.
	 */
	private String paymentMethod;

	/**
	 * Status of payer's PayPal Account.
	 */
	private String status;

	/**
	 * Type of account relationship payer has with PayPal.
	 */
	private String accountType;

	/**
	 * Duration since the payer established account relationship with PayPal in days.
	 */
	private String accountAge;

	/**
	 * List of funding instruments to fund the payment. 'OneOf' funding_instruments,funding_option_id to be used to identify the specifics of payment method passed.
	 */
	private List<FundingInstrument> fundingInstruments;

	/**
	 * Id of user selected funding option for the payment.'OneOf' funding_instruments,funding_option_id to be used to identify the specifics of payment method passed.
	 */
	private String fundingOptionId;

	/**
	 * Default funding option available for the payment 
	 */
	private FundingOption fundingOption;

	/**
	 * Funding option related to default funding option.
	 */
	private FundingOption relatedFundingOption;

	/**
	 * Information related to the Payer. 
	 */
	private PayerInfo payerInfo;

	/**
	 * Default Constructor
	 */
	public Payer() {
	}
}
