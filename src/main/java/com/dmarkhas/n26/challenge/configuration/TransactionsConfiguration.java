package com.dmarkhas.n26.challenge.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "transactions")
public class TransactionsConfiguration {

    private int maxMsToKeep;

}
