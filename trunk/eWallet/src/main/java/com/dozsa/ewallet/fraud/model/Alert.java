package com.dozsa.ewallet.fraud.model;

public class Alert {

	private Customer customer;
	private Transaction transaction;

	public Alert(Transaction transaction) {
		this.transaction = transaction;
	}

	public Alert(Customer customer, Transaction transaction) {
		this.customer = customer;
		this.transaction = transaction;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}
