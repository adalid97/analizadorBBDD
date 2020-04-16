package com.proyectoConexionBBDD;

public class FormularioConexion {
	private String user;
	private String pass;
	private String h;
	private String p;
	private String b;

	public FormularioConexion(String user, String pass, String h, String p, String b) {
		super();
		this.user = user;
		this.pass = pass;
		this.h = h;
		this.p = p;
		this.b = b;
	}

	public FormularioConexion() {
		super();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

}
