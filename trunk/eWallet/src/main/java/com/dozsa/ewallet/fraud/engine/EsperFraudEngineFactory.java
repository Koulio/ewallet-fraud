package com.dozsa.ewallet.fraud.engine;

import com.dozsa.ewallet.fraud.model.Customer;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

public class EsperFraudEngineFactory implements FraudEngineFactory {

	private EPServiceProvider serviceProvider;
	private EsperFraudEngine fraudEngine;

	public EsperFraudEngineFactory() {
		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.dozsa.ewallet.fraud.model");
		serviceProvider = EPServiceProviderManager.getDefaultProvider(config);

		fraudEngine = new EsperFraudEngine(serviceProvider);
	}

	public FraudEngine newFraudEngine(Customer customer) {
		return fraudEngine;
	}

}
