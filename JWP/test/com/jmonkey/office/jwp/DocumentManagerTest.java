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
	private static DocumentManager manager;
	private static DocumentFrame doc;
	private static Editor ed;

	private static String filepath = "resources/text.txt";

	@BeforeClass
	public static void setupClass() throws RegistryFormatException {
		jwp = new JWP(null);
		doc = new DocumentFrame(jwp, "text/plain");
		ed = doc.getEditor();
	}

	@Before
	public void setup() {
		manager = new DocumentManager(jwp); 
	}

	@Test
	public void testEditorNew() {
		manager.editorNew();
	}

	@Test
	public void testEditorOpenFile() {
		File file = new File(filepath);

		manager.editoropen(file);
	}

	@Test
	public void testEditorOpen() {
		manager.editorOpen();
	}

	/*  Opens a file chooser don't know how that works with JUnit
	@Test
	public void testEditorOpenAs() {
		manager.editorOpenAs();
	}
	*/

	@Test
	public void testEditorRevert() {
		manager.editorRevert(ed);
	}

	/* Uses a file Chooser window?
	@Test
	public void testEditorSave() {
		manager.editorSave(ed);
	}
	*/

	/* Uses a file Chooser window?
	@Test
	public void testEditorSaveAs() {
		manager.editorSaveAs(ed);
	}
	*/

	/* Uses a file Chooser window?
	@Test
	public void testEditorCopy() {
		manager.editorCopy(ed);
	}
	*/

	@Test
	public void testCreateDocumentFrame1() {
		File file = new File(filepath);

		DocumentFrame df = manager.createDocumentFrame(file, "testdoc.txt", "text/plain");

		assertFalse(df == null);
	}

	@Test
	public void testCreateDocumentFrame2() {
		File file = new File(filepath);

		DocumentFrame df = manager.createDocumentFrame(file);

		assertFalse(df == null);
	}


	@Test
		public void testCreateDocumentFrame3() {
			DocumentFrame df = manager.createDocumentFrame("text/plain");

			assertFalse(df == null);
	}

	@Test
		public void testCreateDocumentFrame4() {
			DocumentFrame df = manager.createDocumentFrame();

			assertFalse(df == null);
	}


	@Test
	public void testOpenDocumentList() {
		String[] list = manager.openDocumentList();
	}

	@Test
	public void testGetOpenDocument() {
		DocumentFrame df = manager.getopenDocument("text.txt");
	}

	@Test
	public void testActiveFrame() {
		manager.activateFrame(doc);
	}

	@Test
	public void testActive() {
		DocumentFrame df = manager.active();
	}

	@Test
	public void testGetApp() {
		JWP words = manager.getApp();
	}

	@Test
	public void testInit() {
		manager.init();
	}

	@Test
	public void testSwitchedDocument() {
		manager.switchedDocument(doc, false);
	}

	/* This is a visual thing so I can only see if it runs
	@Test
	public void testCascade() {
		manager.cascade(doc);
	}
	*/

	/* This is a visual thing so I can only see if it runs
	@Test
	public void testCascadeAll() {
		manager.cascadeAll();
	}
	*/

	@Test
	public void testCloseActiveDocument() {
		boolean bool = manager.closeActiveDocument();
	}

	@Test
	public void testCloseAllDocuments() {
		manager.closeAllDocument();
	}

	@Test
	public void testMinimizeAll() {
		manager.minimizeAll();
	}

	@Test
	public void testTileAll() {
		manager.tileAll();
	}

	@Test
	public void testGetParent() {
		JWP words = manager.getParent();
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