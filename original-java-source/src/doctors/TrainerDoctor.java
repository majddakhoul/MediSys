package doctors;

import java.time.LocalDate;

// Doctor in training program
public class TrainerDoctor extends Doctor {

    private static final long serialVersionUID = 1L;

    private LocalDate startDate;
    private LocalDate endDate;

    public TrainerDoctor(String name, int salary, LocalDate birthDate, String address, LocalDate startDate, LocalDate endDate) {
        super(name, salary, birthDate, address);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getDoctorType() {
        return "Trainer";
    }

    @Override
    public String toString() {
        return super.toString() + " | Training: " + startDate + " to " + endDate;
    }
}
