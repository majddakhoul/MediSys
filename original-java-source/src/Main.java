import doctors.*;
import hospital.*;
import patient.*;
import treatment.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// Main class for full CRUD
public class Main {

    private static Hospital hospital = new Hospital();
    private static final Scanner scanner = new Scanner(System.in);
    private static final String SAVE_FILE = "medisys-data.dat";

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Hospital Management System ===");
            System.out.println("1.  Add Clinic");
            System.out.println("2.  Show Clinics");
            System.out.println("3.  Edit Clinic");
            System.out.println("4.  Delete Clinic");
            System.out.println("5.  Add Doctor");
            System.out.println("6.  Show Doctors");
            System.out.println("7.  Edit Doctor");
            System.out.println("8.  Delete Doctor");
            System.out.println("9.  Add Patient");
            System.out.println("10. Show Patients");
            System.out.println("11. Edit Patient");
            System.out.println("12. Delete Patient");
            System.out.println("13. Add Treatment to Patient");
            System.out.println("14. Delete Treatment from Patient");
            System.out.println("15. Show Treatments of Patient");
            System.out.println("16. Save Data to File");
            System.out.println("17. Load Data from File");
            System.out.println("18. Exit");

            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1 -> addClinic();
                case 2 -> hospital.showAllClinics();
                case 3 -> editClinic();
                case 4 -> deleteClinic();
                case 5 -> addDoctor();
                case 6 -> hospital.showAllDoctors();
                case 7 -> editDoctor();
                case 8 -> deleteDoctor();
                case 9 -> addPatient();
                case 10 -> hospital.showAllPatients();
                case 11 -> editPatient();
                case 12 -> deletePatient();
                case 13 -> addTreatmentToPatient();
                case 14 -> deleteTreatmentFromPatient();
                case 15 -> showTreatmentsOfPatient();
                case 16 -> saveData();
                case 17 -> loadData();
                case 18 -> running = false;
                default -> System.out.println("Invalid choice!");
            }
        }

        scanner.close();
        System.out.println("Exiting...");
    }

    // ------------------ Input helpers (validated, never crash) ------------------

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return LocalDate.parse(line);
            } catch (DateTimeParseException e) {
                System.out.println("Please use the format YYYY-MM-DD.");
            }
        }
    }

    private static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim().toLowerCase();
            if (line.equals("true") || line.equals("yes") || line.equals("y")) return true;
            if (line.equals("false") || line.equals("no") || line.equals("n")) return false;
            System.out.println("Please answer true/false (or yes/no).");
        }
    }

    // ------------------ Clinics ------------------
    private static void addClinic() {
        String name = readLine("Clinic Name: ");
        String type = readLine("Clinic Type: ");
        hospital.addClinic(new Clinic(name, type));
    }

    private static void deleteClinic() {
        int id = readInt("Clinic ID to delete: ");
        hospital.deleteClinic(id);
    }

    private static void editClinic() {
        int id = readInt("Clinic ID to edit: ");
        Clinic clinic = hospital.getClinicById(id);
        if (clinic == null) {
            System.out.println("Clinic not found.");
            return;
        }
        clinic.setName(readLine("New Name (" + clinic.getName() + "): "));
        clinic.setType(readLine("New Type (" + clinic.getType() + "): "));
        System.out.println("Updated: " + clinic);
    }

    // ------------------ Doctors ------------------
    private static void addDoctor() {
        int type = readInt("Doctor type (1-Regular, 2-Contracted, 3-Trainer, 4-Inner): ");
        String name = readLine("Name: ");
        int salary = readInt("Salary: ");
        LocalDate bd = readDate("BirthDate (YYYY-MM-DD): ");
        String address = readLine("Address: ");

        switch (type) {
            case 1 -> hospital.addDoctor(new Doctor(name, salary, bd, address));
            case 2 -> {
                LocalDate cd = readDate("Contract Date (YYYY-MM-DD): ");
                hospital.addDoctor(new ContractedDoctor(name, salary, bd, address, cd));
            }
            case 3 -> {
                LocalDate sd = readDate("Start Date (YYYY-MM-DD): ");
                LocalDate ed = readDate("End Date (YYYY-MM-DD): ");
                hospital.addDoctor(new TrainerDoctor(name, salary, bd, address, sd, ed));
            }
            case 4 -> {
                int dep = readInt("Department Number: ");
                hospital.addDoctor(new InnerDoctor(name, salary, bd, address, dep));
            }
            default -> System.out.println("Unknown doctor type; nothing added.");
        }
    }

    private static void deleteDoctor() {
        int id = readInt("Doctor ID to delete: ");
        hospital.deleteDoctor(id);
    }

    private static void editDoctor() {
        int id = readInt("Doctor ID to edit: ");
        Doctor d = hospital.getDoctorById(id);
        if (d == null) {
            System.out.println("Doctor not found.");
            return;
        }

        d.setName(readLine("New Name (" + d.getName() + "): "));
        d.setSalary(readInt("New Salary (" + d.getSalary() + "): "));
        d.setAddress(readLine("New Address (" + d.getAddress() + "): "));
        d.setBirthDate(readDate("New BirthDate (" + d.getBirthDate() + ", YYYY-MM-DD): "));

        if (d instanceof ContractedDoctor cd) {
            cd.setContractDate(readDate("New Contract Date (" + cd.getContractDate() + "): "));
        } else if (d instanceof TrainerDoctor td) {
            td.setStartDate(readDate("New Start Date (" + td.getStartDate() + "): "));
            td.setEndDate(readDate("New End Date (" + td.getEndDate() + "): "));
        } else if (d instanceof InnerDoctor id2) {
            id2.setNumberOfDepartment(readInt("New Department Number (" + id2.getNumberOfDepartment() + "): "));
        }

        System.out.println("Updated: " + d);
    }

    // ------------------ Patients ------------------
    private static void addPatient() {
        int type = readInt("Patient type (1-Regular, 2-External, 3-Internal): ");
        String name = readLine("Name: ");
        String address = readLine("Address: ");
        LocalDate bd = readDate("BirthDate (YYYY-MM-DD): ");

        switch (type) {
            case 1 -> hospital.addPatient(new Patient(name, address, bd));
            case 2 -> {
                boolean acc = readBoolean("Accepted? (true/false): ");
                LocalDate ad = readDate("Accept Date (YYYY-MM-DD): ");
                hospital.addPatient(new ExternalPatient(name, address, bd, acc, ad));
            }
            case 3 -> hospital.addPatient(new InternalPatient(name, address, bd));
            default -> System.out.println("Unknown patient type; nothing added.");
        }
    }

    private static void deletePatient() {
        int id = readInt("Patient ID to delete: ");
        hospital.deletePatient(id);
    }

    private static void editPatient() {
        int id = readInt("Patient ID to edit: ");
        Patient p = hospital.getPatientById(id);
        if (p == null) {
            System.out.println("Patient not found.");
            return;
        }

        p.setName(readLine("New Name (" + p.getName() + "): "));
        p.setAddress(readLine("New Address (" + p.getAddress() + "): "));
        p.setBirthDate(readDate("New BirthDate (" + p.getBirthDate() + ", YYYY-MM-DD): "));

        if (p instanceof ExternalPatient ep) {
            ep.setAcceptance(readBoolean("Accepted? (" + ep.isAcceptance() + "): "));
            ep.setAcceptDate(readDate("New Accept Date (" + ep.getAcceptDate() + "): "));
        } else if (p instanceof InternalPatient ip) {
            boolean discharged = readBoolean("Discharged? (" + ip.isDischarged() + "): ");
            ip.setDischarge(discharged);
            if (discharged) {
                ip.setDischargeDate(readDate("Discharge Date (YYYY-MM-DD): "));
            }
        }

        System.out.println("Updated: " + p);
    }

    // ------------------ Treatments ------------------
    private static void addTreatmentToPatient() {
        int pid = readInt("Patient ID: ");
        Patient patient = hospital.getPatientById(pid);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        LocalDate td = readDate("Treatment Date (YYYY-MM-DD): ");
        int cost = readInt("Cost: ");

        if (patient instanceof InternalPatient) {
            int dep = readInt("Department ID: ");
            InternalTreatment it = new InternalTreatment(td, cost, dep);
            hospital.addTreatmentToPatient(pid, it);
        } else if (patient instanceof ExternalPatient) {
            int did = readInt("Doctor ID: ");
            Doctor d = hospital.getDoctorById(did);
            if (d == null) {
                System.out.println("Doctor not found.");
                return;
            }
            int cid = readInt("Clinic ID: ");
            Clinic clinic = hospital.getClinicById(cid);
            if (clinic == null) {
                System.out.println("Clinic not found.");
                return;
            }
            ExternalTreatment et = new ExternalTreatment(td, cost, d, cid);
            hospital.addTreatmentToPatient(pid, et);
        } else {
            Treatment t = new Treatment(td, cost);
            hospital.addTreatmentToPatient(pid, t);
        }
    }

    private static void deleteTreatmentFromPatient() {
        int pid = readInt("Patient ID: ");
        int tid = readInt("Treatment ID to delete: ");
        hospital.deleteTreatmentFromPatient(pid, tid);
    }

    private static void showTreatmentsOfPatient() {
        int pid = readInt("Patient ID: ");
        var treatments = hospital.getTreatmentsForPatient(pid);
        if (treatments.isEmpty()) System.out.println("No treatments found.");
        treatments.forEach(System.out::println);
    }

    // ------------------ Persistence ------------------
    private static void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(hospital);
            System.out.println("Saved to " + SAVE_FILE);
        } catch (IOException e) {
            System.out.println("Failed to save: " + e.getMessage());
        }
    }

    private static void loadData() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No save file found at " + SAVE_FILE);
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            hospital = (Hospital) in.readObject();
            System.out.println("Loaded from " + SAVE_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load: " + e.getMessage());
        }
    }
}
