package com.dozsa.ewallet.fraud.actors;

import org.apache.log4j.Logger;

import akka.actor.UntypedActor;

import com.dozsa.ewallet.fraud.engine.FraudEngine;
import com.dozsa.ewallet.fraud.model.Alert;
import com.dozsa.ewallet.fraud.model.Customer;
import com.dozsa.ewallet.fraud.model.Transaction;
import com.dozsa.ewallet.fraud.service.AlertService;

public class CustomerFraudActor extends UntypedActor {

	private static Logger logger = Logger.getLogger(CustomerFraudActor.class);

	private Customer customer;
	private FraudEngine fraudEngine;
	private AlertService alertService;

	public CustomerFraudActor(Customer customer, FraudEngine fraudEngine, AlertService alertService) {
		this.customer = customer;
		this.fraudEngine = fraudEngine;
		this.alertService = alertService;
	}

	@Override
	public void onReceive(Object msg) {
		if (msg instanceof Request) {
			Request request = (Request) msg;
			Transaction transaction = request.getTransaction();

			boolean isFraud = fraudEngine.isFraud(transaction);

			if (request.isSynchronous()) {
				getSender().tell(isFraud);
			} else if (isFraud) {
				alertService.addAlert(new Alert(customer, transaction));
			}
		} else {
			logger.error("Unknown message type: " + msg.getClass());
		}
	}
}
