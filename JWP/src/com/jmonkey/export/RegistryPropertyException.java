package com.jmonkey.export;

/**
 * This exception is thrown if there is a problem with a Registry property; e.g.
 * a requested property is missing.
 */
public class RegistryPropertyException extends RegistryException {
	private static final long serialVersionUID = 5610018903294835120L;
	
	private String m_key;
	private String m_group;

	/**
	 * Create a new exception with a message and property key.
	 * @param message
	 * @param key the key of the property.
	 */
	public RegistryPropertyException(String message, String key) {
		super(message);
		m_key = key;
	}

	/**
	 * Create a new exception with a message and property group and key.
	 * @param message
	 * @param group the group of the property.
	 * @param key the key of the property.
	 */
	public RegistryPropertyException(String message, String group, String key) {
		super(message);
		m_key = key;
		m_group = group;
	}

	/**
	 * Get the property group.
	 * @return the property group, or null if none.
	 */
	public String getGroup() {
		return m_group;
	}

	/**
	 * Get the property key.
	 * @return the property key.
	 */
	public String getKey() {
		return m_key;
	}

	/**
	 * Format this exception's message with the property group and key.
	 */
	@Override
	public String getMessage() {
		StringBuffer sb = new StringBuffer(40);
		sb.append(super.getMessage());
		if (m_key != null | m_group != null) {
			sb.append(": ");
			if (m_group != null) {
				sb.append("group = ").append(m_group);
				if (m_key != null) {
					sb.append(", ");
				}
			}
			if (m_key != null) {
				sb.append("key = ").append(m_key);
			}
		}
		return sb.toString();
	}
}
