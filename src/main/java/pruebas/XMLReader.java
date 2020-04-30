package pruebas;

import java.io.File;
import java.io.IOException;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

public class XMLReader {

	public static void main(String[] args) throws ParsingException, IOException {
		File file = new File("jejeFje.xml");
		// builder builds xml data
		Builder builder = new Builder();
		Document doc = builder.build(file);

		// get the root element <example>
		Element root = doc.getRootElement();

		// gets all element with tag <person>
		Elements tabla = root.getChildElements("tabla");

		for (int q = 0; q < tabla.size(); q++) {
			// get the current person element
			Element table = tabla.get(q);

			// get the name element and its children: first and last
			Elements columnaElement = table.getChildElements("columna");

			Element nombreTablaElement = table.getFirstChildElement("nombreTabla");
			String nombreTabla;
			nombreTabla = nombreTablaElement.getValue();
			System.out.println("nombreTabla: " + nombreTabla + "\n");

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

					System.out.println("campo: " + campo);
					System.out.println("tipo: " + tipo);
					System.out.println("valor: " + valor);
					System.out.println("");

//					System.out.println( "\t"+nombre+"\t\t"+tipo+"\t\t"+valor);

				} catch (NullPointerException ex) {
					ex.printStackTrace();
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("----------------\n");

		}
	}

}