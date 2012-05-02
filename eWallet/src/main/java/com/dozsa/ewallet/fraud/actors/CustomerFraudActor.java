package com.dozsa.ewallet.fraud.actors;

import org.apache.log4j.Logger;

import akka.actor.UntypedActor;

import com.dozsa.ewallet.fraud.engine.FraudEngine;
import com.dozsa.ewallet.fraud.model.Customer;
import com.dozsa.ewallet.fraud.model.Transaction;

public class CustomerFraudActor extends UntypedActor {

	private static Logger logger = Logger.getLogger(CustomerFraudActor.class);

	private Customer customer;
	private FraudEngine fraudEngine;

	public CustomerFraudActor(Customer customer, FraudEngine fraudEngine) {
		this.customer = customer;
		this.fraudEngine = fraudEngine;
	}

	@Override
	public void onReceive(Object msg) {
		if (msg instanceof Transaction) {
			Transaction transaction = (Transaction) msg;
			// logger.info("scoring txn " + transaction.getTxnRefNo() +
			// " on thread " + Thread.currentThread());

			boolean isFraud = fraudEngine.isFraud(transaction);

			getSender().tell(isFraud);
		} else {
			logger.error("Unknown message type: " + msg.getClass());
		}
	}
}
