package com.jmonkey.office.jwp.support;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JEditorPane;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;

import org.junit.Before;
import org.junit.Test;

import com.jmonkey.export.Registry;
import com.jmonkey.export.RegistryFormatException;
import com.jmonkey.office.jwp.JWP;
import com.jmonkey.office.jwp.support.editors.HTMLEditor;
import com.jmonkey.office.jwp.support.editors.RTFEditor;
import com.jmonkey.office.jwp.support.editors.TEXTEditor;

public class EditorTest {

	EditorActionManager mgr;
	MockEditor mockEditor;

	@Before
	public void setup() {
		mgr = new EditorActionManager(null, null);
		mockEditor = new MockEditor(mgr);
	}

	@Test
	public void testActivate() {
		assertNotSame(EditorActionManager.getActiveEditor(), mockEditor);
		mockEditor.activate();
		assertSame(EditorActionManager.getActiveEditor(), mockEditor);
	}

	@Test
	public void testDeactivate() {
		mockEditor.activate();
		assertSame(EditorActionManager.getActiveEditor(), mockEditor);
		mockEditor.deactivate();
		assertNotSame(EditorActionManager.getActiveEditor(), mockEditor);
	}

	@Test
	public void testGetEditorActionManager() {
		assertSame(mgr, mockEditor.getEditorActionManager());
	}

	@Test
	public void testCreateForContentType_TEXT() throws RegistryFormatException {
		JWP jwp = new JWP(null);
		Editor editor = Editor.createEditorForContentType("text/plain", jwp);
		assertTrue(editor instanceof TEXTEditor);
	}

	// **** Create for Content Type **** //

	@Test
	public void testCreateForContentType_HTML() throws RegistryFormatException {
		JWP jwp = new JWP(null);
		Editor editor = Editor.createEditorForContentType("text/html", jwp);
		assertTrue(editor instanceof HTMLEditor);
	}

	@Test
	public void testCreateForContentType_RTF() throws RegistryFormatException {
		JWP jwp = new JWP(null);
		Editor editor = Editor.createEditorForContentType("text/rtf", jwp);
		assertTrue(editor instanceof RTFEditor);
	}

	@Test
	public void testCreateForContentType_UNKNOWN() throws RegistryFormatException {
		JWP jwp = new JWP(null);
		Editor editor = Editor.createEditorForContentType("text/non-existant", jwp);
		assertTrue(editor instanceof TEXTEditor);
	}

	// **** Create for Extension **** //
	@Test
	public void testCreateForExtension_TEXT1() throws RegistryFormatException {
		JWP jwp = new JWP(null);
		Editor editor = Editor.createEditorForExtension("txt", jwp);
		assertTrue(editor instanceof TEXTEditor);
	}

	@Test
	public void testCreateForExtension_TEXT2() throws RegistryFormatException {
		JWP jwp = new JWP(null);
		Editor editor = Editor.createEditorForExtension("text", jwp);
		assertTrue(editor instanceof TEXTEditor);
	}

	@Test
	public void testCreateForExtension_TEXT3() throws RegistryFormatException {
		JWP jwp = new JWP(null);
		Editor editor = Editor.createEditorForExtension("blarg", jwp);
		assertTrue(editor instanceof TEXTEditor);
	}

	@Test
	public void testCreateForExtension_HTML1() throws RegistryFormatException {
		JWP jwp = new JWP(null);
		Editor editor = Editor.createEditorForExtension("html", jwp);
		assertTrue(editor instanceof HTMLEditor);
	}

	@Test
	public void testCreateForExtension_HTML2() throws RegistryFormatException {
		JWP jwp = new JWP(null);
		Editor editor = Editor.createEditorForExtension("htm", jwp);
		assertTrue(editor instanceof HTMLEditor);
	}

	@Test
	public void testCreateForExtension_RTF() throws RegistryFormatException {
		JWP jwp = new JWP(null);
		Editor editor = Editor.createEditorForExtension("rtf", jwp);
		assertTrue(editor instanceof RTFEditor);
	}

	@Test
	public void testGetFile_NULL() {
		assertNull(mockEditor.getFile());
	}

	@Test
	public void testGetSetFile() {
		File f = new File("");
		assertNull(mockEditor.getFile());
		mockEditor.setFile(f);
		assertNotNull(mockEditor.getFile());
		assertSame(f, mockEditor.getFile());
	}

	@Test
	public void testGetPopup() {
		assertNotNull(mockEditor.getPopup());
	}

	@Test
	public void testGetRegistry() {
		Registry registry = mockEditor.getRegistry();
		// / Init the registry
		assertNotNull(registry);
		Enumeration<?> keys = registry.getKeys("POPUP");
		// Make sure each key as an accociated action
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			assertNotNull(mgr.getActionByKey(key));
		}
	}

	@Test
	public void testGetSimpleAttributeSet() {
		assertNotNull(mockEditor.getSimpleAttributeSet());
	}

	@Test
	public void testGetUndoManager() {
		assertNotNull(mockEditor.getUndoManager());
	}

	/**
	 * Mock Editor class that does not implement any of the abstract methods.
	 * Used for testing the methos on the abstract Editor class.
	 */
	private class MockEditor extends Editor {

		private static final long serialVersionUID = 1L;

		protected MockEditor(EditorActionManager eam) {
			super(eam);
		}

		@Override
		public void append(File file) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void documentSetSelection(int start, int length, boolean wordsOnly) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getContentType() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Element getCurrentParagraph() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Element getCurrentRun() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String[] getFileExtensions() {
			throw new UnsupportedOperationException();
		}

		@Override
		public MutableAttributeSet getInputAttributes() {
			throw new UnsupportedOperationException();
		}

		@Override
		public JEditorPane getTextComponent() {
			// Return a dummy editor pane for testing
			return new JEditorPane();
		}

		@Override
		public void hasBeenActivated(Editor editor) {
			// NOP
		}

		@Override
		public void hasBeenDeactivated(Editor editor) {
			// NOP
		}

		@Override
		public void insert(File file, int position) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isChanged() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isEmpty() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isFormatted() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNew() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void read(File file) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChanged(boolean changed) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrentParagraph(Element paragraph) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrentRun(Element run) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void write(File file) throws IOException {
			throw new UnsupportedOperationException();
		}
	}
}
