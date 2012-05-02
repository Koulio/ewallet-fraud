package com.dozsa.ewallet.fraud.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.dozsa.ewallet.fraud.model.Transaction;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class EsperFraudEngine implements FraudEngine {

	private static Logger logger = Logger.getLogger(EsperFraudEngine.class);

	private EPServiceProvider epService;
	private Map<String, Boolean> eventFired;

	public EsperFraudEngine(EPServiceProvider epService) {
		this.epService = epService;
		eventFired = new ConcurrentHashMap<String, Boolean>();

		String expression =//@formatter:off
				"select pan, count(*) as count "
				+ " from Transaction.win:time(1 sec) " 
				+ " where amout > 1000 "
				+ " group by pan "
				+ " having count(*) > 2 ";
				//@formatter:on
		EPStatement statement = epService.getEPAdministrator().createEPL(expression);

		UpdateListener listener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				EventBean event = newEvents[0];
				eventFired.put((String) event.get("pan"), true);
			}
		};
		statement.addListener(listener);
	}

	public boolean isFraud(Transaction transaction) {
		eventFired.put(transaction.getPan(), false);
		epService.getEPRuntime().sendEvent(transaction);
		return eventFired.get(transaction.getPan());
	}
}
