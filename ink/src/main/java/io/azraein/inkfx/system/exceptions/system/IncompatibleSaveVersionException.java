package io.azraein.inkfx.system.exceptions.system;

public class IncompatibleSaveVersionException extends RuntimeException {

	private static final long serialVersionUID = -1207853456337167075L;

	public IncompatibleSaveVersionException() {
		super();
	}

	public IncompatibleSaveVersionException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncompatibleSaveVersionException(String message) {
		super(message);
	}

	public IncompatibleSaveVersionException(Throwable cause) {
		super(cause);
	}

}
