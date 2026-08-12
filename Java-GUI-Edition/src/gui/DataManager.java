package gui;

import hospital.Hospital;

import java.io.*;

public class DataManager {

    public static void save(File file, Hospital hospital) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(hospital);
        }
    }

    public static Hospital load(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Hospital) in.readObject();
        }
    }
}
