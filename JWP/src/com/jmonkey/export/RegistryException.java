package com.jmonkey.export;

/**
 * This exception is thrown if a Registry is misused or if it is found to be
 * inconsistent.
 */
public class RegistryException extends RuntimeException {
	private static final long serialVersionUID = -6866454479978066517L;

	public RegistryException() {
		super();
	}

	public RegistryException(String message) {
		super(message);
	}

	public RegistryException(Throwable cause) {
		super(cause);
	}

	public RegistryException(String message, Throwable cause) {
		super(message, cause);
	}

}
