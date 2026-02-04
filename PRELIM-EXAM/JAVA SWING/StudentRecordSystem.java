/*
 * Student Record System - Java Swing Implementation
 * Programmer: Karina Cass - STUDENT_ID_HERE
 * Date: February 4, 2026
 */

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class StudentRecordSystem extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtStudentID, txtFirstName, txtLastName;
    private JTextField txtLab1, txtLab2, txtLab3, txtPrelim, txtAttendance;

    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    private final ArrayList<String[]> studentData = new ArrayList<>();

    private final String[] COLUMN_NAMES = {
        "Student ID", "First Name", "Last Name",
        "Lab Work 1", "Lab Work 2", "Lab Work 3",
        "Prelim Exam", "Attendance"
    };

    // Theme colors
    private final Color PINK = new Color(255, 182, 193);
    private final Color LIGHT_PINK = new Color(255, 209, 220);
    private final Color PEACH = new Color(255, 218, 185);
    private final Color LAVENDER = new Color(230, 230, 250);

    public StudentRecordSystem() {
        setTitle("🌸 Student Records - Karina Cass STUDENT_ID_HERE 🌸");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeUI();
        loadDataFromCSV();

        setVisible(true);
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(PEACH);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("🌸 Student Record Management System 🌸", JLabel.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        title.setForeground(new Color(139, 0, 139));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(PINK);
        titlePanel.add(title);

        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionBackground(LIGHT_PINK);

        JTableHeader header = table.getTableHeader();
        header.setBackground(PINK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        table.getSelectionModel().addListSelectionListener(e -> populateFields());

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel inputPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        inputPanel.setBackground(LAVENDER);

        txtStudentID = createField();
        txtFirstName = createField();
        txtLastName = createField();
        txtLab1 = createField();
        txtLab2 = createField();
        txtLab3 = createField();
        txtPrelim = createField();
        txtAttendance = createField();

        inputPanel.add(new JLabel("Student ID")); inputPanel.add(txtStudentID);
        inputPanel.add(new JLabel("First Name")); inputPanel.add(txtFirstName);
        inputPanel.add(new JLabel("Last Name")); inputPanel.add(txtLastName);
        inputPanel.add(new JLabel("Lab 1")); inputPanel.add(txtLab1);
        inputPanel.add(new JLabel("Lab 2")); inputPanel.add(txtLab2);
        inputPanel.add(new JLabel("Lab 3")); inputPanel.add(txtLab3);
        inputPanel.add(new JLabel("Prelim")); inputPanel.add(txtPrelim);
        inputPanel.add(new JLabel("Attendance")); inputPanel.add(txtAttendance);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearFields());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

        add(mainPanel);
    }

    private JTextField createField() {
        return new JTextField();
    }

    private void loadDataFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("MOCK_DATA.csv"))) {
            String line;
            boolean skip = true;

            while ((line = br.readLine()) != null) {
                if (skip) { skip = false; continue; }
                String[] row = line.split(",");
                if (row.length >= 8) {
                    tableModel.addRow(row);
                    studentData.add(row);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "CSV not found!");
        }
    }

    private void addStudent() {
        String[] row = getFormData();
        if (row == null) return;

        tableModel.addRow(row);
        studentData.add(row);
        clearFields();
    }

    private void updateStudent() {
        int r = table.getSelectedRow();
        if (r == -1) return;

        String[] row = getFormData();
        if (row == null) return;

        for (int i = 0; i < row.length; i++) {
            tableModel.setValueAt(row[i], r, i);
        }
        studentData.set(r, row);
        clearFields();
    }

    private void deleteStudent() {
        int r = table.getSelectedRow();
        if (r == -1) return;

        tableModel.removeRow(r);
        studentData.remove(r);
        clearFields();
    }

    private void populateFields() {
        int r = table.getSelectedRow();
        if (r == -1) return;

        txtStudentID.setText(tableModel.getValueAt(r, 0).toString());
        txtFirstName.setText(tableModel.getValueAt(r, 1).toString());
        txtLastName.setText(tableModel.getValueAt(r, 2).toString());
        txtLab1.setText(tableModel.getValueAt(r, 3).toString());
        txtLab2.setText(tableModel.getValueAt(r, 4).toString());
        txtLab3.setText(tableModel.getValueAt(r, 5).toString());
        txtPrelim.setText(tableModel.getValueAt(r, 6).toString());
        txtAttendance.setText(tableModel.getValueAt(r, 7).toString());
    }

    private String[] getFormData() {
        if (txtStudentID.getText().isEmpty()) return null;

        return new String[]{
            txtStudentID.getText(),
            txtFirstName.getText(),
            txtLastName.getText(),
            txtLab1.getText(),
            txtLab2.getText(),
            txtLab3.getText(),
            txtPrelim.getText(),
            txtAttendance.getText()
        };
    }

    private void clearFields() {
        txtStudentID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtLab1.setText("");
        txtLab2.setText("");
        txtLab3.setText("");
        txtPrelim.setText("");
        txtAttendance.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ignored) {}

        SwingUtilities.invokeLater(StudentRecordSystem::new);
    }

    public ArrayList<String[]> getStudentData() {
        return studentData;
    }
}
