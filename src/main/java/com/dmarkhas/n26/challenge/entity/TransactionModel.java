package com.dmarkhas.n26.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TransactionModel {

    private double amount;

    private long timestamp;

}
