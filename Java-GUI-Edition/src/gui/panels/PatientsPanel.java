package gui.panels;

import gui.Theme;
import gui.dialogs.PatientDialog;
import hospital.Hospital;
import patient.ExternalPatient;
import patient.InternalPatient;
import patient.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientsPanel extends JPanel {

    private final Hospital hospital;
    private final Runnable onDataChanged;
    private final DefaultTableModel model;
    private final JTable table;

    public PatientsPanel(Hospital hospital, Runnable onDataChanged) {
        this.hospital = hospital;
        this.onDataChanged = onDataChanged;

        setLayout(new BorderLayout(0, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        model = new DefaultTableModel(new Object[]{"ID", "Type", "Name", "Address", "Birth Date", "Status"}, 0) {
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
        JButton addBtn = Theme.primaryButton("Add Patient");
        JButton editBtn = Theme.button("Edit");
        JButton deleteBtn = Theme.dangerButton("Delete");
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);

        addBtn.addActionListener(e -> {
            PatientDialog dlg = new PatientDialog(SwingUtilities.getWindowAncestor(this), null);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                hospital.addPatient(dlg.getResult());
                refresh();
                onDataChanged.run();
            }
        });

        editBtn.addActionListener(e -> {
            Patient p = selected();
            if (p == null) return;
            PatientDialog dlg = new PatientDialog(SwingUtilities.getWindowAncestor(this), p);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                refresh();
                onDataChanged.run();
            }
        });

        deleteBtn.addActionListener(e -> {
            Patient p = selected();
            if (p == null) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Delete patient #" + p.getId() + " (" + p.getName() + ")?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                hospital.deletePatient(p.getId());
                refresh();
                onDataChanged.run();
            }
        });

        add(toolbar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    private Patient selected() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int id = (int) model.getValueAt(row, 0);
        return hospital.getPatientById(id);
    }

    private String statusInfo(Patient p) {
        if (p instanceof ExternalPatient ep) return "Accepted=" + ep.isAcceptance() + " on " + ep.getAcceptDate();
        if (p instanceof InternalPatient ip) return ip.isDischarged() ? "Discharged on " + ip.getDischargeDate() : "Admitted";
        return "-";
    }

    public void refresh() {
        model.setRowCount(0);
        for (Patient p : hospital.getPatients()) {
            model.addRow(new Object[]{p.getId(), p.getPatientType(), p.getName(), p.getAddress(),
                    p.getBirthDate(), statusInfo(p)});
        }
    }
}
