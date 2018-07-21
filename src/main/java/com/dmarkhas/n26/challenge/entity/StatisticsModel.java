package com.dmarkhas.n26.challenge.entity;

import lombok.Data;

@Data
public class StatisticsModel {

    private double sum;

    private double avg;

    private double max;

    private double min;

    private long count;

    public void aggregate(StatisticsAggregator aggregator)
    {
        this.avg = ((this.count * this.avg) + (aggregator.getCount() * aggregator.getAvg())) / (this.count + aggregator.getCount());
        this.sum = this.sum + aggregator.getSum();
        this.count = this.count + aggregator.getCount();
        this.min = this.min > 0 ? Math.min(this.min, aggregator.getMin()) : aggregator.getMin();
        this.max = Math.max(this.max, aggregator.getMax());

    }

}
