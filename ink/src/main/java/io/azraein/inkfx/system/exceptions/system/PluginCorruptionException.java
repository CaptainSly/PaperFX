package io.azraein.inkfx.system.exceptions.system;

public class PluginCorruptionException extends RuntimeException {

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
