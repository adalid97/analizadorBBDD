package com.proyectoConexionBBDD;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import exceptions.ExcepcionPersonalizada;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField host;
	private JTextField port;
	private JTextField bd;
	private JButton conectar;
	private JPasswordField contraseña;
	private JTextField usuario;
	private final ButtonGroup arquitectura = new ButtonGroup();
	private JLabel validar;
	private String panel = "";
	private JMenu mnNewMenu;
	private JMenuItem mntmNewMenuItem;
	private Boolean resultadoConexion = false;
	final TextArea textPane = new TextArea();
	final JRadioButton oracle = new JRadioButton("OracleDataBase");
	final JRadioButton sqlServer = new JRadioButton("SQL-Server");
	private JMenuItem mntmNewMenuItem_2;
	private JMenuItem mntmNewMenuItem_3;
	private JMenuItem mntmNewMenuItem_4;
	private JMenuItem mntmNewMenuItem_5;

	public App() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 682, 600);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setToolTipText("gfdfgfd");
		setJMenuBar(menuBar);

		mnNewMenu = new JMenu("Conexión");
		menuBar.add(mnNewMenu);

		mntmNewMenuItem = new JMenuItem("Guardar Conexión");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (resultadoConexion == true) {

					JFileChooser fileChooser = new JFileChooser();
					int seleccion = fileChooser.showSaveDialog(null);
					if (seleccion == JFileChooser.APPROVE_OPTION) {
						File ficheroSeleccionado = fileChooser.getSelectedFile();
						FileWriter fichero = null;
						try {
							fichero = new FileWriter(ficheroSeleccionado);

							if (oracle.isSelected()) {
								fichero.write("oracle\n" + String.valueOf(usuario.getText()) + "\n"
										+ String.valueOf(contraseña.getPassword()) + "\n" + host.getText() + "\n"
										+ port.getText() + "\n" + bd.getText());
							} else if (sqlServer.isSelected()) {
								fichero.write("sqlServer\n" + String.valueOf(usuario.getText()) + "\n"
										+ String.valueOf(contraseña.getPassword()) + "\n" + host.getText() + "\n"
										+ port.getText() + "\n" + bd.getText());
							}

							fichero.close();

							panel = "Se han exportado los datos correctamente.";

						} catch (IOException e) {
							panel = "Error al exportar los datos. Error: " + e.getMessage();
						}

					}

				} else {
					panel = "Por favor, establece una conexión y vuelve a intentarlo.";
				}

				textPane.setText(panel);
			}
		});
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Cargar Datos Conexión");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();
				int seleccion = fileChooser.showOpenDialog(null);
				if (seleccion == JFileChooser.APPROVE_OPTION) {
					File fichero = fileChooser.getSelectedFile();
					Scanner s = null;

					try {
						s = new Scanner(fichero);

						ArrayList<String> contenidoArchivo = new ArrayList<String>();
						while (s.hasNextLine()) {
							contenidoArchivo.add(s.nextLine());
						}

						if ("oracle".equals(contenidoArchivo.get(0))) {
							oracle.setSelected(true);
						} else if ("sqlServer".equals(contenidoArchivo.get(0))) {
							sqlServer.setSelected(true);
						}

						usuario.setText(contenidoArchivo.get(1));
						contraseña.setText(contenidoArchivo.get(2));
						host.setText(contenidoArchivo.get(3));
						port.setText(contenidoArchivo.get(4));
						bd.setText(contenidoArchivo.get(5));

					} catch (Exception ex) {
						System.out.println("Mensaje: " + ex.getMessage());
					}

				}

				textPane.setText(panel);

			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);

		JMenu mnNewMenu_1 = new JMenu("Datos");
		menuBar.add(mnNewMenu_1);

		mntmNewMenuItem_2 = new JMenuItem("Mostrar Datos");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel = "";
				ConexionSQLServer conn = new ConexionSQLServer(usuario.getText(),
						String.valueOf(contraseña.getPassword()), host.getText(), port.getText(), bd.getText());

				if (conn.conectar() != null) {
					String sqlTablas = "SELECT CAST(table_name as varchar)  FROM INFORMATION_SCHEMA.TABLES ORDER BY 1";

					Statement statement = null;
					try {
						statement = conn.conectar().createStatement();
						ResultSet result = statement.executeQuery(sqlTablas);

//						while (result.next()) {
//							for (int x = 1; x <= result.getMetaData().getColumnCount(); x++)
//
//								panel += result.getString(x) + "\t";
//
//							panel += "\n";
//						}

						ArrayList<String> tablas = new ArrayList<String>();
						while (result.next()) {

							for (int x = 1; x <= result.getMetaData().getColumnCount(); x++)

								tablas.add(result.getString(x) + "");

						}

						for (int i = 0; i < tablas.size(); i++) {
							String sqlColumnas = "SELECT COLUMN_NAME,DATA_TYPE, CHARACTER_MAXIMUM_LENGTH\r\n"
									+ "FROM Information_Schema.Columns\r\n" + "WHERE TABLE_NAME = '" + tablas.get(i)
									+ "';";

							ResultSet result1 = statement.executeQuery(sqlColumnas);

							panel += "\n\t" + tablas.get(i) + "\n";
							while (result1.next()) {
								for (int x = 1; x <= result1.getMetaData().getColumnCount(); x++)

									panel += result1.getString(x) + "\t";

								panel += "\n";
							}

						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
				textPane.setText(panel);
			}

		});
		mnNewMenu_1.add(mntmNewMenuItem_2);

		mntmNewMenuItem_3 = new JMenuItem("Guardar Datos");
		mnNewMenu_1.add(mntmNewMenuItem_3);

		mntmNewMenuItem_4 = new JMenuItem("Cargar Datos");
		mnNewMenu_1.add(mntmNewMenuItem_4);

		mntmNewMenuItem_5 = new JMenuItem("Comparar Datos con...");
		mnNewMenu_1.add(mntmNewMenuItem_5);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Usuario:");
		lblNewLabel.setBounds(150, 45, 88, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Contraseña:");
		lblNewLabel_1.setBounds(363, 45, 86, 14);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Host:");
		lblNewLabel_2.setBounds(123, 84, 46, 14);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Port:");
		lblNewLabel_3.setBounds(256, 84, 46, 14);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_5 = new JLabel("DataBase:");
		lblNewLabel_5.setBounds(392, 84, 72, 14);
		contentPane.add(lblNewLabel_5);

		host = new JTextField();
		host.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});
		host.setBounds(159, 81, 79, 20);
		contentPane.add(host);
		host.setColumns(10);

		port = new JTextField();
		port.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});
		port.setBounds(298, 81, 79, 20);
		contentPane.add(port);
		port.setColumns(10);

		bd = new JTextField();
		bd.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});
		bd.setBounds(467, 81, 79, 20);
		contentPane.add(bd);
		bd.setColumns(10);
		arquitectura.add(oracle);
		oracle.setSelected(true);
		oracle.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});
		oracle.addFocusListener(new FocusAdapter() {

		});
		oracle.setBounds(218, 7, 140, 23);
		contentPane.add(oracle);

		arquitectura.add(sqlServer);
		sqlServer.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});
		sqlServer.setBounds(380, 7, 109, 23);
		contentPane.add(sqlServer);

		textPane.setBounds(10, 176, 649, 307);
		contentPane.add(textPane);

		validar = new JLabel("Por favor, rellena todos los campos");
		validar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		validar.setForeground(new Color(255, 0, 0));
		validar.setBackground(new Color(255, 0, 0));
		validar.setBounds(249, 156, 200, 14);
		contentPane.add(validar);
		validar.setVisible(false);

		conectar = new JButton("Conectar");

		conectar.setForeground(SystemColor.windowText);
		conectar.setBackground(new Color(255, 255, 255));
		conectar.setBounds(283, 121, 109, 31);
		conectar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				panel = "";
				if (validarCampos() == true) {
					if (oracle.isSelected()) {
						ConexionOracleDatabase conn = new ConexionOracleDatabase(usuario.getText(),
								String.valueOf(contraseña.getPassword()), host.getText(), port.getText(), bd.getText());

						if (conn.conectar() != null) {
							panel = "Conexion exitosa!";
							resultadoConexion = true;
							try {
								panel += "\n\n\n\n" + "Version Oracle DataBase instalada: \n" + conn.consultaVersion();
							} catch (SQLException e) {

							}

						} else {
							panel += "No se pudo realizar la conexión TCP/IP al host " + host.getText() + ", puerto "
									+ port.getText();

						}
					} else if (sqlServer.isSelected()) {
						ConexionSQLServer conn = new ConexionSQLServer(usuario.getText(),
								String.valueOf(contraseña.getPassword()), host.getText(), port.getText(), bd.getText());

						if (conn.conectar() != null) {
							panel = "Conexion exitosa!";
							resultadoConexion = true;
							try {
								panel += "\n\n\n\n" + "Version SQL-Server instalada: \n" + conn.consultaVersion();
							} catch (SQLException e) {

							}

						} else {

							panel += "No se pudo realizar la conexión TCP/IP al host " + host.getText() + ", puerto "
									+ port.getText();
						}
					}
				}

				textPane.setText(panel);
			}
		});
		contentPane.add(conectar);

		JButton Salir = new JButton("Salir");
		Salir.setBounds(570, 506, 89, 23);
		Salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		contentPane.add(Salir);

		contraseña = new JPasswordField();
		contraseña.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});
		contraseña.setBounds(439, 42, 86, 20);
		contentPane.add(contraseña);

		usuario = new JTextField();
		usuario.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});
		usuario.setBounds(222, 42, 105, 20);
		contentPane.add(usuario);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 192, 203));
		panel.setBounds(10, 33, 649, 80);
		contentPane.add(panel);

	}

	public void validar() {
		if (host.getText().equals("") || usuario.getText().equals("")
				|| String.valueOf(contraseña.getPassword()).equals("") || port.getText().equals("")
				|| bd.getText().equals("")) {
			validar.setVisible(true);

			throw new ExcepcionPersonalizada("· Por favor, rellena todos los campos");

		} else {
			validar.setVisible(false);
		}
	}

	public Boolean portNumeric(String port) {
		Boolean resultado = true;

		try {
			Integer.parseInt(port);
			resultado = true;
		} catch (NumberFormatException excepcion) {
			resultado = false;
		}
		return resultado;
	}

	public Boolean validarCampos() {
		Boolean resultado = true;

		try {
			validar();

			try {
				if (portNumeric(port.getText()) == false) {
					throw new ExcepcionPersonalizada("· El puerto debe de ser un valor numérico");
				}
			} catch (ExcepcionPersonalizada e) {
				panel += e.getMessage();
				resultado = false;
			}
		} catch (ExcepcionPersonalizada e) {
			panel += e.getMessage();
			resultado = false;
		}

		return resultado;

	}
}
