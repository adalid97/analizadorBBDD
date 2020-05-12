package com.proyectoConexionBBDD;

import java.util.ArrayList;

public class Tab {
	String nombre;
	ArrayList<Columna> columna;

	public Tab(String nombre, ArrayList<Columna> arrayColumnas) {
		super();
		this.nombre = nombre;
		this.columna = arrayColumnas;
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
