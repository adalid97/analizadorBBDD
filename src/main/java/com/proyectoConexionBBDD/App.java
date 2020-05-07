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
import java.io.FileOutputStream;
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
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.Serializer;

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
	FileOutputStream fileOutputStream;
	Document doc;
	Document docRead = null;
	Element rootReader;

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

						Element root = new Element("infoBaseDatos");

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
								String sqlColumnas = "select column_name, data_type, data_length from all_tab_columns where table_name = '"
										+ tablas.get(i) + "'";

								String sqlPK = " select column_name from user_cons_columns ucc join user_constraints uc on ucc.constraint_name=uc.constraint_name where uc.constraint_type='P' and uc.table_name='"
										+ tablas.get(i) + "'";

								String sqlFK = "select C.NAME INDEX2, B.NAME RELACION from SYS.CDEF$ t,SYS.OBJ$ O,SYS.OBJ$ B,SYS.CON$ C WHERE T.ROBJ# IS NOT NULL AND T.OBJ# = O.OBJ# AND T.ROBJ# = B.OBJ# AND T.CON# = C.CON# AND O.NAME = UPPER('"
										+ tablas.get(i) + "')";

								String sqlTrigger = "select trigger_name, triggering_event from ALL_TRIGGERS WHERE TABLE_NAME = '"
										+ tablas.get(i) + "'";

								Element tablaElement = new Element("tabla");
								Element nombreTablaElement = new Element("nombreTabla");

								tablaElement.appendChild(nombreTablaElement);
								nombreTablaElement.appendChild(tablas.get(i));

								ResultSet result1 = statement.executeQuery(sqlColumnas);

								ArrayList<Tabla> coleccionTablas = new ArrayList<Tabla>();
								while (result1.next()) {
									for (int x = 1; x <= result1.getMetaData().getColumnCount(); x++) {

										Tabla tabla = new Tabla(result1.getString(x), result1.getString(++x),
												result1.getString(++x));
										coleccionTablas.add(tabla);
									}

								}

								for (Tabla tab : coleccionTablas) {

									Element columnaElement = new Element("columna");
									Element campoElement = new Element("campo");
									Element tipoElement = new Element("tipo");
									Element valorElement = new Element("valor");

									// add value to columna
									campoElement.appendChild(tab.getCampo());
									tipoElement.appendChild(tab.getTipo());
									valorElement.appendChild(tab.getValor());

									// add names to columna
									columnaElement.appendChild(campoElement);
									columnaElement.appendChild(tipoElement);
									columnaElement.appendChild(valorElement);

									tablaElement.appendChild(columnaElement);

								}

								ArrayList<Tabla> coleccionPK = new ArrayList<Tabla>();
								ResultSet result2 = statement.executeQuery(sqlPK);
								while (result2.next()) {
									for (int x = 1; x <= result2.getMetaData().getColumnCount(); x++) {

										Tabla pk = new Tabla(result2.getString(x));
										coleccionPK.add(pk);
									}

								}

								for (Tabla pk : coleccionPK) {
									Element primaryKeyElement = new Element("primaryKey");
									Element pkElement = new Element("nombre");
									pkElement.appendChild(pk.getPk());
									primaryKeyElement.appendChild(pkElement);
									tablaElement.appendChild(primaryKeyElement);
								}

								ArrayList<Tabla> coleccionFK = new ArrayList<Tabla>();
								ResultSet result3 = statement.executeQuery(sqlFK);
								while (result3.next()) {
									for (int x = 1; x <= result3.getMetaData().getColumnCount(); x++) {

										Tabla fk = new Tabla(result3.getString(x), result3.getString(++x));
										coleccionFK.add(fk);
									}
								}

								for (Tabla fk : coleccionFK) {
									Element foreignKeyElement = new Element("foreignKey");
									Element fkElement = new Element("nombre");
									Element referenciaFKElement = new Element("referencia");
									fkElement.appendChild(fk.getFk());
									referenciaFKElement.appendChild(fk.getReferenciaFK());
									foreignKeyElement.appendChild(fkElement);
									foreignKeyElement.appendChild(referenciaFKElement);
									tablaElement.appendChild(foreignKeyElement);
								}

								ArrayList<Tabla> coleccionTrigger = new ArrayList<Tabla>();
								ResultSet result4 = statement.executeQuery(sqlTrigger);
								while (result4.next()) {
									for (int x = 1; x <= result4.getMetaData().getColumnCount(); x++) {

										Tabla trigger = new Tabla(result4.getString(x), result4.getString(++x));
										coleccionTrigger.add(trigger);
									}

								}

								for (Tabla tr : coleccionTrigger) {
									Element triggerElement = new Element("trigger");
									Element fkElement = new Element("nombre");
									Element referenciaFKElement = new Element("tipo");
									fkElement.appendChild(tr.getFk());
									referenciaFKElement.appendChild(tr.getReferenciaFK());
									triggerElement.appendChild(fkElement);
									triggerElement.appendChild(referenciaFKElement);
									tablaElement.appendChild(triggerElement);
								}

								root.appendChild(tablaElement);

							}

							doc = new Document(root);

							File tempFile = File.createTempFile("ficherotemporal.xml", null);

							tempFile.deleteOnExit();

							// get a file output stream ready
							fileOutputStream = new FileOutputStream(tempFile);

							// use the serializer class to write it all
							Serializer serializer = new Serializer(fileOutputStream, "UTF-8");
							serializer.setIndent(4);
							serializer.write(doc);

//							Document doc = new Document(root);
//
//							File file = new File("outw.xml");
//							if (!file.exists()) {
//								file.createNewFile();
//							}
//
//							FileOutputStream fileOutputStream = new FileOutputStream(file);
//
//							Serializer serializer = new Serializer(fileOutputStream, "UTF-8");
//							serializer.setIndent(4);
//							serializer.write(doc);

							// -----------------------------------------------------------------------------------------------------
							// Lectura
							// -----------------------------------------------------------------------------------------------------

							Builder builder = new Builder();
							Document docReader = builder.build(tempFile);

							rootReader = docReader.getRootElement();
							leerDatos();

						} catch (SQLException | IOException | ParsingException e) {
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

						Element root = new Element("infoBaseDatosSQLServer");

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

								Element tablaElement = new Element("tabla");
								Element nombreTablaElement = new Element("nombreTabla");

								tablaElement.appendChild(nombreTablaElement);
								nombreTablaElement.appendChild(tablas.get(i));

								ResultSet result1 = statement.executeQuery(sqlColumnas);

								ArrayList<Tabla> coleccionTablas = new ArrayList<Tabla>();
								while (result1.next()) {
									for (int x = 1; x <= result1.getMetaData().getColumnCount(); x++) {

										Tabla tabla = new Tabla(result1.getString(x), result1.getString(++x),
												result1.getString(++x));
										coleccionTablas.add(tabla);
									}

								}

								for (Tabla tab : coleccionTablas) {

									Element columnaElement = new Element("columna");
									Element campoElement = new Element("campo");
									Element tipoElement = new Element("tipo");
									Element valorElement = new Element("valor");

									// add value to columna
									campoElement.appendChild(tab.getCampo());
									tipoElement.appendChild(tab.getTipo());
									valorElement.appendChild(tab.getValor());

									// add names to columna
									columnaElement.appendChild(campoElement);
									columnaElement.appendChild(tipoElement);
									columnaElement.appendChild(valorElement);

									tablaElement.appendChild(columnaElement);

								}

								ArrayList<Tabla> coleccionPK = new ArrayList<Tabla>();
								ResultSet result2 = statement.executeQuery(sqlPK);
								while (result2.next()) {
									for (int x = 1; x <= result2.getMetaData().getColumnCount(); x++) {

										Tabla pk = new Tabla(result2.getString(x));
										coleccionPK.add(pk);
									}

								}

								for (Tabla pk : coleccionPK) {
									Element primaryKeyElement = new Element("primaryKey");
									Element pkElement = new Element("nombre");
									pkElement.appendChild(pk.getPk());
									primaryKeyElement.appendChild(pkElement);
									tablaElement.appendChild(primaryKeyElement);
								}

								ArrayList<Tabla> coleccionFK = new ArrayList<Tabla>();
								ResultSet result3 = statement.executeQuery(sqlFK);
								while (result3.next()) {
									for (int x = 1; x <= result3.getMetaData().getColumnCount(); x++) {

										Tabla fk = new Tabla(result3.getString(x), result3.getString(++x));
										coleccionFK.add(fk);
									}
								}

								for (Tabla fk : coleccionFK) {
									Element foreignKeyElement = new Element("foreignKey");
									Element fkElement = new Element("nombre");
									Element referenciaFKElement = new Element("referencia");
									fkElement.appendChild(fk.getFk());
									referenciaFKElement.appendChild(fk.getReferenciaFK());
									foreignKeyElement.appendChild(fkElement);
									foreignKeyElement.appendChild(referenciaFKElement);
									tablaElement.appendChild(foreignKeyElement);
								}

								ArrayList<Tabla> coleccionTrigger = new ArrayList<Tabla>();
								ResultSet result4 = statement.executeQuery(sqlTrigger);
								while (result4.next()) {
									for (int x = 1; x <= result4.getMetaData().getColumnCount(); x++) {

										Tabla trigger = new Tabla(result4.getString(x), result4.getString(++x));
										coleccionTrigger.add(trigger);
									}

								}

								for (Tabla tr : coleccionTrigger) {
									Element triggerElement = new Element("trigger");
									Element fkElement = new Element("nombre");
									Element referenciaFKElement = new Element("tipo");
									fkElement.appendChild(tr.getFk());
									referenciaFKElement.appendChild(tr.getReferenciaFK());
									triggerElement.appendChild(fkElement);
									triggerElement.appendChild(referenciaFKElement);
									tablaElement.appendChild(triggerElement);
								}

								root.appendChild(tablaElement);

							}

							doc = new Document(root);

							File tempFile = File.createTempFile("ficherotemporal.xml", null);

							tempFile.deleteOnExit();

							// get a file output stream ready
							fileOutputStream = new FileOutputStream(tempFile);

							// use the serializer class to write it all
							Serializer serializer = new Serializer(fileOutputStream, "UTF-8");
							serializer.setIndent(4);
							serializer.write(doc);

//							Document doc = new Document(root);
//
//							File file = new File("outw.xml");
//							if (!file.exists()) {
//								file.createNewFile();
//							}
//
//							FileOutputStream fileOutputStream = new FileOutputStream(file);
//
//							Serializer serializer = new Serializer(fileOutputStream, "UTF-8");
//							serializer.setIndent(4);
//							serializer.write(doc);

							// -----------------------------------------------------------------------------------------------------
							// Lectura
							// -----------------------------------------------------------------------------------------------------
							Builder builder = new Builder();
							Document docReader = builder.build(tempFile);

							rootReader = docReader.getRootElement();
							leerDatos();

						} catch (SQLException | IOException | ParsingException e) {
							e.printStackTrace();
						}

						datosOk = true;

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
						try {
							FileOutputStream fileOutputStream = new FileOutputStream(ficheroSeleccionado);

							Serializer serializer = new Serializer(fileOutputStream, "UTF-8");
							serializer.setIndent(4);
							serializer.write(doc);

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

					Builder builder = new Builder();

					try {
						docRead = builder.build(fichero);
					} catch (ParsingException | IOException e1) {
						e1.printStackTrace();
					}
					rootReader = docRead.getRootElement();
					leerDatos();

				}
				textPane.setText(panel);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_4);

		mntmNewMenuItem_5 = new JMenuItem("Comparar Datos con...");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

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

	public String leerDatos() {

		Elements tabla = rootReader.getChildElements("tabla");

		for (int q = 0; q < tabla.size(); q++) {
			Element table = tabla.get(q);

			Elements columnaElement = table.getChildElements("columna");

			Element nombreTablaElement = table.getFirstChildElement("nombreTabla");
			String nombreTabla;
			nombreTabla = nombreTablaElement.getValue();
			panel += "<h2 style=\"background-color:#FDEDEC;\">" + nombreTabla + "</h2>";

			for (int j = 0; j < columnaElement.size(); j++) {
				Element columnas = columnaElement.get(j);

				Element campoElement = columnas.getFirstChildElement("campo");
				Element tipoElement = columnas.getFirstChildElement("tipo");
				Element valorElement = columnas.getFirstChildElement("valor");

				String campo, tipo, valor;

				try {

					campo = campoElement.getValue();
					tipo = tipoElement.getValue();
					valor = valorElement.getValue();

					panel += "campo: " + campo + "<br>tipo: " + tipo + "<br>valor: " + valor + "<br><br>";

				} catch (NullPointerException ex) {
					ex.printStackTrace();
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}

			Elements primaryKeyElement = table.getChildElements("primaryKey");

			for (int j = 0; j < primaryKeyElement.size(); j++) {
				Element pks = primaryKeyElement.get(j);

				Element pkElement = pks.getFirstChildElement("nombre");

				panel += "Primary Key: " + pkElement.getValue() + "<br>";

			}

			Elements foreignKeyElement = table.getChildElements("foreignKey");

			for (int j = 0; j < foreignKeyElement.size(); j++) {
				Element fks = foreignKeyElement.get(j);

				Element fkElement = fks.getFirstChildElement("nombre");
				Element referenciaFKElement = fks.getFirstChildElement("referencia");

				panel += "Nombre Foreign Key: " + fkElement.getValue() + " referencia a la tabla "
						+ referenciaFKElement.getValue() + "<br>";

			}

			Elements triggerElement = table.getChildElements("trigger");

			for (int j = 0; j < triggerElement.size(); j++) {
				Element fks = triggerElement.get(j);

				Element nombreTriggerElement = fks.getFirstChildElement("nombre");
				Element referenciaFKElement = fks.getFirstChildElement("tipo");

				panel += "Nombre Trigger: " + nombreTriggerElement.getValue() + " | Tipo Trigger: "
						+ referenciaFKElement.getValue() + "<br>";

			}

		}
		return panel;

	}
}
