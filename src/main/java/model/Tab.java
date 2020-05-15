package model;

import java.util.ArrayList;

public class Tab {
	private String nombre;
	private ArrayList<Columna> columna;
	private ArrayList<String> pk;
	private ArrayList<String> fk;
	private ArrayList<String> trigger;

	public Tab(String nombre, ArrayList<Columna> arrayColumnas, ArrayList<String> pk) {
		super();
		this.nombre = nombre;
		this.columna = arrayColumnas;
		this.pk = pk;
	}

	public Tab(String nombre, ArrayList<Columna> columna) {
		super();
		this.nombre = nombre;
		this.columna = columna;
	}

	public Tab(String nombre, ArrayList<Columna> columna, ArrayList<String> pk, ArrayList<String> fk) {
		super();
		this.nombre = nombre;
		this.columna = columna;
		this.pk = pk;
		this.fk = fk;
	}

	public Tab(String nombre, ArrayList<Columna> columna, ArrayList<String> pk, ArrayList<String> fk,
			ArrayList<String> trigger) {
		super();
		this.nombre = nombre;
		this.columna = columna;
		this.pk = pk;
		this.fk = fk;
		this.trigger = trigger;
	}

	public ArrayList<String> getTrigger() {
		return trigger;
	}

	public void setTrigger(ArrayList<String> trigger) {
		this.trigger = trigger;
	}

	public ArrayList<String> getFk() {
		return fk;
	}

	public void setFk(ArrayList<String> fk) {
		this.fk = fk;
	}

	public ArrayList<String> getPk() {
		return pk;
	}

	public void setPk(ArrayList<String> pk) {
		this.pk = pk;
	}

	public ArrayList<Columna> getColumna() {
		return columna;
	}

	public void setColumna(ArrayList<Columna> columna) {
		this.columna = columna;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Tab [nombre=" + nombre + "]";
	}

}
