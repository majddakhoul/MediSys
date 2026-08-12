package gui.panels;

import gui.Theme;
import gui.dialogs.TreatmentDialog;
import hospital.Hospital;
import patient.Patient;
import treatment.ExternalTreatment;
import treatment.InternalTreatment;
import treatment.Treatment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TreatmentsPanel extends JPanel {

    private final Hospital hospital;
    private final Runnable onDataChanged;
    private final DefaultTableModel model;
    private final JTable table;
    private final JComboBox<String> patientBox;
    private List<Patient> patientList;

    public TreatmentsPanel(Hospital hospital, Runnable onDataChanged) {
        this.hospital = hospital;
        this.onDataChanged = onDataChanged;

        setLayout(new BorderLayout(0, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.setOpaque(false);
        JLabel label = Theme.fieldLabel("Patient:");
        patientBox = Theme.comboBox(new String[]{});
        patientBox.addActionListener(e -> refreshTable());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        left.add(label);
        left.add(patientBox);
        top.add(left, BorderLayout.WEST);

        model = new DefaultTableModel(new Object[]{"ID", "Type", "Date", "Cost", "Extra"}, 0) {
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
        JButton addBtn = Theme.primaryButton("Add Treatment");
        JButton deleteBtn = Theme.dangerButton("Delete");
        toolbar.add(addBtn);
        toolbar.add(deleteBtn);

        addBtn.addActionListener(e -> {
            Patient p = selectedPatient();
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Add a patient first.");
                return;
            }
            TreatmentDialog dlg = new TreatmentDialog(SwingUtilities.getWindowAncestor(this), hospital, p);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                hospital.addTreatmentToPatient(p.getId(), dlg.getResult());
                refreshTable();
                onDataChanged.run();
            }
        });

        deleteBtn.addActionListener(e -> {
            Patient p = selectedPatient();
            int row = table.getSelectedRow();
            if (p == null || row < 0) return;
            int tid = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete treatment #" + tid + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                hospital.deleteTreatmentFromPatient(p.getId(), tid);
                refreshTable();
                onDataChanged.run();
            }
        });

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.add(toolbar, BorderLayout.NORTH);
        south.add(scroll, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(south, BorderLayout.CENTER);

        refresh();
    }

    private Patient selectedPatient() {
        int idx = patientBox.getSelectedIndex();
        if (patientList == null || idx < 0 || idx >= patientList.size()) return null;
        return patientList.get(idx);
    }

    private String extraInfo(Treatment t) {
        if (t instanceof InternalTreatment it) return "Department #" + it.getDepID();
        if (t instanceof ExternalTreatment et) return "Doctor: " + et.getDoctor().getName() + " | Clinic #" + et.getCliID();
        return "-";
    }

    private void refreshTable() {
        model.setRowCount(0);
        Patient p = selectedPatient();
        if (p == null) return;
        for (Treatment t : hospital.getTreatmentsForPatient(p.getId())) {
            model.addRow(new Object[]{t.getId(), t.getTreatmentType(), t.getDate(), t.getCost(), extraInfo(t)});
        }
    }

    public void refresh() {
        patientList = hospital.getPatients();
        String currentSelection = (String) patientBox.getSelectedItem();
        patientBox.removeAllItems();
        for (Patient p : patientList) {
            patientBox.addItem("#" + p.getId() + " " + p.getName() + " (" + p.getPatientType() + ")");
        }
        if (currentSelection != null) {
            patientBox.setSelectedItem(currentSelection);
        }
        refreshTable();
    }
}
