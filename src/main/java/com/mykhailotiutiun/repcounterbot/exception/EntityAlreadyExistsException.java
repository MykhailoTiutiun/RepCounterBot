package com.mykhailotiutiun.repcounterbot.exception;

public class EntityAlreadyExistsException extends PersistenceException{

    public EntityAlreadyExistsException(String msg, Throwable t) {
        super(msg, t);
    }

    public EntityAlreadyExistsException(Throwable t) {
        super("Entity already exists", t);
    }

    public EntityAlreadyExistsException(String msg) {
        super(msg);
    }

    public EntityAlreadyExistsException() {
        super("Entity already exists");
    }
}
