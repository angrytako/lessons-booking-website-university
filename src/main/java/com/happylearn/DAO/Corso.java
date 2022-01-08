package com.happylearn.DAO;

public class Corso {
	private String materia;
	private boolean rimosso;

	public Corso(String materia, boolean rimosso) {
		this.materia = materia;
		this.rimosso = rimosso;     //volendo già a false
	}

	public String getMateria() {
		return materia;
	}

	public boolean isRimosso() {
		return rimosso;
	}

	@Override
	public String toString() {
		return "Corso{" +
				"materia='" + materia + '\'' +
				", rimosso=" + rimosso +
				'}';
	}
}
