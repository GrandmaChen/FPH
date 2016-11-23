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

//	public void mostCommonNotes(List<int[]> rawChords, int[] prevChord) {
//
//		int[] temp = new int[rawChords.size()];
//		for (int i = 0; i < temp.length; i++) {
//			temp[i] = 0;
//		}
//		int count = 0;
//		for (ConcreteChord cc : rawChords) {
//			for (int i = 0; i < 4; i++) {
//				for (int j = 0; j < 4; j++) {
//					if (cc.getNotes(i) == prevChord[j]) {
//						temp[count]++;
//					}
//				}
//			}
//		}
//
//		int index = 0;
//		int max = temp[0];
//		for (int i = 0; i < temp.length; i++) {
//			if (temp[i] > max) {
//				max = temp[i];
//				index = i;
//			}
//		}
//
//		return rawChords.get(index);
//
//	}
	
	public void JumpFilter(List<int[]> rawChords, int[] prevChord) {

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

	public void preDecidedBassFilter(List<int[]> rawChords, int bass) {

		for (int i = 0; i < rawChords.size(); i++) {

			int[] currChord = rawChords.get(i);
			if (currChord[0] != bass) {
				rawChords.remove(i);
				i--;
			}
		}
		return;
	}

	public void preDecidedTenorFilter(List<int[]> rawChords, int tenor) {

		for (int i = 0; i < rawChords.size(); i++) {

			int[] currChord = rawChords.get(i);
			if (currChord[1] != tenor) {
				rawChords.remove(i);
				i--;
			}
		}
		return;
	}

	public void preDecidedAltoFilter(List<int[]> rawChords, int alto) {

		for (int i = 0; i < rawChords.size(); i++) {

			int[] currChord = rawChords.get(i);
			if (currChord[2] != alto) {
				rawChords.remove(i);
				i--;
			}
		}
		return;
	}

	public void preDecidedSopranoFilter(List<int[]> rawChords, int soprano) {

		for (int i = 0; i < rawChords.size(); i++) {

			int[] currChord = rawChords.get(i);
			if (currChord[3] != soprano) {
				rawChords.remove(i);
				i--;
			}
		}
		return;
	}

	// 外声部隐伏五八度
	// 两个外声部同方向进行到8度或5度，高音部一跳进，就错
	public void exteriorHiddenParralelFilterToPrev(List<int[]> rawChords, int[] prevChord) {

		for (int i = 0; i < rawChords.size(); i++) {

			int[] currChord = rawChords.get(i);

			// If not 5th/8th
			if (!((currChord[3] - currChord[0] == 7) || (currChord[3] - currChord[0] == 12))) {
				continue;
			}

			// 同向
			if (!((currChord[0] - prevChord[0] < 0) && (currChord[3] - prevChord[3] < 0)
					|| (currChord[0] - prevChord[0] > 0) && (currChord[3] - prevChord[3] > 0))) {
				continue;
			}

			// 高音跳进
			if (!(Math.abs(currChord[3] - prevChord[3]) > 3)) {
				continue;
			}

			rawChords.remove(i);
			i--;

		}
		return;
	}

	public void exteriorHiddenParralelFilterToNext(List<int[]> rawChords, int[] nextChord) {

		// If not 5th/8th
		if (!((nextChord[3] - nextChord[0] == 7) || (nextChord[3] - nextChord[0] == 12))) {
			return;
		}

		for (int i = 0; i < rawChords.size(); i++) {

			int[] currChord = rawChords.get(i);

			// 同向
			if (!((currChord[0] - nextChord[0] < 0) && (currChord[3] - nextChord[3] < 0)
					|| (currChord[0] - nextChord[0] > 0) && (currChord[3] - nextChord[3] > 0))) {
				continue;
			}

			// 高音跳进
			if (!(Math.abs(currChord[3] - nextChord[3]) > 3)) {
				continue;
			}
			rawChords.remove(i);
			i--;
		}
		return;
	}

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
