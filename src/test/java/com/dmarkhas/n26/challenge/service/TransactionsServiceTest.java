package com.dmarkhas.n26.challenge.service;

import com.dmarkhas.n26.challenge.configuration.TransactionsConfiguration;
import com.dmarkhas.n26.challenge.entity.StatisticsModel;
import com.dmarkhas.n26.challenge.entity.TransactionModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

@EnableConfigurationProperties(TransactionsConfiguration.class)
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {TransactionsService.class}, initializers = ConfigFileApplicationContextInitializer.class)
public class TransactionsServiceTest {


    @Autowired
    private TransactionsService transactionService;

    @Autowired
    private TransactionsConfiguration transactionsConfiguration;

    @Test
    public void aggregateOnlyValidTransactions()
    {
        long timestamp = System.currentTimeMillis();
        TransactionModel transaction1 = new TransactionModel();
        TransactionModel transaction2 = new TransactionModel();
        transaction1.setTimestamp(timestamp);
        transaction1.setAmount(10);
        transaction2.setTimestamp(timestamp);
        transaction2.setAmount(20);

        transactionService.addTransaction(transaction1);
        transactionService.addTransaction(transaction2);

        StatisticsModel stats = transactionService.getStatistics();
        assertEquals(15,stats.getAvg(),0);
        assertEquals(20,stats.getMax(),0);
        assertEquals(2,stats.getCount(),0);
        assertEquals(10,stats.getMin(),0);
        assertEquals(30,stats.getSum(),0);

    }

    @Test
    public void aggregateWithInvalidTransactions()
    {
        long timestamp = System.currentTimeMillis();
        TransactionModel transaction1 = new TransactionModel();
        TransactionModel transaction2 = new TransactionModel();
        TransactionModel transaction3 = new TransactionModel();
        transaction1.setTimestamp(timestamp);
        transaction1.setAmount(10);
        transaction2.setTimestamp(timestamp);
        transaction2.setAmount(20);
        transaction3.setTimestamp(timestamp - 80 * 1000);
        transaction3.setAmount(30);

        transactionService.addTransaction(transaction1);
        transactionService.addTransaction(transaction2);
        transactionService.addTransaction(transaction3);

        StatisticsModel stats = transactionService.getStatistics();

        assertEquals(15,stats.getAvg(),0);
        assertEquals(20,stats.getMax(),0);
        assertEquals(2,stats.getCount(),0);
        assertEquals(10,stats.getMin(),0);
        assertEquals(30,stats.getSum(),0);

    }


    @Test
    public void aggregateWithExpiredTransactions() throws InterruptedException
    {
        long timestamp = System.currentTimeMillis();
        TransactionModel transaction1 = new TransactionModel();
        TransactionModel transaction2 = new TransactionModel();
        transaction1.setTimestamp(timestamp);
        transaction1.setAmount(10);
        transaction2.setTimestamp(timestamp + 1000);
        transaction2.setAmount(20);

        // Set maxMsToKeep to low value
        ReflectionTestUtils.setField(transactionsConfiguration,"maxMsToKeep",110);
        transactionService.addTransaction(transaction1);
        transactionService.addTransaction(transaction2);
        Thread.sleep(110L);

        StatisticsModel stats = transactionService.getStatistics();

        assertEquals(20,stats.getAvg(),0);
        assertEquals(20,stats.getMax(),0);
        assertEquals(1,stats.getCount(),0);
        assertEquals(20,stats.getMin(),0);
        assertEquals(20,stats.getSum(),0);
    }

}