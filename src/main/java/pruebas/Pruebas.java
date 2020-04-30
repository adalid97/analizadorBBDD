package pruebas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

public class Pruebas {

	public static void main(String[] args) throws FileNotFoundException, DiffException, PatchFailedException {
		String cadenaa = "Hola|Stackoverflow|en|español";
		String[] parts = cadenaa.split("\\|");
		System.out.println(Arrays.asList(parts));

//		FICHERO1
		JFileChooser fileChooser = new JFileChooser();
		int seleccion = fileChooser.showOpenDialog(null);
		File fichero = fileChooser.getSelectedFile();

		Scanner sc = new Scanner(fichero);
		String cadena = "";
		while (sc.hasNextLine()) {
			cadena += sc.nextLine();
		}

		System.out.println(cadena);
		String[] partes = cadena.split("db.");
		System.out.println(Arrays.asList(partes));

//		FICHERO2
		JFileChooser fileChooser2 = new JFileChooser();
		int seleccion2 = fileChooser2.showOpenDialog(null);
		File fichero2 = fileChooser.getSelectedFile();
		Scanner sc2 = new Scanner(fichero2);
		String cadena2 = "";
		while (sc2.hasNextLine()) {
			cadena2 += sc2.nextLine();
		}

		String[] partes2 = cadena2.split("db.");
		System.out.println(Arrays.asList(partes2));

//		for (int i = 0; i < partes2.length; i++) {
//			System.out.println(partes[i]);
//			System.out.println(partes2[i]);
//			System.out.println(partes[i].compareTo(partes2[i]));
////			if () {
////
////				System.out.println("Las tablas son iguales");
////			} else {
////				System.out.println("Las tablas son distintas");
////			}
//		}

		List<String> text1 = Arrays.asList(partes);
		List<String> text2 = Arrays.asList(partes2);

		// generating diff information.
		Patch<String> diff = DiffUtils.diff(text1, text2);

		// apply patch to original list

		DiffRowGenerator generator = DiffRowGenerator.create().showInlineDiffs(true).inlineDiffByWord(true)
				.oldTag(f -> "~").newTag(f -> "**").build();
		List<DiffRow> rows = generator.generateDiffRows(text1, text2);
		System.out.println("|original|new|");
		System.out.println("|--------|---|");
		for (DiffRow row : rows) {
			System.out.println("|" + row.getOldLine() + "|" + row.getNewLine() + "|");
		}

	}

}
