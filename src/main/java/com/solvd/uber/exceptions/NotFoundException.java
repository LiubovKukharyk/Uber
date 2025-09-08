package com.solvd.uber.exceptions;

public class NotFoundException extends Exception {
    private static final long serialVersionUID = 6745029337595744938L;

	public NotFoundException(String message) {
        super(message);
    }
}
