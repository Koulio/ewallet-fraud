package com.dozsa.ewallet.fraud.actors;

import com.dozsa.ewallet.fraud.model.Customer;
import com.dozsa.ewallet.fraud.model.Transaction;

public class Request {

	private Transaction transaction;
	private Customer customer;
	private boolean synchronous;

	public Request(Transaction transaction, boolean synchronous) {
		this.transaction = transaction;
		this.synchronous = synchronous;
	}

	public Request(Transaction transaction, Customer customer, boolean synchronous) {
		this.transaction = transaction;
		this.customer = customer;
		this.synchronous = synchronous;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isSynchronous() {
		return synchronous;
	}

	public void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
	}
}
