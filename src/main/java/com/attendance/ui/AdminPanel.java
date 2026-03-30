package com.attendance.ui;

import com.attendance.model.*;
import com.attendance.service.AdminService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

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

        String[] menuItems = { "Dashboard", "Manage Students", "Manage Faculty", "Manage Subjects", "Reports",
                "View Students", "View Faculty", "Manage Timetable" };
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
        contentPanel.add(createViewStudentsPanel(), "View Students");
        contentPanel.add(createViewFacultyPanel(), "View Faculty");
        contentPanel.add(createManageTimetablePanel(), "Manage Timetable");
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
        } else if (name.equals("View Students")) {
            contentPanel.add(createViewStudentsPanel(), "View Students");
        } else if (name.equals("View Faculty")) {
            contentPanel.add(createViewFacultyPanel(), "View Faculty");
        } else if (name.equals("Manage Timetable")) {
            contentPanel.add(createManageTimetablePanel(), "Manage Timetable");
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

    // ===== VIEW STUDENTS =====
    private JPanel createViewStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Filter Bar
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filterPanel.setBackground(CARD_BG);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT),
                        "Search Students", 0, 0, new Font("Segoe UI", Font.BOLD, 14), ACCENT),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // Section dropdown
        JComboBox<String> sectionCombo = new JComboBox<>();
        sectionCombo.addItem("-- All Sections --");
        try {
            for (int sec : adminService.getDistinctSections()) {
                sectionCombo.addItem("Section " + sec);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        // Department dropdown
        JComboBox<String> deptCombo = new JComboBox<>();
        deptCombo.addItem("-- All Departments --");
        try {
            for (String dept : adminService.getDistinctDepartments()) {
                deptCombo.addItem(dept);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        // Semester dropdown
        JComboBox<String> semCombo = new JComboBox<>();
        semCombo.addItem("-- All Semesters --");
        try {
            for (int sem : adminService.getDistinctSemesters()) {
                semCombo.addItem("Semester " + sem);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        JTextField rollField = new JTextField(10);
        JButton searchBtn = styledButton("Search", ACCENT);
        JButton showAllBtn = styledButton("Show All", GREEN);

        filterPanel.add(label("Section:"));
        filterPanel.add(sectionCombo);
        filterPanel.add(label("Department:"));
        filterPanel.add(deptCombo);
        filterPanel.add(label("Semester:"));
        filterPanel.add(semCombo);
        filterPanel.add(label("Roll No:"));
        filterPanel.add(rollField);
        filterPanel.add(searchBtn);
        filterPanel.add(showAllBtn);

        panel.add(filterPanel, BorderLayout.NORTH);

        // Results Table
        String[] cols = { "Roll No", "Name", "Department", "Section", "Semester" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);
        loadStudents(model); // Show all by default
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Search action
        searchBtn.addActionListener(e -> {
            Integer section = null;
            String department = null;
            Integer semester = null;
            String roll = rollField.getText().trim();

            if (sectionCombo.getSelectedIndex() > 0) {
                String secText = (String) sectionCombo.getSelectedItem();
                section = Integer.parseInt(secText.replace("Section ", ""));
            }
            if (deptCombo.getSelectedIndex() > 0) {
                department = (String) deptCombo.getSelectedItem();
            }
            if (semCombo.getSelectedIndex() > 0) {
                String semText = (String) semCombo.getSelectedItem();
                semester = Integer.parseInt(semText.replace("Semester ", ""));
            }
            if (roll.isEmpty()) roll = null;

            model.setRowCount(0);
            try {
                List<Student> results = adminService.searchStudents(section, department, semester, roll);
                for (Student s : results) {
                    model.addRow(new Object[] { s.getStudentRoll(), s.getName(),
                            s.getDepartment(), s.getSection(), s.getSemester() });
                }
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No students found matching the filters.",
                            "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        showAllBtn.addActionListener(e -> {
            sectionCombo.setSelectedIndex(0);
            deptCombo.setSelectedIndex(0);
            semCombo.setSelectedIndex(0);
            rollField.setText("");
            loadStudents(model);
        });

        return panel;
    }

    // ===== VIEW FACULTY =====
    private JPanel createViewFacultyPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Filter Bar
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filterPanel.setBackground(CARD_BG);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT),
                        "Search Faculty", 0, 0, new Font("Segoe UI", Font.BOLD, 14), ACCENT),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // Department dropdown
        JComboBox<String> deptCombo = new JComboBox<>();
        deptCombo.addItem("-- All Departments --");
        try {
            for (String dept : adminService.getDistinctFacultyDepartments()) {
                deptCombo.addItem(dept);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        JTextField facIdField = new JTextField(12);
        JButton searchBtn = styledButton("Search", ACCENT);
        JButton showAllBtn = styledButton("Show All", GREEN);

        filterPanel.add(label("Department:"));
        filterPanel.add(deptCombo);
        filterPanel.add(label("Faculty ID:"));
        filterPanel.add(facIdField);
        filterPanel.add(searchBtn);
        filterPanel.add(showAllBtn);

        panel.add(filterPanel, BorderLayout.NORTH);

        // Results Table
        String[] cols = { "Faculty ID", "Name", "Department", "Assigned Subject Code", "Assigned Subject Name" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);
        loadFacultyDetails(model, null, null); // Show all by default
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Search action
        searchBtn.addActionListener(e -> {
            String department = null;
            String facId = facIdField.getText().trim();

            if (deptCombo.getSelectedIndex() > 0) {
                department = (String) deptCombo.getSelectedItem();
            }
            if (facId.isEmpty()) facId = null;

            model.setRowCount(0);
            try {
                List<FacultyDetailDTO> results = adminService.searchFaculty(department, facId);
                for (FacultyDetailDTO d : results) {
                    model.addRow(new Object[] {
                            d.getFacultyId(), d.getFacultyName(), d.getDepartment(),
                            d.getSubjectCode() != null ? d.getSubjectCode() : "(None)",
                            d.getSubjectName() != null ? d.getSubjectName() : "(No subject assigned)" });
                }
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No faculty found matching the filters.",
                            "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        showAllBtn.addActionListener(e -> {
            deptCombo.setSelectedIndex(0);
            facIdField.setText("");
            loadFacultyDetails(model, null, null);
        });

        return panel;
    }

    private void loadFacultyDetails(DefaultTableModel model, String department, String facultyId) {
        model.setRowCount(0);
        try {
            List<FacultyDetailDTO> details = adminService.searchFaculty(department, facultyId);
            for (FacultyDetailDTO d : details) {
                model.addRow(new Object[] {
                        d.getFacultyId(), d.getFacultyName(), d.getDepartment(),
                        d.getSubjectCode() != null ? d.getSubjectCode() : "(None)",
                        d.getSubjectName() != null ? d.getSubjectName() : "(No subject assigned)" });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ===== MANAGE TIMETABLE =====
    private JPanel createManageTimetablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Selector Bar
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        selectorPanel.setBackground(CARD_BG);
        selectorPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT),
                        "Select Class to Manage", 0, 0, new Font("Segoe UI", Font.BOLD, 14), ACCENT),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JComboBox<String> deptCombo = new JComboBox<>();
        try {
            for (String dept : adminService.getDistinctDepartments()) {
                deptCombo.addItem(dept);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        JComboBox<Integer> secCombo = new JComboBox<>();
        try {
            for (int sec : adminService.getDistinctSections()) {
                secCombo.addItem(sec);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        JComboBox<Integer> semCombo = new JComboBox<>();
        try {
            for (int sem : adminService.getDistinctSemesters()) {
                semCombo.addItem(sem);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        JButton loadBtn = styledButton("Load Timetable", ACCENT);

        selectorPanel.add(label("Department:"));
        selectorPanel.add(deptCombo);
        selectorPanel.add(label("Section:"));
        selectorPanel.add(secCombo);
        selectorPanel.add(label("Semester:"));
        selectorPanel.add(semCombo);
        selectorPanel.add(loadBtn);

        panel.add(selectorPanel, BorderLayout.NORTH);

        // Editor Grid
        String[] cols = { "Timing", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                // Timing column is not editable, lunch row is not editable
                return c > 0 && r != 4;
            }
        };
        JTable table = new JTable(model);
        styleTable(table);
        table.setRowHeight(40);

        // Populate empty structure
        List<String> timings = new com.attendance.service.TimetableService().getTimings();
        for (int i = 0; i < 4; i++) model.addRow(new Object[]{timings.get(i), "", "", "", "", ""});
        model.addRow(new Object[]{timings.get(4), "LUNCH BREAK", "LUNCH BREAK", "LUNCH BREAK", "LUNCH BREAK", "LUNCH BREAK"});
        for (int i = 5; i < 8; i++) model.addRow(new Object[]{timings.get(i), "", "", "", "", ""});

        // Subject dropdown for cells
        JComboBox<String> editorCombo = new JComboBox<>();
        editorCombo.addItem(""); // Empty option for free period
        try {
            for (Subject s : adminService.getAllSubjects()) {
                editorCombo.addItem(s.getSubjectName() + " (" + s.getSubjectCode() + ")");
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        DefaultCellEditor cellEditor = new DefaultCellEditor(editorCombo);
        for (int i = 1; i <= 5; i++) {
            table.getColumnModel().getColumn(i).setCellEditor(cellEditor);
        }

        // Center all cells visually
        javax.swing.table.DefaultTableCellRenderer center = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (r == 4) {
                    setBackground(YELLOW);
                    setForeground(BG);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else if (c == 0) {
                    setBackground(new Color(36, 36, 54));
                    setForeground(ACCENT);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else {
                    if (sel) {
                        setBackground(ACCENT);
                        setForeground(BG);
                    } else {
                        setBackground(CARD_BG);
                        setForeground(TEXT);
                    }
                    setFont(new Font("Segoe UI", Font.PLAIN, 12));
                }
                return this;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BG);
        JButton saveBtn = styledButton("Save Timetable", GREEN);
        saveBtn.setEnabled(false); // Enable only after loading
        bottomPanel.add(saveBtn);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        panel.add(centerPanel, BorderLayout.CENTER);

        // Actions
        loadBtn.addActionListener(e -> {
            if (deptCombo.getSelectedItem() == null || secCombo.getSelectedItem() == null || semCombo.getSelectedItem() == null) {
                return;
            }
            String dept = (String) deptCombo.getSelectedItem();
            int sec = (Integer) secCombo.getSelectedItem();
            int sem = (Integer) semCombo.getSelectedItem();

            try {
                // Clear existing inputs
                for (int r = 0; r < model.getRowCount(); r++) {
                    if (r == 4) continue;
                    for (int c = 1; c <= 5; c++) model.setValueAt("", r, c);
                }

                java.util.Map<String, List<String>> rawTable = adminService.getRawTimetable(dept, sec, sem);
                if (rawTable != null) {
                    // Create map to format codes as "Name (Code)" locally
                    java.util.Map<String, String> codeToDisplay = new java.util.HashMap<>();
                    try {
                        for (Subject s : adminService.getAllSubjects()) {
                            codeToDisplay.put(s.getSubjectCode(), s.getSubjectName() + " (" + s.getSubjectCode() + ")");
                        }
                    } catch (Exception ex) {}

                    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                    for (int c = 0; c < 5; c++) {
                        List<String> periodCodes = rawTable.get(days[c]);
                        if (periodCodes != null) {
                            for (int p = 0; p < periodCodes.size(); p++) {
                                int modelRow = (p >= 4) ? p + 1 : p;
                                String code = periodCodes.get(p);
                                String display = code != null ? codeToDisplay.getOrDefault(code, code) : "";
                                model.setValueAt(display, modelRow, c + 1);
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Loaded existing timetable for " + dept + " Sec " + sec + " Sem " + sem);
                } else {
                    JOptionPane.showMessageDialog(this, "No saved timetable found. You can create a new one.");
                }
                saveBtn.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading timetable: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        saveBtn.addActionListener(e -> {
            // End edit before save
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }

            String dept = (String) deptCombo.getSelectedItem();
            int sec = (Integer) secCombo.getSelectedItem();
            int sem = (Integer) semCombo.getSelectedItem();

            try {
                java.util.Map<String, List<String>> newTimetable = new java.util.LinkedHashMap<>();
                String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                
                for (int c = 0; c < 5; c++) {
                    List<String> periods = new ArrayList<>();
                    for (int r = 0; r < 8; r++) {
                        if (r == 4) continue; // Skip lunch
                        String val = (String) model.getValueAt(r, c + 1);
                        String code = null;
                        if (val != null && !val.trim().isEmpty()) {
                            val = val.trim();
                            if (val.contains("(") && val.endsWith(")")) {
                                code = val.substring(val.lastIndexOf("(") + 1, val.length() - 1);
                            } else {
                                code = val;
                            }
                        }
                        periods.add(code);
                    }
                    newTimetable.put(days[c], periods);
                }

                adminService.saveTimetable(dept, sec, sem, newTimetable);
                JOptionPane.showMessageDialog(this, "Timetable saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving timetable: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
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
