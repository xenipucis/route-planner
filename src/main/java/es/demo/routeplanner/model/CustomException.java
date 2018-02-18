package es.demo.routeplanner.model;

import java.io.Serializable;

public class CustomException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -8225779515518494944L;

    private ErrorObject errorObject;

    public CustomException(final String message) {
        super(message);
    }

    public CustomException(final ErrorObject errorObject) {
        super(errorObject.getShortMessage());
        this.errorObject = errorObject;
    }

    public CustomException(final String message, Throwable t, ErrorObject errorObject) {
        super(message, t);
        this.errorObject = errorObject;
    }

    public CustomException(Throwable t, final ErrorObject errorObject) {
        super(errorObject.getShortMessage(), t);
        this.errorObject = errorObject;
    }

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(final ErrorObject errorObject) {
        this.errorObject = errorObject;
    }
}
