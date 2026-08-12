package doctors;

import java.time.LocalDate;

// Doctor assigned to departments
public class InnerDoctor extends Doctor {

    private static final long serialVersionUID = 1L;

    private int numberOfDepartment;

    public InnerDoctor(String name, int salary, LocalDate birthDate, String address, int numberOfDepartment) {
        super(name, salary, birthDate, address);
        this.numberOfDepartment = numberOfDepartment;
    }

    public int getNumberOfDepartment() {
        return numberOfDepartment;
    }

    public void setNumberOfDepartment(int numberOfDepartment) {
        this.numberOfDepartment = numberOfDepartment;
    }

    @Override
    public String getDoctorType() {
        return "Inner";
    }

    @Override
    public String toString() {
        return super.toString() + " | Department=" + numberOfDepartment;
    }
}
