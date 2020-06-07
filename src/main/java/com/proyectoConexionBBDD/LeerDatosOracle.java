package com.proyectoConexionBBDD;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

public class LeerDatosOracle {
	String panel;
	Document docRead = null;
	Element root;
	Elements tabla;
	Document doc;

	public String leerDatos() {

		tabla = root.getChildElements("tabla");

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
