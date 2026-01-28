import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.swing.*;

/**
 * Attendance Tracker Application
 * A Java Swing application for tracking student attendance
 * with automatic time stamping and e-signature generation
 */
public class AttendanceTracker {
    
    // Declare GUI components
    private JFrame frame;
    private JTextField nameField;
    private JTextField courseField;
    private JTextField timeInField;
    private JTextField eSignatureField;
    private Process notepadProcess; // Track the Notepad process
    
    /**
     * Constructor - initializes and displays the GUI
     */
    public AttendanceTracker() {
        createAndShowGUI();
    }
    
    /**
     * Creates and configures the main GUI window
     */
    private void createAndShowGUI() {
        // Define color scheme - Light Blue and Beige
        Color lightBlue = new Color(173, 216, 230);      // Light blue background
        Color darkBlue = new Color(100, 149, 237);       // Cornflower blue for accents
        Color beige = new Color(245, 245, 220);          // Beige color
        Color darkBeige = new Color(222, 184, 135);      // Burlywood for borders
        Color textColor = new Color(47, 79, 79);         // Dark slate gray for text
        
        // Create the main frame
        frame = new JFrame("Attendance Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null); // Center the window on screen
        frame.getContentPane().setBackground(lightBlue); // Set frame background
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(lightBlue); // Light blue background
        
        // Create title label
        JLabel titleLabel = new JLabel("Student Attendance System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(textColor);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(beige);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBeige, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Create form panel for input fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 15, 15));
        formPanel.setBackground(lightBlue);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Attendance Name field
        JLabel nameLabel = new JLabel("Attendance Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(textColor);
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 13));
        nameField.setBackground(beige);
        nameField.setForeground(textColor);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBeige, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        
        // Course/Year field
        JLabel courseLabel = new JLabel("Course/Year:");
        courseLabel.setFont(new Font("Arial", Font.BOLD, 14));
        courseLabel.setForeground(textColor);
        courseField = new JTextField(20);
        courseField.setFont(new Font("Arial", Font.PLAIN, 13));
        courseField.setBackground(beige);
        courseField.setForeground(textColor);
        courseField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBeige, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(courseLabel);
        formPanel.add(courseField);
        
        // Time In field (auto-generated, non-editable)
        JLabel timeLabel = new JLabel("Time In:");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timeLabel.setForeground(textColor);
        timeInField = new JTextField(20);
        timeInField.setEditable(false); // User cannot edit this field
        timeInField.setFont(new Font("Arial", Font.PLAIN, 13));
        timeInField.setBackground(new Color(211, 211, 211)); // Light gray
        timeInField.setForeground(textColor);
        timeInField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBeige, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(timeLabel);
        formPanel.add(timeInField);
        
        // E-Signature field (auto-generated, non-editable)
        JLabel signatureLabel = new JLabel("E-Signature:");
        signatureLabel.setFont(new Font("Arial", Font.BOLD, 14));
        signatureLabel.setForeground(textColor);
        eSignatureField = new JTextField(20);
        eSignatureField.setEditable(false); // User cannot edit this field
        eSignatureField.setFont(new Font("Arial", Font.PLAIN, 13));
        eSignatureField.setBackground(new Color(211, 211, 211)); // Light gray
        eSignatureField.setForeground(textColor);
        eSignatureField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBeige, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(signatureLabel);
        formPanel.add(eSignatureField);
        
        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(lightBlue);
        
        // Submit button - generates timestamp and signature
        JButton submitButton = new JButton("Submit Attendance");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(darkBlue);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBeige, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> submitAttendance());
        buttonPanel.add(submitButton);
        
        // Clear button - resets all fields
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBackground(beige);
        clearButton.setForeground(textColor);
        clearButton.setFocusPainted(false);
        clearButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBeige, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(clearButton);
        
        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame and display
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    /**
     * Submits attendance by generating timestamp and e-signature
     */
    private void submitAttendance() {
        // Check if required fields are filled
        if (nameField.getText().trim().isEmpty() || courseField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "Please fill in Name and Course/Year fields!", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeIn = now.format(formatter);
        timeInField.setText(timeIn);
        
        // Generate unique e-signature using UUID
        String eSignature = UUID.randomUUID().toString();
        eSignatureField.setText(eSignature);
        
        // Save attendance record to file
        saveToFile(nameField.getText(), courseField.getText(), timeIn, eSignature);
        
        // Show confirmation message
        JOptionPane.showMessageDialog(frame, 
            "Attendance recorded successfully!\nName: " + nameField.getText() + 
            "\nCourse: " + courseField.getText() + 
            "\nTime: " + timeIn +
            "\n\nRecord saved to: AllAttendanceRecords.txt", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Saves attendance record to a text file
     * Appends each record to a single file for all students
     * Automatically opens the file in Notepad after saving
     */
    private void saveToFile(String name, String course, String timeIn, String eSignature) {
        try {
            // Use one file for all attendance records
            File file = new File("AllAttendanceRecords.txt");
            
            // FileWriter with append mode (true) - adds to existing file
            try (FileWriter writer = new FileWriter(file, true)) {
                // Write the current attendance record in the specified format
                writer.write("Name: " + name + " Course/Year: " + course + " Time In: " + timeIn + " E-Signature: " + eSignature + "\n");
            }
            
            // Optional: Show the file location in console
            System.out.println("Record saved to: " + file.getAbsolutePath());
            
            // Automatically open the file in Notepad
            openInNotepad(file);
            
        } catch (IOException e) {
            // Handle any file writing errors
            JOptionPane.showMessageDialog(frame, 
                "Error saving to file: " + e.getMessage(), 
                "File Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opens the specified file in Notepad (Windows)
     * For other operating systems, uses the default text editor
     * Prevents duplicate windows by closing previous instance
     */
    private void openInNotepad(File file) {
        try {
            // Close previous Notepad instance if it exists
            if (notepadProcess != null && notepadProcess.isAlive()) {
                notepadProcess.destroy();
                Thread.sleep(100); // Brief pause to ensure process closes
            }
            
            // Get the operating system name
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                // Windows - open with Notepad
                notepadProcess = Runtime.getRuntime().exec("notepad.exe " + file.getAbsolutePath());
            } else if (os.contains("mac")) {
                // macOS - open with TextEdit
                notepadProcess = Runtime.getRuntime().exec("open -a TextEdit " + file.getAbsolutePath());
            } else {
                // Linux/Unix - use Desktop API to open with default editor
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            }
            
            System.out.println("Opening file in text editor...");
            
        } catch (IOException | InterruptedException e) {
            // If opening fails, show error but don't crash the program
            System.err.println("Could not open file automatically: " + e.getMessage());
        }
    }
    
    /**
     * Clears all input fields
     */
    private void clearFields() {
        nameField.setText("");
        courseField.setText("");
        timeInField.setText("");
        eSignatureField.setText("");
    }
    
    /**
     * Main method - entry point of the application
     */
    public static void main(String[] args) {
        // Use SwingUtilities to ensure GUI is created on Event Dispatch Thread
        SwingUtilities.invokeLater(AttendanceTracker::new);
    }
}