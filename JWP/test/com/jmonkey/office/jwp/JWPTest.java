package com.jmonkey.office.jwp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import org.junit.Before;
import org.junit.Test;

import com.jmonkey.export.RegistryFormatException;
import com.jmonkey.office.jwp.JWP.MainDesktop;
import com.jmonkey.office.jwp.support.EditorActionManager;


public class JWPTest {

	public static final String HISTORY_FILE = "resources/file_history.txt";
	
	public static final int VERTICAL = 1;
	public static final int HORIZONTAL = 0;

	public static JWP jwp;
	
	@Before
	public void setup() throws RegistryFormatException {
		jwp = new JWP(null);
	}

	@Test
	public void testGetDesktop() {
		MainDesktop desktop = null;

		desktop = jwp.getDesktop();

		assertTrue(desktop instanceof MainDesktop);
	}

	@Test
	public void testGetResources() {
		ResourceBundle bundle = null;

		bundle = jwp.getResources();

		assertTrue(bundle instanceof ResourceBundle);
	}
	
	@Test
	public void testGetRegistry() {
		jwp.getRegistry();
	}

	@Test
	public void testScrollableViewportSize() {
		Dimension dim = new Dimension();

		dim = ((MainDesktop) instantiatePrivateClass(jwp, "MainDesktop")).getPreferredScrollableViewportSize();

		assertTrue(dim instanceof Dimension);
		assertFalse(dim == null);
	}

	@Test
	public void testScrollingUnitIncrement() {
		int hori, vert;
		Rectangle rect = new Rectangle(10, 10, 0, 0);

		hori = ((MainDesktop) instantiatePrivateClass(jwp, "MainDesktop")).getScrollableUnitIncrement(rect, HORIZONTAL, 0);
		vert = ((MainDesktop) instantiatePrivateClass(jwp, "MainDesktop")).getScrollableUnitIncrement(rect, VERTICAL, 0);

		assertTrue(hori == 1);
		assertTrue(vert == 1);
	}

	@Test
	public void testScrollingBlockIncrement() {
		int height, width;
		Rectangle rect = new Rectangle(10, 10, 0, 0);

		width = ((MainDesktop) instantiatePrivateClass(jwp, "MainDesktop")).getScrollableBlockIncrement(rect, HORIZONTAL, 0);
		height = ((MainDesktop) instantiatePrivateClass(jwp, "MainDesktop")).getScrollableBlockIncrement(rect, VERTICAL, 0);

		assertTrue(width == 10);
		assertTrue(height == 10);
	}

	@Test
	public void testScrollableTrackingWidth() {
		((MainDesktop) instantiatePrivateClass(jwp, "MainDesktop")).getScrollableTracksViewportWidth();
	}

	@Test
	public void testScrollableTrackingHeight() {
		((MainDesktop) instantiatePrivateClass(jwp, "MainDesktop")).getScrollableTracksViewportHeight();
	}


	//Note to self ask how to make a class in a class
	/* Wierd Triple class case?
	@Test
	public void testIconWidth() {
		assertEqual(jwp.getIconWidth(), 16);
	}

	@Test
	public void testIconHeight() {
		assertEqual(jwp.getIconHeight(), 16);
	}
	*/
	
	@Test
	public void testAddToFileHistory() throws RegistryFormatException {
		jwp.addToFileHistory(new File(HISTORY_FILE));

		assertTrue(jwp.m_fileHistory.getItemCount() == 1);
	}

	//Look at Jareds private method stuff
	@Test
	public void testCreateMenuBar() {
		JMenuBar menu = new JMenuBar();

		menu = (JMenuBar) invokePrivateMethod(jwp, "createMenuBar", null);

		assertTrue(menu instanceof JMenuBar);
		assertFalse(menu == null);
	}
	/*
	@Test
	public void testExit() {
		jwp.doExit();

		assertTrue(jwp == null);
	}
	*/
	@Test
	public void testEditorActionManager() {
		EditorActionManager eam = new EditorActionManager(null, null);

		eam = jwp.getEditorActionManager();

		assertTrue(eam instanceof EditorActionManager);
		assertFalse(eam == null);
	}

	@Test
	public void testGetCommandToolbar() {
		JToolBar toolbar = new JToolBar();

		toolbar = jwp.getFileToolBar();

		assertTrue(toolbar instanceof JToolBar);
		assertFalse(toolbar == null);
	}

	@Test
	public void testGetFormatToolbar() {
		JToolBar toolbar = new JToolBar();

		toolbar = jwp.getFormatToolBar();

		assertTrue(toolbar instanceof JToolBar);
		assertFalse(toolbar == null);
	}

	@Test
	public void testGetFontSizes() {
		int[] sizes;

		sizes = jwp.getFontSizes();

		assertTrue(sizes.length > 0);
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
					inner = constructor.newInstance(new Object[] { jwp });
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