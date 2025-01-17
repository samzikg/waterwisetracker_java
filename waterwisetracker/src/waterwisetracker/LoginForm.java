/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package waterwisetracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Login form for WaterWise Tracker application
 * @author YourName
 */
public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private SQLiteManager dbManager;
    
    public LoginForm() {
        dbManager = new SQLiteManager();
        dbManager.connect();
        
        initComponents();
    }
    
    private void initComponents() {
        // Set window properties
        setTitle("WaterWise Tracker - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create logo/title panel
        JPanel titlePanel = new JPanel();
        JLabel lblTitle = new JLabel("WaterWise Tracker");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(lblTitle);
        
        // Create login panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username field
        JLabel lblUsername = new JLabel("Username:");
        txtUsername = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(lblUsername, gbc);
        gbc.gridx = 1;
        loginPanel.add(txtUsername, gbc);
        
        // Password field
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(lblPassword, gbc);
        gbc.gridx = 1;
        loginPanel.add(txtPassword, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        
        // Style buttons
        btnLogin.setPreferredSize(new Dimension(100, 30));
        btnRegister.setPreferredSize(new Dimension(100, 30));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        
        // Add action listeners
        btnLogin.addActionListener(e -> handleLogin());
        btnRegister.addActionListener(e -> openRegistrationForm());
        
        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add key listener for Enter key
        KeyListener enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        
        txtUsername.addKeyListener(enterListener);
        txtPassword.addKeyListener(enterListener);
    }
    
    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }
        
        User user = dbManager.verifyLogin(username, password);
        if (user != null) {
            // Login successful
            openMainApplication(user);
        } else {
            showError("Invalid username or password");
        }
    }
    
    private void openRegistrationForm() {
        RegistrationForm regForm = new RegistrationForm(dbManager);
        regForm.setVisible(true);
        this.setVisible(false);
    }
    
    private void openMainApplication(User user) {
        MainForm mainForm = new MainForm(user);
        mainForm.setVisible(true);
        this.dispose();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    
    
    public static void main(String args[]) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and display the form
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
