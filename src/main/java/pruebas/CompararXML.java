package pruebas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class CompararXML {
	public static void main(String[] args) throws ValidityException, ParsingException, IOException {
		ArrayList<Tab> nombreTablas = new ArrayList<Tab>();
		File file = new File("original.xml");
		Builder builder = new Builder();
		Document doc = builder.build(file);

		Element root = doc.getRootElement();

		Elements tabla = root.getChildElements("tabla");

		for (int q = 0; q < tabla.size(); q++) {
			Element table = tabla.get(q);
			Element nombreTablaElement = table.getFirstChildElement("nombreTabla");
			String nombreTabla;
			nombreTabla = nombreTablaElement.getValue();
			Tab tabla1 = new Tab(nombreTabla);
			nombreTablas.add(tabla1);

		}

		for (Tab t : nombreTablas) {
			System.out.println(t.getNombre());
		}

		System.out.println("---------------------------------.--------------------------");
//		-----------------------------

		ArrayList<Tab> nombreTablas2 = new ArrayList<Tab>();
		File file2 = new File("response.xml");
		Builder builder2 = new Builder();
		Document doc2 = builder2.build(file2);

		Element root2 = doc2.getRootElement();

		Elements tabla2 = root2.getChildElements("tabla");

		for (int q = 0; q < tabla2.size(); q++) {
			Element table = tabla2.get(q);
			Element nombreTablaElement = table.getFirstChildElement("nombreTabla");
			String nombreTabla2;
			nombreTabla2 = nombreTablaElement.getValue();
			Tab tabla1 = new Tab(nombreTabla2);
			nombreTablas2.add(tabla1);

		}

		for (Tab t : nombreTablas2) {
			System.out.println(t.getNombre());
		}

		if (nombreTablas2.containsAll(nombreTablas) == false) {
			System.out.println("Son Diferentes");
		}

		nombreTablas.retainAll(nombreTablas2);
		System.out.println(nombreTablas2);

	}

}
