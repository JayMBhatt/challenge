package com.db.awmd.challenge.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.AccountTransactions;
import com.db.awmd.challenge.exception.MoneyTransactionException;
import com.db.awmd.challenge.service.AccountTransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/money/transactions")
@Slf4j
public class AccountTransactionsController {

	private final AccountTransactionService accountTransactionService;

	@Autowired
	public AccountTransactionsController(AccountTransactionService accountTransactionService) {
		this.accountTransactionService = accountTransactionService;
	}

	@PostMapping
	public ResponseEntity<Object> transferMoney(@RequestBody @Valid AccountTransactions accountTransactions) {

		try {
			accountTransactionService.moneyTransfer(accountTransactions);
			log.info("Money Transfer {}", accountTransactions);
		} catch (MoneyTransactionException e) {
			log.error("Money Transaction cancel: " + accountTransactions);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@GetMapping
	public List<AccountTransactions> showTransactions() {
		return accountTransactionService.showAllTransactions();
	}
}
