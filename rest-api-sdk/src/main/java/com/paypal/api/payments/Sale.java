package com.paypal.api.payments;

import com.paypal.base.rest.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Sale  extends PayPalResource {

	/**
	 * ID of the sale transaction.
	 */
	private String id;

	/**
	 * Identifier of the purchased unit associated with this object.
	 */
	private String purchaseUnitReferenceId;

	/**
	 * Amount being collected.
	 */
	private Amount amount;

	/**
	 * Specifies payment mode of the transaction. Only supported when the `payment_method` is set to `paypal`.
	 */
	private String paymentMode;

	/**
	 * State of the sale.
	 */
	private String state;

	/**
	 * Reason code for the transaction state being Pending or Reversed. Only supported when the `payment_method` is set to `paypal`.
	 */
	private String reasonCode;

	/**
	 * The level of seller protection in force for the transaction. Only supported when the `payment_method` is set to `paypal`. 
	 */
	private String protectionEligibility;

	/**
	 * The kind of seller protection in force for the transaction. It is returned only when protection_eligibility is ELIGIBLE or PARTIALLY_ELIGIBLE. Only supported when the `payment_method` is set to `paypal`.
	 */
	private String protectionEligibilityType;

	/**
	 * Expected clearing time for eCheck transactions. Only supported when the `payment_method` is set to `paypal`.
	 */
	private String clearingTime;

	/**
	 * Status of the Recipient Fund. For now, it will be returned only when fund status is held
	 */
	private String paymentHoldStatus;

	/**
	 * Reasons for PayPal holding recipient fund. It is set only if payment hold status is held
	 */
	private List<String> paymentHoldReasons;

	/**
	 * Transaction fee charged by PayPal for this transaction.
	 */
	private Currency transactionFee;

	/**
	 * Net amount the merchant receives for this transaction in their receivable currency. Returned only in cross-currency use cases where a merchant bills a buyer in a non-primary currency for that buyer.
	 */
	private Currency receivableAmount;

	/**
	 * Exchange rate applied for this transaction. Returned only in cross-currency use cases where a merchant bills a buyer in a non-primary currency for that buyer.
	 */
	private String exchangeRate;

	/**
	 * Fraud Management Filter (FMF) details applied for the payment that could result in accept, deny, or pending action. Returned in a payment response only if the merchant has enabled FMF in the profile settings and one of the fraud filters was triggered based on those settings. See [Fraud Management Filters Summary](/docs/classic/fmf/integration-guide/FMFSummary/) for more information.
	 */
	private FmfDetails fmfDetails;

	/**
	 * Receipt id is a payment identification number returned for guest users to identify the payment.
	 */
	private String receiptId;

	/**
	 * ID of the payment resource on which this transaction is based.
	 */
	private String parentPayment;

	/**
	 * Response codes returned by the processor concerning the submitted payment. Only supported when the `payment_method` is set to `credit_card`.
	 */
	private ProcessorResponse processorResponse;

	/**
	 * ID of the billing agreement used as reference to execute this transaction.
	 */
	private String billingAgreementId;

	/**
	 * Time of sale as defined in [RFC 3339 Section 5.6](http://tools.ietf.org/html/rfc3339#section-5.6)
	 */
	private String createTime;

	/**
	 * Time the resource was last updated in UTC ISO8601 format.
	 */
	private String updateTime;

	/**
	 * 
	 */
	private List<Links> links;

	/**
	 * Default Constructor
	 */
	public Sale() {
	}

	/**
	 * Parameterized Constructor
	 */
	public Sale(String id, Amount amount, String state, String parentPayment, String createTime) {
		this.id = id;
		this.amount = amount;
		this.state = state;
		this.parentPayment = parentPayment;
		this.createTime = createTime;
	}
	
	/**
	 * Obtain the Sale transaction resource for the given identifier.
	 * @deprecated Please use {@link #get(APIContext, String)} instead.
	 *
	 * @param accessToken
	 *            Access Token used for the API call.
	 * @param saleId
	 *            String
	 * @return Sale
	 * @throws PayPalRESTException
	 */
	public static Sale get(String accessToken, String saleId) throws PayPalRESTException {
		APIContext apiContext = new APIContext(accessToken);
		return get(apiContext, saleId);
	}

	/**
	 * Obtain the Sale transaction resource for the given identifier.
	 * @param apiContext
	 *            {@link APIContext} used for the API call.
	 * @param saleId
	 *            String
	 * @return Sale
	 * @throws PayPalRESTException
	 */
	public static Sale get(APIContext apiContext, String saleId) throws PayPalRESTException {

		if (saleId == null) {
			throw new IllegalArgumentException("saleId cannot be null");
		}
		Object[] parameters = new Object[] {saleId};
		String pattern = "v1/payments/sale/{0}";
		String resourcePath = RESTUtil.formatURIPath(pattern, parameters);
		String payLoad = "";
		return configureAndExecute(apiContext, HttpMethod.GET, resourcePath, payLoad, Sale.class);
	}


	/**
	 * Creates (and processes) a new Refund Transaction added as a related resource.
	 * @deprecated Please use {@link #refund(APIContext, Refund)} instead.
	 *
	 * @param accessToken
	 *            Access Token used for the API call.
	 * @param refund
	 *            Refund
	 * @return Refund
	 * @throws PayPalRESTException
	 */
	public Refund refund(String accessToken, Refund refund) throws PayPalRESTException {
		APIContext apiContext = new APIContext(accessToken);
		return refund(apiContext, refund);
	}

	/**
	 * Creates (and processes) a new Refund Transaction added as a related resource.
	 * @param apiContext
	 *            {@link APIContext} used for the API call.
	 * @param refund
	 *            Refund
	 * @return Refund
	 * @throws PayPalRESTException
	 */
	public Refund refund(APIContext apiContext, Refund refund) throws PayPalRESTException {

		if (this.getId() == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		if (refund == null) {
			throw new IllegalArgumentException("refund cannot be null");
		}
		Object[] parameters = new Object[] {this.getId()};
		String pattern = "v1/payments/sale/{0}/refund";
		String resourcePath = RESTUtil.formatURIPath(pattern, parameters);
		String payLoad = refund.toJSON();
		return configureAndExecute(apiContext, HttpMethod.POST, resourcePath, payLoad, Refund.class);
	}


}
