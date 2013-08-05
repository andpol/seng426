package com.jmonkey.office.jwp.support;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

import org.junit.Before;
import org.junit.Test;

public class PropertySheetDialogTest {
	
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
	
	
	Properties p;
	JFrame j;
	final Constructor<?> psd = 
			PropertySheetDialog.class.getDeclaredConstructors()[0];
	
	final Constructor<?> ptm =
			PropertySheetDialog.class.getDeclaredClasses()[0].getDeclaredConstructors()[0];
	
	@Before
	public void setup(){
		j = new JFrame();
		p = new Properties();
		p.put("Hello", 0);
		p.put("Hello2", 1);
		psd.setAccessible(true);
		ptm.setAccessible(true);
	}
	
	@Test
	public void testGetRowCount() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		PropertySheetDialog u = (PropertySheetDialog) psd.newInstance(j,p,true);
		AbstractTableModel p = (AbstractTableModel) ptm.newInstance(u);
		//invokePrivateMethod(u, "doExit");
		assertEquals(2,p.getRowCount());
	}
	@Test
	public void testGetColumnCount() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		PropertySheetDialog u = (PropertySheetDialog) psd.newInstance(j,p,true);
		AbstractTableModel p = (AbstractTableModel) ptm.newInstance(u);
		assertEquals(2,p.getColumnCount());
	}
	
	@Test
	public void testGetColumnName() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		PropertySheetDialog u = (PropertySheetDialog) psd.newInstance(j,p,true);
		AbstractTableModel p = (AbstractTableModel) ptm.newInstance(u);
		assertNull(p.getColumnName(2));
		
	}
	
	@Test
	public void testGetColumnClass() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		PropertySheetDialog u = (PropertySheetDialog) psd.newInstance(j,p,true);
		AbstractTableModel p = (AbstractTableModel) ptm.newInstance(u);
		assertEquals(java.lang.String.class, p.getColumnClass(1));
		
		
	}
	
	@Test
	public void testIsCellEditable() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		PropertySheetDialog u = (PropertySheetDialog) psd.newInstance(j,p,true);
		AbstractTableModel p = (AbstractTableModel) ptm.newInstance(u);
		assertFalse(p.isCellEditable(0, 3));
		assertFalse(p.isCellEditable(0, 0));
		assertTrue(p.isCellEditable(0, 1));
		
	}
	
	@Test
	public void testGetSetValueAt() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		PropertySheetDialog u = (PropertySheetDialog) psd.newInstance(j,p,true);
		AbstractTableModel p = (AbstractTableModel) ptm.newInstance(u);
		String input = "Hola";
		p.setValueAt(input, 0, 1);
		String output = (String)p.getValueAt(0, 1);
		assertEquals(input,output);
		
	}
	
	@Test
	public void testGetProperties() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		PropertySheetDialog u = (PropertySheetDialog) psd.newInstance(j,p,true);
		
	}
	
	@Test
	public void testDisplay()
	{
		assertEquals(p,PropertySheetDialog.display(j, p));
		assertEquals(p,PropertySheetDialog.display(j, p,true));
	}
	
	@Test
	public void testdoExit() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		PropertySheetDialog u = (PropertySheetDialog) psd.newInstance(j,p,true);
		invokePrivateMethod(u, "doExit");
		assertNotNull(u);
		
	}
	

}
