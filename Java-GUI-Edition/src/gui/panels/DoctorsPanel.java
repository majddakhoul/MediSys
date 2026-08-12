package gui.panels;

import doctors.*;
import gui.Theme;
import gui.dialogs.DoctorDialog;
import hospital.Hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DoctorsPanel extends JPanel {

    private final Hospital hospital;
    private final Runnable onDataChanged;
    private final DefaultTableModel model;
    private final JTable table;

    public DoctorsPanel(Hospital hospital, Runnable onDataChanged) {
        this.hospital = hospital;
        this.onDataChanged = onDataChanged;

        setLayout(new BorderLayout(0, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        model = new DefaultTableModel(new Object[]{"ID", "Type", "Name", "Salary", "Birth Date", "Address", "Extra"}, 0) {
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
        JButton addBtn = Theme.primaryButton("Add Doctor");
        JButton editBtn = Theme.button("Edit");
        JButton deleteBtn = Theme.dangerButton("Delete");
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);

        addBtn.addActionListener(e -> {
            DoctorDialog dlg = new DoctorDialog(SwingUtilities.getWindowAncestor(this), null);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                hospital.addDoctor(dlg.getResult());
                refresh();
                onDataChanged.run();
            }
        });

        editBtn.addActionListener(e -> {
            Doctor d = selected();
            if (d == null) return;
            DoctorDialog dlg = new DoctorDialog(SwingUtilities.getWindowAncestor(this), d);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                refresh();
                onDataChanged.run();
            }
        });

        deleteBtn.addActionListener(e -> {
            Doctor d = selected();
            if (d == null) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Delete doctor #" + d.getId() + " (" + d.getName() + ")?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                hospital.deleteDoctor(d.getId());
                refresh();
                onDataChanged.run();
            }
        });

        add(toolbar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    private Doctor selected() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int id = (int) model.getValueAt(row, 0);
        return hospital.getDoctorById(id);
    }

    private String extraInfo(Doctor d) {
        if (d instanceof ContractedDoctor cd) return "Contract: " + cd.getContractDate();
        if (d instanceof TrainerDoctor td) return "Training: " + td.getStartDate() + " to " + td.getEndDate();
        if (d instanceof InnerDoctor id) return "Department: " + id.getNumberOfDepartment();
        return "-";
    }

    public void refresh() {
        model.setRowCount(0);
        for (Doctor d : hospital.getDoctors()) {
            model.addRow(new Object[]{d.getId(), d.getDoctorType(), d.getName(), d.getSalary(),
                    d.getBirthDate(), d.getAddress(), extraInfo(d)});
        }
    }
}
