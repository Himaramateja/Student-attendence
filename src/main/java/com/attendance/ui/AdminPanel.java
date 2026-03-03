package com.attendance.ui;

import com.attendance.model.*;
import com.attendance.service.AdminService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class AdminPanel extends JPanel {
    private final SwingUI parent;
    private final AdminService adminService = new AdminService();
    private final JPanel contentPanel = new JPanel(new CardLayout());
    private final Color ACCENT = new Color(137, 180, 250);
    private final Color BG = new Color(30, 30, 46);
    private final Color CARD_BG = new Color(49, 50, 68);
    private final Color TEXT = new Color(205, 214, 244);
    private final Color GREEN = new Color(166, 227, 161);
    private final Color RED = new Color(243, 139, 168);
    private final Color YELLOW = new Color(249, 226, 175);

    public AdminPanel(SwingUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(BG);

        // --- Top Bar ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(CARD_BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(ACCENT);
        topBar.add(titleLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setBackground(RED);
        logoutBtn.setForeground(BG);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> parent.logout());
        topBar.add(logoutBtn, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // --- Sidebar ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(36, 36, 54));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));

        String[] menuItems = { "Dashboard", "Manage Students", "Manage Faculty", "Manage Subjects", "Reports" };
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setMaximumSize(new Dimension(180, 40));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setBackground(new Color(36, 36, 54));
            btn.setForeground(TEXT);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> showContent(item));
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(6));
        }
        add(sidebar, BorderLayout.WEST);

        // --- Content Area ---
        contentPanel.setBackground(BG);
        contentPanel.add(createOverviewPanel(), "Dashboard");
        contentPanel.add(createStudentPanel(), "Manage Students");
        contentPanel.add(createFacultyPanel(), "Manage Faculty");
        contentPanel.add(createSubjectPanel(), "Manage Subjects");
        contentPanel.add(createReportPanel(), "Reports");
        add(contentPanel, BorderLayout.CENTER);
        showContent("Dashboard");
    }

    private void showContent(String name) {
        // Refresh panels
        if (name.equals("Dashboard")) {
            contentPanel.add(createOverviewPanel(), "Dashboard");
        } else if (name.equals("Manage Students")) {
            contentPanel.add(createStudentPanel(), "Manage Students");
        } else if (name.equals("Manage Faculty")) {
            contentPanel.add(createFacultyPanel(), "Manage Faculty");
        } else if (name.equals("Manage Subjects")) {
            contentPanel.add(createSubjectPanel(), "Manage Subjects");
        } else if (name.equals("Reports")) {
            contentPanel.add(createReportPanel(), "Reports");
        }
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, name);
    }

    // ===== DASHBOARD OVERVIEW =====
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;

        try {
            int studentCount = adminService.getStudentCount();
            int facultyCount = adminService.getFacultyCount();
            int subjectCount = adminService.getAllSubjects().size();

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(createStatCard("Total Students", String.valueOf(studentCount), ACCENT), gbc);
            gbc.gridx = 1;
            panel.add(createStatCard("Total Faculty", String.valueOf(facultyCount), GREEN), gbc);
            gbc.gridx = 2;
            panel.add(createStatCard("Total Subjects", String.valueOf(subjectCount), YELLOW), gbc);
        } catch (Exception ex) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            JLabel err = new JLabel("Error loading data: " + ex.getMessage());
            err.setForeground(RED);
            panel.add(err, gbc);
        }
        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)));
        card.setPreferredSize(new Dimension(220, 130));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 42));
        val.setForeground(color);
        val.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(val);
        card.add(Box.createVerticalStrut(8));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(TEXT);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lbl);
        return card;
    }

    // ===== STUDENT MANAGEMENT =====
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form
        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBackground(CARD_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT), "Add / Update Student",
                        0, 0, new Font("Segoe UI", Font.BOLD, 14), ACCENT),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JTextField rollField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField sectionField = new JTextField();
        JTextField semesterField = new JTextField();

        form.add(label("Roll No:"));
        form.add(rollField);
        form.add(label("Name:"));
        form.add(nameField);
        form.add(label("Department:"));
        form.add(deptField);
        form.add(label("Section:"));
        form.add(sectionField);
        form.add(label("Semester:"));
        form.add(semesterField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setBackground(CARD_BG);
        JButton addBtn = styledButton("Add", GREEN);
        JButton deleteBtn = styledButton("Delete", RED);
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        form.add(label(""));
        form.add(btnPanel);

        panel.add(form, BorderLayout.NORTH);

        // Table
        String[] cols = { "Roll", "Name", "Department", "Section", "Semester" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);
        styleTable(table);
        loadStudents(model);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try {
                adminService.addStudent(rollField.getText().trim(), nameField.getText().trim(),
                        deptField.getText().trim(), Integer.parseInt(sectionField.getText().trim()),
                        Integer.parseInt(semesterField.getText().trim()));
                loadStudents(model);
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                rollField.setText("");
                nameField.setText("");
                deptField.setText("");
                sectionField.setText("");
                semesterField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            String roll = rollField.getText().trim();
            if (roll.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Roll No to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete student " + roll + "?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    adminService.deleteStudent(roll);
                    loadStudents(model);
                    rollField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                rollField.setText((String) model.getValueAt(row, 0));
                nameField.setText((String) model.getValueAt(row, 1));
                deptField.setText((String) model.getValueAt(row, 2));
                sectionField.setText(String.valueOf(model.getValueAt(row, 3)));
                semesterField.setText(String.valueOf(model.getValueAt(row, 4)));
            }
        });

        return panel;
    }

    private void loadStudents(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (Student s : adminService.getAllStudents()) {
                model.addRow(new Object[] { s.getStudentRoll(), s.getName(), s.getDepartment(), s.getSection(),
                        s.getSemester() });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ===== FACULTY MANAGEMENT =====
    private JPanel createFacultyPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBackground(CARD_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT), "Add Faculty",
                        0, 0, new Font("Segoe UI", Font.BOLD, 14), ACCENT),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();
        form.add(label("Faculty ID:"));
        form.add(idField);
        form.add(label("Name:"));
        form.add(nameField);
        form.add(label("Department:"));
        form.add(deptField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setBackground(CARD_BG);
        JButton addBtn = styledButton("Add", GREEN);
        JButton deleteBtn = styledButton("Delete", RED);
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        form.add(label(""));
        form.add(btnPanel);

        panel.add(form, BorderLayout.NORTH);

        String[] cols = { "Faculty ID", "Name", "Department" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);
        styleTable(table);
        loadFaculty(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try {
                adminService.addFaculty(idField.getText().trim(), nameField.getText().trim(),
                        deptField.getText().trim());
                loadFaculty(model);
                JOptionPane.showMessageDialog(this, "Faculty added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                idField.setText("");
                nameField.setText("");
                deptField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            String fid = idField.getText().trim();
            if (fid.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Faculty ID to delete.");
                return;
            }
            try {
                adminService.deleteFaculty(fid);
                loadFaculty(model);
                idField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idField.setText((String) model.getValueAt(row, 0));
                nameField.setText((String) model.getValueAt(row, 1));
                deptField.setText((String) model.getValueAt(row, 2));
            }
        });

        return panel;
    }

    private void loadFaculty(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (Faculty f : adminService.getAllFaculty()) {
                model.addRow(new Object[] { f.getFacultyId(), f.getName(), f.getDepartment() });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ===== SUBJECT MANAGEMENT =====
    private JPanel createSubjectPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(CARD_BG);
        tabbedPane.setForeground(TEXT);

        // --- TAB 1: DEFINE SUBJECTS ---
        JPanel defineTab = new JPanel(new BorderLayout(10, 10));
        defineTab.setBackground(BG);
        defineTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel subForm = new JPanel(new GridLayout(3, 2, 8, 8));
        subForm.setBackground(CARD_BG);
        subForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT), "New Subject",
                        0, 0, new Font("Segoe UI", Font.BOLD, 14), ACCENT),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        subForm.add(label("Subject Code:"));
        subForm.add(codeField);
        subForm.add(label("Subject Name:"));
        subForm.add(nameField);

        JPanel subBtnPnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        subBtnPnl.setBackground(CARD_BG);
        JButton subAddBtn = styledButton("Add Subject", GREEN);
        JButton subDelBtn = styledButton("Delete Subject", RED);
        subBtnPnl.add(subAddBtn);
        subBtnPnl.add(subDelBtn);
        subForm.add(label(""));
        subForm.add(subBtnPnl);

        String[] subCols = { "Code", "Name" };
        DefaultTableModel subModel = new DefaultTableModel(subCols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable subTable = new JTable(subModel);
        styleTable(subTable);

        defineTab.add(subForm, BorderLayout.NORTH);
        defineTab.add(new JScrollPane(subTable), BorderLayout.CENTER);

        // --- TAB 2: ASSIGN FACULTY ---
        JPanel assignTab = new JPanel(new BorderLayout(10, 10));
        assignTab.setBackground(BG);
        assignTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel assignForm = new JPanel(new GridLayout(3, 2, 8, 8));
        assignForm.setBackground(CARD_BG);
        assignForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(YELLOW), "Link Faculty to Subject",
                        0, 0, new Font("Segoe UI", Font.BOLD, 14), YELLOW),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JComboBox<String> subjectCombo = new JComboBox<>();
        JComboBox<String> facultyCombo = new JComboBox<>();
        assignForm.add(label("Select Subject:"));
        assignForm.add(subjectCombo);
        assignForm.add(label("Select Faculty:"));
        assignForm.add(facultyCombo);

        JButton assignBtn = styledButton("Assign Faculty", ACCENT);
        assignForm.add(label(""));
        assignForm.add(assignBtn);

        String[] assignCols = { "Faculty Name", "Faculty ID", "Subject Name", "Subject Code" };
        DefaultTableModel assignModel = new DefaultTableModel(assignCols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable assignTable = new JTable(assignModel);
        styleTable(assignTable);

        JButton removeAssignBtn = styledButton("Remove Selected Assignment", RED);

        assignTab.add(assignForm, BorderLayout.NORTH);
        assignTab.add(new JScrollPane(assignTable), BorderLayout.CENTER);
        assignTab.add(removeAssignBtn, BorderLayout.SOUTH);

        tabbedPane.addTab("Define Subjects", defineTab);
        tabbedPane.addTab("Faculty Assignments", assignTab);
        panel.add(tabbedPane, BorderLayout.CENTER);

        // --- Shared Data Loading ---
        Runnable refreshAll = () -> {
            loadSubjects(subModel);
            loadAssignments(assignModel);
            loadSubjectDropdown(subjectCombo);
            loadFacultyDropdown(facultyCombo);
        };
        refreshAll.run();

        // --- Listeners ---
        subAddBtn.addActionListener(e -> {
            try {
                adminService.addSubject(codeField.getText().trim(), nameField.getText().trim());
                refreshAll.run();
                codeField.setText("");
                nameField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        subDelBtn.addActionListener(e -> {
            try {
                adminService.deleteSubject(codeField.getText().trim());
                refreshAll.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        assignBtn.addActionListener(e -> {
            try {
                String subItem = (String) subjectCombo.getSelectedItem();
                String facItem = (String) facultyCombo.getSelectedItem();
                if (subItem == null || facItem == null)
                    return;

                String subCode = subItem.split(" - ")[0];
                String facId = facItem.split(" - ")[0];

                adminService.assignSubject(facId, subCode);
                refreshAll.run();
                JOptionPane.showMessageDialog(this, "Subject assigned successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        removeAssignBtn.addActionListener(e -> {
            int row = assignTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select an assignment to remove.");
                return;
            }
            try {
                List<FacultyAssignmentDTO> list = adminService.getAllFacultyAssignments();
                FacultyAssignmentDTO dto = list.get(row);
                adminService.removeSubjectAssignment(dto.getFacultyDbId(), dto.getSubjectId());
                refreshAll.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        subTable.getSelectionModel().addListSelectionListener(e -> {
            int row = subTable.getSelectedRow();
            if (row >= 0) {
                codeField.setText((String) subModel.getValueAt(row, 0));
                nameField.setText((String) subModel.getValueAt(row, 1));
            }
        });

        return panel;
    }

    private void loadSubjectDropdown(JComboBox<String> combo) {
        combo.removeAllItems();
        try {
            for (Subject s : adminService.getAllSubjects()) {
                combo.addItem(s.getSubjectCode() + " - " + s.getSubjectName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadFacultyDropdown(JComboBox<String> combo) {
        combo.removeAllItems();
        try {
            for (Faculty f : adminService.getAllFaculty()) {
                combo.addItem(f.getFacultyId() + " - " + f.getName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadSubjects(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (Subject s : adminService.getAllSubjects()) {
                model.addRow(new Object[] { s.getSubjectCode(), s.getSubjectName() });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadAssignments(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (FacultyAssignmentDTO a : adminService.getAllFacultyAssignments()) {
                model.addRow(
                        new Object[] { a.getFacultyName(), a.getFacultyId(), a.getSubjectName(), a.getSubjectCode() });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ===== REPORTS =====
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(CARD_BG);
        filterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT), "Filters", 0, 0,
                new Font("Segoe UI", Font.BOLD, 12), ACCENT));

        JTextField dateField = new JTextField(new Date(System.currentTimeMillis()).toString(), 10);
        JTextField sectionField = new JTextField(5);
        JButton filterBtn = styledButton("Search Date/Section Wise", ACCENT);
        JButton todayBtn = styledButton("Generate Today's Summary", GREEN);
        JButton allBtn = styledButton("Show General Logs", YELLOW);

        filterPanel.add(label("Date (YYYY-MM-DD):"));
        filterPanel.add(dateField);
        filterPanel.add(label("Section:"));
        filterPanel.add(sectionField);
        filterPanel.add(filterBtn);
        filterPanel.add(todayBtn);
        filterPanel.add(allBtn);

        panel.add(filterPanel, BorderLayout.NORTH);

        // Dynamic Table
        DefaultTableModel model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);
        styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Actions
        filterBtn.addActionListener(e -> {
            try {
                Date d = Date.valueOf(dateField.getText().trim());
                Integer sec = sectionField.getText().trim().isEmpty() ? null
                        : Integer.parseInt(sectionField.getText().trim());
                loadFilteredReports(model, d, sec);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Date (YYYY-MM-DD) or Section.");
            }
        });

        todayBtn.addActionListener(e -> {
            try {
                adminService.generateDailyReport(new Date(System.currentTimeMillis()));
                loadReports(model); // Load general logs after generating
                JOptionPane.showMessageDialog(this, "Daily summary generated!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        allBtn.addActionListener(e -> loadReports(model));

        // Default Load
        loadReports(model);

        return panel;
    }

    private void loadReports(DefaultTableModel model) {
        model.setColumnIdentifiers(
                new String[] { "Date", "Total Students", "Present", "Avg Attendance %", "Description" });
        model.setRowCount(0);
        try {
            for (Report r : adminService.getAllReports()) {
                model.addRow(new Object[] { r.getReportDate(), r.getTotalStudents(), r.getTotalPresent(),
                        String.format("%.2f%%", r.getAverageAttendance()), r.getDescription() });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadFilteredReports(DefaultTableModel model, Date date, Integer section) {
        model.setColumnIdentifiers(
                new String[] { "Date", "Section", "Semester", "Total Hits", "Present Hits", "Percentage" });
        model.setRowCount(0);
        try {
            for (SectionReportDTO r : adminService.getSectionWiseReport(date, section)) {
                model.addRow(new Object[] {
                        r.getDate(),
                        r.getSection(),
                        r.getSemester(),
                        r.getTotal(),
                        r.getPresent(),
                        String.format("%.2f%%", r.getPercentage())
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ===== UTILITY =====
    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(TEXT);
        return lbl;
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(BG);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setBackground(CARD_BG);
        table.setForeground(TEXT);
        table.setGridColor(new Color(69, 71, 90));
        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(BG);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(36, 36, 54));
        table.getTableHeader().setForeground(ACCENT);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }
}
