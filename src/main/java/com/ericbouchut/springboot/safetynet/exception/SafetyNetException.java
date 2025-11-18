package com.ericbouchut.springboot.safetynet.exception;

/**
 * Abstract base class for the Safety Net Alerting App exceptions.
 */
public abstract class SafetyNetException extends RuntimeException {

    public SafetyNetException(String message) {
        super(message);
    }

    public SafetyNetException(String message, Throwable cause) {
        super(message, cause);
    }
}
