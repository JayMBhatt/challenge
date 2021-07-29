package com.db.awmd.challenge.repository;

import java.util.List;

import com.db.awmd.challenge.domain.AccountTransactions;

public interface MoneyTransactionRepository {

	void createTransaction(AccountTransactions accountTransactions);

	List<AccountTransactions> getAccountTransactions();

}
