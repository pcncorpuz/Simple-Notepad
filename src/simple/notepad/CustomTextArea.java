package simple.notepad;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Element;
import javax.swing.undo.*;


public class CustomTextArea extends JTextArea{
    private String filename = "Untitled";
    private String path = null;
    private UndoManager undoManager = new UndoManager();
    
    CustomTextArea(){
        addUndoManager();
    }
    
    CustomTextArea(String fn, String p){
        filename = fn;
        path = p;
        addUndoManager();
    }
    
    public void addUndoManager(){
        this.getDocument().addUndoableEditListener(
                new UndoableEditListener(){
                    public void undoableEditHappened(UndoableEditEvent e){
                    undoManager.addEdit(e.getEdit());
                    }
        });
    }
    
    public void setFileName(String x){
        this.filename = x;
    }
    
    public String getFileName(){
        return filename;
    }
    
    public void setFilePath(String x){
        this.path = x;
    }
    
    public String getFilePath(){
        return path;
    }
    
    public void undo(){
        try{
            undoManager.undo();
        }catch(CannotRedoException e){
            e.printStackTrace();
        }
    }
    
    public void redo(){
        try{
            undoManager.undo();
        }catch(CannotRedoException e){
            e.printStackTrace();
        }
    }
    
    public void toggleWordWrap(boolean x){
        this.setLineWrap(x);
        this.setWrapStyleWord(x);
    }
}
