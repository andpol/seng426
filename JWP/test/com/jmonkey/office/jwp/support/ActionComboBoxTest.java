package com.jmonkey.office.jwp.support;


import static org.junit.Assert.*;

import java.awt.event.ItemEvent;

import javax.swing.Action;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

import org.junit.Before;
import org.junit.Test;

import com.jmonkey.export.RegistryFormatException;
import com.jmonkey.office.jwp.JWP;
import com.jmonkey.office.jwp.support.editors.HTMLEditor;


public class ActionComboBoxTest {
	
	
	
	private EditorActionManager ea;
	private Editor editor;
	private Action action1;
	private Action action3;
	private Action[] actionlist;
	
	@Before
	public void setup ()
	{
		ea = new EditorActionManager(null,null);
		editor = new HTMLEditor(ea);
		ea.activate(editor);
		action1 = ea.getBeepAction();
		action3 = ea.getBoldAction();
		actionlist = new Action[2];
		actionlist[0] = action1;
		actionlist[1] = action3;
	}
	
	@Test
	public void testgetItemAt()
	{
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		Action first =  (Action)box.getItemAt(0);
		assertEquals(action1,first);
	}
	
	@Test
	public void testRemoveItem()
	{
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		box.removeItem(action1);
		Action tmpaction = (Action)box.getItemAt(0);
		assertNull(tmpaction);
	}
	@Test
	public void testRemoveItemAt()
	{
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		box.removeItemAt(0);
		Action tmpaction = (Action)box.getItemAt(0);
		assertNull(tmpaction);
	}
	
	@Test
	public void testInsertItemAt()
	{
		ActionComboBox box = new ActionComboBox();
		box.insertItemAt(action1,0);
		Action tmpaction = (Action)box.getItemAt(0);
		assertEquals(action1, tmpaction);
	}
	
	@Test
	public void testremoveAllItems()
	{
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		box.addItem(action3);
		box.removeAllItems();
		box.addItem(action3);
		assertEquals(box.getItemAt(0), action3);
	}
	
	@Test
	public void testConstructor()
	{
		ActionComboBox box2 = new ActionComboBox(actionlist);
		Action newaction = (Action)box2.getItemAt(1);
		assertEquals(newaction,action3);
	}
}
