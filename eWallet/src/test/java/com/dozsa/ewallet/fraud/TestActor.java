package com.dozsa.ewallet.fraud;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import com.dozsa.ewallet.fraud.model.Transaction;
import com.dozsa.ewallet.fraud.service.FraudService;

public class TestActor extends UntypedActor {

	private FraudService fraudService;
	private ActorRef aggregatorActor;
	private boolean synchronous;

	public TestActor(FraudService fraudService, ActorRef aggregatorActor, boolean synchronous) {
		this.fraudService = fraudService;
		this.aggregatorActor = aggregatorActor;
		this.synchronous = synchronous;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Transaction) {
			Transaction transaction = (Transaction) msg;
			if (synchronous) {
				Boolean isFraud = fraudService.isFraudWithReply(transaction);
				aggregatorActor.tell(isFraud);
			} else {
				fraudService.isFraudWithAlert(transaction);
				aggregatorActor.tell(new Integer(1));
			}
		}
	}

}
