package com.mykhailotiutiun.repcounterbot.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class PersistenceException extends RuntimeException {

    public PersistenceException(String msg, Throwable t){
        super(msg, t);
        log.error(msg, t);
    }

    public PersistenceException(String msg){
        super(msg);
        log.error(msg);
    }

}
