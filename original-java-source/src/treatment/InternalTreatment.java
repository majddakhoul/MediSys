package treatment;

import doctors.Doctor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Internal Treatment class
public class InternalTreatment extends Treatment {

    private static final long serialVersionUID = 1L;

    private int depID;
    private List<Doctor> doctors = new ArrayList<>();

    public InternalTreatment(LocalDate date, int cost, int depID) {
        super(date, cost);
        this.depID = depID;
    }

    public int getDepID() {
        return depID;
    }

    public void setDepID(int depID) {
        this.depID = depID;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    @Override
    public String getTreatmentType() {
        return "Internal";
    }

    @Override
    public String toString() {
        return super.toString() + " | DepID=" + depID + " | Doctors=" + doctors.size();
    }
}
