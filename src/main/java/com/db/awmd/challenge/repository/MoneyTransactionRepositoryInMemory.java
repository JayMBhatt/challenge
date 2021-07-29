package com.db.awmd.challenge.repository;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.domain.AccountTransactions;

@Repository
public class MoneyTransactionRepositoryInMemory implements MoneyTransactionRepository {

	private final Map<Long, AccountTransactions> transactions = new ConcurrentHashMap<>();

	@Override
	public void createTransaction(AccountTransactions accountTransactions) {
		transactions.putIfAbsent(accountTransactions.getTransactionId(), accountTransactions);
	}

	@Override
	public List<AccountTransactions> getAccountTransactions() {
		return transactions.values().stream().collect(toList());
	}

}
