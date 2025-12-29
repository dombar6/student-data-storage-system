/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projektas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author User
 */
public class GUI extends JFrame{

    private JTextField studentField;
    private JTextArea resultArea;
    private JTable gradesTable;
    private JLabel studentCountLabel;
    UnrolledLinkedListx studList = new UnrolledLinkedListx();
    
    public GUI() {
        
        // Sets up the JFrame
        setTitle("Individualus projektas");
        setSize(600, 370);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        studentField = new JTextField(10);
        resultArea = new JTextArea(10, 20);
        resultArea.setEditable(false);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Studentas");
        tableModel.addColumn("Pažymiai");
        gradesTable = new JTable(tableModel);

        gradesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectedRow = gradesTable.getSelectedRow();
                if (selectedRow != -1) {
                }
            }
        });

        JButton addButton = new JButton("Pridėti studentą");
        JButton averageButton = new JButton("Išvesti vidurkį");
        JButton removeButton = new JButton("Pašalinti pažymėtą studentą");
        JButton clearAllButton = new JButton("Išvalyti viską");
        
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        // Add buttons to the inputPanel
        inputPanel.add(new JLabel("Įveskite studento duomenis:"));
        inputPanel.add(studentField);
        inputPanel.add(addButton);
        inputPanel.add(averageButton);
        inputPanel.add(removeButton);
        inputPanel.add(clearAllButton);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(new JScrollPane(gradesTable));
        
        studentCountLabel = new JLabel("Studentų kiekis: 0");
        tablePanel.add(studentCountLabel);

        add(inputPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        
        clearAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
            }
        });

        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
                updateTable();
            }
        });

        averageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printAverageGrade();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedEntries();
                updateTable();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Failas");
        menuBar.add(fileMenu);

        JMenuItem openMenuItem = new JMenuItem("Atidaryti");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        fileMenu.add(openMenuItem);

        
        JMenuItem saveMenuItem = new JMenuItem("Išsaugoti");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        fileMenu.add(saveMenuItem);

        JMenu findMenu = new JMenu("Paieška");
        menuBar.add(findMenu);

        JMenuItem getRangeMenuItem = new JMenuItem("Studentų nurodytais pažymiais paieška");
        getRangeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findStudents();
            }
        });
        findMenu.add(getRangeMenuItem);
    }
    
    
    private void addStudent() {
        String studentInfo = studentField.getText();
        if (studentInfo != null) {
                studList.parse(studentInfo);
                updateResultArea("Studentas pridėtas");
                studentField.setText("");
        } else {
            updateResultArea("Netinkami duomenys");
        }
    }
    
    
    private void removeSelectedEntries() {
        int selectedRow = gradesTable.getSelectedRow();
        if (selectedRow != -1) {
            
            updateResultArea("Pasirinktas studentas panaikintas");
            studentField.setText("");
            studList.remove(selectedRow);
        } else {
            updateResultArea("Nepasirinktas joks studentas.");
        }
        updateTable();
    }
    
    /**
     * Updates the result area with the given message.
     *
     * @param message The message to display in the result area.
     */
    private void updateResultArea(String message) {
        resultArea.append(message + "\n");
    }

    /**
     * Updates the table with the current students and student count
     */
    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) gradesTable.getModel();
        model.setRowCount(0);

        for (int i = 0; i < studList.getSize(); i++){
            model.addRow(new Object[]{studList.getName(i), studList.gradesToString(i)});
        }
        
        studentCountLabel.setText("Studentų kiekis: " + studList.getSize());
    }

    /**
     * Opens a file dialog for the user to select a file and loads students from the selected file.
     */
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\User\\Documents\\NetBeansProjects\\Projektas\\Duomenys"));
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            loadFromFile(file);
        }
    }

    /**
     * Opens a file dialog for the user to choose a location to save students data to the file.
     */
    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\User\\Documents\\NetBeansProjects\\Projektas\\Duomenys"));
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            saveToFile(file);
        }
    }

    /**
     * Loads students from a file and updates the table
     * @param file The file to load data from.
     */
    private void loadFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                studList.parse(line);
            }
            updateTable();
            updateResultArea("Duomenys užkrauti iš failo: " + file.getName());
        } catch (IOException | NumberFormatException ex) {
            updateResultArea("Įvyko klaida kraunant duomenis iš failo: " + file.getName());
        }
        updateTable();
    }

    /**
     * Saves students to a file.
     * @param file The file to save data to.
     */
    private void saveToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            
            for (int i = 0; i < studList.getSize(); i++) {
                writer.write(studList.getName(i) + ";" + studList.gradesToString(i));
                writer.newLine();
            }
            updateResultArea("Duomenys išsaugoti į failą: " + file.getName());
        } catch (IOException ex) {
            updateResultArea("Įvyko klaida saugant duomenis į failą: " + file.getName());
        }
    }
    
    /**
     * Prints students their grades and their average grade
     */
    private void printAverageGrade(){
        
        if(studList.isEmpty())
            updateResultArea("Nėra duomenų");
        else{
            double[] avrGrades = studList.average();
            updateResultArea("=====Studentų vidurkiai=====");
            DecimalFormat f = new DecimalFormat("##.00");
            for (int i = 0; i < studList.getSize(); i++) {
                updateResultArea("Studento |" + studList.getName(i) + " " +
                studList.gradesToString(i) + "| vidurkis: " + f.format(avrGrades[i]));
            }
        }
    }

    /**
     * Clears all students from the UnrolledLinkedList and updates the table
     */
    private void clearAll() {
        studList.makeEmpty();
        updateTable();
        updateResultArea("Visos įvestys išvalytos.");
    }

    /**
     * Opens a new frame to input grades and find students who have the given grades
     */
    private void findStudents() {
        JFrame gradesFrame = new JFrame("Įveskite pažymius");
        gradesFrame.setSize(300, 100);

        JTextField GradeInput = new JTextField(10);
        JButton getStudentsButton = new JButton("Rasti studentus");

        getStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayFoundStudents(GradeInput.getText());
                gradesFrame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Įveskite pažymį/ius (skirkite ';')"));
        panel.add(GradeInput);
        panel.add(getStudentsButton);

        gradesFrame.add(panel);
        gradesFrame.setVisible(true);
    }

    /**
     * Displays students who have the given grades with a table
     * @param gradeString 
     */
    private void displayFoundStudents(String gradeString) {
        JFrame gradeRangeResultFrame = new JFrame("Studentai turintys nurodytus pažymius");
        gradeRangeResultFrame.setSize(400, 300);

        DefaultTableModel rangeTableModel = new DefaultTableModel();
        rangeTableModel.addColumn("Studentai");
        rangeTableModel.addColumn("Pažymiai");

        JTable rangeTable = new JTable(rangeTableModel);
        
        String[] string = gradeString.replaceAll(" ", "")
                              .split(";");
 
        int[] grades = new int[string.length];
        
        for (int i = 0; i < string.length; i++) {
            grades[i] = Integer.parseInt(string[i]);
        }
        
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();

        for (int element : grades) {
            set1.add(element);
        }
        
        for (int i = 0; i < studList.getSize(); i++){
            for (int element : studList.getGrades(i)) {
            set2.add(element);
            }
            if(set2.containsAll(set1)) {
                rangeTableModel.addRow(new Object[]{studList.getName(i), studList.gradesToString(i)});
            }
            set2.clear();
        }
        
        if(rangeTableModel.getRowCount() == 0)
            JOptionPane.showMessageDialog(null, "Studentų su duotais pažymiais nėra.");
        else{
            gradeRangeResultFrame.add(new JScrollPane(rangeTable));
            gradeRangeResultFrame.setVisible(true);
        }
        
    }

    /**
     * The main method to run the Swing application.
     * @param args 
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
    
}
