package com.dmarkhas.n26.challenge.rest;

import com.dmarkhas.n26.challenge.api.StatisticsDto;
import com.dmarkhas.n26.challenge.api.TransactionDto;
import com.dmarkhas.n26.challenge.entity.StatisticsModel;
import com.dmarkhas.n26.challenge.entity.TransactionModel;
import org.springframework.stereotype.Component;
import org.mapstruct.*;

@Component
@Mapper(componentModel = "spring")
public interface TransactionsMapper {

    StatisticsModel fromDto(StatisticsDto statisticsDto);
    StatisticsDto toDto(StatisticsModel statisticsModel);

    TransactionModel fromDto(TransactionDto transactionDto);
    TransactionDto toDto(TransactionModel transactionModel);


}
