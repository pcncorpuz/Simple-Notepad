package simple.notepad;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class SimpleNotepad extends JFrame{
    private JMenuBar menuBar = new JMenuBar();
    public JTabbedPane tp = new JTabbedPane();
    public JScrollPane scroll;
    
    private JMenu edit, search;
    private JMenuItem save, saveAs, close;
    
    private int xSize = 300;
    private int ySize = 400;
    private boolean wordwrap = false;
    private boolean linenumber = false;
    
    
    SimpleNotepad(){
        super("Simple Notepad");
        setSize(xSize,ySize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(tp);
        
        JMenu file = new JMenu("File");
        JMenuItem fnew = new JMenuItem("New");
        fnew.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        NewF();
                        initPreference();
                        enableModule();
                    }
                }
        );
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        OpenF();
                        initPreference();
                        enableModule();
                    }
                }
        );
        save = new JMenuItem("Save");
        save.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        SaveF();
                    }
                }
        );
        saveAs = new JMenuItem("Save as");

        saveAs.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        SaveAsF();
                    }
                }
        );
        close = new JMenuItem("Close File");
        close.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        tp.remove(tp.getSelectedIndex());
                        if(tp.getTabCount() == 0){
                            disableModule();
                        }
                    }
                }
        );
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        System.exit(0);
                    }
                });
        menuBar.add(file);
        file.add(fnew);
        file.addSeparator();
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(close);
        file.add(exit);
        
        edit = new JMenu("Edit");
        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(
                new ActionListener(){
                   public void actionPerformed(ActionEvent e){
                       CustomTextArea x = getActivePane();
                       x.copy();
                   } 
                });
        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(
                new ActionListener(){
                   public void actionPerformed(ActionEvent e){
                       CustomTextArea x = getActivePane();
                       x.paste();
                   } 
                });
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(
                new ActionListener(){
                   public void actionPerformed(ActionEvent e){
                       CustomTextArea x = getActivePane();
                       x.replaceSelection(null);
                   } 
                });
        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(
                new ActionListener(){
                   public void actionPerformed(ActionEvent e){
                       CustomTextArea x = getActivePane();
                       x.selectAll();
                   } 
                });
        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomTextArea x = getActivePane();
                x.undo();
            }
        });
        JMenuItem redo = new JMenuItem("Redo");
        redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomTextArea x = getActivePane();
                x.redo();
            }
        });
        JMenu preference = new JMenu("Preference");
        JCheckBoxMenuItem cbmi1 = new JCheckBoxMenuItem("Do not split words over two lines");
        cbmi1.setSelected(!wordwrap);
        cbmi1.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    wordwrap = cbmi1.getState()?false:true;
                    setWordWrap();
                }
            });
        JCheckBoxMenuItem cbmi2 = new JCheckBoxMenuItem("Display line numbers");
        cbmi2.setSelected(linenumber);
        cbmi2.addActionListener(
          new ActionListener(){
              public void actionPerformed(ActionEvent e){
                  linenumber = cbmi2.getState()?true:false;
                  setLineNumber();
              }
          }      
        );
        menuBar.add(edit);
        preference.add(cbmi1);
        preference.add(cbmi2);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        edit.add(selectAll);
        edit.addSeparator();
        edit.add(undo);
        edit.add(redo);
        edit.addSeparator();
        edit.add(preference);
        
        search = new JMenu("Search");
        JMenuItem find = new JMenuItem("Find");
        find.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        newFind();
                    }
                }
        );
        JMenuItem findAndReplace = new JMenuItem("Find and Replace");
        findAndReplace.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        newFindAndReplace();
                    }
                }
        );
        menuBar.add(search);
        search.add(find);
        search.add(findAndReplace);
        
        setJMenuBar(menuBar);
        disableModule();
        setVisible(true);
    }
    
    public CustomTextArea getActivePane(){
        return (CustomTextArea)(((JScrollPane) tp.getSelectedComponent()).getViewport()).getView();
    }
    
    public CustomTextArea getPaneAt(int x ){
        return (CustomTextArea)(((JScrollPane) tp.getComponentAt(x)).getViewport()).getView();
    }
    
    public TextLineNumber getLineNumberAt(int x){
        return (TextLineNumber) (((JScrollPane) tp.getComponentAt(x)).getRowHeader().getView());
    }
    
    JTextField textField;
    JTextField textFieldR;
    
    public void newFind(){
        JDialog frame = new JDialog(this,"Find");
        JPanel panel = new JPanel();
        JLabel find = new JLabel();
        textField = new JTextField();
        JButton btnFind = new JButton();
        btnFind.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    findText();
                }
            });
        find.setText("Find:");
        btnFind.setText("Find");
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(find)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFind)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(find)
                    .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFind))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void newFindAndReplace(){
        JDialog frame = new JDialog(this,"Find and Replace");
        JPanel panel = new JPanel();
        JLabel find = new JLabel();
        textField = new JTextField();
        JButton btnReplace = new JButton();
        textFieldR = new JTextField();
        JLabel replace = new JLabel();
        find.setText("Find:");
        btnReplace.setText("Replace");
        replace.setText("Replace:");
        btnReplace.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        findAndReplaceText();
                    }
                }
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(find)
                    .addComponent(replace))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(textFieldR, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(textField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnReplace)
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(find)
                    .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReplace)
                    .addComponent(replace))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void findText(){
        String text = textField.getText();
        CustomTextArea panel = getActivePane();
        int pos =panel.getCaretPosition();
        int length = panel.getText().length();
        if(panel.getText().indexOf(text,pos+1)>-1){
            panel.setCaretPosition(panel.getText().indexOf(text,pos+1));
            panel.moveCaretPosition(panel.getCaretPosition() + text.length());
        }else{
            if(panel.getText().indexOf(text)>-1){
                panel.setCaretPosition(panel.getText().indexOf(text));
                panel.moveCaretPosition(panel.getCaretPosition() + text.length());
            }
        }
    }
    
    public void findAndReplaceText(){
        CustomTextArea panel = getActivePane();
        panel.setText(panel.getText().replaceAll(textField.getText(),textFieldR.getText()));
    }
    
    public void NewF(){
        CustomTextArea pane = new CustomTextArea();
        scroll = new JScrollPane(pane);
        scroll.setRowHeaderView(new TextLineNumber(pane));
        tp.add(pane.getFileName(),scroll);
        tp.setSelectedIndex(tp.getTabCount()-1);
    }
    
    public void OpenF(){
        JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(SimpleNotepad.this);
        if (result == JFileChooser.APPROVE_OPTION){
            File file = fc.getSelectedFile();
            CustomTextArea pane = new CustomTextArea(file.getName(),file.getPath());
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                pane.setText(line);
                while (line != null) {
                    pane.setText(pane.getText() + "\n" + line);
                    line = br.readLine();
                }   
            }catch(FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            scroll = new JScrollPane(pane);
            scroll.setRowHeaderView(new TextLineNumber(pane));
            tp.add(pane.getFileName(),scroll);
            tp.setSelectedIndex(tp.getTabCount()-1);
        }
    }
    
    public void SaveF(){
        File file;
        CustomTextArea pane = getActivePane();
        if(pane.getFilePath() == null){
            JFileChooser c = new JFileChooser();
            int result = c.showSaveDialog(SimpleNotepad.this);
            if(result == JFileChooser.APPROVE_OPTION){
                file = new File(c.getSelectedFile()+".txt");
                BufferedWriter out;
                try{
                    out = new BufferedWriter(new FileWriter(file));
                    pane.write(out);
                    out.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
                pane.setFilePath(file.getPath());
                pane.setFileName(file.getName());
                tp.setTitleAt(tp.getSelectedIndex(), pane.getFileName());
            }
        }
        else{
            file = new File(pane.getFilePath());
            BufferedWriter out ;
            try{
                out = new BufferedWriter(new FileWriter(file));
                pane.write(out);
                out.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    public void SaveAsF(){
        JFileChooser c = new JFileChooser();
        CustomTextArea pane = getActivePane();
        int result = c.showSaveDialog(SimpleNotepad.this);
        if(result == JFileChooser.APPROVE_OPTION){
            File file = c.getSelectedFile();
            if(file.exists()){
                int n = JOptionPane.showConfirmDialog(null,"Do you want to overwrite the file?", "File Exists", JOptionPane.YES_NO_OPTION);
                if(n == JOptionPane.NO_OPTION){
                    return;
                }
            }else{
                file = new File(c.getSelectedFile() + ".txt");
            }
            BufferedWriter out;
            try{
                out = new BufferedWriter(new FileWriter(file));
                pane.write(out);
                out.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
            pane.setFilePath(file.getPath());
            pane.setFileName(file.getName());
            tp.setTitleAt(tp.getSelectedIndex(), pane.getFileName());
        }
    }
    
    public void setWordWrap(){
        CustomTextArea cp;
        for(int c = 0; c < tp.getTabCount(); c++){
            cp = getPaneAt(c);
            cp.setLineWrap(wordwrap);
            cp.setWrapStyleWord(wordwrap);
        }
    }
    
    public void setLineNumber(){
        TextLineNumber cp;
        for(int c = 0; c < tp.getTabCount(); c++){
            cp = getLineNumberAt(c);
            ((JScrollPane) tp.getComponentAt(c)).getRowHeader().setVisible(linenumber);
            cp.setVisible(linenumber);
        }
    }
    
    public void initPreference(){
        setWordWrap();
        setLineNumber();
    }
    public void disableModule(){
        edit.setEnabled(false);
        search.setEnabled(false);
        save.setEnabled(false);
        saveAs.setEnabled(false);
        close.setEnabled(false);
    }
    
    public void enableModule(){
        edit.setEnabled(true);
        search.setEnabled(true);
        save.setEnabled(true);
        saveAs.setEnabled(true);
        close.setEnabled(true);
    }

    public static void main(String[] args) {
        new SimpleNotepad();
    }
}
