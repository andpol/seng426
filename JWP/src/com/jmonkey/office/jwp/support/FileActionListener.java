package com.jmonkey.office.jwp.support;

import com.jmonkey.office.jwp.support.editors.Editor;

public interface FileActionListener {
	public void editorNew();

	public void editorOpen();

	public void editorOpenAs();

	public void editorRevert(Editor editor);

	public void editorSave(Editor editor);

	public void editorSaveAs(Editor editor);

	public void editorSaveCopy(Editor editor);
}
