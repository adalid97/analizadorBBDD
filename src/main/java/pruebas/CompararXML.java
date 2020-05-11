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

			Tab tabla1 = new Tab(nombreTabla, arrayColumnas);
			nombreTablas.add(tabla1);

		}

		for (Tab t : nombreTablas) {
			System.out.println(t.getNombre());
			ArrayList<Columna> columna = t.getColumna();
			for (int i = 0; i < columna.size(); i++) {
				Columna c = columna.get(i);
				System.out.println(c.getCampo());
			}

		}

		System.out.println("---------------------------------.--------------------------");

		ArrayList<Tab> nombreTablas2 = new ArrayList<Tab>();
		File file2 = new File("response.xml");
		Builder builder2 = new Builder();
		Document doc2 = builder2.build(file2);

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

			Tab tabla1 = new Tab(nombreTabla2, arrayColumnas2);
			nombreTablas2.add(tabla1);

		}

		for (Tab t : nombreTablas2) {
			System.out.println(t.getNombre());
			ArrayList<Columna> columna = t.getColumna();
			for (int i = 0; i < columna.size(); i++) {
				Columna c = columna.get(i);
				System.out.println(c.getCampo());
			}

		}
//
//		if (nombreTablas2.containsAll(nombreTablas) == false) {
//			System.out.println("Son Diferentes");
//		}
//
//		nombreTablas.retainAll(nombreTablas2);
//		System.out.println(nombreTablas2);

//		List<Integer> lista = new ArrayList<>();
//		lista.add(1);
//		lista.add(3);
//		lista.add(2);
//		lista.add(3);
//		lista.add(1);
//		lista.add(1);
//		int repe = 0;
//		int k = 0;
//		List<Integer> repetidos = new ArrayList<>();
//		for (int x = 0; x < lista.size(); x++) {
//			for (int y = 0; y < lista.size() && !repetidos.contains(lista.get(x)); y++) {
//				if (lista.get(x).equals(lista.get(y)))
//					repe += 1;
//			}
//			if (repe > 0) {
//				repetidos.add(k, lista.get(x));
//				System.out.println("EL ELEMENTO " + repetidos.get(k) + " SE REPITE " + repe);
//				k++;
//			}
//			repe = 0;
//		}

//		int repe = 0;
//		int k = 0;
//		List<Integer> repetidos = new ArrayList<>();
//		for (int x = 0; x < nombreTablas.size(); x++) {
//			for (int y = 0; y < nombreTablas2.size(); y++) {
//				if (nombreTablas.get(x).equals(nombreTablas2.get(y))) {
//
//					panel += "La tabla " + nombreTablas.get(x) + " es idética";
//				}
//			}
//		}

		System.out.println("\n------\n");
		String panel = "";
		boolean no = false;
		for (Tab t1 : nombreTablas) {
			for (Tab t2 : nombreTablas2) {
				Boolean identica = true;
				if (t2.getNombre().equals(t1.getNombre())) {
					ArrayList<Columna> col1 = t1.getColumna();
					ArrayList<Columna> col2 = t2.getColumna();

					panel += "\nTABLA: " + t2.getNombre() + "\n";
					ArrayList<String> nombreCol = new ArrayList<String>();
					Columna c1 = null, c2 = null;
					System.out.println("**********");
					for (int i = 0; i < col1.size(); i++) {
						nombreCol.add(col1.get(i).getCampo().toString());
					}

					System.out.println(nombreCol);
					for (int j = 0; j < col1.size() && j < col2.size(); j++) {

						c1 = col1.get(j);
						c2 = col2.get(j);

						boolean existe = nombreCol.contains(c2.getCampo());

						if (existe) {

							if (!c1.getCampo().equals(c2.getCampo())) {
								identica = false;
								panel += "\tEl nombre del campo " + c1.getCampo() + " cambia por " + c2.getCampo()
										+ "\n";
							}
							if (!c1.getTipo().equals(c2.getTipo())) {
								identica = false;
								panel += "\tEl tipo del campo " + c1.getCampo() + " cambia de " + c1.getTipo() + " por "
										+ c2.getTipo() + "\n";
							}
							if (!c1.getValor().equals(c2.getValor())) {
								identica = false;
								panel += "\tEl valor del campo " + c1.getCampo() + " cambia de " + c1.getValor()
										+ " por " + c2.getValor() + "\n";
							}
						} else {
							j = col1.size();
						}
					}
					if (identica) {
						panel += "\tES IDÉNTICA\n";
					} else {
						panel += "\tLA TABLA NO ES IDÉNTICA";
					}

				}
			}
		}

		System.out.println(panel);
		// Collection<String> listOne = Arrays.asList("milan", "iga", "dingo", "iga",
		// "elpha", "iga", "hafil", "iga",
//				"meat", "iga", "neeta.peeta", "iga");
//		Collection<String> listTwo = Arrays.asList("hafil", "iga", "binga", "mike", "dingo", "dingo", "dingo");
//		Collection<String> similar = new HashSet<String>(listOne);
//		Collection<String> different = new HashSet<String>();
//		different.addAll(listOne);
//		different.addAll(listTwo);
//		similar.retainAll(listTwo);
//		different.removeAll(similar);
//		System.out.printf("One:%s%nTwo:%s%nSimilar:%s%nDifferent:%s%n", listOne, listTwo, similar, different);

	}

}
