package patient;

import treatment.Treatment;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Base Patient class
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;
    private static int counter = 1;

    private int id;
    private String name;
    private String address;
    private LocalDate birthDate;
    private List<Treatment> treatments = new ArrayList<>();

    public Patient(String name, String address, LocalDate birthDate) {
        this.id = counter++;
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
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

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public String getPatientType() {
        return "Regular";
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
