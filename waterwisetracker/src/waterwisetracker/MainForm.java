/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package waterwisetracker;

/**
 *
 * @author SAM ZI KANG
 */
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;


public class MainForm extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private SQLiteManager dbManager;
    
    // Dashboard components
    private JLabel lblTotalUsage;
    private JLabel lblDailyUsage;
    private JTable activityTable;
    
    public MainForm(User user) {
        this.currentUser = user;
        this.dbManager = new SQLiteManager();
        dbManager.connect();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("WaterWise Tracker - Welcome " + currentUser.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Add tabs
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Log Activity", createActivityPanel());
        tabbedPane.addTab("Water Saving Tips", createTipsPanel());
        tabbedPane.addTab("Profile", createProfilePanel());
        
        // Add to frame
        add(tabbedPane);
        
        // Create menu bar
        createMenuBar();
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Water Usage Statistics"));
        
        lblTotalUsage = new JLabel("Total Usage: 0 L");
        lblDailyUsage = new JLabel("Today's Usage: 0 L");
        statsPanel.add(lblTotalUsage);
        statsPanel.add(lblDailyUsage);
        statsPanel.add(new JLabel("Water Saved: 0 L"));
        statsPanel.add(new JLabel("Environmental Impact: Positive"));
        
        // Recent Activities Table
        String[] columns = {"Date", "Type", "Amount (L)", "Description"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        activityTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(activityTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Recent Activities"));
        
        // Add components
        panel.add(statsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createActivityPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Form Panel
    JPanel formPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    
    // Activity Type
    String[] activityTypes = {"Household", "Irrigation"};
    JComboBox<String> cmbActivityType = new JComboBox<>(activityTypes);
    
    // Water Amount
    JTextField txtAmount = new JTextField(10);
    
    // Description
    JTextArea txtDescription = new JTextArea(3, 20);
    txtDescription.setLineWrap(true);
    JScrollPane scrollDescription = new JScrollPane(txtDescription);
    
    // Add components to form
    gbc.gridx = 0; gbc.gridy = 0;
    formPanel.add(new JLabel("Activity Type:"), gbc);
    gbc.gridx = 1;
    formPanel.add(cmbActivityType, gbc);
    
    gbc.gridx = 0; gbc.gridy = 1;
    formPanel.add(new JLabel("Water Amount (L):"), gbc);
    gbc.gridx = 1;
    formPanel.add(txtAmount, gbc);
    
    gbc.gridx = 0; gbc.gridy = 2;
    formPanel.add(new JLabel("Description:"), gbc);
    gbc.gridx = 1;
    gbc.gridheight = 2;
    formPanel.add(scrollDescription, gbc);
    
    // Button Panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton btnLog = new JButton("Log Activity");
    btnLog.addActionListener(e -> {
        try {
        double waterAmount = Double.parseDouble(txtAmount.getText());
        String description = txtDescription.getText();
        String activityType = (String) cmbActivityType.getSelectedItem();
        String activityId = "A" + System.currentTimeMillis();
        
        WaterActivity activity;
        if (activityType.equals("Household")) {
            activity = new HouseholdActivity(activityId, waterAmount, description, "General", 1);
        } else {
            activity = new IrrigationActivity(activityId, waterAmount, description, 1.0, "General", false);
        }
        
        // Add retry logic for database operations
        int retryCount = 0;
        boolean saved = false;
        while (retryCount < 3 && !saved) {
            saved = dbManager.saveWaterActivity(activity, currentUser.getUserId());
            if (!saved) {
                Thread.sleep(1000); // Wait 1 second before retry
                retryCount++;
            }
        }
        
        if (saved) {
            currentUser.getTracker().addActivity(activity);
            updateDashboardStats();
            txtAmount.setText("");
            txtDescription.setText("");
            JOptionPane.showMessageDialog(this, "Activity logged successfully!");
        } else {
            JOptionPane.showMessageDialog(this,
                "Error saving activity. Please try again.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this,
            "Please enter a valid number for water amount",
            "Error",
            JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
            "An error occurred: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
});
    buttonPanel.add(btnLog);
    
    // Add panels to main panel
    panel.add(formPanel, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    
    return panel;
}

    
    private JPanel createTipsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tips List
        DefaultListModel<String> tipsModel = new DefaultListModel<>();
        JList<String> tipsList = new JList<>(tipsModel);
        JScrollPane scrollPane = new JScrollPane(tipsList);
        
        // Add sample tips
        tipsModel.addElement("Fix leaking faucets - Save up to 20L per day");
        tipsModel.addElement("Use water-efficient appliances - Save up to 50L per use");
        tipsModel.addElement("Collect rainwater for irrigation - Save up to 100L per week");
        tipsModel.addElement("Take shorter showers - Save up to 150L per shower");
        tipsModel.addElement("Install dual-flush toilets - Save up to 50L per day");
        
        // Tip detail panel
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBorder(BorderFactory.createTitledBorder("Tip Details"));
        JTextArea txtDetail = new JTextArea(5, 20);
        txtDetail.setEditable(false);
        txtDetail.setLineWrap(true);
        detailPanel.add(new JScrollPane(txtDetail));
        
        // Add selection listener
        tipsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedTip = tipsList.getSelectedValue();
                if (selectedTip != null) {
                    // Show detailed information about the selected tip
                    txtDetail.setText("Detailed information about: " + selectedTip);
                }
            }
        });
        
        // Add components
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(detailPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Profile information
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(currentUser.getUsername()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(currentUser.getEmail()), gbc);
        
        // Update password section
        JPanel passwordPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        
        passwordPanel.add(new JLabel("Current Password:"));
        JPasswordField txtCurrentPassword = new JPasswordField();
        passwordPanel.add(txtCurrentPassword);
        
        passwordPanel.add(new JLabel("New Password:"));
        JPasswordField txtNewPassword = new JPasswordField();
        passwordPanel.add(txtNewPassword);
        
        passwordPanel.add(new JLabel("Confirm Password:"));
        JPasswordField txtConfirmPassword = new JPasswordField();
        passwordPanel.add(txtConfirmPassword);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(passwordPanel, gbc);
        
        // Update button
        JButton btnUpdate = new JButton("Update Password");
        btnUpdate.addActionListener(e -> handlePasswordUpdate(
            txtCurrentPassword.getPassword(),
            txtNewPassword.getPassword(),
            txtConfirmPassword.getPassword()
        ));
        
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnUpdate, gbc);
        
        return panel;
    }
    
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "WaterWise Tracker v1.0\n" +
            "A water conservation tracking application\n" +
            "Supporting SDG 6: Clean Water and Sanitation",
            "About WaterWise Tracker",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Method to update dashboard statistics
    public void updateDashboardStats() {
    // Update total usage
    double totalUsage = currentUser.getTracker().calculateTotalUsage();
    lblTotalUsage.setText(String.format("Total Usage: %.2f L", totalUsage));
    
    // Update daily usage
    double todayUsage = currentUser.getTracker().getActivities().stream()
        .filter(a -> a.getTimestamp().toLocalDate().equals(java.time.LocalDate.now()))
        .mapToDouble(WaterActivity::calculateWaterUsage)
        .sum();
    lblDailyUsage.setText(String.format("Today's Usage: %.2f L", todayUsage));
    
    // Update activity table
    DefaultTableModel model = (DefaultTableModel) activityTable.getModel();
    model.setRowCount(0); // Clear existing rows
    
    // Add activities to table
    for (WaterActivity activity : currentUser.getTracker().getActivities()) {
        model.addRow(new Object[]{
            activity.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            activity instanceof HouseholdActivity ? "Household" : "Irrigation",
            String.format("%.2f", activity.calculateWaterUsage()),
            activity.getDescription()
        });
    }
}
    private void handlePasswordUpdate(char[] currentPass, char[] newPass, char[] confirmPass) {
        // Convert char arrays to strings
        String currentPassword = new String(currentPass);
        String newPassword = new String(newPass);
        String confirmPassword = new String(confirmPass);
        
        // Validate inputs
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all password fields");
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            showError("New passwords do not match");
            return;
        }
        
        if (newPassword.length() < 6) {
            showError("New password must be at least 6 characters long");
            return;
        }
        
        // Try to update the password
        if (dbManager.updatePassword(currentUser.getUserId(), currentPassword, newPassword)) {
            JOptionPane.showMessageDialog(this,
                "Password updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
                
            // Clear password fields
            clearPasswordFields();
        } else {
            showError("Failed to update password. Please verify your current password and try again.");
        }
    }
    
    private void clearPasswordFields() {
        // Find and clear all password fields in the profile panel
        Component[] components = tabbedPane.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                clearPasswordFieldsInContainer((Container) component);
            }
        }
    }
    
    private void clearPasswordFieldsInContainer(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JPasswordField) {
                ((JPasswordField) component).setText("");
            } else if (component instanceof Container) {
                clearPasswordFieldsInContainer((Container) component);
            }
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
}