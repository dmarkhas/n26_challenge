package com.dmarkhas.n26.challenge.service;

import com.dmarkhas.n26.challenge.configuration.TransactionsConfiguration;
import com.dmarkhas.n26.challenge.entity.StatisticsAggregator;
import com.dmarkhas.n26.challenge.entity.StatisticsModel;
import com.dmarkhas.n26.challenge.entity.TransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@Slf4j
@Service
public class TransactionsService {

    private Map<Long,StatisticsAggregator> transactionsAggregator;

    @Autowired
    private TransactionsConfiguration transactionsConfiguration;

    public TransactionsService()
    {
        this.transactionsAggregator = new ConcurrentHashMap<>();
    }

    public void addTransaction(TransactionModel transactionModel) {

        long bucket = getTransactionSecond(transactionModel);

        // thread safe update of the map
        transactionsAggregator
                .compute(bucket, (k,v) -> (v == null) ? new StatisticsAggregator(transactionModel) : v.addTransaction(transactionModel));

    }

    // Iterate over the buckets - old buckets should be disposed of, relevant buckets are aggregated
    public StatisticsModel getStatistics()
    {
        StatisticsModel result = new StatisticsModel();

        transactionsAggregator
                .entrySet()
                .stream()
                .filter(entry -> isBucketValid(entry.getKey()))
                .forEach(entry -> result.aggregate(entry.getValue()));

        return result;
    }


    private long getTransactionSecond(TransactionModel tmodel)
    {
        return tmodel.getTimestamp() / 1000;
    }

    private boolean isBucketValid(Long second)
    {
        return transactionsConfiguration.getMaxMsToKeep() > (System.currentTimeMillis() - (second * 1000));
    }

    @Scheduled(initialDelay = 10000, fixedRate = 30000)
    private void trimStatistics()
    {
        log.info("Trimming Statistics");
        transactionsAggregator.entrySet().removeIf(k -> !isBucketValid(k.getKey()));
    }

}
