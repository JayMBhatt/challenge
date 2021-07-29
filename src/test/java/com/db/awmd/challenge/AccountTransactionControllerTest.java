package com.db.awmd.challenge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AccountTransactionControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void prepareMockMvc() {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
		this.accountsRepository.clearAccounts();
		String uniqueAccountFromId = "Id-3";
		Account accountFrom = new Account(uniqueAccountFromId, new BigDecimal("100"));
		this.accountsRepository.createAccount(accountFrom);

		String uniqueAccountToId = "Id-4";
		Account accountTo = new Account(uniqueAccountToId, new BigDecimal("90"));
		this.accountsRepository.createAccount(accountTo);

	}

	@Test
	public void checkNegativeFundTransfer() throws Exception {
		this.mockMvc.perform(post("/v1/money/transactions").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "    \"accountIdFrom\":\"Id-3\",\n" + "    \"accountIdTo\":\"Id-4\",\n"
						+ "    \"amount\":-90\n" + "}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void checkFundTransferC() throws Exception {
		this.mockMvc.perform(post("/v1/money/transactions").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "    \"accountIdFrom\":\"Id-3\",\n" + "    \"accountIdTo\":\"Id-4\",\n"
						+ "    \"amount\":90\n" + "}"))
				.andExpect(status().isAccepted());
	}

}
