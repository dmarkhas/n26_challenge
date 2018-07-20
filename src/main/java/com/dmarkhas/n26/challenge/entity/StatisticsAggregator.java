package com.dmarkhas.n26.challenge.entity;

import lombok.Data;

@Data
public class StatisticsAggregator {

    private long second;

    private double sum;

    private double avg;

    private double max;

    private double min;

    private int count;

    public StatisticsAggregator(TransactionModel transaction)
    {
        this.second = transaction.getTimestamp() / 1000;
        this.sum = this.avg = this.max = this.min = transaction.getAmount();
        this.count = 1;
    }

    public StatisticsAggregator addTransaction(TransactionModel transaction)
    {
        double amount = transaction.getAmount();

        this.avg = ((this.count * this.avg) + amount) / (this.count + 1);
        this.count = this.count + 1;
        this.sum = this.sum + amount;
        this.min = Math.min(this.min, amount);
        this.max = Math.max(this.max,amount);

        return this;
    }

}
