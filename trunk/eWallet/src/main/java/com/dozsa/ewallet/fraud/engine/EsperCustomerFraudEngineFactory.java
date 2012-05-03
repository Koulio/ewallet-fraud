/*
 *  Copyright (c) 2010 - 2030 by ACI Worldwide Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ACI Worldwide Inc ("Confidential Information").  You shall not disclose
 *  such Confidential Information and shall use it only in accordance with the terms
 *  of the license agreement you entered with ACI Worldwide Inc.
 *
 * @Created On: May 3, 2012
 */
package com.dozsa.ewallet.fraud.engine;

import com.dozsa.ewallet.fraud.model.Customer;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

public class EsperCustomerFraudEngineFactory implements FraudEngineFactory {

	private EPServiceProvider serviceProvider;

	public EsperCustomerFraudEngineFactory() {
		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.dozsa.ewallet.fraud.model");
		serviceProvider = EPServiceProviderManager.getDefaultProvider(config);
	}

	public FraudEngine newFraudEngine(Customer customer) {
		return new EsperCustomerFraudEngine(serviceProvider, customer);
	}

}
