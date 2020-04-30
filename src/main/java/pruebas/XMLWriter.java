package pruebas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

public class XMLWriter {

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		// root element <example>
		Element root = new Element("infoBaseDatos");

		Tablae[] table = { new Tablae("AYUNTAMIENTO", "idAyuntamiento", "NUMBER", "3"),
				new Tablae("AYUNTAMIENTO", "nombre", "VARCHAR2", "50") };

		Element tablaElement = new Element("tabla");

		Element nombreTablaElement = new Element("nombreTabla");
		tablaElement.appendChild(nombreTablaElement);
		for (int i = 0; i < 1; i++) {
			nombreTablaElement.appendChild(table[i].getNombreTabla());
		}

		for (Tablae tab : table) {

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

		root.appendChild(tablaElement);

		// create doc off of root
		Document doc = new Document(root);

		// the file it will be stored in
		File file = new File("outw.xml");
		if (!file.exists()) {
			file.createNewFile();
		}

		// get a file output stream ready
		FileOutputStream fileOutputStream = new FileOutputStream(file);

		// use the serializer class to write it all
		Serializer serializer = new Serializer(fileOutputStream, "UTF-8");
		serializer.setIndent(4);
		serializer.write(doc);
	}

}
