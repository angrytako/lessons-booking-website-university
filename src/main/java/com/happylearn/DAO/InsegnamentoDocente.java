package com.happylearn.DAO;

import java.util.ArrayList;

public class InsegnamentoDocente {
    private int id;
    private ArrayList<Corso> corsi;

    public InsegnamentoDocente(int id, ArrayList<Corso> corsi) {
        this.id = id;
        this.corsi = corsi;
    }

    public ArrayList<Corso> getCorsi() {
        return corsi;
    }
}
