package com.db.awmd.challenge.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccountTransactions {

	private long transactionId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date transactionDate;

	@NotNull
	@NotEmpty
	private String accountIdFrom;

	@NotNull
	@NotEmpty
	private String accountIdTo;

	@NotNull
	@Min(value = 0,message = "Your transfer balance must be positive.")
	private BigDecimal amount;

	@JsonCreator
	public AccountTransactions(@JsonProperty("transactionId") long transactionId,
			@JsonProperty(value = "transactionDate") Date transactionDate,
			@JsonProperty("accountIdFrom") String accountIdFrom, @JsonProperty("accountIdTo") String accountIdTo,
			@JsonProperty("amount") BigDecimal amount) {
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.accountIdFrom = accountIdFrom;
		this.accountIdTo = accountIdTo;
		this.amount = amount;
	}

}
