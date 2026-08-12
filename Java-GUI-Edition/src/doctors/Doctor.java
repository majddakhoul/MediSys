package doctors;

import java.io.Serializable;
import java.time.LocalDate;

// Base Doctor class
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;
    private static int counter = 1;

    private int id;
    private String name;
    private String address;
    private LocalDate birthDate;
    private int salary;

    public Doctor(String name, int salary, LocalDate birthDate, String address) {
        this.id = counter++;
        this.name = name;
        this.salary = salary;
        this.birthDate = birthDate;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getDoctorType() {
        return "Regular";
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                '}';
    }
}
