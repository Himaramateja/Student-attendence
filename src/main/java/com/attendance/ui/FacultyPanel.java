package com.attendance.ui;

import com.attendance.model.*;
import com.attendance.service.FacultyService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class FacultyPanel extends JPanel {
    private final SwingUI parent;
    private final User currentUser;
    private final FacultyService facultyService = new FacultyService();
    private final JPanel contentPanel = new JPanel(new CardLayout());
    private final Color ACCENT = new Color(137, 180, 250);
    private final Color BG = new Color(30, 30, 46);
    private final Color CARD_BG = new Color(49, 50, 68);
    private final Color TEXT = new Color(205, 214, 244);
    private final Color GREEN = new Color(166, 227, 161);
    private final Color RED = new Color(243, 139, 168);
    private final Color YELLOW = new Color(249, 226, 175);

    public FacultyPanel(SwingUI parent, User currentUser) {
        this.parent = parent;
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(BG);

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(CARD_BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel title = new JLabel("Faculty Dashboard — " + currentUser.getUsername());
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(ACCENT);
        topBar.add(title, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setBackground(RED);
        logoutBtn.setForeground(BG);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> parent.logout());
        topBar.add(logoutBtn, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(36, 36, 54));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));

        String[] menuItems = { "Mark Attendance", "View Records" };
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

        contentPanel.setBackground(BG);
        contentPanel.add(createMarkAttendancePanel(), "Mark Attendance");
        contentPanel.add(createViewRecordsPanel(), "View Records");
        add(contentPanel, BorderLayout.CENTER);
        showContent("Mark Attendance");
    }

    private void showContent(String name) {
        if (name.equals("Mark Attendance")) {
            contentPanel.add(createMarkAttendancePanel(), "Mark Attendance");
        } else if (name.equals("View Records")) {
            contentPanel.add(createViewRecordsPanel(), "View Records");
        }
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, name);
    }

    // ===== MARK ATTENDANCE =====
    private JPanel createMarkAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Filters
        JPanel filters = new JPanel(new GridLayout(2, 4, 10, 8));
        filters.setBackground(CARD_BG);
        filters.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT), "Select Class & Subject",
                        0, 0, new Font("Segoe UI", Font.BOLD, 14), ACCENT),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JComboBox<String> subjectCombo = new JComboBox<>();
        JTextField sectionField = new JTextField();
        JTextField semesterField = new JTextField();
        JTextField periodField = new JTextField();
        JButton loadBtn = styledButton("Load Students", ACCENT);

        // Load subjects
        try {
            List<Subject> subjects = facultyService.getAssignedSubjects(currentUser.getUsername());
            for (Subject s : subjects) {
                subjectCombo.addItem(s.getSubjectCode() + " - " + s.getSubjectName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        filters.add(label("Subject:"));
        filters.add(subjectCombo);
        filters.add(label("Section:"));
        filters.add(sectionField);
        filters.add(label("Semester:"));
        filters.add(semesterField);
        filters.add(label("Period (1-7):"));
        filters.add(periodField);

        JPanel filterWrap = new JPanel(new BorderLayout(0, 8));
        filterWrap.setBackground(BG);
        filterWrap.add(filters, BorderLayout.CENTER);
        filterWrap.add(loadBtn, BorderLayout.SOUTH);
        panel.add(filterWrap, BorderLayout.NORTH);

        // Attendance Table
        String[] cols = { "Roll No", "Name", "Present" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public Class<?> getColumnClass(int col) {
                return col == 2 ? Boolean.class : String.class;
            }

            public boolean isCellEditable(int row, int col) {
                return col == 2;
            }
        };
        JTable table = new JTable(model);
        styleTable(table);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton submitBtn = styledButton("Submit Attendance", GREEN);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panel.add(submitBtn, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> {
            model.setRowCount(0);
            try {
                int section = Integer.parseInt(sectionField.getText().trim());
                int semester = Integer.parseInt(semesterField.getText().trim());
                List<Student> students = facultyService.getStudentsByClass(section, semester);
                for (Student s : students) {
                    model.addRow(new Object[] { s.getStudentRoll(), s.getName(), Boolean.TRUE });
                }
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No students found for this class.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        submitBtn.addActionListener(e -> {
            if (subjectCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Select a subject.");
                return;
            }
            String subjectCode = subjectCombo.getSelectedItem().toString().split(" - ")[0].trim();
            int period;
            try {
                period = Integer.parseInt(periodField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid period number (1-7).");
                return;
            }
            Date today = new Date(System.currentTimeMillis());
            int marked = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                String roll = (String) model.getValueAt(i, 0);
                boolean present = (Boolean) model.getValueAt(i, 2);
                String status = present ? "PRESENT" : "ABSENT";
                try {
                    facultyService.markAttendance(roll, subjectCode, today, period, status);
                    marked++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(this, "Attendance marked for " + marked + " students!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    // ===== VIEW RECORDS =====
    private JPanel createViewRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel filters = new JPanel(new GridLayout(1, 6, 10, 8));
        filters.setBackground(CARD_BG);
        filters.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT),
                        "View Class Attendance Summary",
                        0, 0, new Font("Segoe UI", Font.BOLD, 14), ACCENT),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JComboBox<String> subjectCombo = new JComboBox<>();
        JTextField sectionField = new JTextField();
        JTextField semesterField = new JTextField();

        try {
            List<Subject> subjects = facultyService.getAssignedSubjects(currentUser.getUsername());
            for (Subject s : subjects) {
                subjectCombo.addItem(s.getSubjectCode() + " - " + s.getSubjectName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        filters.add(label("Subject:"));
        filters.add(subjectCombo);
        filters.add(label("Section:"));
        filters.add(sectionField);
        filters.add(label("Semester:"));
        filters.add(semesterField);

        JButton loadBtn = styledButton("Retrieve Summary", ACCENT);

        JPanel filterWrap = new JPanel(new BorderLayout(0, 8));
        filterWrap.setBackground(BG);
        filterWrap.add(filters, BorderLayout.CENTER);
        filterWrap.add(loadBtn, BorderLayout.SOUTH);
        panel.add(filterWrap, BorderLayout.NORTH);

        String[] cols = { "Roll No", "Student Name", "Total Classes", "Attended", "Percentage", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);
        styleTable(table);

        // Custom renderer for last column (Eligible/Shortage)
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r,
                    int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                if ("Eligible".equals(val)) {
                    setForeground(GREEN);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    setForeground(RED);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                }
                return this;
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        loadBtn.addActionListener(e -> {
            model.setRowCount(0);
            if (subjectCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Select a subject.");
                return;
            }
            String subjectCode = subjectCombo.getSelectedItem().toString().split(" - ")[0].trim();
            try {
                int section = Integer.parseInt(sectionField.getText().trim());
                int semester = Integer.parseInt(semesterField.getText().trim());
                List<StudentAttendanceReportDTO> reports = facultyService.getClassAttendanceSummary(subjectCode,
                        section, semester);
                for (StudentAttendanceReportDTO r : reports) {
                    model.addRow(new Object[] {
                            r.getStudentRoll(), r.getStudentName(), r.getTotalPeriods(), r.getAttendedPeriods(),
                            String.format("%.2f%%", r.getPercentage()),
                            r.getPercentage() >= 75 ? "Eligible" : "Shortage"
                    });
                }
                if (reports.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No records found for this class and subject.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
    }
}
