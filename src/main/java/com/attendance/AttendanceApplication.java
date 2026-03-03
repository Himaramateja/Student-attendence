package com.attendance;

import com.attendance.ui.SwingUI;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class AttendanceApplication {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 8);
        } catch (Exception e) {
            System.err.println("Failed to set FlatLaf. Using default.");
        }
        SwingUtilities.invokeLater(() -> new SwingUI());
    }
}
