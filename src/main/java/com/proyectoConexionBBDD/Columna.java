package com.proyectoConexionBBDD;

public class Columna {
	private String campo;
	private String tipo;
	private String valor;

	public Columna(String campo, String tipo, String valor) {
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

	@Override
	public String toString() {
		return "Columna [campo=" + campo + ", tipo=" + tipo + ", valor=" + valor + "]";
	}

}
