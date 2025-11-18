package com.ericbouchut.springboot.safetynet.exception;

/**
 * Exception thrown when the Safety Net Alerting system
 * cannot load its JSON configuration file.
 */
public class JsonConfigurationLoadException extends SafetyNetException {

    public JsonConfigurationLoadException(String message) {
        super(message);
    }

    public JsonConfigurationLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
