package com.pedro.api.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    private List<FieldMessage> fieldMessages = new ArrayList<>();

    // Construtor chamando a classe pai (StandardError)
    public ValidationError(Instant timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }

    public List<FieldMessage> getFieldMessages() {
        return fieldMessages;
    }

    public void addFieldMessage(String field, String message) {
        this.fieldMessages.add(new FieldMessage(field, message));
    }
}