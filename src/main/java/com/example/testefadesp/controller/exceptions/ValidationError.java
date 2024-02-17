package com.example.testefadesp.controller.exceptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError{
    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }
    public void addError(String fieldName, String message){
        this.errors.add(new FieldMessage(fieldName, message));
    }
}
