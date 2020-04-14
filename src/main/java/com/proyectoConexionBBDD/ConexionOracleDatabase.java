package com.proyectoConexionBBDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionOracleDatabase {
	private String usuario;
	private String contraseña;
	private String servidor;
	private String puerto;
	private String bd;

	public ConexionOracleDatabase(String usuario, String contraseña, String servidor, String puerto, String bd) {
		super();
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.servidor = servidor;
		this.puerto = puerto;
		this.bd = bd;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public String getServidor() {
		return servidor;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public String getPuerto() {
		return puerto;
	}

	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public String getBd() {
		return bd;
	}

	public void setBd(String bd) {
		this.bd = bd;
	}

	public Connection conectar() {
		Connection conexion = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conexion = DriverManager.getConnection(
					"jdbc:oracle:thin:@" + getServidor() + ":" + getPuerto() + ":" + getBd() + "",
					"" + getUsuario() + "", "" + getContraseña() + "");

		} catch (Exception e) {
		}
		return conexion;
	}

	public String consultaVersion() throws SQLException {
		String resultado = null;
		if (conectar() != null) {
			String sql = "select value from v$system_parameter where name = 'compatible'";

			Statement statement = conectar().createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				resultado = result.getString(1);
			}
		}
		return resultado;
	}
}