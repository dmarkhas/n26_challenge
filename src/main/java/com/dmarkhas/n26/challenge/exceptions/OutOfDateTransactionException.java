package com.dmarkhas.n26.challenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class OutOfDateTransactionException extends Exception {

    public OutOfDateTransactionException()
    {
        super("Transaction is out of expected range");
    }

    public OutOfDateTransactionException(String message)
    {
        super(message);
    }

    public OutOfDateTransactionException(Exception ex) {

        super(ex);
    }

}
