package com.jmonkey.office.jwp.support;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.junit.Before;
import org.junit.Test;

import com.jmonkey.export.RegistryFormatException;
import com.jmonkey.office.jwp.DocumentManager;
import com.jmonkey.office.jwp.JWP;
import com.jmonkey.office.jwp.support.EditorActionManager.*;
import com.jmonkey.office.jwp.support.editors.HTMLEditor;

public class EditorActionManagerTest {
	
	
	/**
     * Invoke a private method with no arguments.
     *
     * @param object The object to invoke the method on
     * @param methodName The name of the method to invoke
     * @return The return value of the method
     */
    private static Object invokePrivateMethod(Object object, String 
methodName) {
        return invokePrivateMethod(object, methodName, new Object[0]);
    }

    /**
     * Invoke a private method.
     *
     * @param object The object to invoke the method on
     * @param methodName The name of the method to invoke
     * @param args An array of arguments to pass to the method
     * @return The return value of the method
     */
    private static Object invokePrivateMethod(Object object, String 
methodName, Object[] args) {
        try {
            Method method = 
object.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method '" + 
methodName + "'", e);
        }
    }
    
   
	EditorActionManager ea;
	private Editor editor;
	private Action action1;
	JFrame app;
	FileActionListener agent;
	String[] in = {""};
	
	@Before
	public void setup() throws RegistryFormatException
	{
		app = new JFrame();
		//dm = new DocumentManager(new JWP(in));
		ea = new EditorActionManager(app,null);
		editor  = new HTMLEditor(ea);
		ea.activate(editor);
		
	}
	
	
	@Test
	public void testFontFacectionconstructor(){
		action1 = ea.getFontFaceAction("Arial");
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
		Font f = new Font("Arial",10,1);
		action1 = ea.getFontFaceAction(f);
		box.addItem(action1);
		assertNotNull(action1);
	}
	
	@Test
	public void testFontSizeActionConstructor(){
		action1 = ea.getFontSizeAction(1);
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
		
	}
	
	@Test
	public void testBoldActionConstructor(){
		action1 = ea.getBoldAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}
	
	@Test
	public void testItalicActionConstructor(){
		action1 = ea.getItalicAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}
	
	@Test
	public void testUnderlineActionConstructor(){
		action1 = ea.getUnderlineAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}
	
	@Test
	public void testStrikeThroughActionConstructor(){
		action1 = ea.getStrikeThroughAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}
	
	@Test
	public void testCutActionConstructor(){
		action1 = ea.getCutAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}
	
	@Test
	public void testCopyActionConstructor(){
		action1 = ea.getCopyAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}
	@Test
	public void testPasteActionConstructor(){
		action1 = ea.getPasteAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}
	
	@Test
	public void testUndoActionConstructor(){
		action1=ea.getUndoAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}
	
	@Test
	public void testRedoActionConstructor(){
		action1 = ea.getRedoAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}

	@Test
	public void testSaveCopyActionConstructor(){
		action1 = ea.getCopyAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}
	
	@Test
	public void testAlignmentActions(){
		action1 = ea.getAlignLeftAction();
		ActionComboBox box = new ActionComboBox();
		box.addItem(action1);
		assertNotNull(action1);
	}
	
	@Test
	public void testActivate(){
		ea.activate(editor);
		assertEquals(EditorActionManager.getActiveEditor(), editor);
	}

	
	@Test
	public void testCreateDefaultColourActions(){
		Action [] a;
		a = (Action[])invokePrivateMethod(ea, "createDefaultColourActions");
		assertEquals(14,a.length);

	}
	@Test
	public void testCreateDefaultFontFaceActions(){
		Action[]a;
		a = (Action[])invokePrivateMethod(ea, "createDefaultFontFaceActions");
		assertNotNull(a);
		
	}

	@Test
	public void testDeactivate(){
		ea.activate(editor);
		Editor current = EditorActionManager.getActiveEditor();
		ea.deactivate(current);
		assertNull(EditorActionManager.getActiveEditor());
		ea.activate(editor);
		
	}
	
	@Test
	public void testgetColourAtCaret(){	
		Color c;
		c = (Color)invokePrivateMethod(ea, "getColourAtCaret");
		assertNotNull(c);
		
	}
	
	@Test
	public void testIsActiveEditor(){
		boolean active;
		active = EditorActionManager.isActiveEditor();
		assertTrue(active);
	}
	
}
