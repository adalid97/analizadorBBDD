package com.proyectoConexionBBDD;

public class Tabla {
	private String campo;
	private String tipo;
	private String valor;
	private String pk;
	private String fk;
	private String referenciaFK;

	public Tabla() {
		super();
	}

	public Tabla(String campo, String tipo, String valor) {
		super();
		this.campo = campo;
		this.tipo = tipo;
		this.valor = valor;
	}

	public Tabla(String pk) {
		super();
		this.pk = pk;
	}

	public Tabla(String fk, String referenciaFK) {
		super();
		this.fk = fk;
		this.referenciaFK = referenciaFK;
	}

	public String getFk() {
		return fk;
	}

	public void setFk(String fk) {
		this.fk = fk;
	}

	public String getReferenciaFK() {
		return referenciaFK;
	}

	public void setReferenciaFK(String referenciaFK) {
		this.referenciaFK = referenciaFK;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
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
