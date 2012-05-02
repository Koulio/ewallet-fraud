package com.dozsa.ewallet.fraud.model;

public class Customer {

	private String pan;
	private String customerName;
	private String address;
	private long balance;
	private long noOfTxn;

	public Customer() {
	}

	public Customer(String pan) {
		this.pan = pan;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public long getNoOfTxn() {
		return noOfTxn;
	}

	public void setNoOfTxn(long noOfTxn) {
		this.noOfTxn = noOfTxn;
	}

	public void increaseNoOfTxn() {
		this.noOfTxn++;
	}
}
