package treatment;

import java.io.Serializable;
import java.time.LocalDate;

// Base Treatment class
public class Treatment implements Serializable {

    private static final long serialVersionUID = 1L;
    private static int counter = 1;

    private int id;
    private LocalDate date;
    private int cost;

    public Treatment(LocalDate date, int cost) {
        this.id = counter++;
        this.date = date;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getTreatmentType() {
        return "General";
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "id=" + id +
                ", date=" + date +
                ", cost=" + cost +
                '}';
    }
}
