package DAO;

public class Corso {
    private String materia;

    public Corso(String materia) {
        this.materia = materia;
    }

    public String getMateria() {
        return materia;
    }

    @Override
    public String toString() {
        return "Corso{" +
                "materia='" + materia + '\'' +
                '}';
    }
}
