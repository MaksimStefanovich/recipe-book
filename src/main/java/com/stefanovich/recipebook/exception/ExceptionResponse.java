package com.stefanovich.recipebook.exception;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ExceptionResponse {
    private final ZonedDateTime dateTime;
    private final String message;
    private final String details;

    public ExceptionResponse(ZonedDateTime dateTime, String message, String details) {
        this.dateTime = dateTime;
        this.message = message;
        this.details = details;
    }
}
