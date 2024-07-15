package io.azraein.paperfx.system.exceptions;

public class IncompatibleDatabaseVersionException extends Exception {

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
