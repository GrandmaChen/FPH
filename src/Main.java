import java.io.IOException;
import java.util.List;
import org.jfugue.player.Player;
import algorithms.Filter;
import algorithms.FourPartHarmony;
import algorithms.Interpreter;
import algorithms.NoteList;

public class Main {
	public static void main(String[] args) throws NumberFormatException, IOException {

		// UIPanal ui = new UIPanal("ËÄ²¿ºÍÉù", 500, 500);
		// ui.setVisible(true);

		FourPartHarmony fph = new FourPartHarmony();

		Interpreter interpreter = new Interpreter();
		NoteList notelist = new NoteList();

		Filter filter = new Filter();

		int[] test = notelist.getRawNotes("C", "dom7");

		List<int[]> testing = fph.getRawTriads(test, 0, 50, 80);
		System.out.println(testing.size());
		filter.thirdRepetitonFilter(testing, test[1]);
		System.out.println(testing.size());
		Player player = new Player();

		// player.play("C2ww+C3ww+E3ww+G3ww");

		for (int[] array : testing) {

			String chord = "";

			for (int i = 0; i < array.length; i++) {
				if (i < array.length - 1) {
					// System.out.println(i);
					// System.out.println(interpreter.numberToLetter(array[i],
					// true));
					chord += interpreter.numberToLetter(array[i], true) + "+";
				} else {
					chord += interpreter.numberToLetter(array[i], true) + "";
				}
			}
			System.out.println(chord);
			player.play(chord);

			// for (int i : array)

			// System.out.println(i);
			// System.out.print(interpreter.numberToLetter(i, true) + " ");

			// System.out.println();
		}

	}
}
