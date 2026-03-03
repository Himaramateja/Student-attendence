package com.attendance.ui;

import com.attendance.dao.UserDAO;
import com.attendance.model.User;
import com.attendance.service.AdminService;
import javax.swing.*;
import java.awt.*;

public class SwingUI extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private User currentUser;

    private static final String MASTER_PASSWORD = "teja@123";
    private static final Color BG = new Color(30, 30, 46);
    private static final Color CARD_BG = new Color(49, 50, 68);
    private static final Color ACCENT = new Color(137, 180, 250);
    private static final Color TEXT = new Color(205, 214, 244);
    private static final Color SUBTEXT = new Color(166, 173, 200);
    private static final Color ERROR_COLOR = new Color(243, 139, 168);
    private static final Color GREEN = new Color(166, 227, 161);
    private static final Color YELLOW = new Color(249, 226, 175);
    private static final Color MAUVE = new Color(203, 166, 247);

    public SwingUI() {
        setTitle("Student Attendance Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 650));

        mainPanel.add(createRoleSelectionPanel(), "ROLE_SELECT");
        add(mainPanel);
        cardLayout.show(mainPanel, "ROLE_SELECT");
        setVisible(true);
    }

    // ===== ROLE SELECTION SCREEN =====
    private JPanel createRoleSelectionPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 2, true),
                BorderFactory.createEmptyBorder(40, 60, 40, 60)));
        card.setPreferredSize(new Dimension(480, 480));

        JLabel title = new JLabel("SAMS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(5));

        JLabel subtitle = new JLabel("Student Attendance Management System", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(SUBTEXT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createVerticalStrut(10));

        JLabel chooseLabel = new JLabel("Choose Your Role", SwingConstants.CENTER);
        chooseLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chooseLabel.setForeground(TEXT);
        chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(chooseLabel);
        card.add(Box.createVerticalStrut(25));

        // Admin Button
        JButton adminBtn = createRoleButton("🔒  Admin Login", ACCENT);
        adminBtn.addActionListener(e -> showLoginScreen("ADMIN"));
        card.add(adminBtn);
        card.add(Box.createVerticalStrut(15));

        // Faculty Button
        JButton facultyBtn = createRoleButton("👨‍🏫  Faculty Login", GREEN);
        facultyBtn.addActionListener(e -> showLoginScreen("FACULTY"));
        card.add(facultyBtn);
        card.add(Box.createVerticalStrut(15));

        // Student Button
        JButton studentBtn = createRoleButton("🎓  Student Login", MAUVE);
        studentBtn.addActionListener(e -> showLoginScreen("STUDENT"));
        card.add(studentBtn);
        card.add(Box.createVerticalStrut(25));

        // Footer
        JLabel footer = new JLabel("© 2026 SAMS", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(SUBTEXT);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(footer);

        outer.add(card);
        return outer;
    }

    private JButton createRoleButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(BG);
        btn.setMaximumSize(new Dimension(320, 48));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ===== LOGIN SCREEN FOR EACH ROLE =====
    private void showLoginScreen(String role) {
        JPanel loginPanel = createLoginPanel(role);
        mainPanel.add(loginPanel, "LOGIN_" + role);
        cardLayout.show(mainPanel, "LOGIN_" + role);
    }

    private JPanel createLoginPanel(String role) {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG);

        // Pick color based on role
        Color roleColor;
        String roleTitle;
        switch (role) {
            case "ADMIN":
                roleColor = ACCENT;
                roleTitle = "Admin Login";
                break;
            case "FACULTY":
                roleColor = GREEN;
                roleTitle = "Faculty Login";
                break;
            case "STUDENT":
                roleColor = MAUVE;
                roleTitle = "Student Login";
                break;
            default:
                roleColor = ACCENT;
                roleTitle = "Login";
                break;
        }

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(roleColor, 2, true),
                BorderFactory.createEmptyBorder(35, 50, 35, 50)));
        card.setPreferredSize(new Dimension(420, role.equals("ADMIN") ? 500 : 420));

        // Back button
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backBtn.setForeground(SUBTEXT);
        backBtn.setBackground(CARD_BG);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "ROLE_SELECT"));
        card.add(backBtn);
        card.add(Box.createVerticalStrut(10));

        JLabel title = new JLabel(roleTitle, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(roleColor);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(25));

        // Username
        card.add(createLabel("Username"));
        card.add(Box.createVerticalStrut(5));
        JTextField userField = new JTextField();
        userField.setMaximumSize(new Dimension(300, 36));
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(userField);
        card.add(Box.createVerticalStrut(12));

        // Password
        card.add(createLabel("Password"));
        card.add(Box.createVerticalStrut(5));
        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(300, 36));
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(passField);
        card.add(Box.createVerticalStrut(20));

        // Sign In Button
        JButton loginBtn = new JButton("Sign In");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setBackground(roleColor);
        loginBtn.setForeground(BG);
        loginBtn.setMaximumSize(new Dimension(300, 42));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));

        // Status label
        JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(ERROR_COLOR);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(statusLabel);

        // Admin Register button
        if (role.equals("ADMIN")) {
            card.add(Box.createVerticalStrut(15));
            JButton registerBtn = new JButton("Register New Admin");
            registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            registerBtn.setBackground(YELLOW);
            registerBtn.setForeground(BG);
            registerBtn.setMaximumSize(new Dimension(300, 38));
            registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            registerBtn.setFocusPainted(false);
            registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            registerBtn.addActionListener(e -> showAdminRegisterDialog());
            card.add(registerBtn);
        }

        // Login action
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both username and password.");
                return;
            }
            UserDAO userDAO = new UserDAO();
            User user = userDAO.login(username, password);
            if (user == null) {
                statusLabel.setText("Invalid credentials. Try again.");
                passField.setText("");
                return;
            }
            // Verify role matches
            if (!user.getRole().equals(role)) {
                statusLabel.setText("This account is not a " + role + " account.");
                passField.setText("");
                return;
            }
            currentUser = user;
            navigateToDashboard();
        });

        passField.addActionListener(e -> loginBtn.doClick());
        userField.addActionListener(e -> passField.requestFocus());

        outer.add(card);
        return outer;
    }

    // ===== ADMIN REGISTER DIALOG =====
    private void showAdminRegisterDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setPreferredSize(new Dimension(380, 160));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();
        JPasswordField masterField = new JPasswordField();

        panel.add(new JLabel("Admin Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmField);
        panel.add(new JLabel("Master Password:"));
        panel.add(masterField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Register New Admin",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirm = new String(confirmField.getPassword()).trim();
            String master = new String(masterField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password are required.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!master.equals(MASTER_PASSWORD)) {
                JOptionPane.showMessageDialog(this, "Invalid Master Password! Registration denied.", "Access Denied",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                AdminService adminService = new AdminService();
                adminService.addAdmin(username, password);
                JOptionPane.showMessageDialog(this,
                        "Admin '" + username + "' registered successfully!\nYou can now login.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ===== NAVIGATION =====
    private void navigateToDashboard() {
        String role = currentUser.getRole();
        JPanel dashboard;
        switch (role) {
            case "ADMIN":
                dashboard = new AdminPanel(this);
                break;
            case "FACULTY":
                dashboard = new FacultyPanel(this, currentUser);
                break;
            case "STUDENT":
                dashboard = new StudentPanel(this, currentUser);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown role: " + role);
                return;
        }
        mainPanel.add(dashboard, "DASHBOARD");
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    public void logout() {
        currentUser = null;
        mainPanel.removeAll();
        mainPanel.add(createRoleSelectionPanel(), "ROLE_SELECT");
        cardLayout.show(mainPanel, "ROLE_SELECT");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }
}
