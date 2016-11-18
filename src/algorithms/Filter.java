package algorithms;

import java.util.List;

public class Filter {

	public boolean[] getParralelIntervals(int[] chord) {

		boolean[] parralel = new boolean[12];
		if (chord[1] - chord[0] == 7) {
			parralel[0] = true;
		}
		if (chord[1] - chord[0] == 12) {
			parralel[1] = true;
		}
		if (chord[2] - chord[0] == 7) {
			parralel[2] = true;
		}
		if (chord[2] - chord[0] == 12) {
			parralel[3] = true;
		}
		if (chord[3] - chord[0] == 7) {
			parralel[4] = true;
		}
		if (chord[3] - chord[0] == 12) {
			parralel[5] = true;
		}
		if (chord[2] - chord[1] == 7) {
			parralel[6] = true;
		}
		if (chord[2] - chord[1] == 12) {
			parralel[7] = true;
		}
		if (chord[3] - chord[1] == 7) {
			parralel[8] = true;
		}
		if (chord[3] - chord[1] == 12) {
			parralel[9] = true;
		}
		if (chord[3] - chord[2] == 7) {
			parralel[10] = true;
		}
		if (chord[3] - chord[2] == 12) {
			parralel[11] = true;
		}
		return parralel;
	}

	public void noJump(List<int[]> rawChords, int[] prevChord) {

		for (int i = 0; i < rawChords.size(); i++) {
			int[] currChord = rawChords.get(i);
			boolean flag = false;
			for (int j = 1; j < 4; j++) {
				if (Math.abs(currChord[j] - prevChord[j]) > 4) {
					flag = true;
					break;
				}
			}
			if (flag) {
				rawChords.remove(i);
				i--;
			}
		}
	}

	// ������˶�

	// Concurrent
	public void concurrentFilter(List<int[]> rawChords, int[] prevChord) {

		for (int i = 0; i < rawChords.size(); i++) {
			int[] currChord = rawChords.get(i);
			if ((currChord[0] < prevChord[0] && currChord[1] < prevChord[1] && currChord[2] < prevChord[2]
					&& currChord[3] < prevChord[3])
					|| (currChord[0] > prevChord[0] && currChord[1] > prevChord[1] && currChord[2] > prevChord[2]
							&& currChord[3] > prevChord[3])) {
				rawChords.remove(i);
				i--;
			}
		}
		return;
	}

	// No third repetition allowed
	public void thirdRepetitonFilter(List<int[]> rawChords, int third) {

		for (int i = 0; i < rawChords.size(); i++) {

			int[] currChord = rawChords.get(i);
			int numberOfThird = 0;

			for (int note : currChord) {
				if (note % 12 == third)
					numberOfThird++;

			}
			if (numberOfThird > 1) {
				rawChords.remove(i);
				i--;
			}
		}
		return;
	}

	// Parallel perfect 5th/8th filter
	public void parralelFilter(List<int[]> rawChords, int[] prevChord) {

		boolean[] prevParralel = getParralelIntervals(prevChord);

		for (int i = 0; i < rawChords.size(); i++) {

			// Parallel check
			boolean[] currentParralel = getParralelIntervals(rawChords.get(i));
			boolean parallel = false;

			for (int j = 0; j < 12; j++) {

				if (currentParralel[j] == true && prevParralel[j] == true) {
					parallel = true;
					break;
				}
			}

			if (parallel) {
				rawChords.remove(i);
				i--;
			}
		}
		return;
	}
}
