package pruebas;

import java.util.Arrays;
import java.util.List;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

public class ComputeDifference {
	public static void main(String[] args) throws DiffException, PatchFailedException {

		List<String> text1 = Arrays.asList("this is a test", "a test");
		List<String> text2 = Arrays.asList("this is a testfile", "a test");

		// generating diff information.
		Patch<String> diff = DiffUtils.diff(text1, text2);

		// generating unified diff format
		List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff("original-file.txt", "new-file.txt", text1,
				diff, 0);

		unifiedDiff.forEach(System.out::println);

		// importing unified diff format from file or here from memory to a Patch
		Patch<String> importedPatch = UnifiedDiffUtils.parseUnifiedDiff(unifiedDiff);

		// apply patch to original list
		List<String> patchedText = DiffUtils.patch(text1, importedPatch);

		System.out.println(patchedText);

		DiffRowGenerator generator = DiffRowGenerator.create().showInlineDiffs(true).inlineDiffByWord(true)
				.oldTag(f -> "~").newTag(f -> "**").build();
		List<DiffRow> rows = generator.generateDiffRows(
				Arrays.asList("This is a test senctence.", "This is the second line.", "And here is the finish."),
				Arrays.asList("This is a test for diffutils.", "This is the second line."));

		System.out.println("|original|new|");
		System.out.println("|--------|---|");
		for (DiffRow row : rows) {
			System.out.println("|" + row.getOldLine() + "|" + row.getNewLine() + "|");
		}

	}
}
