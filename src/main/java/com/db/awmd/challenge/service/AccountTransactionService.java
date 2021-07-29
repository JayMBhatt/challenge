package com.db.awmd.challenge.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AccountTransactions;
import com.db.awmd.challenge.exception.MoneyTransactionException;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.MoneyTransactionRepository;

@Service
public class AccountTransactionService {

	private final AccountsRepository accountsRepository;

	private final MoneyTransactionRepository moneyTransactionRepository;

	private final NotificationService notificationService;

	@Autowired
	public AccountTransactionService(AccountsRepository accountsRepository,
			MoneyTransactionRepository moneyTransactionRepository, NotificationService notificationService) {
		this.accountsRepository = accountsRepository;
		this.moneyTransactionRepository = moneyTransactionRepository;
		this.notificationService = notificationService;
	}

	public void moneyTransfer(AccountTransactions accountTransactions) throws MoneyTransactionException {
		Account accountFrom = accountsRepository.getAccount(accountTransactions.getAccountIdFrom());
		if (accountFrom == null) {
			throw new MoneyTransactionException(
					"AccountId " + accountTransactions.getAccountIdFrom() + " does not exist");
		}

		if (accountFrom.getBalance().doubleValue() <= accountTransactions.getAmount().doubleValue()) {
			throw new MoneyTransactionException(
					"Insufficient fund at AccountId " + accountTransactions.getAccountIdFrom());
		}

		if (accountTransactions.getAccountIdFrom().equalsIgnoreCase(accountTransactions.getAccountIdTo())) {
			throw new MoneyTransactionException(
					"Money Transfer sender and recever Account is same so we can not transfer");

		}
		Account accountTo = accountsRepository.getAccount(accountTransactions.getAccountIdTo());
		if (accountTo == null) {
			throw new MoneyTransactionException(
					"AccountId " + accountTransactions.getAccountIdTo() + " does not exist");
		}

		accountFrom.setBalance(accountFrom.getBalance().subtract(accountTransactions.getAmount()));
		accountTo.setBalance(accountTo.getBalance().add(accountTransactions.getAmount()));
		accountsRepository.updateAccount(accountFrom);
		accountsRepository.updateAccount(accountTo);
		Date currentDate = new Date();
		moneyTransactionRepository.createTransaction(
				new AccountTransactions(currentDate.getTime(), currentDate, accountTransactions.getAccountIdFrom(),
						accountTransactions.getAccountIdTo(), accountTransactions.getAmount()));
		notificationService.notifyAboutTransfer(accountFrom, "Amount $" + accountTransactions.getAmount()
				+ " has been debited from your account:" + accountFrom.getAccountId());
		notificationService.notifyAboutTransfer(accountTo, "Amount $" + accountTransactions.getAmount()
				+ " has been credited to your account:" + accountTo.getAccountId());

	}

	public List<AccountTransactions> showAllTransactions() {
		return moneyTransactionRepository.getAccountTransactions();
	}
}
