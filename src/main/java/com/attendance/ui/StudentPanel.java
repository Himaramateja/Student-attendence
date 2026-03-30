package com.attendance.ui;

import com.attendance.model.*;
import com.attendance.service.StudentService;
import com.attendance.service.TimetableService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class StudentPanel extends JPanel {
    private final SwingUI parent;
    private final User currentUser;
    private final StudentService studentService = new StudentService();
    private final TimetableService timetableService = new TimetableService();
    private final JPanel contentPanel = new JPanel(new CardLayout());
    private final Color ACCENT = new Color(137, 180, 250);
    private final Color BG = new Color(30, 30, 46);
    private final Color CARD_BG = new Color(49, 50, 68);
    private final Color TEXT = new Color(205, 214, 244);
    private final Color GREEN = new Color(166, 227, 161);
    private final Color RED = new Color(243, 139, 168);
    private final Color YELLOW = new Color(249, 226, 175);
    private final Color MAUVE = new Color(203, 166, 247);

    public StudentPanel(SwingUI parent, User currentUser) {
        this.parent = parent;
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(BG);

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(CARD_BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel title = new JLabel("Student Dashboard — " + currentUser.getUsername());
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(ACCENT);
        topBar.add(title, BorderLayout.WEST);

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightBtns.setBackground(CARD_BG);
        JButton changePwdBtn = styledButton("Change Password", YELLOW);
        JButton logoutBtn = styledButton("Logout", RED);
        rightBtns.add(changePwdBtn);
        rightBtns.add(logoutBtn);
        topBar.add(rightBtns, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        logoutBtn.addActionListener(e -> parent.logout());
        changePwdBtn.addActionListener(e -> {
            String newPwd = JOptionPane.showInputDialog(this, "Enter new password:");
            if (newPwd != null && !newPwd.trim().isEmpty()) {
                try {
                    studentService.updatePassword(currentUser.getUsername(), newPwd.trim());
                    JOptionPane.showMessageDialog(this, "Password updated!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(36, 36, 54));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));

        String[] menuItems = { "Overview", "Subject Attendance", "Timetable" };
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
        contentPanel.add(createOverviewPanel(), "Overview");
        contentPanel.add(createSubjectAttendancePanel(), "Subject Attendance");
        contentPanel.add(createTimetablePanel(), "Timetable");
        add(contentPanel, BorderLayout.CENTER);
        showContent("Overview");
    }

    private void showContent(String name) {
        if (name.equals("Overview")) {
            contentPanel.add(createOverviewPanel(), "Overview");
        } else if (name.equals("Subject Attendance")) {
            contentPanel.add(createSubjectAttendancePanel(), "Subject Attendance");
        } else if (name.equals("Timetable")) {
            contentPanel.add(createTimetablePanel(), "Timetable");
        }
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, name);
    }

    // ===== OVERVIEW =====
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            Student student = studentService.getStudentDetails(currentUser.getUsername());
            StudentStatsDTO stats = studentService.getSummaryStats(currentUser.getUsername());

            // Student Info Card
            JPanel infoCard = new JPanel();
            infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
            infoCard.setBackground(CARD_BG);
            infoCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MAUVE, 2, true),
                    BorderFactory.createEmptyBorder(20, 25, 20, 25)));

            if (student != null) {
                addInfoLine(infoCard, "Name", student.getName());
                addInfoLine(infoCard, "Roll No", student.getStudentRoll());
                addInfoLine(infoCard, "Department", student.getDepartment());
                addInfoLine(infoCard, "Section", String.valueOf(student.getSection()));
                addInfoLine(infoCard, "Semester", String.valueOf(student.getSemester()));
            }
            panel.add(infoCard, BorderLayout.NORTH);

            // Stats Cards
            JPanel statsRow = new JPanel(new GridLayout(1, 3, 15, 0));
            statsRow.setBackground(BG);

            statsRow.add(createStatCard("Total Conducted", String.valueOf(stats.getTotalConducted()), ACCENT));
            statsRow.add(createStatCard("Total Attended", String.valueOf(stats.getTotalAttended()), GREEN));
            statsRow.add(createStatCard("Overall %", String.format("%.2f%%", stats.getOverallPercentage()),
                    stats.getOverallPercentage() >= 75 ? GREEN : RED));

            panel.add(statsRow, BorderLayout.CENTER);
        } catch (Exception ex) {
            JLabel err = new JLabel("Error loading data: " + ex.getMessage());
            err.setForeground(RED);
            panel.add(err, BorderLayout.CENTER);
        }

        return panel;
    }

    private void addInfoLine(JPanel parent, String key, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        row.setBackground(CARD_BG);
        JLabel k = new JLabel(key + ": ");
        k.setFont(new Font("Segoe UI", Font.BOLD, 14));
        k.setForeground(ACCENT);
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        v.setForeground(TEXT);
        row.add(k);
        row.add(v);
        parent.add(row);
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 38));
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

    // ===== SUBJECT ATTENDANCE =====
    private JPanel createSubjectAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("Subject-wise Attendance Breakdown");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(ACCENT);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(heading, BorderLayout.NORTH);

        String[] cols = { "Code", "Subject Name", "Conducted", "Attended", "Percentage", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);
        styleTable(table);

        // Custom renderer for percentage column
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r,
                    int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                return this;
            }
        });

        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r,
                    int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                if ("Eligible".equals(val))
                    setForeground(GREEN);
                else
                    setForeground(RED);
                return this;
            }
        });

        try {
            List<SubjectAttendanceDTO> list = studentService.getSubjectWiseAttendance(currentUser.getUsername());
            for (SubjectAttendanceDTO s : list) {
                model.addRow(new Object[] {
                        s.getSubjectCode(), s.getSubjectName(), s.getTotalPeriods(), s.getAttendedPeriods(),
                        String.format("%.2f%%", s.getPercentage()),
                        s.getPercentage() >= 75 ? "Eligible" : "Shortage"
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // ===== TIMETABLE =====
    private JPanel createTimetablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("Class Timetable (Weekly Schedule)");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(ACCENT);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(heading, BorderLayout.NORTH);

        try {
            Student student = studentService.getStudentDetails(currentUser.getUsername());
            if (student == null) {
                panel.add(new JLabel("Student details not found."), BorderLayout.CENTER);
                return panel;
            }

            Map<String, List<String>> timetable = timetableService.generateTimetable(
                    student.getDepartment(), student.getSection(), student.getSemester());
            List<String> timings = timetableService.getTimings();
            String[] days = timetable.keySet().toArray(new String[0]);

            // Build column headers: Timing | Monday | Tuesday | ...
            String[] cols = new String[days.length + 1];
            cols[0] = "Timing";
            System.arraycopy(days, 0, cols, 1, days.length);

            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };

            // Periods 1-4
            for (int i = 0; i < 4; i++) {
                Object[] row = new Object[days.length + 1];
                row[0] = timings.get(i);
                for (int d = 0; d < days.length; d++) {
                    row[d + 1] = timetable.get(days[d]).get(i);
                }
                model.addRow(row);
            }

            // Lunch Row
            Object[] lunchRow = new Object[days.length + 1];
            lunchRow[0] = timings.get(4);
            for (int d = 0; d < days.length; d++) {
                lunchRow[d + 1] = "LUNCH BREAK";
            }
            model.addRow(lunchRow);

            // Periods 5-7
            for (int i = 4; i < 7; i++) {
                Object[] row = new Object[days.length + 1];
                row[0] = timings.get(i + 1);
                for (int d = 0; d < days.length; d++) {
                    row[d + 1] = timetable.get(days[d]).get(i);
                }
                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.setRowHeight(38);
            table.setBackground(CARD_BG);
            table.setForeground(TEXT);
            table.setGridColor(new Color(69, 71, 90));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.getTableHeader().setBackground(new Color(36, 36, 54));
            table.getTableHeader().setForeground(ACCENT);

            // Center all cells
            DefaultTableCellRenderer center = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r,
                        int c) {
                    super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    if (val != null && val.toString().contains("LUNCH")) {
                        setBackground(YELLOW);
                        setForeground(BG);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else if (c == 0) {
                        setBackground(new Color(36, 36, 54));
                        setForeground(ACCENT);
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    } else {
                        setBackground(CARD_BG);
                        setForeground(TEXT);
                        setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    }
                    return this;
                }
            };

            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(center);
            }

            panel.add(new JScrollPane(table), BorderLayout.CENTER);
        } catch (Exception ex) {
            JLabel err = new JLabel("Error loading timetable: " + ex.getMessage());
            err.setForeground(RED);
            panel.add(err, BorderLayout.CENTER);
        }

        return panel;
    }

    // ===== UTILITY =====
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
