package io.azraein.inkfx.system.exceptions;

public class MissingPluginDependencyException extends Exception {

	private static final long serialVersionUID = -4330242787511639296L;

	public MissingPluginDependencyException() {
		super();
	}

	public MissingPluginDependencyException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingPluginDependencyException(String message) {
		super(message);
	}

	public MissingPluginDependencyException(Throwable cause) {
		super(cause);
	}

}
