import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notepad extends JFrame implements ActionListener {
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu editMenu;
    JMenu displayMenu;
    JMenu selectMode;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;
    JCheckBoxMenuItem lineWrap;
    JTextArea textArea;
    JMenuItem dateItem;

    JMenuItem darkMode;
    JMenuItem lightMode;
    JMenuItem undoItem;
    JMenuItem zoomIn;
    JMenuItem zoomOut;
    JLabel statusLabel;
    UndoManager undoManager;
    JMenuItem newWindow;
    JCheckBoxMenuItem barStatus;
    JMenuItem closeWindow;
    Notepad() {
        //setting up the frame
        this.setLayout(new BorderLayout());
        this.setSize(1000, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);


        //creating menus and menu bar
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        displayMenu = new JMenu("Display");
        selectMode = new JMenu("Select Mode");
        undoManager = new UndoManager();

        // Menu items
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");
        dateItem = new JMenuItem("Datetime");
        darkMode = new JMenuItem("Dark mode");
        lightMode = new JMenuItem("Light mode");
        lineWrap = new JCheckBoxMenuItem("Line Wrap", true);
        undoItem = new JMenuItem("Undo");
        statusLabel = new JLabel("Lines: 1, Columns: 1");//Status bar
        zoomIn = new JMenuItem("Zoom In");
        zoomOut = new JMenuItem("Zoom out");
        newWindow = new JMenuItem("New Window");
        barStatus = new JCheckBoxMenuItem("Status Bar");
        closeWindow = new JMenuItem("Close Window");


        //adding ActionListener to items
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);
        dateItem.addActionListener(this);
        darkMode.addActionListener(this);
        lightMode.addActionListener(this);
        lineWrap.addActionListener(this);
        undoItem.addActionListener(this);
        zoomIn.addActionListener(this);
        zoomOut.addActionListener(this);
        newWindow.addActionListener(this);
        barStatus.addActionListener(this);
        closeWindow.addActionListener(this);



        //Keyboard shortcuts
        openItem.setMnemonic('o'); //o for load
        saveItem.setMnemonic('s');//S for save
        exitItem.setMnemonic('e');//E for exit
        dateItem.setMnemonic('d');//D for datetime
        darkMode.setMnemonic('n');//n dark mode
        lightMode.setMnemonic('f');//f for light mode
        lineWrap.setMnemonic('w');//w to enable line wrapping
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));//ctrl+z for undo
        zoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.SHIFT_DOWN_MASK));/*
            shift + "+" to zoom in*/
        zoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.SHIFT_DOWN_MASK));/* shift + "-"
            to zoom out*/
        newWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));/* ctrl +t for
            new window*/
        barStatus.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));/*ctrl+o for
        bar status*/
        closeWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        //adding items to menus
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        fileMenu.add(newWindow);
        fileMenu.add(closeWindow);

        editMenu.add(dateItem);
        editMenu.add(undoItem);

        displayMenu.add(lineWrap);
        displayMenu.add(zoomOut);
        displayMenu.add(zoomIn);
        displayMenu.add(barStatus);

        selectMode.add(darkMode);
        selectMode.add(lightMode);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(displayMenu);
        menuBar.setBackground(Color.lightGray);
        menuBar.add(selectMode);

        this.setJMenuBar(menuBar);
        this.add(statusLabel, BorderLayout.SOUTH);
        textArea = new JTextArea();
        textArea.setBackground(Color.white);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setTitle("Notepad");
        statusLabel.setVisible(false);
        this.setVisible(true);

        //undo action
        Action undoAction = new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        };
        textArea.getActionMap().put("Undo", undoAction);
        textArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        //added Document listener for tracking changes in text area(status bar)
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    updateStatusBar();
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    updateStatusBar();
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    updateStatusBar();
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    //opening files
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openItem) {
            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(null); // select file to open
            if (response == JFileChooser.APPROVE_OPTION) {
                // Loading a file
                File file = fileChooser.getSelectedFile();
                System.out.println(file);
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    textArea.setText(content.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        //saving files
        if (e.getSource() == saveItem) {

            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    //Saves the entered text to a file
                    String content = textArea.getText();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write(content);
                    writer.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
        //exit
        if (e.getSource() == exitItem) {
            System.exit(0);
        }
        //display date
        if (e.getSource() == dateItem) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = now.format(formatter);
            textArea.setText(textArea.getText() + "  " + formattedTime);
        }
        //line wrapping
        if (e.getSource() == lineWrap) {
            boolean wrapEnabled = lineWrap.isSelected();
            textArea.setLineWrap(wrapEnabled);
            textArea.setWrapStyleWord(wrapEnabled);
        }
        //dark/light mode
        if (e.getSource() == darkMode) {
            textArea.setCaretColor(Color.white);
            textArea.setBackground(Color.DARK_GRAY);
            textArea.setForeground(Color.white);
            this.setBackground(Color.DARK_GRAY);
        }
        if (e.getSource() == lightMode) {
            textArea.setCaretColor(Color.black);
            textArea.setBackground(Color.WHITE);
            textArea.setForeground(Color.black);
            this.setBackground(Color.white);
        }
        if (e.getSource() == undoItem) {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        }
        //zoom in/out
        if (e.getSource() == zoomIn) {
            changeFontSize(2.0);
        }
        if (e.getSource() == zoomOut) {
            changeFontSize(0.5);
        }
        //new window
        if (e.getSource() == newWindow) {
          new Notepad();
        }
        if(e.getSource()==barStatus){
            statusLabel.setVisible(barStatus.isSelected());
        }
        if(e.getSource()==closeWindow){
            dispose();
        }
    }
    //method that updates the bar whenever you write something
    private void updateStatusBar() throws BadLocationException {
        int lines = textArea.getLineCount();
        int columns = textArea.getCaretPosition() - textArea.getLineStartOffset(textArea.getLineCount() - 1);
        statusLabel.setText("Lines" + lines + " ,Columns:" + columns);
    }
    private void changeFontSize(double scaleFactor) {
        Font currentFont = textArea.getFont();//current font
        float newSize = currentFont.getSize() * (float) scaleFactor;//calculate new font size
        Font newFont = currentFont.deriveFont(newSize);//creating new font
        textArea.setFont(newFont);//setting a new font
    }
}