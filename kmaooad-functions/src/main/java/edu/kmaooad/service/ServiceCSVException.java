package edu.kmaooad.service;

import lombok.Getter;

@Getter
public class ServiceCSVException extends Exception {
    private final String line;
    private final TypeOfCSVException errorType;

    public ServiceCSVException(TypeOfCSVException errorType, String line) {
        super();
        this.errorType = errorType;
        this.line = line;
    }

    public enum TypeOfCSVException {
        EMAIL_DUPLICATE, ACTIVITY_DOESNT_EXIST, NOT_ENOUGH_PARAMS_ON_LINE
    }
}
