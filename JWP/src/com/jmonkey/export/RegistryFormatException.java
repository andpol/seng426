package com.jmonkey.export;

import java.io.IOException;

/**
 * This exception is thrown if you try to load a Registry whose format is not
 * understood.
 */
public class RegistryFormatException extends IOException {
	private static final long serialVersionUID = 4847437500908162846L;

	private String m_fileName;

	/**
	 * Create a new RegistryFormatException.
	 * 
	 * @param message
	 */
	public RegistryFormatException(String message) {
		super(message);
	}

	/**
	 * Set the filename of the invalid registry file.
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		m_fileName = fileName;
	}

	/**
	 * Get the filename of the invalid registry file.
	 * 
	 * @return
	 */
	public String getFileName() {
		return m_fileName;
	}
}
