package com.dozsa.ewallet.fraud.service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dozsa.ewallet.fraud.model.Transaction;

public class TransactionService {

	private Map<Long, Transaction> transactions;

	public TransactionService() {
		transactions = new ConcurrentHashMap<Long, Transaction>();
	}

	public TransactionService(Map<Long, Transaction> transactions) {
		this();
		this.transactions.putAll(transactions);
	}

	public void addTransaction(Transaction transaction) {
		transactions.put(transaction.getTxnRefNo(), transaction);
	}

	public Transaction getTransaction(String txnRefNo) {
		return transactions.get(txnRefNo);
	}

	public Map<Long, Transaction> getAllTransactions() {
		return Collections.unmodifiableMap(transactions);
	}
}
