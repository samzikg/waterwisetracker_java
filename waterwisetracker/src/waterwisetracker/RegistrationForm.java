/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package waterwisetracker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author SAM ZI KANG
 */
public class RegistrationForm extends JFrame {
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnBack;
    private SQLiteManager dbManager;
    
    public RegistrationForm(SQLiteManager dbManager) {
        this.dbManager = dbManager;
        initComponents();
    }
    
    private void initComponents() {
        // Set window properties 
        setTitle("WaterWise Tracker - Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(400, 350);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(20);
        mainPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        mainPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        mainPanel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        txtConfirmPassword = new JPasswordField(20);
        mainPanel.add(txtConfirmPassword, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRegister = new JButton("Register");
        btnBack = new JButton("Back to Login");
        
        btnRegister.addActionListener(e -> handleRegistration());
        btnBack.addActionListener(e -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
            dispose();
        });

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }
    
    private void handleRegistration() {
        String username = txtUsername.getText();
    String email = txtEmail.getText();
    String password = new String(txtPassword.getPassword());
    String confirmPassword = new String(txtConfirmPassword.getPassword());

    // Basic validation
    if(username.isEmpty() || email.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "Please fill in all fields", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    if(!password.equals(confirmPassword)) {
        JOptionPane.showMessageDialog(this,
            "Passwords do not match",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Actually register the user in database
    if(dbManager.registerUser(username, email, password)) {
        JOptionPane.showMessageDialog(this,
            "Registration successful! Please login.",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
            
        // Return to login form
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
        dispose();
    } else {
        JOptionPane.showMessageDialog(this,
            "Registration failed. Username may already exist.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
}