package gui.dialogs;

import gui.Theme;
import patient.ExternalPatient;
import patient.InternalPatient;
import patient.Patient;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PatientDialog extends JDialog {

    private boolean confirmed = false;
    private Patient result;
    private final Patient editingExisting;

    private final JComboBox<String> typeBox;
    private final JTextField nameField = Theme.textField();
    private final JTextField addressField = Theme.textField();
    private final JTextField birthField = Theme.textField();

    private final JCheckBox acceptedBox = new JCheckBox("Accepted");
    private final JTextField acceptDateField = Theme.textField();

    private final JCheckBox dischargedBox = new JCheckBox("Discharged");
    private final JTextField dischargeDateField = Theme.textField();

    private final CardLayout cards = new CardLayout();
    private final JPanel extraPanel = new JPanel(cards);

    public PatientDialog(Window owner, Patient existing) {
        super(owner, existing == null ? "Add Patient" : "Edit Patient", ModalityType.APPLICATION_MODAL);
        this.editingExisting = existing;

        getContentPane().setBackground(Theme.PANEL);
        setLayout(new BorderLayout(10, 10));
        setSize(420, 440);
        setLocationRelativeTo(owner);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));

        typeBox = Theme.comboBox(new String[]{"Regular", "External", "Internal"});
        typeBox.addActionListener(e -> cards.show(extraPanel, (String) typeBox.getSelectedItem()));

        addRow(form, "Patient Type", typeBox);
        addRow(form, "Name", nameField);
        addRow(form, "Address", addressField);
        addRow(form, "Birth Date (YYYY-MM-DD)", birthField);

        extraPanel.setOpaque(false);

        JPanel regularCard = new JPanel();
        regularCard.setOpaque(false);

        JPanel externalCard = new JPanel();
        externalCard.setOpaque(false);
        externalCard.setLayout(new BoxLayout(externalCard, BoxLayout.Y_AXIS));
        acceptedBox.setOpaque(false);
        acceptedBox.setForeground(Theme.TEXT);
        acceptedBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        externalCard.add(acceptedBox);
        externalCard.add(Box.createVerticalStrut(6));
        addRow(externalCard, "Accept Date (YYYY-MM-DD)", acceptDateField);

        JPanel internalCard = new JPanel();
        internalCard.setOpaque(false);
        internalCard.setLayout(new BoxLayout(internalCard, BoxLayout.Y_AXIS));
        dischargedBox.setOpaque(false);
        dischargedBox.setForeground(Theme.TEXT);
        dischargedBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        internalCard.add(dischargedBox);
        internalCard.add(Box.createVerticalStrut(6));
        addRow(internalCard, "Discharge Date (YYYY-MM-DD, if discharged)", dischargeDateField);

        extraPanel.add(regularCard, "Regular");
        extraPanel.add(externalCard, "External");
        extraPanel.add(internalCard, "Internal");
        extraPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(extraPanel);

        JLabel error = Theme.fieldLabel(" ");
        error.setForeground(Theme.BAD);
        error.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        JButton cancel = Theme.button("Cancel");
        JButton ok = Theme.primaryButton(existing == null ? "Add" : "Save");
        buttons.add(cancel);
        buttons.add(ok);

        cancel.addActionListener(e -> dispose());
        ok.addActionListener(e -> {
            String err = trySubmit();
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

        if (existing != null) {
            prefill(existing);
            typeBox.setEnabled(false);
        }

        cards.show(extraPanel, (String) typeBox.getSelectedItem());
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

    private void prefill(Patient p) {
        typeBox.setSelectedItem(p.getPatientType());
        nameField.setText(p.getName());
        addressField.setText(p.getAddress());
        birthField.setText(String.valueOf(p.getBirthDate()));

        if (p instanceof ExternalPatient ep) {
            acceptedBox.setSelected(ep.isAcceptance());
            if (ep.getAcceptDate() != null) acceptDateField.setText(String.valueOf(ep.getAcceptDate()));
        } else if (p instanceof InternalPatient ip) {
            dischargedBox.setSelected(ip.isDischarged());
            if (ip.getDischargeDate() != null) dischargeDateField.setText(String.valueOf(ip.getDischargeDate()));
        }
    }

    private String trySubmit() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        if (name.isEmpty()) return "Name is required.";
        if (address.isEmpty()) return "Address is required.";

        LocalDate birth;
        try {
            birth = LocalDate.parse(birthField.getText().trim());
        } catch (DateTimeParseException e) {
            return "Birth date must be YYYY-MM-DD.";
        }

        String type = (String) typeBox.getSelectedItem();

        try {
            if (editingExisting != null) {
                editingExisting.setName(name);
                editingExisting.setAddress(address);
                editingExisting.setBirthDate(birth);

                if (editingExisting instanceof ExternalPatient ep) {
                    ep.setAcceptance(acceptedBox.isSelected());
                    String txt = acceptDateField.getText().trim();
                    if (!txt.isEmpty()) ep.setAcceptDate(LocalDate.parse(txt));
                } else if (editingExisting instanceof InternalPatient ip) {
                    ip.setDischarge(dischargedBox.isSelected());
                    String txt = dischargeDateField.getText().trim();
                    if (dischargedBox.isSelected() && !txt.isEmpty()) ip.setDischargeDate(LocalDate.parse(txt));
                }
                result = editingExisting;
                return null;
            }

            result = switch (type) {
                case "External" -> new ExternalPatient(name, address, birth,
                        acceptedBox.isSelected(),
                        acceptDateField.getText().trim().isEmpty() ? null : LocalDate.parse(acceptDateField.getText().trim()));
                case "Internal" -> new InternalPatient(name, address, birth);
                default -> new Patient(name, address, birth);
            };
            return null;
        } catch (DateTimeParseException e) {
            return "Extra dates must be YYYY-MM-DD.";
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Patient getResult() {
        return result;
    }
}
