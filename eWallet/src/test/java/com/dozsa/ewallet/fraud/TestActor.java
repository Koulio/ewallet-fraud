package com.dozsa.ewallet.fraud;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import com.dozsa.ewallet.fraud.model.Transaction;
import com.dozsa.ewallet.fraud.service.FraudService;

public class TestActor extends UntypedActor {

	private FraudService fraudService;
	private ActorRef aggregatorActor;

	public TestActor(FraudService fraudService, ActorRef aggregatorActor) {
		this.fraudService = fraudService;
		this.aggregatorActor = aggregatorActor;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Transaction) {
			Transaction transaction = (Transaction) msg;
			Boolean isFraud = fraudService.isFraud(transaction);
			aggregatorActor.tell(isFraud);
		}
	}

}
