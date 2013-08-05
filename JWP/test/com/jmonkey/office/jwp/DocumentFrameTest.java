package com.jmonkey.office.jwp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jmonkey.export.RegistryFormatException;

public class DocumentFrameTest {

	private static JWP jwp;
	private static DocumentFrame frame;

	@BeforeClass
	public static void setupClass() throws RegistryFormatException {
		jwp = new JWP(null);
	}

	@Before
	public void setup() {
		frame = new DocumentFrame(jwp, "text/plain"); 
	}

	@Test
	public void testGetName() {
		assertTrue(frame.getName() != null);
	}

	@Test
	public void testGetEditor() {
		assertTrue(frame.getEditor() instanceof Editor);
		assertFalse(frame.getEditor() == null);
	}

	@Test
	public void testVetoableChange() throws PropertyVetoException {
		PropertyChangeEvent pce = new PropertyChangeEvent(frame, "closed", false, true);
	}

	@Test
	public void testEvents() {
		InternalFrameEvent ife = new InternalFrameEvent(frame, 25555);
		FocusEvent fe = new FocusEvent(frame, 1004);

		internalFrameOpened(ife);
		internalFrameClosing(ife);
		internalFrameClosed(ife);
		internalFrameIconified(ife);
		internalFrameDeiconified(ife);
		internalFrameActivated(ife);
		internalFrameDeactivated(ife);
		focusGained(fe);
		focusLost(fe);
	}

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
			if ("PairTableModel".equals(c.getSimpleName())) {
				Constructor<?> constructor = c.getDeclaredConstructors()[0];
				constructor.setAccessible(true);

				try {
					inner = constructor.newInstance(new Object[] { cps });
				} catch (Exception e) {
					throw new RuntimeException("Could not instantiate inner class '"
							+ innerClassName + "'", e);
				}

				return inner;
			}
		}

		throw new RuntimeException("Could not find inner class '" + innerClassName + "'");
	}
}