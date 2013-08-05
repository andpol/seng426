package com.jmonkey.office.jwp.support.editors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.Action;
import javax.swing.text.Element;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jmonkey.office.jwp.support.EditorActionManager;

public class StyledEditorTest {

	public static final String SAMPLE_FILE_PATH = "resources/sample_file.txt";

	StyledEditor editor;
	EditorActionManager mgr;
	File sampleFile;

	@Before
	public void setup() throws Exception {
		mgr = new EditorActionManager(null, null);
		editor = new MockStyledEditor(mgr);

		sampleFile = new File(SAMPLE_FILE_PATH);
		if (!(sampleFile.exists() && sampleFile.isFile() && sampleFile.canRead())) {
			throw new Exception("Could not setup tests.");
		}
	}

	@After
	public void teardown() throws IOException {
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

	private class MockStyledEditor extends StyledEditor {
		private static final long serialVersionUID = 1L;

		public MockStyledEditor(EditorActionManager eam) {
			super(eam);
		}

		@Override
		public String[] getFileExtensions() {
			return null;
		}

	}

	@Test
	public void testAppend() throws Exception {
		editor.append(sampleFile);
		FileInputStream fs = new FileInputStream(sampleFile);
		byte[] data = new byte[(int) sampleFile.length()];
		fs.read(data);
		fs.close();
		String content = new String(data);
		// We need to sleep for a bit, since append is an async call
		Thread.sleep(50);
		assertEquals(content, editor.getTextComponent().getDocument().getText(0, editor.getTextComponent().getDocument().getLength()));
	}

	@Test
	public void testDocumentSetSelection_SPLIT_WORD() throws Exception {
		editor.getTextComponent().setText("This is a testing string");
		editor.documentSetSelection(0, 12, false);
		assertEquals("This is a te", editor.getTextComponent().getSelectedText());
	}

	@Test
	public void testDocumentSetSelection_WHOLE_WORD() throws Exception {
		editor.getTextComponent().setText("This is a testing string");
		editor.documentSetSelection(0, 12, true);
		assertEquals("This is a testing", editor.getTextComponent().getSelectedText());
	}

	@Test
	public void testGetContentType() {
		assertEquals("text/plain", editor.getContentType());
	}

	@Test
	public void testGetTextComponentt() {
		assertNotNull(editor.getTextComponent());
	}

	/**
	 * Test that the editor in the "new" state has the correct state
	 */
	@Test
	public void testEditorHasBeenActivated1() {
		Action revert = mgr.getActionByKey(EditorActionManager.F_R_A_P);
		Action save = mgr.getActionByKey(EditorActionManager.F_S_A_P);

		editor.setFile(new File("some_text_file_that_does_not_exist.txt"));
		editor.postActivate();

		assertEquals(false, revert.isEnabled());
		assertEquals(true, save.isEnabled());
	}

	/**
	 * Test that the editor actions have the correct state when the editor does
	 * not have a file set.
	 */
	@Test
	public void testEditorHasBeenActivated2() {
		Action revert = mgr.getActionByKey(EditorActionManager.F_R_A_P);
		Action save = mgr.getActionByKey(EditorActionManager.F_S_A_P);

		editor.postActivate();

		assertEquals(false, revert.isEnabled());
		assertEquals(true, save.isEnabled());
	}

	/**
	 * Test that the editor in the "changed" state has the correct state
	 */
	@Test
	public void testEditorHasBeenActivated3() {
		Action revert = mgr.getActionByKey(EditorActionManager.F_R_A_P);
		Action save = mgr.getActionByKey(EditorActionManager.F_S_A_P);

		editor.setFile(sampleFile); // File that exists
		editor.setChanged(true);
		editor.postActivate();

		assertEquals(true, revert.isEnabled());
		assertEquals(true, save.isEnabled());
	}

	/**
	 * Test that the editor NOT in the "changed" state has the correct state
	 */
	@Test
	public void testEditorHasBeenActivated4() {
		Action revert = mgr.getActionByKey(EditorActionManager.F_R_A_P);
		Action save = mgr.getActionByKey(EditorActionManager.F_S_A_P);

		editor.setFile(sampleFile); // File that exists
		editor.setChanged(false);
		editor.postActivate();

		assertEquals(true, revert.isEnabled());
		assertEquals(false, save.isEnabled());
	}

	@Test
	public void testEditorHasBeenDeactivated() {
		editor.postDeactivate();
	}

	@Test
	public void testInsert() throws Exception {
		editor.getTextComponent().setText("012345");
		editor.insert(sampleFile, 2);

		FileInputStream fs = new FileInputStream(sampleFile);
		byte[] data = new byte[(int) sampleFile.length()];
		fs.read(data);
		fs.close();
		String content = new String(data);

		// We need to sleep for a bit, since insert is an async call
		Thread.sleep(50);
		assertEquals("01" + content + "2345", editor.getTextComponent().getDocument().getText(0, editor.getTextComponent().getDocument().getLength()));
	}

	@Test
	public void testIsSetChanged_FALSE() {
		editor.setChanged(false);
		assertEquals(false, editor.isChanged());
	}

	@Test
	public void testIsSetChanged_TRUE() {
		editor.setChanged(true);
		assertEquals(true, editor.isChanged());
	}

	@Test
	public void testIsEmpty_FALSE() {
		editor.getTextComponent().setText("not empty text");
		assertEquals(false, editor.isEmpty());
	}

	@Test
	public void testIsEmpty_TRUE() {
		assertEquals(true, editor.isEmpty());
	}

	@Test
	public void testIsFormatted() {
		assertEquals(false, editor.isFormatted());
	}

	@Test
	public void testIsNew_FALSE() {
		editor.setFile(sampleFile); // Existing file
		assertEquals(false, editor.isNew());
	}

	@Test
	public void testIsNew_TRUE() {
		assertEquals(true, editor.isNew());
	}

	@Test
	public void testGetSetCurrentParagraph() {
		Element e = editor.getTextComponent().getDocument().getDefaultRootElement();
		editor.setCurrentParagraph(e);
		assertSame(e, editor.getCurrentParagraph());
	}

	@Test
	public void testGetSetCurrentRun() {
		Element e = editor.getTextComponent().getDocument().getDefaultRootElement();
		editor.setCurrentRun(e);
		assertSame(e, editor.getCurrentRun());
	}

	@Test
	public void testRead() throws Exception {
		editor.read(sampleFile); // Non-empty file

		FileInputStream fs = new FileInputStream(sampleFile);
		byte[] data = new byte[(int) sampleFile.length()];
		fs.read(data);
		fs.close();
		String content = new String(data);

		// We need to sleep for a bit, since read is an async call
		Thread.sleep(50);
		assertEquals(content, editor.getTextComponent().getDocument().getText(0, editor.getTextComponent().getDocument().getLength()));
	}

	@Test
	public void testWrite() throws Exception {
		String str = "My example text to be written!";
		editor.getTextComponent().setText(str);
		File f = new File("some_random_new_filename.txt");
		editor.write(f);
		// We need to sleep for a bit, since write is an async call
		Thread.sleep(50);

		FileInputStream fs = new FileInputStream(f);
		byte[] data = new byte[(int) f.length()];
		fs.read(data);
		fs.close();
		String fileContent = new String(data);

		assertEquals(str, fileContent);
	}

	@Test
	public void testSetCaretBlinkRate() {
		editor.setCaretBlinkRate(9001);
		assertEquals(9001, editor.getTextComponent().getCaret().getBlinkRate());
	}

	@Test
	public void testSetCaretColor() {
		editor.setCaretColor(Color.RED);
		assertEquals(Color.RED, editor.getTextComponent().getCaretColor());
	}

	@Test
	public void testSetSelectionColor() {
		editor.getTextComponent().setText("Foxes jumping over dogs");
		editor.getTextComponent().setSelectionStart(0);
		editor.getTextComponent().setSelectionEnd(8);

		editor.setSelectionColor(Color.RED);
		assertEquals(Color.RED, editor.getTextComponent().getSelectionColor());

		editor.setSelectionColor(Color.BLUE);
		assertEquals(Color.BLUE, editor.getTextComponent().getSelectionColor());
	}

	@Test
	public void testGetInputAttributes() {
		assertNotNull(editor.getInputAttributes());
	}
}
