package com.ada.avanadestore.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DatabaseException extends DataIntegrityViolationException {

    public DatabaseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
