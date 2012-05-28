package com.dozsa.ewallet.fraud.service;

import java.util.Properties;

import org.apache.log4j.Logger;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.dispatch.Await;
import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;

import com.dozsa.ewallet.fraud.actors.CustomerFraudActor;
import com.dozsa.ewallet.fraud.actors.CustomerRouter;
import com.dozsa.ewallet.fraud.actors.Request;
import com.dozsa.ewallet.fraud.engine.FraudEngineFactory;
import com.dozsa.ewallet.fraud.model.Transaction;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ActorBasedFraudService implements FraudService {

	private static Logger logger = Logger.getLogger(ActorBasedFraudService.class);

	private Properties properties;
	private TransactionService transactionService;
	private CustomerService customerService;
	private AlertService alertService;
	private FraudEngineFactory fraudEngineFactory;
	private ActorSystem system;
	private ActorRef routedActor;
	private Timeout requestTimeout;

	public ActorBasedFraudService(Properties properties, TransactionService transactionService,
			CustomerService customerService, AlertService alertService, FraudEngineFactory fraudEngineFactory) {
		this.properties = properties;
		this.transactionService = transactionService;
		this.alertService = alertService;
		this.customerService = customerService;
		this.fraudEngineFactory = fraudEngineFactory;
		requestTimeout = new Timeout(Duration.parse("10 seconds"));
	}

	public void initFraudEngines() {
		Config config = ConfigFactory.load();
		system = ActorSystem.create("ewallet-system", config.getConfig("ewallet-system").withFallback(config));

		SupervisorStrategy strategy = new OneForOneStrategy(-1, Duration.Inf(), new Class<?>[] { Exception.class });
		routedActor = system.actorOf(new Props(CustomerFraudActor.class).withRouter(new CustomerRouter(customerService
				.getCustomersList(), fraudEngineFactory, alertService, strategy)), "ewallet-router");
	}

	public boolean isFraudWithReply(Transaction transaction) {
		Boolean response = false;
		try {
			Request request = new Request(transaction, true);
			Future future = Patterns.ask(routedActor, request, requestTimeout);

			response = (Boolean) Await.result(future, requestTimeout.duration());
		} catch (Exception e) {
			logger.error("timeout on txn: " + transaction.getTxnRefNo());
			e.printStackTrace();
		}
		return response;
	}

	public void isFraudWithAlert(Transaction transaction) {
		Request request = new Request(transaction, false);
		routedActor.tell(request);
	}
}
