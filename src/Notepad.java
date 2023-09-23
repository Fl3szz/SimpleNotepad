import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notepad extends JFrame implements ActionListener {
        JMenuBar menuBar;
        JMenu fileMenu;
        JMenu editMenu;
        JMenu helpMenu;
        JMenu selectMode;
        JMenuItem loadItem;
        JMenuItem saveItem;
        JMenuItem exitItem;
        JTextArea textArea;
        JMenuItem aboutItem;
        ImageIcon icon;
        JMenuItem dateItem;

        JMenuItem darkMode;
        JMenuItem lightMode;

        Notepad() {
            this.setLayout(new BorderLayout());
            this.setSize(1000,750);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);


            menuBar = new JMenuBar();
            icon = new ImageIcon("C:\\Users\\Admin\\Desktop\\note.jpg");
            Image image = icon.getImage();
            fileMenu = new JMenu("File");
            editMenu = new JMenu("Edit");
            helpMenu = new JMenu("Help");
            selectMode = new JMenu("Select Mode");

            // Menu items
            loadItem = new JMenuItem("Load");
            saveItem = new JMenuItem("Save");
            exitItem = new JMenuItem("Exit");
            aboutItem = new JMenuItem("About");
            dateItem = new JMenuItem("Display Time");
            darkMode = new JMenuItem("Dark mode");
            lightMode = new JMenuItem("Light mode");


            loadItem.addActionListener(this);
            saveItem.addActionListener(this);
            exitItem.addActionListener(this);
            aboutItem.addActionListener(this);
            dateItem.addActionListener(this);
            darkMode.addActionListener(this);
            lightMode.addActionListener(this);

            loadItem.setMnemonic(KeyEvent.VK_L); //L for load
            saveItem.setMnemonic(KeyEvent.VK_S);//S for save
            exitItem.setMnemonic(KeyEvent.VK_E);//E for exit
            aboutItem.setMnemonic(KeyEvent.VK_A);//A for About
            dateItem.setMnemonic(KeyEvent.VK_D);//D for datetime
            darkMode.setMnemonic(KeyEvent.VK_N);//n dark mode
            lightMode.setMnemonic(KeyEvent.VK_F);//f for light mode



            fileMenu.add(loadItem);
            fileMenu.add(saveItem);
            fileMenu.add(exitItem);

            helpMenu.add(aboutItem);

            editMenu.add(dateItem);

            selectMode.add(darkMode);
            selectMode.add(lightMode);

            menuBar.add(fileMenu);
            menuBar.add(editMenu);
            menuBar.add(helpMenu);
            menuBar.setBackground(Color.lightGray);
            menuBar.add(selectMode);

            this.setIconImage(image);
            this.setJMenuBar(menuBar);


            textArea = new JTextArea();
            textArea.setBackground(Color.white);
            JScrollPane scrollPane = new JScrollPane(textArea);
            this.add(scrollPane, BorderLayout.CENTER);
            this.setTitle("Notepad");
            this.setVisible(true);

        }



    @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loadItem) {
                JFileChooser fileChooser = new JFileChooser();
                int response = fileChooser.showOpenDialog(null); // select file to open
                JOptionPane.showMessageDialog(null, "You loaded a file!");
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
            if (e.getSource() == saveItem) {

                JFileChooser fileChooser = new JFileChooser();
                int response = fileChooser.showSaveDialog(null);
                JOptionPane.showMessageDialog(null, "You saved a file!");



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
            if (e.getSource() == exitItem) {
                System.exit(0);
            }
            if (e.getSource() == dateItem) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String formattedTime = now.format(formatter);
                textArea.setText(textArea.getText()+"  "+formattedTime);
            }
            if (e.getSource() == aboutItem) {
                JOptionPane.showMessageDialog(null, "Any errors you find will be fixed as soon" +
                                " as possible, thank you for using this notepad");
            }
            if(e.getSource() == darkMode){
                textArea.setCaretColor(Color.white);
                textArea.setBackground(Color.DARK_GRAY);
                textArea.setForeground(Color.white);
                this.setBackground(Color.DARK_GRAY);

            }
            if(e.getSource() == lightMode){
                textArea.setCaretColor(Color.black);
                textArea.setBackground(Color.WHITE);
                textArea.setForeground(Color.black);
                this.setBackground(Color.white);
            }


        }
}





