package com.dozsa.ewallet.fraud.actors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.dispatch.Dispatchers;
import akka.routing.CustomRoute;
import akka.routing.CustomRouterConfig;
import akka.routing.Destination;
import akka.routing.RouteeProvider;

import com.dozsa.ewallet.fraud.engine.FraudEngineFactory;
import com.dozsa.ewallet.fraud.model.Customer;
import com.dozsa.ewallet.fraud.model.Transaction;

public class CustomerRouter extends CustomRouterConfig {

	private static Logger logger = Logger.getLogger(CustomerRouter.class);

	private List<Customer> customers;
	private Map<String, ActorRef> customerActors;
	private FraudEngineFactory fraudEngineFactory;

	public CustomerRouter(List<Customer> customers, FraudEngineFactory fraudEngineFactory) {
		this.customers = customers;
		this.fraudEngineFactory = fraudEngineFactory;
		customerActors = new ConcurrentHashMap<String, ActorRef>();
	}

	public String routerDispatcher() {
		return Dispatchers.DefaultDispatcherId();
	}

	public SupervisorStrategy supervisorStrategy() {
		return SupervisorStrategy.defaultStrategy();
	}

	private ActorRef buildActor(RouteeProvider routeeProvider, final Customer customer) {
		return routeeProvider.context().actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new CustomerFraudActor(customer, fraudEngineFactory.newFraudEngine(customer));
			}
		}).withDispatcher("ewallet-dispatcher"), customer.getPan());
	}

	@Override
	public CustomRoute createCustomRoute(Props props, final RouteeProvider routeeProvider) {
		for (Customer customer : customers) {
			customerActors.put(customer.getPan(), buildActor(routeeProvider, customer));
		}
		routeeProvider.registerRoutees(new ArrayList<ActorRef>(customerActors.values()));

		return new CustomRoute() {
			private Logger logger = Logger.getLogger(CustomRoute.class);

			public Iterable<Destination> destinationsFor(ActorRef sender, Object msg) {
				if (msg instanceof Transaction) {
					Transaction transaction = (Transaction) msg;
					String pan = transaction.getPan();
					if (!customerActors.containsKey(pan)) {
						ActorRef actor = buildActor(routeeProvider, new Customer(pan));
						customerActors.put(pan, actor);
						routeeProvider.registerRoutees(Arrays.asList(new ActorRef[] { actor }));
					}
					ActorRef destination = customerActors.get(pan);
					return Arrays.asList(new Destination[] { new Destination(sender, destination) });
				} else {
					logger.error("Unknown message type: " + msg.getClass());
					throw new IllegalArgumentException("Unknown message type: " + msg.getClass());
				}
			}
		};
	}

}
