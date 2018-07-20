package com.dmarkhas.n26.challenge.rest;

import com.dmarkhas.n26.challenge.configuration.TransactionsConfiguration;
import com.dmarkhas.n26.challenge.service.TransactionsService;
import com.dmarkhas.n26.challenge.api.StatisticsDto;
import com.dmarkhas.n26.challenge.api.TransactionDto;
import com.dmarkhas.n26.challenge.entity.StatisticsModel;
import com.dmarkhas.n26.challenge.entity.TransactionModel;
import com.dmarkhas.n26.challenge.exceptions.OutOfDateTransactionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
public class TransactionsController {

    private final TransactionsService transactionsService;

    private final TransactionsMapper transactionsMapper;

    private final TransactionsConfiguration transactionsConfiguration;

    @Inject
    public TransactionsController(TransactionsService transactionsService, TransactionsMapper transactionsMapper, TransactionsConfiguration transactionsConfiguration)
    {
        this.transactionsService = transactionsService;
        this.transactionsMapper = transactionsMapper;
        this.transactionsConfiguration = transactionsConfiguration;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public void submitTransaction(@RequestBody TransactionDto transaction) throws OutOfDateTransactionException
    {
        TransactionModel tmodel = transactionsMapper.fromDto(transaction);
        if (isValid(tmodel)) {
            transactionsService.addTransaction(tmodel);
        }
        else {
            throw new OutOfDateTransactionException();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/statistics")
    public StatisticsDto getStatistics()
    {
        StatisticsModel smodel = transactionsService.getStatistics();
        return transactionsMapper.toDto(smodel);
    }

    private boolean isValid(TransactionModel tmodel)
    {
        return tmodel.getTimestamp() > System.currentTimeMillis() - transactionsConfiguration.getMaxMsToKeep();
    }

}
