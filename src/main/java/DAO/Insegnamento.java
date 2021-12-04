package DAO;

public class Insegnamento {
    private String corso;
    private int id_docente;

    public Insegnamento(String corso, int id_docente) {
        this.corso = corso;
        this.id_docente = id_docente;
    }

    public String getCorso() {
        return corso;
    }

    public int getIdDocente() {
        return id_docente;
    }

    @Override
    public String toString() {
        return "Insegnamento{" +
                "corso=" + corso +
                ", id_docente=" + id_docente +
                '}';
    }
}
