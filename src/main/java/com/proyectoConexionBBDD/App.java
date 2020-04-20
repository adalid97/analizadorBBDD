package com.proyectoConexionBBDD;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
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

	final JRadioButton oracle = new JRadioButton("OracleDataBase");
	final JRadioButton sqlServer = new JRadioButton("SQL-Server");
	private JMenuItem mntmNewMenuItem_2;
	private JMenuItem mntmNewMenuItem_3;
	private JMenuItem mntmNewMenuItem_4;
	private JMenuItem mntmNewMenuItem_5;
	private JTextPane textPane;
	private JScrollPane scrollPane;

	public App() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\jadal\\OneDrive\\Imágenes\\50.png"));
		setFont(new Font("Arial", Font.PLAIN, 12));
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

				if (oracle.isSelected()) {
					ConexionOracleDatabase connOracle = new ConexionOracleDatabase(usuario.getText(),
							String.valueOf(contraseña.getPassword()), host.getText(), port.getText(), bd.getText());

					if (connOracle.conectar() != null) {

						String sqlTablas = "select table_name from user_tables order by table_name";

						Statement statement = null;
						try {
							statement = connOracle.conectar().createStatement();
							ResultSet result = statement.executeQuery(sqlTablas);

							ArrayList<String> tablas = new ArrayList<String>();
							while (result.next()) {

								for (int x = 1; x <= result.getMetaData().getColumnCount(); x++)

									tablas.add(result.getString(x) + "");

							}

							System.out.println(tablas.get(1));

							for (int i = 0; i < tablas.size(); i++) {
								String sqlColumnas = "SELECT column_name \"Name\",   concat(concat(concat(data_type,'('),data_length),')') \"Type\"\r\n"
										+ "FROM user_tab_columns\r\n" + "WHERE table_name='" + tablas.get(i) + "'";

								ResultSet result1 = statement.executeQuery(sqlColumnas);

								panel += "<h2 style=\"background-color:#FDEDEC;\">" + tablas.get(i) + "</h2>";
								while (result1.next()) {
									for (int x = 1; x <= result1.getMetaData().getColumnCount(); x++)

										panel += result1.getString(x) + "&emsp;";

									panel += "<br>";
								}

							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

					} else {
						panel = "<p style=\"text-align:center; color:red\">Debes establecer primero una conexión.</p>";
					}

				}
				if (sqlServer.isSelected()) {

					ConexionSQLServer connSql = new ConexionSQLServer(usuario.getText(),
							String.valueOf(contraseña.getPassword()), host.getText(), port.getText(), bd.getText());

					if (connSql.conectar() != null) {

						String sqlTablas = "SELECT CAST(table_name as varchar)  FROM INFORMATION_SCHEMA.TABLES ORDER BY 1";

						Statement statement = null;
						try {
							statement = connSql.conectar().createStatement();
							ResultSet result = statement.executeQuery(sqlTablas);

							ArrayList<String> tablas = new ArrayList<String>();
							while (result.next()) {

								for (int x = 1; x <= result.getMetaData().getColumnCount(); x++)

									tablas.add(result.getString(x) + "");

							}

							for (int i = 0; i < tablas.size(); i++) {
								String sqlColumnas = "SELECT COLUMN_NAME,DATA_TYPE, CHARACTER_MAXIMUM_LENGTH\r\n"
										+ "FROM Information_Schema.Columns\r\n" + "WHERE TABLE_NAME = '" + tablas.get(i)
										+ "';";

								String sqlPK = "SELECT column_name as PRIMARYKEYCOLUMN FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS "
										+ "AS TC INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KU ON "
										+ "TC.CONSTRAINT_TYPE = 'PRIMARY KEY' AND TC.CONSTRAINT_NAME = KU.CONSTRAINT_NAME AND "
										+ "KU.table_name='" + tablas.get(i)
										+ "' ORDER BY KU.TABLE_NAME, KU.ORDINAL_POSITION;";

								ResultSet result1 = statement.executeQuery(sqlColumnas);

								panel += "<h2 style=\"background-color:#FDEDEC;\">" + tablas.get(i) + "</h2>";
								while (result1.next()) {
									for (int x = 1; x <= result1.getMetaData().getColumnCount(); x++)

										panel += result1.getString(x) + "&emsp;";

									panel += "\n<br>";
								}

								ResultSet result2 = statement.executeQuery(sqlPK);
								while (result2.next()) {
									for (int x = 1; x <= result2.getMetaData().getColumnCount(); x++)

										panel += "<strong>" + result2.getString(x) + "</strong>";

									panel += "\n<br>";
								}

							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

					} else {
						panel = "<p style=\"text-align:center; color:red\">Debes establecer primero una conexión.</p>";
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

		JLabel lblNewLabel = new JLabel("Usuario:");
		lblNewLabel.setBounds(150, 45, 88, 14);

		JLabel lblNewLabel_1 = new JLabel("Contraseña:");
		lblNewLabel_1.setBounds(363, 45, 86, 14);

		JLabel lblNewLabel_2 = new JLabel("Host:");
		lblNewLabel_2.setBounds(123, 84, 46, 14);

		JLabel lblNewLabel_3 = new JLabel("Port:");
		lblNewLabel_3.setBounds(256, 84, 46, 14);

		JLabel lblNewLabel_5 = new JLabel("DataBase:");
		lblNewLabel_5.setBounds(392, 84, 72, 14);

		host = new JTextField();
		host.setBounds(159, 81, 79, 20);
		host.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});
		host.setColumns(10);

		port = new JTextField();
		port.setBounds(298, 81, 79, 20);
		port.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});
		port.setColumns(10);

		bd = new JTextField();
		bd.setBounds(467, 81, 79, 20);
		bd.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});
		bd.setColumns(10);
		arquitectura.add(oracle);
		oracle.setBounds(218, 7, 140, 23);
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

		arquitectura.add(sqlServer);
		sqlServer.setBounds(380, 7, 109, 23);
		sqlServer.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});

		validar = new JLabel("Por favor, rellena todos los campos");
		validar.setBounds(249, 156, 200, 14);
		validar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		validar.setForeground(new Color(255, 0, 0));
		validar.setBackground(new Color(255, 0, 0));
		validar.setVisible(false);

		conectar = new JButton("Conectar");
		conectar.setBounds(283, 121, 109, 31);
		botonConectar();

		JButton Salir = new JButton("Salir");
		Salir.setBounds(570, 506, 89, 23);
		Salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		contraseña = new JPasswordField();
		contraseña.setBounds(439, 42, 86, 20);
		contraseña.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});

		usuario = new JTextField();
		usuario.setBounds(222, 42, 105, 20);
		usuario.addKeyListener(new KeyAdapter() {
			// Al presionar Enter llama al botón conectar
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					conectar.doClick();
			}
		});

		JPanel panel = new JPanel();
		panel.setBounds(10, 33, 649, 80);
		panel.setBackground(new Color(255, 192, 203));
		contentPane.setLayout(null);
		contentPane.add(oracle);
		contentPane.add(sqlServer);
		contentPane.add(lblNewLabel);
		contentPane.add(usuario);
		contentPane.add(lblNewLabel_1);
		contentPane.add(lblNewLabel_2);
		contentPane.add(port);
		contentPane.add(lblNewLabel_3);
		contentPane.add(lblNewLabel_5);
		contentPane.add(bd);
		contentPane.add(host);
		contentPane.add(contraseña);
		contentPane.add(panel);
		contentPane.add(conectar);
		contentPane.add(validar);
		contentPane.add(Salir);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 178, 649, 317);
		contentPane.add(scrollPane);

		textPane = new JTextPane();
		textPane.setContentType("text/html");
		scrollPane.setViewportView(textPane);

	}

	public void botonConectar() {
		conectar.setForeground(SystemColor.windowText);
		conectar.setBackground(new Color(255, 255, 255));
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
