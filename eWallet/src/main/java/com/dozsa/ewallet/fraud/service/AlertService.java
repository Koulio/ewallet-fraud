package com.dozsa.ewallet.fraud.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.dozsa.ewallet.fraud.model.Alert;

public class AlertService {

	private Queue<Alert> alerts;

	public AlertService() {
		alerts = new ConcurrentLinkedQueue<Alert>();
	}

	public void addAlert(Alert alert) {
		alerts.add(alert);
	}

	public Alert getNextAlert() {
		return alerts.remove();
	}

	public long alertCount() {
		return alerts.size();
	}
}
