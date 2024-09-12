package io.azraein.inkfx.system.exceptions.system;

public class IncompatibleDatabaseVersionException extends RuntimeException {

	private static final long serialVersionUID = -6376036345636477272L;

	public IncompatibleDatabaseVersionException() {
		super();
	}

	public IncompatibleDatabaseVersionException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncompatibleDatabaseVersionException(String message) {
		super(message);
	}

	public IncompatibleDatabaseVersionException(Throwable cause) {
		super(cause);
	}

}
