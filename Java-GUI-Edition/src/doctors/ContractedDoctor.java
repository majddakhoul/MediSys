package doctors;

import java.time.LocalDate;

// Doctor with contract date
public class ContractedDoctor extends Doctor {

    private static final long serialVersionUID = 1L;

    private LocalDate contractDate;

    public ContractedDoctor(String name, int salary, LocalDate birthDate, String address, LocalDate contractDate) {
        super(name, salary, birthDate, address);
        this.contractDate = contractDate;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    @Override
    public String getDoctorType() {
        return "Contracted";
    }

    @Override
    public String toString() {
        return super.toString() + " | ContractDate=" + contractDate;
    }
}
