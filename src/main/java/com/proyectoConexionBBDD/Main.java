package com.proyectoConexionBBDD;

import java.awt.EventQueue;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws SQLException {
		/*
		 * JOptionPane.showMessageDialog(null, "Hola, Bienvenid@!");
		 * 
		 * String[] arquitecturas = { "Oracle DataBase", "SQL-Server" };
		 * 
		 * String arquitectura = (String) JOptionPane.showInputDialog(null,
		 * "Seleccione una arquitectura", "Arquitectura", JOptionPane.DEFAULT_OPTION,
		 * null, arquitecturas, arquitecturas[0]);
		 * 
		 * String usuario = JOptionPane.showInputDialog("User");
		 * 
		 * String contraseña = JOptionPane.showInputDialog("Password");
		 * 
		 * String servidor = JOptionPane.showInputDialog("Host");
		 * 
		 * String puerto = JOptionPane.showInputDialog("Port");
		 * 
		 * String bd = JOptionPane.showInputDialog("DataBase");
		 * 
		 * if (arquitectura == arquitecturas[0]) { ConexionOracleDatabase conn = new
		 * ConexionOracleDatabase(usuario, contraseña, servidor, puerto, bd); if
		 * (conn.conectar() != null) {
		 * 
		 * JOptionPane.showMessageDialog(null, "Conexión exitosa!"); } else {
		 * JOptionPane.showMessageDialog(null, "Error al conectarse a la BD", "Error",
		 * JOptionPane.WARNING_MESSAGE); } conn.consultaVersion();
		 * 
		 * } if (arquitectura == arquitecturas[1]) { ConexionSQLServer conn = new
		 * ConexionSQLServer(usuario, contraseña, servidor, puerto, bd); if
		 * (conn.conectar() != null) {
		 * 
		 * JOptionPane.showMessageDialog(null, "Conexión exitosa!"); } else {
		 * JOptionPane.showMessageDialog(null, "Error al conectarse a la BD", "Error",
		 * JOptionPane.WARNING_MESSAGE); } conn.consultaVersion(); }
		 */

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
