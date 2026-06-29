package com.adithya.mlops.exception;

public class ModelDeprecatedException extends RuntimeException {
    public ModelDeprecatedException(String message) {
        super(message);
    }
}
