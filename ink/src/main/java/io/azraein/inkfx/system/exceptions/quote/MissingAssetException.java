package io.azraein.inkfx.system.exceptions.quote;

public class MissingAssetException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MissingAssetException() {
        super();
    }

    public MissingAssetException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingAssetException(String message) {
        super(message);
    }

    public MissingAssetException(Throwable cause) {
        super(cause);
    }

}
