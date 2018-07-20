package com.dmarkhas.n26.challenge.rest;

import com.dmarkhas.n26.challenge.api.TransactionDto;
import com.dmarkhas.n26.challenge.service.TransactionsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TransactionsController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionsControllerTest {

    @MockBean
    private TransactionsService transactionsService;

    @Autowired
    private TransactionsController controller;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Value("${transactions.maxMsToKeep}")
    private int maxMsToKeep;

    @PostConstruct
    public void init()
    {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void submitValidTransaction() throws Exception {

        TransactionDto transactionRequest = new TransactionDto();
        transactionRequest.setAmount(100);
        transactionRequest.setTimestamp(System.currentTimeMillis());

        String bla = objectMapper.writeValueAsString(transactionRequest);

        mockMvc.perform(post("/transactions")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(transactionRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void submitInvalidTransaction() throws Exception {

        TransactionDto transactionRequest = new TransactionDto();
        transactionRequest.setAmount(100);
        transactionRequest.setTimestamp(System.currentTimeMillis() - (maxMsToKeep + 10) * 1000);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(transactionRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getStatistics() {
    }

    @TestConfiguration
    public static class Conf
    {
        @Bean
        public TransactionsMapper getTransactionsMapper() { return Mappers.getMapper(TransactionsMapper.class);}

        @Bean
        public ObjectMapper getObjectMapper() { return new ObjectMapper(); }
    }
}