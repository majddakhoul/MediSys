package gui.dialogs;

import doctors.Doctor;
import gui.Theme;
import hospital.Clinic;
import hospital.Hospital;
import patient.ExternalPatient;
import patient.InternalPatient;
import patient.Patient;
import treatment.ExternalTreatment;
import treatment.InternalTreatment;
import treatment.Treatment;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TreatmentDialog extends JDialog {

    private boolean confirmed = false;
    private Treatment result;

    private final JTextField dateField = Theme.textField();
    private final JTextField costField = Theme.textField();
    private final JTextField departmentField = Theme.textField();
    private final JComboBox<String> doctorBox;
    private final JComboBox<String> clinicBox;

    private final List<Doctor> doctors;
    private final List<Clinic> clinics;

    public TreatmentDialog(Window owner, Hospital hospital, Patient patient) {
        super(owner, "Add Treatment", ModalityType.APPLICATION_MODAL);

        this.doctors = hospital.getDoctors();
        this.clinics = hospital.getClinics();

        getContentPane().setBackground(Theme.PANEL);
        setLayout(new BorderLayout(10, 10));
        setSize(400, 380);
        setLocationRelativeTo(owner);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));

        JLabel patientLabel = Theme.fieldLabel("Patient: #" + patient.getId() + " " + patient.getName()
                + " (" + patient.getPatientType() + ")");
        patientLabel.setForeground(Theme.ACCENT);
        patientLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(patientLabel);
        form.add(Box.createVerticalStrut(12));

        addRow(form, "Treatment Date (YYYY-MM-DD)", dateField);
        addRow(form, "Cost", costField);

        String[] doctorNames = doctors.stream().map(d -> "#" + d.getId() + " " + d.getName()).toArray(String[]::new);
        String[] clinicNames = clinics.stream().map(c -> "#" + c.getId() + " " + c.getName()).toArray(String[]::new);
        doctorBox = Theme.comboBox(doctorNames.length == 0 ? new String[]{"(no doctors yet)"} : doctorNames);
        clinicBox = Theme.comboBox(clinicNames.length == 0 ? new String[]{"(no clinics yet)"} : clinicNames);

        if (patient instanceof InternalPatient) {
            addRow(form, "Department ID", departmentField);
        } else if (patient instanceof ExternalPatient) {
            addRow(form, "Doctor", doctorBox);
            addRow(form, "Clinic", clinicBox);
        }

        JLabel error = Theme.fieldLabel(" ");
        error.setForeground(Theme.BAD);
        error.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        JButton cancel = Theme.button("Cancel");
        JButton ok = Theme.primaryButton("Add");
        buttons.add(cancel);
        buttons.add(ok);

        cancel.addActionListener(e -> dispose());
        ok.addActionListener(e -> {
            String err = trySubmit(patient);
            if (err != null) {
                error.setText(err);
            } else {
                confirmed = true;
                dispose();
            }
        });

        add(form, BorderLayout.CENTER);
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(0, 18, 12, 18));
        south.add(error, BorderLayout.WEST);
        south.add(buttons, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);
    }

    private void addRow(JPanel parent, String label, JComponent field) {
        JLabel l = Theme.fieldLabel(label);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        parent.add(l);
        parent.add(field);
        parent.add(Box.createVerticalStrut(10));
    }

    private String trySubmit(Patient patient) {
        LocalDate date;
        try {
            date = LocalDate.parse(dateField.getText().trim());
        } catch (DateTimeParseException e) {
            return "Date must be YYYY-MM-DD.";
        }

        int cost;
        try {
            cost = Integer.parseInt(costField.getText().trim());
        } catch (NumberFormatException e) {
            return "Cost must be a whole number.";
        }

        if (patient instanceof InternalPatient) {
            int dep;
            try {
                dep = Integer.parseInt(departmentField.getText().trim());
            } catch (NumberFormatException e) {
                return "Department ID must be a whole number.";
            }
            result = new InternalTreatment(date, cost, dep);
            return null;
        }

        if (patient instanceof ExternalPatient) {
            if (doctors.isEmpty()) return "Add a doctor first.";
            if (clinics.isEmpty()) return "Add a clinic first.";
            Doctor doctor = doctors.get(doctorBox.getSelectedIndex());
            Clinic clinic = clinics.get(clinicBox.getSelectedIndex());
            result = new ExternalTreatment(date, cost, doctor, clinic.getId());
            return null;
        }

        result = new Treatment(date, cost);
        return null;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Treatment getResult() {
        return result;
    }
}
