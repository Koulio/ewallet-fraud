package com.dozsa.ewallet.fraud.engine;

import com.dozsa.ewallet.fraud.model.Customer;
import com.dozsa.ewallet.fraud.model.Transaction;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class EsperCustomerFraudEngine implements FraudEngine {

	private EPServiceProvider epService;
	private Customer customer;
	private boolean eventFired;

	public EsperCustomerFraudEngine(EPServiceProvider epService, final Customer customer) {
		this.epService = epService;
		this.customer = customer;

		String expression =//@formatter:off
				"select count(*) as count "
				+ " from Transaction(pan='" + customer.getPan() + "', amout>1000).win:time(1 sec) " 
				+ " having count(*) > 2 ";
				//@formatter:on
		EPStatement statement = epService.getEPAdministrator().createEPL(expression);

		UpdateListener listener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				EventBean event = newEvents[0];
				eventFired = true;
			}
		};
		statement.addListener(listener);
	}

	public boolean isFraud(Transaction transaction) {
		eventFired = false;
		epService.getEPRuntime().sendEvent(transaction);
		return eventFired;
	}

}
