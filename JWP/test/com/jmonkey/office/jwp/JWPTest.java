package com.jmonkey.office.jwp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import org.junit.Test;

import com.jmonkey.export.RegistryFormatException;
import com.jmonkey.office.jwp.JWP.MainDesktop;
import com.jmonkey.office.jwp.support.EditorActionManager;


public class JWPTest {

	public static final String HISTORY_FILE = "resources/file_history.txt";
	
	public static final int VERTICAL = 1;
	public static final int HORIZONTAL = 0;

	public static JWP jwp;

	@Test
	public void testGetDesktop() throws RegistryFormatException {
		MainDesktop desktop = null;

		jwp = new JWP(null);

		desktop = jwp.getDesktop();

		assertTrue(desktop instanceof MainDesktop);
	}

	@Test
	public void testGetResources() {
		ResouceBundle bundle = null;

		jwp = new JWP(null);

		bundle = jwp.getResources();

		assertTrue(bundle instanceof ResourceBundle);
	}

	@Test
	public void testScrollableViewportSize() {
		Dimension dim = new Dimension();

		jwp = new JWP(null);

		dim = jwp.getPreferredScrollableViewportSize();

		assertTrue(dim instanceof Dimension);
		assertFalse(dim == null);
	}

	@Test
	public void testScrollingUnitIncrement() {
		int hori, vert;
		Rectangle rect = new Rectangle(10, 10, 0, 0);

		jwp = new JWP(null);

		hori = jwp.getScrollableUnitIncrement(rect, HORIZONTAL, 0);
		vert = jwp.getScrollableUnitIncrement(rect, VERTICAL, 0);

		assertTrue(hori == 1);
		assertTrue(vert == 1);
	}

	@Test
	public void testScrollingBlockIncrement() {
		int height, width;
		Rectangle rect = new Rectangle(10, 10, 0, 0);

		jwp = new JWP(null);

		width = jwp.getScrollableBlockIncrement(rect, HORIZONTAL, 0);
		height = jwp.getScrollableBlockIncrement(rect, VERTICAL, 0);

		assertTrue(width == 10);
		assertTrue(height == 10);
	}

	@Test
	public void testScrollableTrackingWidth() {
		jwp = new JWP(null);

		assertFalse(jwp.getScrollableTrackingViewportWidth());
	}

	@Test
	public void testScrollableTrackingHeight() {
		jwp = new JWP(null);

		assertFalse(jwp.getScrollableTrackingViewportHeight());
	}


	//Note to self ask how to make a class in a class
	@Test
	public void testIconWidth() {
		jwp = new JWP(null);

		assertEqual(jwp.getIconWidth(), 16);
	}

	@Test
	public void testIconHeight() {
		jwp = new JWP(null);

		assertEqual(jwp.getIconHeight(), 16);
	}

	@Test
	public void testAddToFileHistory() throws RegistryFormatException {
		jwp = new JWP(null);

		jwp.addToFileHistory(new File(HISTORY_FILE));

		assertTrue(jwp.m_fileHistory.getItemCount() == 1);
	}

	//Look at Jareds private method stuff
	@Test
	public void testCreateMenuBar() {
		JMenuBar menu = new JMenuBar();

		jwp = new JWP(null);

		menu = jwp.createMenuBar();

		assertTrue(menu instanceof JMenuBar);
		assertFalse(menu == null);
	}

	@Test
	public void testExit() throws RegistryFormatException {
		jwp = new JWP(null);

		jwp.doExit();

		assertTrue(jwp == null);
	}

	@Test
	public void testEditorActionManager() throws RegistryFormatException {
		EditorActionManager eam = new EditorActionManager(null, null);

		jwp = new JWP(null);

		eam = jwp.getEditorActionManager();

		assertTrue(eam instanceof EditorActionManager);
		assertFalse(eam == null);
	}

	@Test
	public void testGetCommandToolbar() throws RegistryFormatException {
		JToolBar toolbar = new JToolBar();

		jwp = new JWP(null);

		toolbar = jwp.getFileToolBar();

		assertTrue(toolbar instanceof JToolBar);
		assertFalse(toolbar == null);
	}

	@Test
	public void testGetFormatToolbar() throws RegistryFormatException {
		JToolBar toolbar = new JToolBar();

		jwp = new JWP(null);

		toolbar = jwp.getFormatToolBar();

		assertTrue(toolbar instanceof JToolBar);
		assertFalse(toolbar == null);
	}

	@Test
	public void testGetFontSizes() throws RegistryFormatException {
		jwp = new JWP(null);

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