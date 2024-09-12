package io.azraein.inkfx.system.exceptions.system;

public class SaveCorruptionException extends Exception {

	private static final long serialVersionUID = -6198876710825429381L;

	public SaveCorruptionException() {
		super();
	}

	public SaveCorruptionException(String message) {
		super(message);
	}

	public SaveCorruptionException(String message, Throwable cause) {
		super(message, cause);
	}

	public SaveCorruptionException(Throwable cause) {
		super(cause);
	}
}
