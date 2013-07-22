package com.jmonkey.export;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegistryPropertyExceptionTest {
	
	private static final String TEST_MESSAGE = "message";
	private static final String TEST_KEY = "test_key";
	private static final String TEST_GROUP = "test_group";
	
	private static RegistryPropertyException exception;

	@Test
	public void testConstruction() {
		exception = new RegistryPropertyException(TEST_MESSAGE, TEST_KEY);
		assertEquals(TEST_KEY, exception.getKey());
		assertNull(exception.getGroup());
	}
	
	@Test
	public void testConstructionWithGroup() {
		exception = new RegistryPropertyException(TEST_MESSAGE, TEST_GROUP, TEST_KEY);
		assertEquals(TEST_GROUP, exception.getGroup());
		assertEquals(TEST_KEY, exception.getKey());
	}
	
	@Test
	public void testGetMessage() {
		exception = new RegistryPropertyException(TEST_MESSAGE, TEST_GROUP, TEST_KEY);
		String message = exception.getMessage();
		assertNotNull(message);

		assertTrue(message.contains(TEST_MESSAGE));
		assertTrue(message.contains("key = " + TEST_KEY));
		assertTrue(message.contains("group = " + TEST_GROUP));
	}
	
	@Test
	public void testGetMessageNullGroup() {
		exception = new RegistryPropertyException(TEST_MESSAGE, null, TEST_KEY);
		String message = exception.getMessage();
		assertNotNull(message);
		
		assertTrue(message.contains(TEST_MESSAGE));
		assertTrue(message.contains("key = " + TEST_KEY));
		assertFalse(message.contains("group = " + TEST_GROUP));
	}
	
	@Test
	public void testGetMessageNullKey() {
		exception = new RegistryPropertyException(TEST_MESSAGE, TEST_GROUP, null);
		String message = exception.getMessage();
		assertNotNull(message);
		
		assertTrue(message.contains(TEST_MESSAGE));
		assertFalse(message.contains("key = " + TEST_KEY));
		assertTrue(message.contains("group = " + TEST_GROUP));
	}
	
	@Test
	public void testGetMessageBothNull() {
		exception = new RegistryPropertyException(TEST_MESSAGE, null, null);
		String message = exception.getMessage();
		assertNotNull(message);
		
		assertTrue(message.contains(TEST_MESSAGE));
		assertFalse(message.contains("key = " + TEST_KEY));
		assertFalse(message.contains("group = " + TEST_GROUP));
	}

}
