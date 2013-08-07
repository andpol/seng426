package com.jmonkey.office.jwp.support.editors;

import java.util.ArrayList;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;

import com.jmonkey.office.jwp.support.EditorActionManager;

/**
 * This class is the Lexi document editor for Rich Text (RTF) documents.
 */
public final class RTFEditor extends StyledEditor {
	private static final long serialVersionUID = -5332137491506456678L;

	/**
	 * The Content type of the editor.
	 */
	public static final String CONTENT_TYPE = VALID_CONTENT_TYPES[2];

	/**
	 * File Extensions this editor will handle.
	 */
	public static final String[] FILE_EXTENSIONS = { "rtf" };

	/**
	 * The AttributeSets corresponding to copied text.
	 */
	private ArrayList<AttributeSet> clipboardAttributes;
	/**
	 * The clipboard text, split into segments based on attributes.
	 */
	private ArrayList<String> clipboardText;

	/**
	 * Default Document Constructor.
	 */
	public RTFEditor(EditorActionManager eam) {
		super(eam);
	}

	@Override
	public final String[] getFileExtensions() {
		return FILE_EXTENSIONS;
	}

	@Override
	public String getContentType() {
		return CONTENT_TYPE;
	}

	/**
	 * Save the current selection's text and attributes so that formatting is
	 * preserved when pasting.
	 */
	private void saveSelectionAttributes() {
		JTextComponent textComp = (JTextComponent) this.getTextComponent();
		StyledDocument document = (StyledDocument) textComp.getDocument();

		// Reinit data structures
		clipboardAttributes = new ArrayList<AttributeSet>();
		clipboardText = new ArrayList<String>();

		int selectionPos = textComp.getSelectionStart();
		while (selectionPos < textComp.getSelectionEnd()) {
			Element e = document.getCharacterElement(selectionPos);
			// Save attribute
			clipboardAttributes.add(e.getAttributes());

			try {
				// Get actual text segment
				int begin = Math.max(e.getStartOffset(), textComp.getSelectionStart());
				int len = Math.min(e.getEndOffset(), textComp.getSelectionEnd()) - begin;
				clipboardText.add(document.getText(begin, len));
			} catch (BadLocationException e1) {
			}

			selectionPos = e.getEndOffset();
		}
	}

	@Override
	public void cut() {
		saveSelectionAttributes();

		// Cut plaintext
		this.getTextComponent().cut();
	}

	@Override
	public void copy() {
		saveSelectionAttributes();

		// Copy plaintext
		this.getTextComponent().copy();
	}

	@Override
	public void paste() {
		JTextComponent textComp = (JTextComponent) this.getTextComponent();
		StyledDocument document = (StyledDocument) textComp.getDocument();
		
		if (clipboardText == null) {
			this.getTextComponent().paste();
			return;
		}

		try {
			// Paste each saved element with the corresponding attributes
			for (int i = 0; i < clipboardAttributes.size(); i++) {
				int caret = textComp.getCaretPosition();
				document.insertString(caret, clipboardText.get(i), clipboardAttributes.get(i));
				textComp.setCaretPosition(caret + clipboardText.get(i).length());
			}
		} catch (BadLocationException e) {
			// TODO
		}
	}
}
