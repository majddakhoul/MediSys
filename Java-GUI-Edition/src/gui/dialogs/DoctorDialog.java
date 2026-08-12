package gui.dialogs;

import doctors.*;
import gui.Theme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DoctorDialog extends JDialog {

    private boolean confirmed = false;
    private Doctor result;
    private final Doctor editingExisting;

    private final JComboBox<String> typeBox;
    private final JTextField nameField = Theme.textField();
    private final JTextField salaryField = Theme.textField();
    private final JTextField birthField = Theme.textField();
    private final JTextField addressField = Theme.textField();

    private final JTextField contractDateField = Theme.textField();
    private final JTextField startDateField = Theme.textField();
    private final JTextField endDateField = Theme.textField();
    private final JTextField departmentField = Theme.textField();

    private final CardLayout cards = new CardLayout();
    private final JPanel extraPanel = new JPanel(cards);

    public DoctorDialog(Window owner, Doctor existing) {
        super(owner, existing == null ? "Add Doctor" : "Edit Doctor", ModalityType.APPLICATION_MODAL);
        this.editingExisting = existing;

        getContentPane().setBackground(Theme.PANEL);
        setLayout(new BorderLayout(10, 10));
        setSize(420, 480);
        setLocationRelativeTo(owner);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));

        typeBox = Theme.comboBox(new String[]{"Regular", "Contracted", "Trainer", "Inner"});
        typeBox.addActionListener(e -> cards.show(extraPanel, (String) typeBox.getSelectedItem()));

        addRow(form, "Doctor Type", typeBox);
        addRow(form, "Name", nameField);
        addRow(form, "Salary", salaryField);
        addRow(form, "Birth Date (YYYY-MM-DD)", birthField);
        addRow(form, "Address", addressField);

        extraPanel.setOpaque(false);

        JPanel regularCard = new JPanel();
        regularCard.setOpaque(false);

        JPanel contractedCard = new JPanel();
        contractedCard.setOpaque(false);
        contractedCard.setLayout(new BoxLayout(contractedCard, BoxLayout.Y_AXIS));
        addRow(contractedCard, "Contract Date (YYYY-MM-DD)", contractDateField);

        JPanel trainerCard = new JPanel();
        trainerCard.setOpaque(false);
        trainerCard.setLayout(new BoxLayout(trainerCard, BoxLayout.Y_AXIS));
        addRow(trainerCard, "Start Date (YYYY-MM-DD)", startDateField);
        addRow(trainerCard, "End Date (YYYY-MM-DD)", endDateField);

        JPanel innerCard = new JPanel();
        innerCard.setOpaque(false);
        innerCard.setLayout(new BoxLayout(innerCard, BoxLayout.Y_AXIS));
        addRow(innerCard, "Department Number", departmentField);

        extraPanel.add(regularCard, "Regular");
        extraPanel.add(contractedCard, "Contracted");
        extraPanel.add(trainerCard, "Trainer");
        extraPanel.add(innerCard, "Inner");
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

    private void prefill(Doctor d) {
        typeBox.setSelectedItem(d.getDoctorType());
        nameField.setText(d.getName());
        salaryField.setText(String.valueOf(d.getSalary()));
        birthField.setText(String.valueOf(d.getBirthDate()));
        addressField.setText(d.getAddress());

        if (d instanceof ContractedDoctor cd) contractDateField.setText(String.valueOf(cd.getContractDate()));
        else if (d instanceof TrainerDoctor td) {
            startDateField.setText(String.valueOf(td.getStartDate()));
            endDateField.setText(String.valueOf(td.getEndDate()));
        } else if (d instanceof InnerDoctor id) {
            departmentField.setText(String.valueOf(id.getNumberOfDepartment()));
        }
    }

    private String trySubmit() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        if (name.isEmpty()) return "Name is required.";
        if (address.isEmpty()) return "Address is required.";

        int salary;
        try {
            salary = Integer.parseInt(salaryField.getText().trim());
        } catch (NumberFormatException e) {
            return "Salary must be a whole number.";
        }

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
                editingExisting.setSalary(salary);
                editingExisting.setAddress(address);
                editingExisting.setBirthDate(birth);

                if (editingExisting instanceof ContractedDoctor cd) {
                    cd.setContractDate(LocalDate.parse(contractDateField.getText().trim()));
                } else if (editingExisting instanceof TrainerDoctor td) {
                    td.setStartDate(LocalDate.parse(startDateField.getText().trim()));
                    td.setEndDate(LocalDate.parse(endDateField.getText().trim()));
                } else if (editingExisting instanceof InnerDoctor id) {
                    id.setNumberOfDepartment(Integer.parseInt(departmentField.getText().trim()));
                }
                result = editingExisting;
                return null;
            }

            result = switch (type) {
                case "Contracted" -> new ContractedDoctor(name, salary, birth, address,
                        LocalDate.parse(contractDateField.getText().trim()));
                case "Trainer" -> new TrainerDoctor(name, salary, birth, address,
                        LocalDate.parse(startDateField.getText().trim()),
                        LocalDate.parse(endDateField.getText().trim()));
                case "Inner" -> new InnerDoctor(name, salary, birth, address,
                        Integer.parseInt(departmentField.getText().trim()));
                default -> new Doctor(name, salary, birth, address);
            };
            return null;
        } catch (DateTimeParseException e) {
            return "Extra dates must be YYYY-MM-DD.";
        } catch (NumberFormatException e) {
            return "Department number must be a whole number.";
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Doctor getResult() {
        return result;
    }
}
