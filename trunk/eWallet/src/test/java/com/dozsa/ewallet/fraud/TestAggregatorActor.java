package com.dozsa.ewallet.fraud;

import org.apache.log4j.Logger;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class TestAggregatorActor extends UntypedActor {

	private static Logger logger = Logger.getLogger(TestAggregatorActor.class);

	private long noOfMessages;
	private long startTime;
	private long messageCount = 0;
	private long fraudCount = 0;
	private ActorRef mainTestClass;

	public TestAggregatorActor(long noOfMessages, long startTime) {
		this.noOfMessages = noOfMessages;
		this.startTime = startTime;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Boolean) {
			Boolean isFraud = (Boolean) msg;
			messageCount++;

			if (messageCount % 10000 == 0) {
				logger.info("received " + messageCount);
			}
			if (isFraud) {
				fraudCount++;
			}
			if (messageCount == noOfMessages) {
				logger.info("Fraud count: " + fraudCount);
				long endTime = System.currentTimeMillis();
				logger.info("Total time: " + (endTime - startTime) / 1000 + " sec");
				if (mainTestClass != null) {
					mainTestClass.tell("DONE");
				}
			}
		}
		if (msg instanceof String) {
			String s = (String) msg;
			if ("WAITING".equals(s)) {
				mainTestClass = getSender();
				if (messageCount == noOfMessages) {
					mainTestClass.tell("DONE");
				}
			}
		}
	}

}
