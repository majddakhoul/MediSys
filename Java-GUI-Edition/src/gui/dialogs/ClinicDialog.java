package gui.dialogs;

import gui.Theme;
import hospital.Clinic;

import javax.swing.*;
import java.awt.*;

public class ClinicDialog extends JDialog {

    private boolean confirmed = false;
    private Clinic result;
    private final Clinic editingExisting;

    private final JTextField nameField = Theme.textField();
    private final JTextField typeField = Theme.textField();

    public ClinicDialog(Window owner, Clinic existing) {
        super(owner, existing == null ? "Add Clinic" : "Edit Clinic", ModalityType.APPLICATION_MODAL);
        this.editingExisting = existing;

        getContentPane().setBackground(Theme.PANEL);
        setLayout(new BorderLayout(10, 10));
        setSize(360, 260);
        setLocationRelativeTo(owner);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));

        addRow(form, "Clinic Name", nameField);
        addRow(form, "Clinic Type", typeField);

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
            String name = nameField.getText().trim();
            String type = typeField.getText().trim();
            if (name.isEmpty() || type.isEmpty()) {
                error.setText("Name and type are both required.");
                return;
            }
            if (editingExisting != null) {
                editingExisting.setName(name);
                editingExisting.setType(type);
                result = editingExisting;
            } else {
                result = new Clinic(name, type);
            }
            confirmed = true;
            dispose();
        });

        add(form, BorderLayout.CENTER);
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(0, 18, 12, 18));
        south.add(error, BorderLayout.WEST);
        south.add(buttons, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);

        if (existing != null) {
            nameField.setText(existing.getName());
            typeField.setText(existing.getType());
        }
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

    public boolean isConfirmed() {
        return confirmed;
    }

    public Clinic getResult() {
        return result;
    }
}
