package pruebas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Pruebas {

	public static void main(String[] args) {
		ArrayList<String> listaA = new ArrayList<String>();
		ArrayList<String> listaB = new ArrayList<String>();

		listaA.add("uno");
		listaA.add("dos");
		listaA.add("tres");
		listaA.add("cuatro");
		listaA.add("cinco");

		listaB.add("uno");
		listaB.add("dos");
		listaB.add("tres");
		listaB.add("cuatro");
		listaB.add("cinco");
		listaB.add("seis");
		listaB.add("siete");
		listaB.add("ocho");
		listaB.add("nueve");
		listaB.add("diez");
		// Aqui por ejemplo debería recuperar un tercer `ArrayList` con los valores:
		// Seis, Siete, ocho, nueve,diez
		// Los valores no necesariamente vendrán en el mismo orden en el archivo.

		List<String> lista = listaB.stream().filter(f -> !listaA.contains(f)).collect(Collectors.toList());
		System.out.println(lista);

	}

}
