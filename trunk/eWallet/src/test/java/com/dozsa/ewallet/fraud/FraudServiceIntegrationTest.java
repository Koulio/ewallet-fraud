package com.dozsa.ewallet.fraud;

import java.sql.Timestamp;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.dispatch.Await;
import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;
import akka.util.Timeout;

import com.dozsa.ewallet.fraud.model.Customer;
import com.dozsa.ewallet.fraud.model.Transaction;
import com.dozsa.ewallet.fraud.service.AlertService;
import com.dozsa.ewallet.fraud.service.CustomerService;
import com.dozsa.ewallet.fraud.service.FraudService;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class FraudServiceIntegrationTest {

	private static Logger logger = Logger.getLogger(FraudServiceIntegrationTest.class);

	private Random randomGen = new Random(System.currentTimeMillis());
	private static final int nrOfInstances = 2;
	private long txnRefNoSequence = 1L;
	private static final int NO_OF_TXNS_TO_TEST = 1000000;
	private static final int NO_OF_PANS = 500000;
	private static final boolean synchronous = true;

	@Test
	public void testActorFraudService() {
		logger.info("Starting Spring app ctx...");
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "appctx-actor-fraud.xml" });

		final FraudService fraudService = (FraudService) appContext.getBean("fraudServiceBean");
		CustomerService customerService = (CustomerService) appContext.getBean("customerServiceBean");
		AlertService alertService = (AlertService) appContext.getBean("alertServiceBean");

		logger.info("Creating customers...");
		createCustomers(customerService);
		logger.info("Init fraud engine(s)...");
		fraudService.initFraudEngines();

		logger.info("Creating test actors...");
		Config config = ConfigFactory.load("test.conf");
		ActorSystem system = ActorSystem.create("test-system", config.getConfig("test-system").withFallback(config));
		final ActorRef aggregatorActor = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new TestAggregatorActor(NO_OF_TXNS_TO_TEST);
			}
		}));
		ActorRef testActor = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new TestActor(fraudService, aggregatorActor, synchronous);
			}
		}).withRouter(new RoundRobinRouter(nrOfInstances)).withDispatcher("test-dispatcher"));

		logger.info("Scoring txns...");
		long startTime = System.currentTimeMillis();

		for (int i = 1; i <= NO_OF_TXNS_TO_TEST; i++) {
			Transaction transaction = generateTransaction();
			testActor.tell(transaction);
		}

		Timeout testTimeout = new Timeout(Duration.parse("600 seconds"));
		Future future = Patterns.ask(aggregatorActor, "WAITING", testTimeout);
		try {
			long fraudCount = (Long) Await.result(future, testTimeout.duration());

			if (fraudCount == 0) {
				fraudCount = alertService.alertCount();
			}
			logger.info("Fraud count: " + fraudCount);
			long endTime = System.currentTimeMillis();
			logger.info("Total time: " + (endTime - startTime) / 1000 + " sec");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createCustomers(CustomerService customerService) {
		for (int i = 1; i <= NO_OF_PANS; i++) {
			String pan = String.valueOf(i);
			customerService.addCustomer(createCustomer(pan));
		}
	}

	private Customer createCustomer(String pan) {
		Customer customer = new Customer();
		customer.setPan(pan);
		customer.setCustomerName("cName" + pan);
		customer.setAddress("cAddress" + pan);
		customer.setBalance(0);
		customer.setNoOfTxn(0);
		return customer;
	}

	private Transaction generateTransaction() {
		Transaction transaction = new Transaction();
		String pan = String.valueOf(randomGen.nextInt(NO_OF_PANS) + 1);
		transaction.setTxnRefNo(txnRefNoSequence++);
		transaction.setPan(pan);
		transaction.setCustomerId("c" + pan);
		transaction.setAccountId("a" + pan);
		transaction.setMerchantId(String.valueOf(randomGen.nextInt(10)));
		transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
		transaction.setAmout(randomGen.nextInt(2000));
		return transaction;
	}

}
