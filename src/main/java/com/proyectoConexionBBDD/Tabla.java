package com.proyectoConexionBBDD;

public class Tabla {
	private String campo;
	private String tipo;
	private String valor;

	public Tabla() {
		super();
	}

	public Tabla(String campo, String tipo, String valor) {
		super();
		this.campo = campo;
		this.tipo = tipo;
		this.valor = valor;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
