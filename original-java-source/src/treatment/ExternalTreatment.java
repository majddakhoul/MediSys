package treatment;

import doctors.Doctor;

import java.time.LocalDate;

// External Treatment class
public class ExternalTreatment extends Treatment {

    private static final long serialVersionUID = 1L;

    private int cliID;
    private Doctor doctor;

    public ExternalTreatment(LocalDate date, int cost, Doctor doctor, int cliID) {
        super(date, cost);
        this.doctor = doctor;
        this.cliID = cliID;
    }

    public int getCliID() {
        return cliID;
    }

    public void setCliID(int cliID) {
        this.cliID = cliID;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public String getTreatmentType() {
        return "External";
    }

    @Override
    public String toString() {
        return super.toString() + " | Doctor=" + doctor.getName() + " | ClinicID=" + cliID;
    }
}
