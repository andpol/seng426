package com.jmonkey.office.jwp;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jmonkey.export.RegistryFormatException;
import com.jmonkey.office.jwp.support.Editor;

public class DocumentManagerTest {

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

		manager.editorOpen(file);
	}

	@Test
	public void testEditorOpen() {
		manager.editorOpen();
	}

	@Test
	public void testEditorOpenAs() {
		manager.editorOpenAs();
	}

	@Test
	public void testEditorRevert() {
		manager.editorRevert(ed);
	}


	@Test
	public void testEditorSave() {
		manager.editorSave(ed);
	}

	@Test
	public void testEditorSaveAs() {
		manager.editorSaveAs(ed);
	}

	@Test
	public void testEditorCopy() {
		manager.editorSaveCopy(ed);
	}

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
		DocumentFrame df = manager.getOpenDocument("text.txt");
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
		invokePrivateMethod(manager, "init", null);
	}

	@Test
	public void testSwitchedDocument() {
		manager.switchedDocument(doc, false);
	}

	@Test
	public void testCascade() {
		manager.cascade(doc);
	}

	@Test
	public void testCascadeAll() {
		manager.cascadeAll();
	}

	@Test
	public void testCloseActiveDocument() {
		boolean bool = manager.closeActiveDocument();
	}

	@Test
	public void testCloseAllDocuments() {
		manager.closeAllDocuments();
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
		JWP words = (JWP) invokePrivateMethod(manager, "getParent", null);
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
			if (innerClassName.equals(c.getSimpleName())) {
				Constructor<?> constructor = c.getDeclaredConstructors()[0];
				constructor.setAccessible(true);

				try {
					inner = constructor.newInstance(new Object[] { manager });
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