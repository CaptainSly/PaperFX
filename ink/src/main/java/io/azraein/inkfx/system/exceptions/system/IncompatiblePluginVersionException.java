package io.azraein.inkfx.system.exceptions.system;

public class IncompatiblePluginVersionException extends RuntimeException {

	private static final long serialVersionUID = -6376036345636477272L;

	public IncompatiblePluginVersionException() {
		super();
	}

	public IncompatiblePluginVersionException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncompatiblePluginVersionException(String message) {
		super(message);
	}

	public IncompatiblePluginVersionException(Throwable cause) {
		super(cause);
	}

}
