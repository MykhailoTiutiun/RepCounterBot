package com.mykhailotiutiun.repcounterbot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends PersistenceException {

    public EntityNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public EntityNotFoundException(Throwable t) {
        super("Entity Not Found", t);
    }

    public EntityNotFoundException(String msg) {
        super(msg);
    }

    public EntityNotFoundException() {
        super("Entity Not Found");
    }
}
