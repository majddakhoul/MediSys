package patient;

import treatment.ExternalTreatment;
import treatment.InternalTreatment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Internal Patient class
public class InternalPatient extends Patient {

    private static final long serialVersionUID = 1L;

    private List<InternalTreatment> internalTreatments = new ArrayList<>();
    private List<ExternalTreatment> externalTreatments = new ArrayList<>();
    private boolean discharge = false;
    private LocalDate dischargeDate;

    public InternalPatient(String name, String address, LocalDate birthDate) {
        super(name, address, birthDate);
    }

    public List<InternalTreatment> getInternalTreatments() {
        return internalTreatments;
    }

    public List<ExternalTreatment> getExternalTreatments() {
        return externalTreatments;
    }

    public boolean isDischarged() {
        return discharge;
    }

    public void setDischarge(boolean discharge) {
        this.discharge = discharge;
    }

    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    @Override
    public String getPatientType() {
        return "Internal";
    }

    @Override
    public String toString() {
        return super.toString() + " | InternalPatient | Discharged=" + discharge;
    }
}
