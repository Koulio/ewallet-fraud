package com.dozsa.ewallet.fraud.service;

import org.apache.log4j.Logger;

import com.dozsa.ewallet.fraud.model.Transaction;

public class Statistics {

	private static Logger logger = Logger.getLogger(Statistics.class);

	private int windowSizeInMiliSec;
	private int noOfWindows;
	private int firstWindow;
	private long[] statistics;

	public Statistics(int windowSizeInSec, int noOfWindows) {
		this.windowSizeInMiliSec = windowSizeInSec * 1000;
		this.noOfWindows = noOfWindows;
		statistics = new long[noOfWindows];
		firstWindow = (int) ((System.currentTimeMillis() / windowSizeInMiliSec) % noOfWindows);
	}

	public void increase(Transaction transaction) {
		long nowInSec = System.currentTimeMillis() / windowSizeInMiliSec;
		statistics[(int) (nowInSec % noOfWindows)]++;
	}

	public void printStatistics() {
		logger.info("Statistics:");
		for (int i = 0; i < noOfWindows; i++) {
			logger.info(statistics[(i + firstWindow) % noOfWindows] + " TPS");
		}
	}
}
