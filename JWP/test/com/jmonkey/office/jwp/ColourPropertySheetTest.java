package com.jmonkey.office.jwp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jmonkey.export.RegistryFormatException;

public class ColourPropertySheetTest {

	private static JWP jwp;
	private static Properties properties;
	private static ColourPropertySheet cps;

	@BeforeClass
	public static void setupClass() throws RegistryFormatException {
		jwp = new JWP(null);
		properties = new Properties();
		properties.setProperty("red", "#FF0000");
	}

	@Before
	public void setup() {
		cps = new ColourPropertySheet(jwp, properties, true);
	}

	@Test
	public void testConstructor() throws RegistryFormatException {
		assertSame(jwp, invokePrivateMethod(cps, "getMain"));
		assertSame(properties, invokePrivateMethod(cps, "getProperties"));
	}
	
	@Test
	public void testModelIsCellEditable() throws RegistryFormatException {
		AbstractTableModel model = (AbstractTableModel) instantiatePrivateClass(cps, "PairTableModel");

		assertFalse(model.isCellEditable(0, 0));
		assertTrue(model.isCellEditable(0, 1));
		assertFalse(model.isCellEditable(0, 2));
	}

	@Test
	public void testModelGetColumnClass() throws RegistryFormatException {
		AbstractTableModel model = (AbstractTableModel) instantiatePrivateClass(cps, "PairTableModel");

		assertEquals(String.class, model.getColumnClass(0));
		assertEquals(String.class, model.getColumnClass(1));
		assertEquals(String.class, model.getColumnClass(2));
	}
	
	@Test
	public void testModelGetColumnName() throws RegistryFormatException {
		AbstractTableModel model = (AbstractTableModel) instantiatePrivateClass(cps, "PairTableModel");

		assertEquals("Colour Name", model.getColumnName(0));
		assertEquals("RGB Hex", model.getColumnName(1));
		assertEquals(null, model.getColumnName(2));
	}
	
	@Test
	public void testModelGetValueAt() throws RegistryFormatException {
		AbstractTableModel model = (AbstractTableModel) instantiatePrivateClass(cps, "PairTableModel");

		assertEquals("red", model.getValueAt(0, 0));
		assertEquals("#FF0000", model.getValueAt(0, 1));
		assertEquals("", model.getValueAt(0, 2));
	}
	
	@Test
	public void testModelSetValueAt() throws RegistryFormatException {
		AbstractTableModel model = (AbstractTableModel) instantiatePrivateClass(cps, "PairTableModel");

		assertEquals("red", model.getValueAt(0, 0));
		assertEquals("#FF0000", model.getValueAt(0, 1));
		
		model.setValueAt("blue", 0, 0);
		model.setValueAt("#0000FF", 0, 1);
		
		assertEquals("red", model.getValueAt(0, 0));
		assertEquals("#0000FF", model.getValueAt(0, 1));
	}

	/**
	 * Invoke a private method with no arguments.
	 * 
	 * @param object
	 *            The object to invoke the method on
	 * @param methodName
	 *            The name of the method to invoke
	 * @return The return value of the method
	 */
	private static Object invokePrivateMethod(Object object, String methodName) {
		return invokePrivateMethod(object, methodName, new Object[0]);
	}

	/**
	 * Invoke a private method.
	 * 
	 * @param object
	 *            The object to invoke the method on
	 * @param methodName
	 *            The name of the method to invoke
	 * @param args
	 *            An array of arguments to pass to the method
	 * @return The return value of the method
	 */
	private static Object invokePrivateMethod(Object object, String methodName, Object[] args) {
		try {
			Method method = object.getClass().getDeclaredMethod(methodName);
			method.setAccessible(true);
			return method.invoke(object, args);
		} catch (Exception e) {
			throw new RuntimeException("Failed to invoke method '" + methodName + "'", e);
		}
	}

	private static Object instantiatePrivateClass(Object object, String innerClassName) {
		Object inner = null;
		Class<?> innerClasses[] = object.getClass().getDeclaredClasses();
		for (Class<?> c : innerClasses) {
			if (innerClassName.equals(c.getSimpleName())) {
				Constructor<?> constructor = c.getDeclaredConstructors()[0];
				constructor.setAccessible(true);

				try {
					System.out.println(object);
					inner = constructor.newInstance(new Object[] { object });
				} catch (Exception e) {
					throw new RuntimeException("Could not instantiate inner class '" + innerClassName + "'", e);
				}

				return inner;
			}
		}

		throw new RuntimeException("Could not find inner class '" + innerClassName + "'");
	}
}
