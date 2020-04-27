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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	private String panel = "";
	private String panel2 = "";
	private JMenu mnNewMenu;
	private JMenuItem mntmNewMenuItem;
	private Boolean resultadoConexion = false;
	private JTextPane textPane;

	final JRadioButton oracle = new JRadioButton("OracleDataBase");
	final JRadioButton sqlServer = new JRadioButton("SQL-Server");
	private JMenuItem mntmNewMenuItem_2;
	private JMenuItem mntmNewMenuItem_3;
	private JMenuItem mntmNewMenuItem_4;
	private JMenuItem mntmNewMenuItem_5;
	private JScrollPane scrollPane;
	private Boolean datosOk = false;

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

							panel = "<p style=\"text-align:center; color:green\">Se han exportado los datos correctamente.</p>";

						} catch (IOException e) {
							panel = "Error al exportar los datos. Error: " + e.getMessage();
						}

					}

				} else {
					panel = "<p style=\"text-align:center; color:red\">Por favor, establece una conexión y vuelve a intentarlo.</p>";
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

							for (int i = 0; i < tablas.size(); i++) {
								String sqlColumnas = "SELECT column_name \"Name\",   concat(concat(concat(data_type,'('),data_length),')') \"Type\"\r\n"
										+ "FROM user_tab_columns\r\n" + "WHERE table_name='" + tablas.get(i) + "'";

								String sqlPK = " select column_name from user_cons_columns ucc join user_constraints uc on ucc.constraint_name=uc.constraint_name where uc.constraint_type='P' and uc.table_name='"
										+ tablas.get(i) + "'";

								String sqlFK = "select C.NAME INDEX2, B.NAME RELACION from SYS.CDEF$ t,SYS.OBJ$ O,SYS.OBJ$ B,SYS.CON$ C WHERE T.ROBJ# IS NOT NULL AND T.OBJ# = O.OBJ# AND T.ROBJ# = B.OBJ# AND T.CON# = C.CON# AND O.NAME = UPPER('"
										+ tablas.get(i) + "')";

								String sqlTrigger = "select trigger_name, triggering_event, table_name from ALL_TRIGGERS WHERE TABLE_NAME = '"
										+ tablas.get(i) + "'";

								ResultSet result1 = statement.executeQuery(sqlColumnas);

								panel += "<h2 style=\"background-color:#FDEDEC;\">" + tablas.get(i) + "</h2>";
								panel += "<table>";
								while (result1.next()) {
									panel += "<tr>";
									for (int x = 1; x <= result1.getMetaData().getColumnCount(); x++)

										panel += "<td>" + result1.getString(x) + "&emsp;&emsp;&emsp;</td>";

									panel += "</tr>";
								}
								panel += "</table>";

								ResultSet result2 = statement.executeQuery(sqlPK);
								panel += "<table>";
								while (result2.next()) {
									panel += "<tr><td><strong>Primary Key:&emsp;&emsp;&emsp;</strong></td>";
									for (int x = 1; x <= result2.getMetaData().getColumnCount(); x++)

										panel += "<td>" + result2.getString(x) + "&emsp;&emsp;&emsp;</td>";

									panel += "</tr>";
								}
								panel += "</table>";

								ResultSet result3 = statement.executeQuery(sqlFK);
								panel += "<table>";
								while (result3.next()) {
									panel += "<tr><td><strong>Foreign Key:&emsp;&emsp;&emsp;</strong></td>";
									for (int x = 1; x <= result3.getMetaData().getColumnCount(); x++)

										panel += result3.getString(x) + "&emsp;&emsp;&emsp;</td>";

									panel += "</tr>";
								}
								panel += "</table>";

								ResultSet result4 = statement.executeQuery(sqlTrigger);
								panel += "<table>";
								while (result4.next()) {
									panel += "<tr><td><strong>Triggers:&emsp;&emsp;&emsp;</strong></td>";
									for (int x = 1; x <= result4.getMetaData().getColumnCount(); x++)

										panel += result4.getString(x) + "&emsp;&emsp;&emsp;</td>";

									panel += "</tr>";
								}
								panel += "</table>";

							}

						} catch (SQLException e) {
							e.printStackTrace();
						}

						datosOk = true;
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

								String sqlFK = "SELECT OBJECT_NAME(f.constid) AS 'FKName', c.name AS 'ColName' FROM sysforeignkeys f INNER JOIN syscolumns c ON f.fkeyid = c.id AND f.fkey = c.colid WHERE fkeyid = OBJECT_ID('"
										+ tablas.get(i) + "')";

								String sqlTrigger = "SELECT sysobjects.name AS trigger_name, OBJECT_NAME(parent_obj) AS table_name FROM sysobjects INNER JOIN sys.tables t ON sysobjects.parent_obj = t.object_id INNER JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE sysobjects.type = 'TR' AND OBJECT_NAME(parent_obj) = '"
										+ tablas.get(i) + "'";

								ResultSet result1 = statement.executeQuery(sqlColumnas);

								panel += "<h2 style=\"background-color:#FDEDEC;\">" + tablas.get(i) + "</h2><table>";
								panel2 += "db." + tablas.get(i) + "\n";

								while (result1.next()) {
									panel += "<tr>";
									for (int x = 1; x <= result1.getMetaData().getColumnCount(); x++) {

										panel += "<td>" + result1.getString(x) + "&emsp;&emsp;&emsp;</td>";
										panel2 += result1.getString(x) + " ";
									}

									panel += "</tr>";
									panel2 += "\n";
								}
								panel += "</table>";

								ResultSet result2 = statement.executeQuery(sqlPK);
								panel += "<table>";
								while (result2.next()) {
									panel += "<tr><td><strong>Primary Key:&emsp;&emsp;&emsp;</strong></td>";
									for (int x = 1; x <= result2.getMetaData().getColumnCount(); x++) {

										panel += "<td>" + result2.getString(x) + "&emsp;&emsp;&emsp;</td>";
										panel2 += result2.getString(x) + " ";
									}
									panel += "</tr>";
									panel2 += "\n";
								}
								panel += "</table>";

								ResultSet result3 = statement.executeQuery(sqlFK);
								panel += "<table>";
								while (result3.next()) {
									panel += "<tr><td><strong>Foreign Key:&emsp;&emsp;&emsp;</strong></td>";
									for (int x = 1; x <= result3.getMetaData().getColumnCount(); x++) {

										panel += result3.getString(x) + "&emsp;&emsp;&emsp;</td>";
										panel2 += result3.getString(x) + " ";

									}

									panel += "</tr>";
									panel2 += "\n";
								}
								panel += "</table>";

								ResultSet result4 = statement.executeQuery(sqlTrigger);
								panel += "<table>";
								while (result4.next()) {
									panel += "<tr><td><strong>Triggers:&emsp;&emsp;&emsp;</strong></td>";
									for (int x = 1; x <= result4.getMetaData().getColumnCount(); x++) {

										panel += result4.getString(x) + "&emsp;&emsp;&emsp;</td>";

										panel2 += result4.getString(x) + " ";
									}

									panel += "</tr>";
									panel2 += "\n";
								}
								panel += "</table>";

							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

					} else {
						panel = "<p style=\"text-align:center; color:red\">Debes establecer primero una conexión.</p>";
					}
					datosOk = true;
				}

				textPane.setText(panel);
			}

		});

		mnNewMenu_1.add(mntmNewMenuItem_2);

		// GUARDAD DATOS

		mntmNewMenuItem_3 = new JMenuItem("Guardar Datos");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (datosOk) {
					JFileChooser fileChooser = new JFileChooser();
					int seleccion = fileChooser.showSaveDialog(null);
					if (seleccion == JFileChooser.APPROVE_OPTION) {
						File ficheroSeleccionado = fileChooser.getSelectedFile();
						FileWriter fichero = null;
						try {
//							panel.replaceAll(
//									"<h2 style=\"background-color:#FDEDEC;\">|</h2>|<table>|<td>|<tr>|&emsp;|</table>",
//									"");

							fichero = new FileWriter(ficheroSeleccionado);

							fichero.write(panel2);

							fichero.close();

							panel = "<p style=\"text-align:center; color:green\">Se han exportado los datos correctamente.</p>";

						} catch (IOException e) {
							panel = "Error al exportar los datos. Error: " + e.getMessage();
						}

					}
				} else {
					panel = "<p style=\"text-align:center; color:red\">Debes mostrar primero los datos.</p>";
				}
				textPane.setText(panel);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_3);

		mntmNewMenuItem_4 = new JMenuItem("Cargar Datos");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();
				int seleccion = fileChooser.showOpenDialog(null);
				if (seleccion == JFileChooser.APPROVE_OPTION) {
					File fichero = fileChooser.getSelectedFile();
					panel = "";

					try {
						String cadena;
						FileReader f = new FileReader(fichero);
						BufferedReader b = new BufferedReader(f);
						while ((cadena = b.readLine()) != null) {
							panel += cadena;
						}
						b.close();

					} catch (Exception ex) {
					}

				}

				textPane.setText(panel);

			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_4);

		mntmNewMenuItem_5 = new JMenuItem("Comparar Datos con...");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();
				int seleccion = fileChooser.showOpenDialog(null);
				if (seleccion == JFileChooser.APPROVE_OPTION) {
					File fichero = fileChooser.getSelectedFile();

					panel = "";
					ArrayList<String> contenidoArchivo = new ArrayList<String>();
					Scanner sc;
					try {
						sc = new Scanner(fichero);

						while (sc.hasNextLine()) {
							contenidoArchivo.add(sc.nextLine());
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();

					}

					ArrayList<String> contenidoArchivo2 = new ArrayList<String>();

					try {

						File temp = File.createTempFile("archivo", ".tmp");

						temp.deleteOnExit();

						BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
						bw.write(panel2);
						bw.close();

						sc = new Scanner(temp);

						while (sc.hasNextLine()) {
							int x = 0;

							if (panel2.startsWith("db.")) {

							}
							contenidoArchivo2.add(sc.nextLine());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					for (int x = 0; x < contenidoArchivo2.size(); x++) {
						System.out.println(contenidoArchivo2.get(x));
					}

					System.out.println("\n\n\n\n\n=======================\n\n\n\n\n\n");
					for (int x = 0; x < contenidoArchivo.size(); x++) {
						System.out.println(contenidoArchivo.get(x));
					}

					System.out.println(contenidoArchivo.get(1));
					System.out.println(contenidoArchivo2.get(1));

					ArrayList<String> newList = new ArrayList<String>();
					for (String element : contenidoArchivo2) {
						if (!contenidoArchivo.contains(element)) {
							newList.add(element);
						}
					}
					System.out.println(newList);

//					List<String> lista = contenidoArchivo2.stream().filter(f -> !contenidoArchivo.contains(f))
//							.collect(Collectors.toList());
//					System.out.println(lista);

//					try {
//						String cadena;
//						FileReader f = new FileReader(fichero);
//						BufferedReader b = new BufferedReader(f);
//						while ((cadena = b.readLine()) != null) {
//							panel += cadena;
//						}
//						b.close();
//
//					} catch (Exception ex) {
//					}

				}
			}
		});
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

		conectar = new JButton("Conectar");
		conectar.setBounds(283, 121, 109, 31);
		botonConectar();

		JButton Salir = new JButton("Salir");
		Salir.setBounds(567, 506, 89, 23);
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
		panel.setBounds(10, 33, 646, 80);
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
		contentPane.add(Salir);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 158, 646, 340);
		contentPane.add(scrollPane);

		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		scrollPane.setViewportView(textPane);
	}

	public void validar() {
		if (host.getText().equals("") || usuario.getText().equals("")
				|| String.valueOf(contraseña.getPassword()).equals("") || port.getText().equals("")
				|| bd.getText().equals("")) {

			throw new ExcepcionPersonalizada(
					"<p style=\"text-align:center; color:red\">Por favor, rellena todos los campos.</p>");
		}
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
							panel = "<p style=\"text-align:center; color:green\">¡Conectado correctamente!</p>";
							resultadoConexion = true;
							try {
								panel += "<br><br>" + "<strong>Version Oracle DataBase instalada:</strong> <br>"
										+ conn.consultaVersion();
							} catch (SQLException e) {

							}

						} else {
							panel += "<p style=\"text-align:center; color:red\">No se pudo realizar la conexión TCP/IP al host "
									+ host.getText() + ", puerto " + port.getText() + ".</p>";

						}
					} else if (sqlServer.isSelected()) {
						ConexionSQLServer conn = new ConexionSQLServer(usuario.getText(),
								String.valueOf(contraseña.getPassword()), host.getText(), port.getText(), bd.getText());

						if (conn.conectar() != null) {
							panel = "<p style=\"text-align:center; color:green\">¡Conectado correctamente!</p>";
							resultadoConexion = true;
							try {
								panel += "<br><br>" + "<strong>Version SQL-Server instalada: </strong><br>"
										+ conn.consultaVersion();
							} catch (SQLException e) {

							}

						} else {

							panel += "<p style=\"text-align:center; color:red\">No se pudo realizar la conexión TCP/IP al host "
									+ host.getText() + ", puerto " + port.getText() + ".</p>";
						}
					}
				}

				textPane.setText(panel);
			}
		});
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
					throw new ExcepcionPersonalizada(
							"<p style=\"text-align:center; color:red\">El puerto debe de ser un valor numérico.</p>");
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
