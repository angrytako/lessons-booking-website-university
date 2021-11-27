package com.example.demo.DAO;

public class Utente {
    private int id;
    private String account;
    private String password;
    private int ruolo;

    public Utente(int id, String account, String password, int ruolo) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.ruolo = ruolo;                     // 1: amministratore, 2: cliente
    }

    public int getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public int getRuolo() {
        return ruolo;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", ruolo=" + ruolo +
                '}';
    }
}
