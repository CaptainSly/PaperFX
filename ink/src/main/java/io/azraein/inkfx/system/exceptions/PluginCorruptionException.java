package io.azraein.inkfx.system.exceptions;

public class PluginCorruptionException extends Exception {

	private static final long serialVersionUID = -4330242787511639296L;

	public PluginCorruptionException() {
		super();
	}

	public PluginCorruptionException(String message, Throwable cause) {
		super(message, cause);
	}

	public PluginCorruptionException(String message) {
		super(message);
	}

	public PluginCorruptionException(Throwable cause) {
		super(cause);
	}

}
