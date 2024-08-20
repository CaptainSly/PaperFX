package io.azraein.inkfx.system.exceptions;

public class DatabaseCorruptionException extends Exception {

	private static final long serialVersionUID = -4330242787511639296L;

	public DatabaseCorruptionException() {
		super();
	}

	public DatabaseCorruptionException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseCorruptionException(String message) {
		super(message);
	}

	public DatabaseCorruptionException(Throwable cause) {
		super(cause);
	}

}
