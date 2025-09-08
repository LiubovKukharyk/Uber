package com.solvd.uber.exceptions;

public class UnknownPaymentMethodException extends Exception {
    private static final long serialVersionUID = 3338563580655150432L;

	public UnknownPaymentMethodException(String message) {
        super(message);
    }
}
