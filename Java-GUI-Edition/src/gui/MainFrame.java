package gui;

import gui.panels.ClinicsPanel;
import gui.panels.DoctorsPanel;
import gui.panels.PatientsPanel;
import gui.panels.TreatmentsPanel;
import hospital.Hospital;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {

    private Hospital hospital = new Hospital();
    private File currentFile = null;

    private ClinicsPanel clinicsPanel;
    private DoctorsPanel doctorsPanel;
    private PatientsPanel patientsPanel;
    private TreatmentsPanel treatmentsPanel;

    private final JLabel statusLabel = new JLabel("Ready.");

    public MainFrame() {
        super("MediSys - Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);

        setJMenuBar(buildMenuBar());

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Theme.PANEL);
        tabs.setForeground(Theme.TEXT);

        clinicsPanel = new ClinicsPanel(hospital, this::refreshAll);
        doctorsPanel = new DoctorsPanel(hospital, this::refreshAll);
        patientsPanel = new PatientsPanel(hospital, this::refreshAll);
        treatmentsPanel = new TreatmentsPanel(hospital, this::refreshAll);

        tabs.addTab("Clinics", clinicsPanel);
        tabs.addTab("Doctors", doctorsPanel);
        tabs.addTab("Patients", patientsPanel);
        tabs.addTab("Treatments", treatmentsPanel);

        statusLabel.setForeground(Theme.MUTED);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(22, 29, 46));
        statusBar.add(statusLabel, BorderLayout.WEST);

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setSize(1100, 720);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        JMenuItem openItem = new JMenuItem("Open...");
        JMenuItem exitItem = new JMenuItem("Exit");

        newItem.addActionListener(e -> newHospital());
        saveItem.addActionListener(e -> save(false));
        saveAsItem.addActionListener(e -> save(true));
        openItem.addActionListener(e -> open());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "MediSys - Java Swing Edition\nHospital management: clinics, doctors, patients, treatments.",
                "About MediSys", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        bar.add(fileMenu);
        bar.add(helpMenu);
        return bar;
    }

    private void newHospital() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Start a new, empty hospital? Unsaved changes will be lost.",
                "New", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        hospital = new Hospital();
        currentFile = null;
        rewireHospital();
        statusLabel.setText("New hospital created.");
    }

    private void rewireHospital() {
        remove(getContentPane().getComponent(0));
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Theme.PANEL);
        tabs.setForeground(Theme.TEXT);

        clinicsPanel = new ClinicsPanel(hospital, this::refreshAll);
        doctorsPanel = new DoctorsPanel(hospital, this::refreshAll);
        patientsPanel = new PatientsPanel(hospital, this::refreshAll);
        treatmentsPanel = new TreatmentsPanel(hospital, this::refreshAll);

        tabs.addTab("Clinics", clinicsPanel);
        tabs.addTab("Doctors", doctorsPanel);
        tabs.addTab("Patients", patientsPanel);
        tabs.addTab("Treatments", treatmentsPanel);

        add(tabs, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void save(boolean forceDialog) {
        File target = currentFile;
        if (forceDialog || target == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("medisys-data.dat"));
            int result = chooser.showSaveDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) return;
            target = chooser.getSelectedFile();
            if (!target.getName().toLowerCase().endsWith(".dat")) {
                target = new File(target.getParentFile(), target.getName() + ".dat");
            }
        }

        try {
            DataManager.save(target, hospital);
            currentFile = target;
            statusLabel.setText("Saved to " + target.getName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to save: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void open() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        try {
            hospital = DataManager.load(chooser.getSelectedFile());
            currentFile = chooser.getSelectedFile();
            rewireHospital();
            statusLabel.setText("Loaded from " + currentFile.getName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAll() {
        clinicsPanel.refresh();
        doctorsPanel.refresh();
        patientsPanel.refresh();
        treatmentsPanel.refresh();
    }
}
