package patient;

import treatment.ExternalTreatment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// External Patient class
public class ExternalPatient extends Patient {

    private static final long serialVersionUID = 1L;

    private List<ExternalTreatment> externalTreatments = new ArrayList<>();
    private boolean acceptance = false;
    private LocalDate acceptDate;

    public ExternalPatient(String name, String address, LocalDate birthDate, boolean acceptance, LocalDate acceptDate) {
        super(name, address, birthDate);
        this.acceptance = acceptance;
        this.acceptDate = acceptDate;
    }

    public List<ExternalTreatment> getExternalTreatments() {
        return externalTreatments;
    }

    public boolean isAcceptance() {
        return acceptance;
    }

    public void setAcceptance(boolean acceptance) {
        this.acceptance = acceptance;
    }

    public LocalDate getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(LocalDate acceptDate) {
        this.acceptDate = acceptDate;
    }

    @Override
    public String getPatientType() {
        return "External";
    }

    @Override
    public String toString() {
        return super.toString() + " | ExternalPatient | Accepted=" + acceptance + " on " + acceptDate;
    }
}
