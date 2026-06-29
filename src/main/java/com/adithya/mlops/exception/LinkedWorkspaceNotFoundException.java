package com.adithya.mlops.exception;

public class LinkedWorkspaceNotFoundException extends RuntimeException {
    public LinkedWorkspaceNotFoundException(String message) {
        super(message);
    }
}
