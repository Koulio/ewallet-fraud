package com.dozsa.ewallet.fraud.engine;

import com.dozsa.ewallet.fraud.model.Customer;
import com.dozsa.ewallet.fraud.model.Transaction;

public class SimpleFraudEngine implements FraudEngine {

	private Customer customer;
	private Transaction lastTransaction;

	public SimpleFraudEngine(Customer customer) {
		this.customer = customer;
	}

	public synchronized boolean isFraud(Transaction transaction) {
		boolean isFraud = false;
		// two txn above 1000 less then a second apart
		if ((lastTransaction != null)
				&& (transaction.getTransactionDate().getTime() - lastTransaction.getTransactionDate().getTime() < 1L)
				&& (transaction.getAmout() > 1000) && (lastTransaction.getAmout() > 1000)) {
			isFraud = true;
		} else {
			isFraud = false;
		}

		lastTransaction = transaction;

		return isFraud;
	}
}
