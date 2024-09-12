package io.azraein.inkfx.system.exceptions.system;

public class DatabaseCorruptionException extends RuntimeException {

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
