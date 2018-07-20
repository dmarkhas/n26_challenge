package com.dmarkhas.n26.challenge.api;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionDto implements Serializable {

    private double amount;

    private long timestamp;

}
