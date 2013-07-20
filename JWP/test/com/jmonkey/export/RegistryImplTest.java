package com.jmonkey.export;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import org.junit.Before;
import org.junit.Test;

public class RegistryImplTest {

	public static final String REGISTRY_FILE = "resources/registry.test";

	public static final String STRING_VALUE = "testValue";
	public static final String[] STRING_ARRAY_VALUE = { "abc", "def" };
	public static final boolean BOOLEAN_VALUE = true;
	public static final int INT_VALUE = 1;
	public static final int[] INT_ARRAY_VALUE = { 3, -6 };
	public static final long LONG_VALUE = 17179869184L;
	public static final byte BYTE_VALUE = 110;
	public static final byte[] BYTE_ARRAY_VALUE = { -52, 76 };
	public static final char CHAR_VALUE = 'c';
	public static final char[] CHAR_ARRAY_VALUE = { 'a', 'Z' };
	public static final double DOUBLE_VALUE = 1.2345678;
	public static final float FLOAT_VALUE = 1.234f;
	public static final short SHORT_VALUE = 1025;

	private static final double FLOAT_DELTA = 0.0001;
	private static final double DOUBLE_DELTA = 0.00000001;

	public static final String DEFAULT_GROUP_NAME = "group1";
	public static final String STRING_KEY = "testString";
	public static final String STRING_ARRAY_KEY = "testStringArray";
	public static final String BOOLEAN_KEY = "testBoolean";
	public static final String INTEGER_KEY = "testInt";
	public static final String INT_ARRAY_KEY = "testIntArray";
	public static final String LONG_KEY = "testLong";
	public static final String BYTE_KEY = "testByte";
	public static final String BYTE_ARRAY_KEY = "testByteArray";
	public static final String CHAR_KEY = "testChar";
	public static final String CHAR_ARRAY_KEY = "testCharArray";
	public static final String DOUBLE_KEY = "testDouble";
	public static final String FLOAT_KEY = "testFloat";
	public static final String SHORT_KEY = "testShort";

	private static RegistryImpl registryImpl;

	@Before
	public void setup() throws IOException {
		registryImpl = new RegistryImpl(new File(REGISTRY_FILE), RegistryImpl.FILE_SYNTAX_VERSION);
	}

	@Test
	public void testRegistryImplIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testRegistryImplReaderIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testRegistryImplFileIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetVersion() {
		assertArrayEquals(RegistryImpl.FILE_SYNTAX_VERSION, registryImpl.getVersion());
	}

	@Test
	public void testGetFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsAltered() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteProperty() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetString() throws IOException {
		assertEquals(STRING_VALUE, registryImpl.getString(DEFAULT_GROUP_NAME, STRING_KEY));
	}

	@Test
	public void testGetStringArray() throws IOException {
		assertArrayEquals(STRING_ARRAY_VALUE,
				registryImpl.getStringArray(DEFAULT_GROUP_NAME, STRING_ARRAY_KEY));
	}

	@Test
	public void testGetBoolean() throws IOException {
		assertEquals(BOOLEAN_VALUE, registryImpl.getBoolean(DEFAULT_GROUP_NAME, BOOLEAN_KEY));
	}

	@Test
	public void testGetInteger() throws IOException {
		assertEquals(INT_VALUE, registryImpl.getInteger(DEFAULT_GROUP_NAME, INTEGER_KEY));
	}

	@Test
	public void testGetIntegerArray() throws IOException {
		assertArrayEquals(INT_ARRAY_VALUE,
				registryImpl.getIntegerArray(DEFAULT_GROUP_NAME, INT_ARRAY_KEY));
	}

	@Test
	public void testGetLong() throws IOException {
		assertEquals(LONG_VALUE, registryImpl.getLong(DEFAULT_GROUP_NAME, LONG_KEY));
	}

	@Test
	public void testGetByte() throws IOException {
		assertEquals(BYTE_VALUE, registryImpl.getByte(DEFAULT_GROUP_NAME, BYTE_KEY));
	}

	@Test
	public void testGetByteArray() throws IOException {
		assertArrayEquals(BYTE_ARRAY_VALUE,
				registryImpl.getByteArray(DEFAULT_GROUP_NAME, BYTE_ARRAY_KEY));
	}

	@Test
	public void testGetChar() throws IOException {
		assertEquals(CHAR_VALUE, registryImpl.getChar(DEFAULT_GROUP_NAME, CHAR_KEY));
	}

	@Test
	public void testGetCharArray() throws IOException {
		assertArrayEquals(CHAR_ARRAY_VALUE,
				registryImpl.getCharArray(DEFAULT_GROUP_NAME, CHAR_ARRAY_KEY));
	}

	@Test
	public void testGetDouble() throws IOException {
		assertEquals(DOUBLE_VALUE, registryImpl.getDouble(DEFAULT_GROUP_NAME, DOUBLE_KEY),
				DOUBLE_DELTA);
	}

	@Test
	public void testGetFloat() throws IOException {
		assertEquals(FLOAT_VALUE, registryImpl.getFloat(DEFAULT_GROUP_NAME, FLOAT_KEY), FLOAT_DELTA);
	}

	@Test
	public void testGetObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetShort() throws IOException {
		assertEquals(SHORT_VALUE, registryImpl.getShort(DEFAULT_GROUP_NAME, SHORT_KEY));
	}

	@Test
	public void testSetPropertyString() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, STRING_KEY, STRING_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_KEY));
		assertEquals(STRING_VALUE, registryImpl.getString(DEFAULT_GROUP_NAME, STRING_KEY));
	}

	@Test
	public void testSetPropertyStringArray() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_ARRAY_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, STRING_ARRAY_KEY, STRING_ARRAY_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_ARRAY_KEY));
		assertArrayEquals(STRING_ARRAY_VALUE,
				registryImpl.getStringArray(DEFAULT_GROUP_NAME, STRING_ARRAY_KEY));
	}

	@Test
	public void testSetPropertyBoolean() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, BOOLEAN_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, BOOLEAN_KEY, BOOLEAN_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, BOOLEAN_KEY));
		assertEquals(BOOLEAN_VALUE, registryImpl.getBoolean(DEFAULT_GROUP_NAME, BOOLEAN_KEY));
	}

	@Test
	public void testSetPropertyInt() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, INTEGER_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, INTEGER_KEY, INT_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INTEGER_KEY));
		assertEquals(INT_VALUE, registryImpl.getInteger(DEFAULT_GROUP_NAME, INTEGER_KEY));
	}

	@Test
	public void testSetPropertyIntArray() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_ARRAY_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, INT_ARRAY_KEY, INT_ARRAY_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_ARRAY_KEY));
		assertArrayEquals(INT_ARRAY_VALUE,
				registryImpl.getIntegerArray(DEFAULT_GROUP_NAME, INT_ARRAY_KEY));
	}

	@Test
	public void testSetPropertyLong() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, LONG_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, LONG_KEY, LONG_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, LONG_KEY));
		assertEquals(LONG_VALUE, registryImpl.getLong(DEFAULT_GROUP_NAME, LONG_KEY));
	}

	@Test
	public void testSetPropertyByte() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, BYTE_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, BYTE_KEY, BYTE_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, BYTE_KEY));
		assertEquals(BYTE_VALUE, registryImpl.getByte(DEFAULT_GROUP_NAME, BYTE_KEY));
	}

	@Test
	public void testSetPropertyByteArray() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, BYTE_ARRAY_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, BYTE_ARRAY_KEY, BYTE_ARRAY_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, BYTE_ARRAY_KEY));
		assertArrayEquals(BYTE_ARRAY_VALUE,
				registryImpl.getByteArray(DEFAULT_GROUP_NAME, BYTE_ARRAY_KEY));
	}

	@Test
	public void testSetPropertyChar() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, CHAR_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, CHAR_KEY, CHAR_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, CHAR_KEY));
		assertEquals(CHAR_VALUE, registryImpl.getChar(DEFAULT_GROUP_NAME, CHAR_KEY));
	}

	@Test
	public void testSetPropertyCharArray() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, CHAR_ARRAY_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, CHAR_ARRAY_KEY, CHAR_ARRAY_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, CHAR_ARRAY_KEY));
		assertArrayEquals(CHAR_ARRAY_VALUE,
				registryImpl.getCharArray(DEFAULT_GROUP_NAME, CHAR_ARRAY_KEY));
	}

	@Test
	public void testSetPropertyDouble() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, DOUBLE_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, DOUBLE_KEY, DOUBLE_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, DOUBLE_KEY));
		assertEquals(DOUBLE_VALUE, registryImpl.getDouble(DEFAULT_GROUP_NAME, DOUBLE_KEY),
				DOUBLE_DELTA);
	}

	@Test
	public void testSetPropertyFloat() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, FLOAT_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, FLOAT_KEY, FLOAT_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, FLOAT_KEY));
		assertEquals(FLOAT_VALUE, registryImpl.getFloat(DEFAULT_GROUP_NAME, FLOAT_KEY), FLOAT_DELTA);
	}

	@Test
	public void testSetPropertyObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetPropertyObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetPropertyShort() {
		registryImpl.deleteAll();
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, SHORT_KEY));
		registryImpl.setProperty(DEFAULT_GROUP_NAME, SHORT_KEY, SHORT_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, SHORT_KEY));
		assertEquals(SHORT_VALUE, registryImpl.getShort(DEFAULT_GROUP_NAME, SHORT_KEY));
	}

	@Test
	public void testGetGroups() throws IOException {
		@SuppressWarnings("rawtypes")
		Enumeration e = registryImpl.getGroups();
		assertEquals(DEFAULT_GROUP_NAME, e.nextElement());
		assertFalse(e.hasMoreElements());
	}

	@Test
	public void testGetKeys() {
		fail("Not yet implemented");
	}

	@Test
	public void testSize() {
		assertEquals(1, registryImpl.size());
	}

	@Test
	public void testGetType() {
		assertEquals(Registry.TYPE_STRING_SINGLE,
				registryImpl.getType(DEFAULT_GROUP_NAME, STRING_KEY));
		assertEquals(Registry.TYPE_STRING_ARRAY,
				registryImpl.getType(DEFAULT_GROUP_NAME, STRING_ARRAY_KEY));
	}

	@Test
	public void testIsArrayType() {
		assertTrue(registryImpl.isArrayType(DEFAULT_GROUP_NAME, STRING_ARRAY_KEY));
		assertFalse(registryImpl.isArrayType(DEFAULT_GROUP_NAME, STRING_KEY));
	}

	@Test
	public void testIsGroup() {
		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertFalse(registryImpl.isGroup("nonexistent"));
	}

	@Test
	public void testIsProperty() {
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_KEY));

		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, "nonexistent"));
		assertFalse(registryImpl.isProperty("nonexistent", STRING_KEY));
		assertFalse(registryImpl.isProperty("nonexistent", "nonexistent"));
	}

	@Test
	public void testIsBlank() {
		assertFalse(registryImpl.isBlank());
		registryImpl.deleteAll();
		assertTrue(registryImpl.isBlank());
	}

	@Test
	public void testSizeOf() {
		assertEquals(13, registryImpl.sizeOf(DEFAULT_GROUP_NAME));
		assertEquals(0, registryImpl.sizeOf("nonexistent"));
	}

	@Test
	public void testDeleteGroup() {
		assertEquals(1, registryImpl.size());
		registryImpl.deleteGroup("nonexistent");
		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertEquals(1, registryImpl.size());

		registryImpl.deleteGroup(DEFAULT_GROUP_NAME);
		assertFalse(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertEquals(0, registryImpl.size());
	}

	@Test
	public void testExportGroup() {
		fail("Not yet implemented");
	}

	@Test
	public void testImportGroup() {
		fail("Not yet implemented");
	}

	@Test
	public void testInitGroup() {
		fail("Not yet implemented");
	}

	@Test
	public void testReferenceGroup() {
		RegistryGroup group = registryImpl.referenceGroup(DEFAULT_GROUP_NAME);
		assertNotNull(group);
		assertEquals(registryImpl.sizeOf(DEFAULT_GROUP_NAME), group.size());

		group.remove(STRING_KEY);
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_KEY));
	}

	@Test
	public void testReplaceGroup() {
		fail("Not yet implemented");
	}

	@Test
	public void testRead() {
		fail("Not yet implemented");
	}

	@Test
	public void testWrite() {
		fail("Not yet implemented");
	}

	@Test
	public void testTypeToMarker() {
		fail("Not yet implemented");
	}

	@Test
	public void testMarkerToType() {
		fail("Not yet implemented");
	}

	@Test
	public void testMergeRegistry() {
		fail("Not yet implemented");
	}

	@Test
	public void testCommit() {
		fail("Not yet implemented");
	}

	@Test
	public void testRevert() {
		fail("Not yet implemented");
	}

}
