package hospital;

import java.io.Serializable;

// Clinic class
public class Clinic implements Serializable {

    private static final long serialVersionUID = 1L;
    private static int counter = 1;

    private int id;
    private String name;
    private String type;

    public Clinic(String name, String type) {
        this.id = counter++;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Clinic{" + "id=" + id + ", name='" + name + '\'' + ", type='" + type + '\'' + '}';
    }
}
