package com.dozsa.ewallet.fraud.model;

import java.sql.Timestamp;

public class Transaction {

	private long txnRefNo;
	private String pan;
	private String customerId;
	private String customerName;
	private String accountId;
	private String merchantId;
	private String merchantName;
	private Timestamp transactionDate;
	private long amout;

	public long getTxnRefNo() {
		return txnRefNo;
	}

	public void setTxnRefNo(long txnRefNo) {
		this.txnRefNo = txnRefNo;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public long getAmout() {
		return amout;
	}

	public void setAmout(long amout) {
		this.amout = amout;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

}
