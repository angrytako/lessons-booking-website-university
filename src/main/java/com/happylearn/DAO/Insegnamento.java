package com.happylearn.DAO;

public class Insegnamento {
	private String corso;
	private int id_docente;
	private boolean rimosso;

	public Insegnamento(String corso, int id_docente, boolean rimosso) {
		this.corso = corso;
		this.id_docente = id_docente;
		this.rimosso = rimosso;
	}

	public String getCorso() {
		return corso;
	}

	public int getIdDocente() {
		return id_docente;
	}

	public boolean isRimosso() {
		return rimosso;
	}

	@Override
	public String toString() {
		return "Insegnamento{" +
				"corso='" + corso + '\'' +
				", id_docente=" + id_docente +
				", rimosso=" + rimosso +
				'}';
	}
}
