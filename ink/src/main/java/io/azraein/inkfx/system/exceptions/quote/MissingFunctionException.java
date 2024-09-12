package io.azraein.inkfx.system.exceptions.quote;

public class MissingFunctionException extends RuntimeException {

    private static final long serialVersionUID = -6376036345636477272L;

    public MissingFunctionException() {
        super();
    }

    public MissingFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingFunctionException(String message) {
        super(message);
    }

    public MissingFunctionException(Throwable cause) {
        super(cause);
    }

}
