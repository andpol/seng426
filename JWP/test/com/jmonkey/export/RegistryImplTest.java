package com.jmonkey.export;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Enumeration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class RegistryImplTest {

	public static final String REGISTRY_FILE = "resources/registry.txt";
	public static final String SIMPLE_REGISTRY_FILE = "resources/registry_simple.txt";
	public static final String MALFORMED_VALUES_REGISTRY_FILE = "resources/registry_bad_values.txt";
	public static final String BAD_VERSION_REGISTRY_FILE = "resources/registry_bad_version.txt";
	public static final String TMP_REGISTRY_FILE = "registry.tmp";

	public static final String STRING_VALUE = "testValue";
	public static final String[] STRING_ARRAY_VALUE = { "abc", "def" };
	public static final boolean BOOLEAN_VALUE = true;
	public static final int INT_VALUE = 1;
	public static final int[] INT_ARRAY_VALUE = { 3, 5 };
	public static final long LONG_VALUE = 17179869184L;
	public static final byte BYTE_VALUE = 110;
	public static final byte[] BYTE_ARRAY_VALUE = { 25, 100 };
	public static final char CHAR_VALUE = 'c';
	public static final char[] CHAR_ARRAY_VALUE = { 'a', 'Z' };
	public static final double DOUBLE_VALUE = 1.2345678;
	public static final float FLOAT_VALUE = 1.234f;
	public static final Foo OBJECT_VALUE = new Foo(3);
	public static final Foo[] OBJECT_ARRAY_VALUE = new Foo[] { new Foo(5), new Foo(7) };
	public static final short SHORT_VALUE = 1024;

	public static final int NUM_TEST_VALUES = 15;

	private static final double FLOAT_DELTA = 0.0001;
	private static final double DOUBLE_DELTA = 0.00000001;

	public static final String DEFAULT_GROUP_NAME = "group1";
	public static final String SECONDARY_GROUP_NAME = "group2";

	public static final String STRING_KEY = "testString";
	public static final String STRING_ARRAY_KEY = "testStringArray";
	public static final String BOOLEAN_KEY = "testBoolean";
	public static final String INT_KEY = "testInt";
	public static final String INT_ARRAY_KEY = "testIntArray";
	public static final String LONG_KEY = "testLong";
	public static final String BYTE_KEY = "testByte";
	public static final String BYTE_ARRAY_KEY = "testByteArray";
	public static final String CHAR_KEY = "testChar";
	public static final String CHAR_ARRAY_KEY = "testCharArray";
	public static final String DOUBLE_KEY = "testDouble";
	public static final String FLOAT_KEY = "testFloat";
	public static final String OBJECT_KEY = "testObject";
	public static final String OBJECT_ARRAY_KEY = "testObjectArray";
	public static final String SHORT_KEY = "testShort";

	private static RegistryImpl registryImpl;

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void setup() throws IOException {
		registryImpl = new RegistryImpl(new File(REGISTRY_FILE), RegistryImpl.FILE_SYNTAX_VERSION);
	}

	@Test
	public void testSimpleConstructor() {
		int[] version = { 2, 9 };
		registryImpl = new RegistryImpl(version);

		assertArrayEquals(version, registryImpl.getVersion());
		assertNull(registryImpl.getFile());
	}

	@Test
	public void testReaderConstructor() throws IOException {
		Reader reader = new FileReader(SIMPLE_REGISTRY_FILE);
		registryImpl = new RegistryImpl(reader, RegistryImpl.FILE_SYNTAX_VERSION);

		assertArrayEquals(RegistryImpl.FILE_SYNTAX_VERSION, registryImpl.getVersion());
		assertNull(registryImpl.getFile());

		assertEquals(1, registryImpl.size());
		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_KEY));
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_KEY));
		assertEquals(STRING_VALUE, registryImpl.getString(DEFAULT_GROUP_NAME, STRING_KEY));
		assertEquals(INT_VALUE, registryImpl.getInteger(DEFAULT_GROUP_NAME, INT_KEY));
	}

	@Test
	public void testFileConstructor() throws IOException {
		File file = new File(SIMPLE_REGISTRY_FILE);
		registryImpl = new RegistryImpl(file, RegistryImpl.FILE_SYNTAX_VERSION);

		assertArrayEquals(RegistryImpl.FILE_SYNTAX_VERSION, registryImpl.getVersion());
		assertNotNull(registryImpl.getFile());

		assertEquals(1, registryImpl.size());
		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_KEY));
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_KEY));
		assertEquals(STRING_VALUE, registryImpl.getString(DEFAULT_GROUP_NAME, STRING_KEY));
		assertEquals(INT_VALUE, registryImpl.getInteger(DEFAULT_GROUP_NAME, INT_KEY));
	}

	@Test(expected = IOException.class)
	public void testNullFileConstructor() throws IOException {
		File file = null;
		registryImpl = new RegistryImpl(file, RegistryImpl.FILE_SYNTAX_VERSION);
	}

	@Test(expected = IOException.class)
	public void testBadRegistryVersion() throws IOException {
		registryImpl = new RegistryImpl(new File(BAD_VERSION_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);
	}

	@Test
	public void testGetVersion() {
		assertArrayEquals(RegistryImpl.FILE_SYNTAX_VERSION, registryImpl.getVersion());
	}

	@Test
	public void testSetFile() throws IOException {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		assertNull(registryImpl.getFile());
		File file = folder.newFile(TMP_REGISTRY_FILE);
		registryImpl.setFile(file);
		assertEquals(file, registryImpl.getFile());
		assertFalse(registryImpl.isAltered());
	}

	@Test
	public void testSetFileOverwrite() throws IOException {
		assertNotNull(registryImpl.getFile());
		File file = folder.newFile(TMP_REGISTRY_FILE);
		registryImpl.setFile(file);
		assertEquals(file, registryImpl.getFile());
		assertTrue(registryImpl.isAltered());
	}

	@Test
	public void testIsAltered() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		assertFalse(registryImpl.isAltered());
		registryImpl.setProperty(DEFAULT_GROUP_NAME, INT_KEY, INT_VALUE);
		assertTrue(registryImpl.isAltered());
	}

	@Test
	public void testDeleteAll() {
		registryImpl.setProperty(SECONDARY_GROUP_NAME, STRING_KEY, STRING_VALUE);
		assertEquals(2, registryImpl.size());

		registryImpl.deleteAll();

		assertEquals(0, registryImpl.size());
		assertTrue(registryImpl.isBlank());
		assertFalse(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertFalse(registryImpl.isGroup(SECONDARY_GROUP_NAME));
		assertTrue(registryImpl.isAltered());
	}

	@Test
	public void testDeleteProperty() {
		assertEquals(NUM_TEST_VALUES, registryImpl.sizeOf(DEFAULT_GROUP_NAME));
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_KEY));
		registryImpl.deleteProperty(DEFAULT_GROUP_NAME, STRING_KEY);
		assertEquals(NUM_TEST_VALUES - 1, registryImpl.sizeOf(DEFAULT_GROUP_NAME));
		assertFalse(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_KEY));
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
		assertEquals(INT_VALUE, registryImpl.getInteger(DEFAULT_GROUP_NAME, INT_KEY));
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
		assertArrayEquals(OBJECT_ARRAY_VALUE,
				registryImpl.getObjectArray(DEFAULT_GROUP_NAME, OBJECT_ARRAY_KEY));
	}

	@Test
	public void testGetObject() {
		assertEquals(OBJECT_VALUE, registryImpl.getObject(DEFAULT_GROUP_NAME, OBJECT_KEY));
	}

	@Test
	public void testGetShort() throws IOException {
		assertEquals(SHORT_VALUE, registryImpl.getShort(DEFAULT_GROUP_NAME, SHORT_KEY));
	}

	@Test(expected = RegistryPropertyException.class)
	public void testGetStringMissingGroup() throws IOException {
		assertEquals(STRING_VALUE, registryImpl.getString("nonexistent", STRING_KEY));
	}

	@Test(expected = RegistryPropertyException.class)
	public void testGetStringMissingKey() throws IOException {
		assertEquals(STRING_VALUE, registryImpl.getString(DEFAULT_GROUP_NAME, "nonexistent"));
	}

	@Test(expected = RegistryTypeException.class)
	public void testGetStringWrongType() throws IOException {
		assertEquals(STRING_VALUE, registryImpl.getString(DEFAULT_GROUP_NAME, INT_KEY));
	}

	@Test(expected = RegistryException.class)
	public void testGetBooleanMalformed() throws IOException {
		registryImpl = new RegistryImpl(new File(MALFORMED_VALUES_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.getBoolean(DEFAULT_GROUP_NAME, BOOLEAN_KEY);
	}

	@Test(expected = RegistryException.class)
	public void testGetIntegerMalformed() throws IOException {
		registryImpl = new RegistryImpl(new File(MALFORMED_VALUES_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.getInteger(DEFAULT_GROUP_NAME, INT_KEY);
	}

	@Test(expected = RegistryException.class)
	public void testGetLongMalformed() throws IOException {
		registryImpl = new RegistryImpl(new File(MALFORMED_VALUES_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.getLong(DEFAULT_GROUP_NAME, LONG_KEY);
	}

	@Test(expected = RegistryException.class)
	public void testGetByteMalformed() throws IOException {
		registryImpl = new RegistryImpl(new File(MALFORMED_VALUES_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.getByte(DEFAULT_GROUP_NAME, BYTE_KEY);
	}

	@Test(expected = RegistryException.class)
	public void testGetCharMalformed() throws IOException {
		registryImpl = new RegistryImpl(new File(MALFORMED_VALUES_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.getChar(DEFAULT_GROUP_NAME, CHAR_KEY);
	}

	@Test(expected = RegistryException.class)
	public void testGetDoubleMalformed() throws IOException {
		registryImpl = new RegistryImpl(new File(MALFORMED_VALUES_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.getDouble(DEFAULT_GROUP_NAME, DOUBLE_KEY);
	}

	@Test(expected = RegistryException.class)
	public void testGetFloatMalformed() throws IOException {
		registryImpl = new RegistryImpl(new File(MALFORMED_VALUES_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.getFloat(DEFAULT_GROUP_NAME, FLOAT_KEY);
	}

	@Test(expected = RegistryException.class)
	public void testGetShortMalformed() throws IOException {
		registryImpl = new RegistryImpl(new File(MALFORMED_VALUES_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.getShort(DEFAULT_GROUP_NAME, SHORT_KEY);
	}

	@Test
	public void testSetPropertyString() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, STRING_KEY, STRING_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_KEY));
		assertEquals(STRING_VALUE, registryImpl.getString(DEFAULT_GROUP_NAME, STRING_KEY));
	}

	@Test
	public void testSetPropertyStringArray() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, STRING_ARRAY_KEY, STRING_ARRAY_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, STRING_ARRAY_KEY));
		assertArrayEquals(STRING_ARRAY_VALUE,
				registryImpl.getStringArray(DEFAULT_GROUP_NAME, STRING_ARRAY_KEY));
	}

	@Test
	public void testSetPropertyBoolean() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, BOOLEAN_KEY, BOOLEAN_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, BOOLEAN_KEY));
		assertEquals(BOOLEAN_VALUE, registryImpl.getBoolean(DEFAULT_GROUP_NAME, BOOLEAN_KEY));
	}

	@Test
	public void testSetPropertyInt() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, INT_KEY, INT_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_KEY));
		assertEquals(INT_VALUE, registryImpl.getInteger(DEFAULT_GROUP_NAME, INT_KEY));
	}

	@Test
	public void testSetPropertyIntArray() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, INT_ARRAY_KEY, INT_ARRAY_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_ARRAY_KEY));
		assertArrayEquals(INT_ARRAY_VALUE,
				registryImpl.getIntegerArray(DEFAULT_GROUP_NAME, INT_ARRAY_KEY));
	}

	@Test
	public void testSetPropertyLong() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, LONG_KEY, LONG_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, LONG_KEY));
		assertEquals(LONG_VALUE, registryImpl.getLong(DEFAULT_GROUP_NAME, LONG_KEY));
	}

	@Test
	public void testSetPropertyByte() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, BYTE_KEY, BYTE_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, BYTE_KEY));
		assertEquals(BYTE_VALUE, registryImpl.getByte(DEFAULT_GROUP_NAME, BYTE_KEY));
	}

	@Test
	public void testSetPropertyByteArray() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, BYTE_ARRAY_KEY, BYTE_ARRAY_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, BYTE_ARRAY_KEY));
		assertArrayEquals(BYTE_ARRAY_VALUE,
				registryImpl.getByteArray(DEFAULT_GROUP_NAME, BYTE_ARRAY_KEY));
	}

	@Test
	public void testSetPropertyChar() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, CHAR_KEY, CHAR_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, CHAR_KEY));
		assertEquals(CHAR_VALUE, registryImpl.getChar(DEFAULT_GROUP_NAME, CHAR_KEY));
	}

	@Test
	public void testSetPropertyCharArray() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, CHAR_ARRAY_KEY, CHAR_ARRAY_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, CHAR_ARRAY_KEY));
		assertArrayEquals(CHAR_ARRAY_VALUE,
				registryImpl.getCharArray(DEFAULT_GROUP_NAME, CHAR_ARRAY_KEY));
	}

	@Test
	public void testSetPropertyDouble() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, DOUBLE_KEY, DOUBLE_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, DOUBLE_KEY));
		assertEquals(DOUBLE_VALUE, registryImpl.getDouble(DEFAULT_GROUP_NAME, DOUBLE_KEY),
				DOUBLE_DELTA);
	}

	@Test
	public void testSetPropertyFloat() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, FLOAT_KEY, FLOAT_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, FLOAT_KEY));
		assertEquals(FLOAT_VALUE, registryImpl.getFloat(DEFAULT_GROUP_NAME, FLOAT_KEY), FLOAT_DELTA);
	}

	@Test
	public void testSetPropertyObject() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, OBJECT_KEY, OBJECT_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, OBJECT_KEY));
		assertEquals(OBJECT_VALUE, registryImpl.getObject(DEFAULT_GROUP_NAME, OBJECT_KEY));
	}

	@Test
	public void testSetPropertyObjectArray() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, OBJECT_ARRAY_KEY, OBJECT_ARRAY_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, OBJECT_ARRAY_KEY));
		assertArrayEquals(OBJECT_ARRAY_VALUE,
				registryImpl.getObjectArray(DEFAULT_GROUP_NAME, OBJECT_ARRAY_KEY));
	}

	@Test
	public void testSetPropertyShort() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, SHORT_KEY, SHORT_VALUE);
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, SHORT_KEY));
		assertEquals(SHORT_VALUE, registryImpl.getShort(DEFAULT_GROUP_NAME, SHORT_KEY));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetPropertyNull() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		String value = null;
		registryImpl.setProperty(DEFAULT_GROUP_NAME, STRING_KEY, value);
	}

	@Test
	public void testGetGroups() throws IOException {
		@SuppressWarnings("rawtypes")
		Enumeration e = registryImpl.getGroups();
		assertTrue(e.hasMoreElements());
		assertEquals(DEFAULT_GROUP_NAME, e.nextElement());
		assertFalse(e.hasMoreElements());
	}

	@Test
	public void testGetGroupsEmpty() throws IOException {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);

		@SuppressWarnings("rawtypes")
		Enumeration e = registryImpl.getGroups();
		assertFalse(e.hasMoreElements());
	}

	@Test
	public void testGetKeys() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, STRING_KEY, STRING_VALUE);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, INT_KEY, INT_VALUE);

		@SuppressWarnings("rawtypes")
		Enumeration e = registryImpl.getKeys(DEFAULT_GROUP_NAME);
		assertTrue(e.hasMoreElements());
		String first = (String) e.nextElement();
		assertTrue(e.hasMoreElements());
		String second = (String) e.nextElement();
		assertFalse(e.hasMoreElements());

		assertTrue((first.equals(STRING_KEY) && second.equals(INT_KEY))
				|| (first.equals(INT_KEY) && second.equals(STRING_KEY)));
	}

	@Test
	public void testGetKeysMissing() {
		@SuppressWarnings("rawtypes")
		Enumeration e = registryImpl.getKeys("nonexistent");
		assertFalse(e.hasMoreElements());
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
		assertEquals(NUM_TEST_VALUES, registryImpl.sizeOf(DEFAULT_GROUP_NAME));
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
		RegistryGroup group = registryImpl.exportGroup(DEFAULT_GROUP_NAME);
		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertNotNull(group);
		assertEquals(NUM_TEST_VALUES, group.size());
		group.clear();
		assertEquals(NUM_TEST_VALUES, registryImpl.sizeOf(DEFAULT_GROUP_NAME));
	}

	@Test
	public void testExportGroupMissing() {
		RegistryGroup group = registryImpl.exportGroup("nonexistent");
		assertNull(group);
	}

	@Test
	public void testImportGroup() {
		RegistryImpl registryImpl2 = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl2.setProperty(SECONDARY_GROUP_NAME, INT_KEY, INT_VALUE);
		registryImpl2.setProperty(SECONDARY_GROUP_NAME, STRING_KEY, STRING_VALUE);
		RegistryGroup group = registryImpl2.exportGroup(SECONDARY_GROUP_NAME);

		registryImpl.importGroup(SECONDARY_GROUP_NAME, group);

		assertEquals(2, registryImpl.size());
		assertTrue(registryImpl.isAltered());
		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME)
				&& registryImpl.isGroup(SECONDARY_GROUP_NAME));
		assertEquals(2, registryImpl.sizeOf(SECONDARY_GROUP_NAME));
		assertEquals(INT_VALUE, registryImpl.getInteger(SECONDARY_GROUP_NAME, INT_KEY));
		assertEquals(STRING_VALUE, registryImpl.getString(SECONDARY_GROUP_NAME, STRING_KEY));
	}

	@Test
	public void testImportGroupAlreadyExists() {
		RegistryImpl registryImpl2 = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl2.setProperty(SECONDARY_GROUP_NAME, INT_KEY, INT_VALUE);
		registryImpl2.setProperty(SECONDARY_GROUP_NAME, STRING_KEY, STRING_VALUE);
		RegistryGroup group = registryImpl2.exportGroup(SECONDARY_GROUP_NAME);

		registryImpl.importGroup(DEFAULT_GROUP_NAME, group);

		assertEquals(1, registryImpl.size());
		assertFalse(registryImpl.isAltered());
		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertEquals(NUM_TEST_VALUES, registryImpl.sizeOf(DEFAULT_GROUP_NAME));
	}

	@Test
	public void testInitGroup() {
		String[][] props = { { STRING_KEY, STRING_VALUE, "String" } };
		registryImpl.initGroup(SECONDARY_GROUP_NAME, props);

		assertTrue(registryImpl.isAltered());
		assertEquals(2, registryImpl.size());
		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME)
				&& registryImpl.isGroup(SECONDARY_GROUP_NAME));
		assertEquals(1, registryImpl.sizeOf(SECONDARY_GROUP_NAME));
		assertEquals(STRING_VALUE, registryImpl.getString(SECONDARY_GROUP_NAME, STRING_KEY));
	}

	@Test
	public void testInitGroupAlreadyExists() {
		String[][] props = { { STRING_KEY, "newValue", "String" } };
		registryImpl.initGroup(DEFAULT_GROUP_NAME, props);

		assertFalse(registryImpl.isAltered());
		assertEquals(1, registryImpl.size());
		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertEquals(STRING_VALUE, registryImpl.getString(DEFAULT_GROUP_NAME, STRING_KEY));
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
	public void testReferenceGroupMissing() {
		RegistryGroup group = registryImpl.referenceGroup("nonexistent");
		assertNull(group);
	}

	@Test
	public void testReplaceGroup() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);

		RegistryImpl registryImpl2 = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl2.setProperty(DEFAULT_GROUP_NAME, INT_KEY, INT_VALUE);
		RegistryGroup group = registryImpl2.referenceGroup(DEFAULT_GROUP_NAME);

		registryImpl.replaceGroup(DEFAULT_GROUP_NAME, group);

		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertTrue(registryImpl.isAltered());
		assertEquals(1, registryImpl.size());
		assertEquals(1, registryImpl.sizeOf(DEFAULT_GROUP_NAME));
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_KEY));
		assertEquals(INT_VALUE, registryImpl.getInteger(DEFAULT_GROUP_NAME, INT_KEY));
	}

	@Test
	public void testReplaceGroupOverwrite() {
		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, INT_KEY, 2);

		RegistryImpl registryImpl2 = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl2.setProperty(DEFAULT_GROUP_NAME, INT_KEY, INT_VALUE);
		RegistryGroup group = registryImpl2.referenceGroup(DEFAULT_GROUP_NAME);

		registryImpl.replaceGroup(DEFAULT_GROUP_NAME, group);

		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME));
		assertTrue(registryImpl.isAltered());
		assertEquals(1, registryImpl.size());
		assertEquals(1, registryImpl.sizeOf(DEFAULT_GROUP_NAME));
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_KEY));
		assertEquals(INT_VALUE, registryImpl.getInteger(DEFAULT_GROUP_NAME, INT_KEY));
	}

	@Test(expected = RegistryException.class)
	public void testTypeToMarkerBadType() {
		RegistryImpl.typeToMarker(-1);
	}

	@Test(expected = RegistryException.class)
	public void testMarkerToTypeBadMarker() {
		RegistryImpl.markerToType("nonexistent");
	}

	@Test
	public void testMergeRegistry() {
		String secondIntKey = "testInt3";

		registryImpl = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, INT_KEY, 1);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, secondIntKey, 3);

		RegistryImpl registryImpl2 = new RegistryImpl(RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl2.setProperty(DEFAULT_GROUP_NAME, INT_KEY, 99);
		registryImpl2.setProperty(SECONDARY_GROUP_NAME, STRING_KEY, STRING_VALUE);

		registryImpl.mergeRegistry(registryImpl2);

		assertEquals(2, registryImpl.size());
		assertTrue(registryImpl.isGroup(DEFAULT_GROUP_NAME)
				&& registryImpl.isGroup(SECONDARY_GROUP_NAME));
		assertTrue(registryImpl.isAltered());

		assertEquals(2, registryImpl.sizeOf(DEFAULT_GROUP_NAME));
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_KEY));
		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, secondIntKey));
		assertEquals(99, registryImpl.getInteger(DEFAULT_GROUP_NAME, INT_KEY));
		assertEquals(3, registryImpl.getInteger(DEFAULT_GROUP_NAME, secondIntKey));

		assertEquals(1, registryImpl.sizeOf(SECONDARY_GROUP_NAME));
		assertTrue(registryImpl.isProperty(SECONDARY_GROUP_NAME, secondIntKey));
		assertEquals(STRING_VALUE, registryImpl.getString(SECONDARY_GROUP_NAME, STRING_KEY));
	}

	@Test
	public void testCommit() throws IOException {
		File tmpRegistryFile = folder.newFile(TMP_REGISTRY_FILE);

		registryImpl = new RegistryImpl(folder.newFile(TMP_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);
		registryImpl.setProperty(DEFAULT_GROUP_NAME, INT_KEY, INT_VALUE);
		registryImpl.commit();

		Reader reader = new FileReader(tmpRegistryFile);
		RegistryImpl registryImpl2 = new RegistryImpl(reader, RegistryImpl.FILE_SYNTAX_VERSION);

		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_KEY));
		assertTrue(registryImpl2.isProperty(DEFAULT_GROUP_NAME, INT_KEY));
		assertEquals(1, registryImpl.size());
		assertEquals(1, registryImpl2.size());

		assertFalse(registryImpl.isAltered());
	}

	@Test
	public void testRevert() throws IOException {
		registryImpl = new RegistryImpl(folder.newFile(TMP_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);

		registryImpl.setProperty(DEFAULT_GROUP_NAME, INT_KEY, INT_VALUE);
		registryImpl.commit();
		registryImpl.setProperty(SECONDARY_GROUP_NAME, INT_KEY, 2);
		registryImpl.revert();

		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_KEY));
		assertFalse(registryImpl.isProperty(SECONDARY_GROUP_NAME, INT_KEY));
		assertEquals(1, registryImpl.size());
		assertFalse(registryImpl.isAltered());
	}

	@Test
	public void testRevertNotAltered() throws IOException {
		registryImpl = new RegistryImpl(folder.newFile(TMP_REGISTRY_FILE),
				RegistryImpl.FILE_SYNTAX_VERSION);

		registryImpl.setProperty(DEFAULT_GROUP_NAME, INT_KEY, INT_VALUE);
		registryImpl.commit();
		registryImpl.revert();

		assertTrue(registryImpl.isProperty(DEFAULT_GROUP_NAME, INT_KEY));
		assertEquals(1, registryImpl.size());
		assertFalse(registryImpl.isAltered());
	}
	
	@Test
	public void testToString() {
		String out = registryImpl.toString();
		assertNotNull(out);
		assertTrue(out.contains("Registry dump:"));
		assertTrue(out.contains(STRING_VALUE));
	}

	private static class Foo implements Serializable {
		int i;

		public Foo(int i) {
			this.i = i;
		}

		@Override
		public boolean equals(Object other) {
			if (other == null) {
				return false;
			}
			if (!(other instanceof Foo)) {
				return false;
			}

			Foo that = (Foo) other;
			return this.i == that.i;
		}
	}
}
