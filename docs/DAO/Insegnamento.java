package com.example.demo.DAO;

public class Insegnamento {
    private int id_corso;
    private int id_docente;

    public Insegnamento(int id_corso, int id_docente) {
        this.id_corso = id_corso;
        this.id_docente = id_docente;
    }

    public int getIdCorso() {
        return id_corso;
    }

    public int getIdDocente() {
        return id_docente;
    }

    @Override
    public String toString() {
        return "Insegnamento{" +
                "id_corso=" + id_corso +
                ", id_docente=" + id_docente +
                '}';
    }
}
