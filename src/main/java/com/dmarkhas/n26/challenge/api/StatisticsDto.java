package com.dmarkhas.n26.challenge.api;

import lombok.Data;

@Data
public class StatisticsDto {

    private double sum;

    private double avg;

    private double max;

    private double min;

    private long count;
}
