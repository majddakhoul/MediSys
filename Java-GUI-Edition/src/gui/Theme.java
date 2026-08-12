package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Theme {

    public static final Color BG = new Color(15, 20, 32);
    public static final Color PANEL = new Color(27, 36, 54);
    public static final Color LINE = new Color(42, 53, 80);
    public static final Color TEXT = new Color(231, 236, 247);
    public static final Color MUTED = new Color(139, 151, 179);
    public static final Color ACCENT = new Color(125, 211, 252);
    public static final Color ACCENT_2 = new Color(167, 139, 250);
    public static final Color GOOD = new Color(74, 222, 128);
    public static final Color BAD = new Color(248, 113, 113);
    public static final Color ROW_ALT = new Color(22, 29, 46);

    public static JButton button(String text) {
        JButton b = new JButton(text);
        b.setBackground(PANEL);
        b.setForeground(TEXT);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE), new EmptyBorder(7, 14, 7, 14)));
        return b;
    }

    public static JButton primaryButton(String text) {
        JButton b = button(text);
        b.setBackground(ACCENT);
        b.setForeground(new Color(11, 15, 24));
        b.setFont(b.getFont().deriveFont(Font.BOLD));
        return b;
    }

    public static JButton dangerButton(String text) {
        JButton b = button(text);
        b.setBackground(new Color(58, 20, 20));
        b.setForeground(BAD);
        return b;
    }

    public static void styleTable(JTable table) {
        table.setBackground(new Color(22, 29, 46));
        table.setForeground(TEXT);
        table.setGridColor(LINE);
        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(new Color(11, 15, 24));
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JTableHeader header = table.getTableHeader();
        header.setBackground(PANEL);
        header.setForeground(MUTED);
        header.setFont(new Font("SansSerif", Font.BOLD, 11));
        header.setReorderingAllowed(false);
    }

    public static JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setForeground(MUTED);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        return l;
    }

    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(MUTED);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }

    public static JTextField textField() {
        JTextField f = new JTextField();
        f.setBackground(new Color(22, 29, 46));
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE), new EmptyBorder(5, 8, 5, 8)));
        return f;
    }

    public static JComboBox<String> comboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setBackground(new Color(22, 29, 46));
        box.setForeground(TEXT);
        return box;
    }
}
