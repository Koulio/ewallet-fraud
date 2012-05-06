package com.dozsa.ewallet.fraud;

import org.apache.log4j.Logger;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class TestAggregatorActor extends UntypedActor {

	private static Logger logger = Logger.getLogger(TestAggregatorActor.class);

	private long noOfMessages;
	private long messageCount = 0;
	private long fraudCount = 0;
	private ActorRef mainTestClass;

	public TestAggregatorActor(long noOfMessages) {
		this.noOfMessages = noOfMessages;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Boolean) {
			Boolean isFraud = (Boolean) msg;
			messageCount++;

			if (messageCount % 10000 == 0) {
				logger.info("received " + messageCount + " messages");
			}
			if (isFraud) {
				fraudCount++;
			}
			if (messageCount == noOfMessages) {
				if (mainTestClass != null) {
					mainTestClass.tell(fraudCount);
				}
			}
		} else if (msg instanceof Integer) {
			Integer i = (Integer) msg;
			messageCount++;

			if (messageCount % 10000 == 0) {
				logger.info("received " + messageCount);
			}
			if (messageCount == noOfMessages) {
				if (mainTestClass != null) {
					mainTestClass.tell(fraudCount);
				}
			}
		} else if (msg instanceof String) {
			String s = (String) msg;
			if ("WAITING".equals(s)) {
				mainTestClass = getSender();
				if (messageCount == noOfMessages) {
					mainTestClass.tell(fraudCount);
				}
			}
		}
	}

}
