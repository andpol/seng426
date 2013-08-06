package com.jmonkey.office.jwp.support;


import static org.junit.Assert.*;

import javax.swing.Action;


import org.junit.Before;
import org.junit.Test;

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
