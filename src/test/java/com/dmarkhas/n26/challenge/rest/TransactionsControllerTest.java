package com.dmarkhas.n26.challenge.rest;

import com.dmarkhas.n26.challenge.api.TransactionDto;
import com.dmarkhas.n26.challenge.entity.StatisticsModel;
import com.dmarkhas.n26.challenge.service.TransactionsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TransactionsController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionsControllerTest {

    private final double TEST_MAX = 41.5;
    private final double TEST_AVG = 39.1;
    private final double TEST_MIN = 11.1;
    private final double TEST_SUM = 87.2;
    private final long TEST_COUNT = 7;

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
        StatisticsModel testStatistics = new StatisticsModel();
        testStatistics.setMax(TEST_MAX);
        testStatistics.setMin(TEST_MIN);
        testStatistics.setAvg(TEST_AVG);
        testStatistics.setSum(TEST_SUM);
        testStatistics.setCount(TEST_COUNT);
        Mockito.when(transactionsService.getStatistics()).thenReturn(testStatistics);
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
    public void getStatistics() throws Exception{

        mockMvc.perform(get("/statistics")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum", Matchers.is(TEST_SUM)))
                .andExpect(jsonPath("$.avg", Matchers.is(TEST_AVG)))
                .andExpect(jsonPath("$.max", Matchers.is(TEST_MAX)))
                .andExpect(jsonPath("$.min", Matchers.is(TEST_MIN)))
                .andExpect(jsonPath("$.count", Matchers.is(TEST_COUNT)));

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