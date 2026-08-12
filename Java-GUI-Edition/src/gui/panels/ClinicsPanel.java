package gui.panels;

import gui.Theme;
import gui.dialogs.ClinicDialog;
import hospital.Clinic;
import hospital.Hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClinicsPanel extends JPanel {

    private final Hospital hospital;
    private final Runnable onDataChanged;
    private final DefaultTableModel model;
    private final JTable table;

    public ClinicsPanel(Hospital hospital, Runnable onDataChanged) {
        this.hospital = hospital;
        this.onDataChanged = onDataChanged;

        setLayout(new BorderLayout(0, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        model = new DefaultTableModel(new Object[]{"ID", "Name", "Type"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        Theme.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(new Color(22, 29, 46));
        scroll.setBorder(BorderFactory.createLineBorder(Theme.LINE));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);
        JButton addBtn = Theme.primaryButton("Add Clinic");
        JButton editBtn = Theme.button("Edit");
        JButton deleteBtn = Theme.dangerButton("Delete");
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);

        addBtn.addActionListener(e -> {
            ClinicDialog dlg = new ClinicDialog(SwingUtilities.getWindowAncestor(this), null);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                hospital.addClinic(dlg.getResult());
                refresh();
                onDataChanged.run();
            }
        });

        editBtn.addActionListener(e -> {
            Clinic c = selected();
            if (c == null) return;
            ClinicDialog dlg = new ClinicDialog(SwingUtilities.getWindowAncestor(this), c);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                refresh();
                onDataChanged.run();
            }
        });

        deleteBtn.addActionListener(e -> {
            Clinic c = selected();
            if (c == null) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Delete clinic #" + c.getId() + " (" + c.getName() + ")?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                hospital.deleteClinic(c.getId());
                refresh();
                onDataChanged.run();
            }
        });

        add(toolbar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    private Clinic selected() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int id = (int) model.getValueAt(row, 0);
        return hospital.getClinicById(id);
    }

    public void refresh() {
        model.setRowCount(0);
        for (Clinic c : hospital.getClinics()) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getType()});
        }
    }
}
