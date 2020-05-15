package com.proyectoConexionBBDD;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import model.Columna;
import model.Tab;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

public class CompararBD {

	public String comparar(File tempFile) {

		String panel = "";
		ArrayList<Tab> nombreTablas = new ArrayList<Tab>();

		Builder builder = new Builder();
		Document doc = null;
		try {
			doc = builder.build(tempFile);
		} catch (ParsingException | IOException e) {
			e.printStackTrace();
		}

		Element root = doc.getRootElement();

		Elements tabla = root.getChildElements("tabla");

		for (int q = 0; q < tabla.size(); q++) {
			Element table = tabla.get(q);

			Elements columnaElement = table.getChildElements("columna");

			Element nombreTablaElement = table.getFirstChildElement("nombreTabla");
			String nombreTabla = nombreTablaElement.getValue();
			ArrayList<Columna> arrayColumnas = new ArrayList<Columna>();
			for (int j = 0; j < columnaElement.size(); j++) {
				Element columnas = columnaElement.get(j);

				Element campoElement = columnas.getFirstChildElement("campo");
				Element tipoElement = columnas.getFirstChildElement("tipo");
				Element valorElement = columnas.getFirstChildElement("valor");

				String campo, tipo, valor;

				campo = campoElement.getValue();
				tipo = tipoElement.getValue();
				valor = valorElement.getValue();

				Columna col = new Columna(campo, tipo, valor);
				arrayColumnas.add(col);

			}

			Elements primaryKeyElement = table.getChildElements("primaryKey");
			ArrayList<String> pks = new ArrayList<String>();
			for (int j = 0; j < primaryKeyElement.size(); j++) {
				Element pk = primaryKeyElement.get(j);

				Element pkElement = pk.getFirstChildElement("nombre");

				pks.add(pkElement.getValue());

			}

			Elements foreignKeyElement = table.getChildElements("foreignKey");
			ArrayList<String> fks = new ArrayList<String>();
			for (int j = 0; j < foreignKeyElement.size(); j++) {
				Element fk = foreignKeyElement.get(j);

				Element fkElement = fk.getFirstChildElement("nombre");

				fks.add(fkElement.getValue());

			}
			Elements triggerElement = table.getChildElements("trigger");
			ArrayList<String> triggers = new ArrayList<String>();
			for (int j = 0; j < triggerElement.size(); j++) {
				Element trigger = triggerElement.get(j);

				Element tElement = trigger.getFirstChildElement("nombre");

				triggers.add(tElement.getValue());

			}

			Tab tabla1 = new Tab(nombreTabla, arrayColumnas, pks, fks, triggers);
			nombreTablas.add(tabla1);

		}
//-------------------------------------------------------------------------------------------------------------

		JFileChooser fileChooser = new JFileChooser();
		int seleccion = fileChooser.showOpenDialog(null);
		File fichero = fileChooser.getSelectedFile();

		ArrayList<Tab> nombreTablas2 = new ArrayList<Tab>();
		Builder builder2 = new Builder();
		Document doc2 = null;
		try {
			doc2 = builder2.build(fichero);
		} catch (ParsingException | IOException e) {
			e.printStackTrace();
		}

		Element root2 = doc2.getRootElement();

		Elements tabla2 = root2.getChildElements("tabla");

		for (int q = 0; q < tabla2.size(); q++) {
			Element table = tabla2.get(q);

			Elements columnaElement = table.getChildElements("columna");

			Element nombreTablaElement = table.getFirstChildElement("nombreTabla");
			String nombreTabla2 = nombreTablaElement.getValue();
			ArrayList<Columna> arrayColumnas2 = new ArrayList<Columna>();
			for (int j = 0; j < columnaElement.size(); j++) {
				Element columnas = columnaElement.get(j);

				Element campoElement = columnas.getFirstChildElement("campo");
				Element tipoElement = columnas.getFirstChildElement("tipo");
				Element valorElement = columnas.getFirstChildElement("valor");

				String campo, tipo, valor;

				campo = campoElement.getValue();
				tipo = tipoElement.getValue();
				valor = valorElement.getValue();

				Columna col = new Columna(campo, tipo, valor);
				arrayColumnas2.add(col);

			}

			Elements primaryKeyElement = table.getChildElements("primaryKey");

			ArrayList<String> pks = new ArrayList<String>();
			for (int j = 0; j < primaryKeyElement.size(); j++) {
				Element pk = primaryKeyElement.get(j);

				Element pkElement = pk.getFirstChildElement("nombre");

				pks.add(pkElement.getValue());

			}

			Elements foreignKeyElement = table.getChildElements("foreignKey");
			ArrayList<String> fks = new ArrayList<String>();
			for (int j = 0; j < foreignKeyElement.size(); j++) {
				Element fk = foreignKeyElement.get(j);

				Element fkElement = fk.getFirstChildElement("nombre");

				fks.add(fkElement.getValue());

			}

			Elements triggerElement = table.getChildElements("trigger");
			ArrayList<String> triggers = new ArrayList<String>();
			for (int j = 0; j < triggerElement.size(); j++) {
				Element trigger = triggerElement.get(j);

				Element tElement = trigger.getFirstChildElement("nombre");

				triggers.add(tElement.getValue());

			}
			Tab tabla1 = new Tab(nombreTabla2, arrayColumnas2, pks, fks, triggers);
			nombreTablas2.add(tabla1);

		}

		ArrayList<String> a = new ArrayList<String>();
		for (Tab t : nombreTablas2) {
			a.add(t.getNombre());
		}
		ArrayList<String> b = new ArrayList<String>();
		for (Tab t2 : nombreTablas) {
			b.add(t2.getNombre());
		}
		for (Tab t2 : nombreTablas2) {

			for (Tab t1 : nombreTablas) {
				if (a.contains(t1.getNombre())) {

					if (t2.getNombre().equals(t1.getNombre())) {

						ArrayList<Columna> col1 = t1.getColumna();
						ArrayList<Columna> col2 = t2.getColumna();

						panel += "<h2 style=\"background-color:#FDEDEC;\">" + t2.getNombre() + "</h2>\n";
						ArrayList<String> nombreCol = new ArrayList<String>();
						Columna c1 = null, c2 = null;
						for (int j = 0; j < col1.size(); j++) {
							nombreCol.add(col1.get(j).getCampo().toString());
						}
						Boolean identica = true;
						for (int j = 0; j < col1.size() && j < col2.size(); j++) {

							c1 = col1.get(j);
							c2 = col2.get(j);
							boolean existe = true;
							for (int i = 0; i < col2.size(); i++) {
								if (!nombreCol.contains(c2.getCampo())) {
									existe = false;
									i = col2.size();
								}
							}

							if (existe && c1.getCampo().equals(c2.getCampo())) {
								if (!c1.getCampo().equals(c2.getCampo())) {
									identica = false;
									panel += "- El nombre del campo " + c1.getCampo() + " cambia por " + c2.getCampo()
											+ "<br>";
								}
								if (!c1.getTipo().equals(c2.getTipo())) {
									identica = false;
									panel += "- El tipo del campo " + c1.getCampo() + " cambia de " + c1.getTipo()
											+ " a " + c2.getTipo() + "<br>";
								}
								if (!c1.getValor().equals(c2.getValor())) {
									identica = false;
									panel += "- El valor del campo " + c1.getCampo() + " cambia de " + c1.getValor()
											+ " a " + c2.getValor() + "<br>";
								}
							} else {
								j = col1.size();
							}
						}
						Boolean pkIdentica = false;
						if (t1.getPk().equals(t2.getPk())) {
							pkIdentica = true;
						} else {
							panel += "- No tienen la misma PK.<br>";
						}

						Boolean fkIdentica = false;
						if (t1.getFk().equals(t2.getFk())) {
							fkIdentica = true;
						} else {
							panel += "- No tienen la misma FK.<br>";
						}
						Boolean triggerIdentica = false;
						if (t1.getTrigger().equals(t2.getTrigger())) {
							triggerIdentica = true;
						} else {
							panel += "- No tienen la misma FK.<br>";
						}

						if (identica && pkIdentica && fkIdentica && triggerIdentica) {
							panel += "&#10004; ES IDÉNTICA\n";
						} else {

						}
					}
				}
			}

		}
		for (int i = 0; i < nombreTablas2.size(); i++) {
			if (!b.contains(nombreTablas2.get(i).getNombre())) {
				panel += "<h2 style=\"background-color:#ABEBC6;\">" + nombreTablas2.get(i).getNombre()
						+ "</h2>&#10069; NUEVA TABLA";
			}
		}
		for (int i = 0; i < nombreTablas.size(); i++) {
			if (!a.contains(nombreTablas.get(i).getNombre())) {
				panel += "<h2 style=\"background-color:#85C1E9;\">" + nombreTablas.get(i).getNombre()
						+ "</h2>&#x2757; NO EXISTE LA TABLA";
			}
		}
		return panel;

	}
}
