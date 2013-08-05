package com.jmonkey.office.jwp;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Window;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jmonkey.export.RegistryFormatException;
import com.jmonkey.office.jwp.support.editors.Editor;

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
		
		boolean success = (new File("dmtests")).mkdirs();
	    assertTrue(success);
		
	}
     
    @AfterClass
    public static void deleteFolder() {
        File dir = new File("dmtests");
        File[] children = dir.listFiles();
        for(File c : children) {
            assertTrue(c.delete());
        }
        assertTrue(dir.delete());
    }

	@Before
	public void setup() {
		manager = new DocumentManager(jwp); 
		manager.createDocumentFrame();
		
		//manager.closeActiveDocument();
	}
	
    private boolean createFile(String fpath, String[] contents) {
        try {
            FileWriter fstream;
            fstream = new FileWriter(fpath);
            BufferedWriter out = new BufferedWriter(fstream);
            for(int i=0; i<contents.length; i++) {
                out.write(contents[i]);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
	
    private void activateFileChooser(File f) {
        //get jfilechooser from component tree
        JDialog dialog = null;
        List<Window> visibleWindows = new ArrayList<Window>();
        for(Window w: Window.getWindows()){
            if(w.isShowing()){
                visibleWindows.add(w);
            }
        }
 
        for(Window w: visibleWindows) {
            try {
                dialog = (JDialog)w;
                break;
            } catch(Exception e) {
                dialog = null;
            }
        }
        assertTrue(dialog != null);
        JFileChooser chooser = (JFileChooser)dialog.getContentPane().getComponent(0);
         
        chooser.setSelectedFile(f);
        chooser.approveSelection();
        System.gc();
    }
	
	private class openChooser implements Runnable {
		DocumentManager dm;
		
		public openChooser(DocumentManager dm) {
			this.dm = dm;
		}
		
		public void run() {
			dm.editorOpen();		
		}
		
	}
	
	private class saveChooser implements Runnable {
		DocumentManager dm;
		Editor ed;
		public saveChooser(DocumentManager dm, Editor ed) {
			this.dm = dm;
			this.ed = ed;
		}
		
		public void run() {
			dm.editorSave(this.ed);		
		}
		
	}
	
	private class saveAsChooser implements Runnable {
		DocumentManager dm;
		Editor ed;
		public saveAsChooser(DocumentManager dm, Editor ed) {
			this.dm = dm;
			this.ed = ed;
		}
		
		public void run() {
			dm.editorSaveAs(this.ed);		
		}
		
	}
	
	private class openAsChooser implements Runnable {
		DocumentManager dm;
		
		public openAsChooser(DocumentManager dm) {
			this.dm = dm;
		}
		
		public void run() {
			dm.editorOpenAs();		
		}
		
	}
	
	private class copyChooser implements Runnable {
		DocumentManager dm;
		Editor ed;
		public copyChooser(DocumentManager dm, Editor ed) {
			this.dm = dm;
			this.ed = ed;
		}
		
		public void run() {
			dm.editorSaveCopy(this.ed);		
		}
		
	}

	@Test
	public void testEditorNew() {
		manager.editorNew();
	}

	@Test
	public void testEditorOpenFile() {
		File file = new File(filepath);

		manager.editorOpen(file);
		//manager.closeAllDocuments();
		System.gc();
	}

	
	@Test
	public void testEditorOpen() {
		String[] contents = {"banana", "apple"};
        this.createFile("dmtests/open.txt", contents);
        File testFile = new File("dmtests/open.txt");
        assertTrue(testFile.exists());
        try {
            openChooser oc = new openChooser(this.manager); 
            Thread p = new Thread(oc);
            p.start();
            Thread.sleep(500);
            this.activateFileChooser(testFile);
            p.join(500);
        } catch (InterruptedException e) {
            fail("");
        }
        
        DocumentFrame df = manager.getOpenDocument("open.txt [text/plain]");
        assertEquals("open.txt [text/plain]", df.getTitle());
        
		df = manager.active();
		Editor ed = df.getEditor();
		
		manager.editorRevert(ed);
         
        //testFile.delete();
        //this.manager.closeAllDocuments();
        System.gc();
	}

	@Test
	public void testEditorOpenAs() {
		String[] contents = {"banana", "apple"};
        this.createFile("dmtests/openAs.txt", contents);
        File testFile = new File("dmtests/openAs.txt");
        assertTrue(testFile.exists());
        try {
            openAsChooser oc = new openAsChooser(this.manager); 
            Thread p = new Thread(oc);
            p.start();
            Thread.sleep(500);
            this.activateFileChooser(testFile);
            p.join(500);
        } catch (InterruptedException e) {
            fail("");
        }
        
        DocumentFrame df = this.manager.getOpenDocument("openAs.txt [text/plain]");
        assertEquals("openAs.txt [text/plain]", df.getTitle());
         
        testFile.delete();
        //this.manager.closeAllDocuments();
        System.gc();
	}
	/*
	@Test
	public void testEditorRevert() {
		manager.editorRevert(ed);
	}
	*/

	@Test
	public void testEditorSave() {
		DocumentFrame df = manager.active();
		
		String[] contents = {"banana", "apple"};
        this.createFile("dmtests/save.txt", contents);
        File testFile = new File("dmtests/save.txt");
        assertTrue(testFile.exists());
        try {
            saveChooser oc = new saveChooser(this.manager, df.getEditor()); 
            Thread p = new Thread(oc);
            p.start();
            Thread.sleep(500);
            this.activateFileChooser(testFile);
            p.join(500);
        } catch (InterruptedException e) {
            fail("");
        }
        
        testFile.delete();
        //this.manager.closeAllDocuments();
        System.gc();
	}
	
	@Test
	public void testEditorSaveAs() {
		DocumentFrame df = manager.getOpenDocument("New Document1 [text/rtf]");
		
		String[] contents = {"banana", "apple"};
        this.createFile("dmtests/saveAs.txt", contents);
        File testFile = new File("dmtests/saveAs.txt");
        assertTrue(testFile.exists());
        
        File newFile = new File("test/files/tmp.rtf");
        
        try {
            this.manager.activateFrame(df);
            Thread.sleep(100);
            newFile.delete();
            df.getEditor().getTextComponent().getDocument().insertString(0, "foo", null);
        } catch (Exception e) {
            fail("");
        }
        System.gc();

        
        try {
            saveAsChooser oc = new saveAsChooser(manager, df.getEditor()); 
            Thread p = new Thread(oc);
            p.start();
            Thread.sleep(500);
            this.activateFileChooser(testFile);
            p.join(500);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("");
        }
        
        this.createFile("dmtests/copy.txt", contents);
        File copyFile = new File("dmtests/copy.txt");
        assertTrue(testFile.exists());
        try {
            copyChooser oc = new copyChooser(manager, df.getEditor()); 
            Thread q = new Thread(oc);
            q.start();
            Thread.sleep(500);
            this.activateFileChooser(copyFile);
            q.join(500);
        } catch (InterruptedException e) {
            fail("");
        }
         
        testFile.delete();
        copyFile.delete();
        //this.manager.closeAllDocuments();
        System.gc();
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
	/*
	@Test
	public void testGetOpenDocument() {
		DocumentFrame df = manager.getOpenDocument("New Document1 [text/rtf]");
	}
	*/

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
		for(int i = 0; i<5; i++) {
			manager.editorNew();
		}
		
		manager.cascadeAll();
	}
	
	/*
	@Test
	public void testCloseActiveDocument() {
		String[] contents = {"banana", "apple"};
        this.createFile("dmtests/closeAct.txt", contents);
        File testFile = new File("dmtests/closeAct.txt");
		assertTrue(testFile.exists());
        
        Editor ed = manager.active().getEditor();
		ed.setFile(testFile);
		manager.closeActiveDocument();
	}
	/*
	@Test
	public void testCloseAllDocuments() {
		manager.closeAllDocuments();
	}
	*/
	@Test
	public void testMinimizeAll() {
		for(int i = 0; i<5; i++) {
			manager.editorNew();
		}
		
		manager.minimizeAll();
	}

	@Test
	public void testTileAll() {
		for(int i = 0; i<5; i++) {
			manager.editorNew();
		}
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