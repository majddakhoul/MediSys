package hospital;

import doctors.Doctor;
import patient.ExternalPatient;
import patient.InternalPatient;
import patient.Patient;
import treatment.ExternalTreatment;
import treatment.InternalTreatment;
import treatment.Treatment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Hospital class with full CRUD for Doctors, Patients, Treatments, Clinics
public class Hospital implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Doctor> doctors = new ArrayList<>();
    private List<Patient> patients = new ArrayList<>();
    private List<Clinic> clinics = new ArrayList<>();

    // ----------------- Doctors -----------------
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void deleteDoctor(int id) {
        doctors.removeIf(d -> d.getId() == id);
    }

    public Doctor getDoctorById(int id) {
        return doctors.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void showAllDoctors() {
        if (doctors.isEmpty()) System.out.println("No doctors.");
        doctors.forEach(System.out::println);
    }

    // ----------------- Patients -----------------
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void deletePatient(int id) {
        patients.removeIf(p -> p.getId() == id);
    }

    public Patient getPatientById(int id) {
        return patients.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void showAllPatients() {
        if (patients.isEmpty()) System.out.println("No patients.");
        patients.forEach(System.out::println);
    }

    // ----------------- Clinics -----------------
    public void addClinic(Clinic clinic) {
        clinics.add(clinic);
    }

    public void deleteClinic(int id) {
        clinics.removeIf(c -> c.getId() == id);
    }

    public Clinic getClinicById(int id) {
        return clinics.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public List<Clinic> getClinics() {
        return clinics;
    }

    public void showAllClinics() {
        if (clinics.isEmpty()) System.out.println("No clinics.");
        clinics.forEach(System.out::println);
    }

    // ----------------- Treatments -----------------
    public void addTreatmentToPatient(int patientId, Treatment treatment) {
        Patient patient = getPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        if (patient instanceof InternalPatient ip) {
            if (treatment instanceof InternalTreatment it) {
                ip.getInternalTreatments().add(it);
                ip.getTreatments().add(it);
            } else {
                System.out.println("Invalid treatment type for Internal Patient.");
            }
        } else if (patient instanceof ExternalPatient ep) {
            if (treatment instanceof ExternalTreatment et) {
                ep.getExternalTreatments().add(et);
                ep.getTreatments().add(et);
            } else {
                System.out.println("Invalid treatment type for External Patient.");
            }
        } else {
            patient.getTreatments().add(treatment);
        }
    }

    public void deleteTreatmentFromPatient(int patientId, int treatmentId) {
        Patient patient = getPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        patient.getTreatments().removeIf(t -> t.getId() == treatmentId);

        if (patient instanceof InternalPatient ip) {
            ip.getInternalTreatments().removeIf(t -> t.getId() == treatmentId);
        } else if (patient instanceof ExternalPatient ep) {
            ep.getExternalTreatments().removeIf(t -> t.getId() == treatmentId);
        }
    }

    public List<Treatment> getTreatmentsForPatient(int patientId) {
        Patient patient = getPatientById(patientId);
        if (patient == null) return new ArrayList<>();
        return patient.getTreatments();
    }
}
