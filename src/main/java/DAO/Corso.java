package DAO;

public class Corso {
    private int id;
    private String materia;

    public Corso(int id, String materia) {
        this.id = id;
        this.materia = materia;
    }

    public int getId() {
        return id;
    }

    public String getMateria() {
        return materia;
    }

    @Override
    public String toString() {
        return "Corso{" +
                "id=" + id +
                ", materia='" + materia + '\'' +
                '}';
    }
}
