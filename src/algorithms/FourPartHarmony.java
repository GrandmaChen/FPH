package algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FourPartHarmony {

	public FourPartHarmony() {
	}

	
	
	// // This function is to get the least movement concreteChord from the
	// // previous given concreteChord, which should be used at the end of
	// filter.
	// public ConcreteChord leastMovement(List<ConcreteChord> rawChords, int[]
	// prevChordNotes) {
	// return null;
	// }
	//
	// // Get a random concreteChord from the list.
	// public ConcreteChord randomChord(List<ConcreteChord> rawChords) {
	// return rawChords.get(new Random().nextInt(rawChords.size()));
	// }
	//
	// // This function is to get the least movement concreteChord from the
	// // previous given concreteChord, which should be used at the end of
	// filter.
	// public ConcreteChord mostCommonNotes(List<ConcreteChord> rawChords, int[]
	// prevChordNotes) {
	//
	// int[] temp = new int[rawChords.size()];
	// for (int i = 0; i < temp.length; i++) {
	// temp[i] = 0;
	// }
	// int count = 0;
	// for (ConcreteChord cc : rawChords) {
	// for (int i = 0; i < 4; i++) {
	// for (int j = 0; j < 4; j++) {
	// if (cc.getNotes(i) == prevChordNotes[j]) {
	// temp[count]++;
	// }
	// }
	// }
	// }
	//
	// int index = 0;
	// int max = temp[0];
	// for (int i = 0; i < temp.length; i++) {
	// if (temp[i] > max) {
	// max = temp[i];
	// index = i;
	// }
	// }
	//
	// return rawChords.get(index);
	//
	// }
	//
	// // Third repetition filter demo. "third" means third note of this chord,
	// // which is currChord.notes.get(1). More details to be added, not allow
	// any
	// // kinds of thirds repetition by now.
	// public void notAllowThirdRepetitionFilter(List<ConcreteChord> rawChords,
	// int third) {
	//
	// for (int i = 0; i < rawChords.size(); i++) {
	// int count = 0;
	// for (int j = 0; j < 4; j++) {
	// if (rawChords.get(i).getNotes(j) % 12 == third) {
	// count++;
	// }
	// }
	// if (count == 2) {
	// rawChords.remove(i);
	// i--;
	// }
	// }
	// }
	//
	// // Not allow unison filter, which is not allowed by default
	// public void notAlloweUnisonFilter(List<ConcreteChord> rawChords) {
	// for (int i = 0; i < rawChords.size(); i++) {
	// int[] notes = rawChords.get(i).getNotes();
	// if (notes[0] == notes[1] || notes[1] == notes[2] || notes[2] == notes[3])
	// {
	// rawChords.remove(i);
	// i--;
	// }
	// }
	// }
	//
	// // Position filter
	// public void positionFilter(List<ConcreteChord> rawChords, String
	// positionType) {
	//
	// if (positionType.equals("close")) {
	//
	// for (int i = 0; i < rawChords.size(); i++) {
	//
	// int[] notes = rawChords.get(i).getNotes();
	//
	// // If any interval between 2 notes exceeds tritone
	// if (notes[2] - notes[1] >= 6 || notes[3] - notes[2] >= 6) {
	//
	// rawChords.remove(i);
	// i--;
	// }
	// }
	// } else if (positionType.equals("open")) {
	//
	// for (int i = 0; i < rawChords.size(); i++) {
	//
	// int[] notes = rawChords.get(i).getNotes();
	//
	// // If any interval between 2 notes below tritone
	// if (notes[2] - notes[1] < 6 || notes[3] - notes[2] < 6) {
	//
	// rawChords.remove(i);
	// i--;
	// }
	// }
	// }
	// }

	// "range" is an array with size 8. range[0] is the lower bound of bass,
	// range[1] is is upper bound of bass, range[2] is the lower bound of tenor
	// and so on. This array should be {24, 50, 33, 57, 41, 65, 45, 69}
	// respectively by default.
	public List<int[]> getRawTriads(int[] NoteList, int inversion, int lowerBound, int upperBound) {

		List<Integer> rootNotes = new ArrayList<Integer>();
		List<Integer> thirdNotes = new ArrayList<Integer>();
		List<Integer> fifthNotes = new ArrayList<Integer>();
		List<Integer> allPossibilities = new ArrayList<Integer>();
		List<int[]> rawList = new ArrayList<int[]>();

		for (int i = 0; i < 10; i++) {

			if (NoteList[0] + 12 * i <= upperBound && NoteList[0] + 12 * i >= lowerBound) {
				rootNotes.add(NoteList[0] + 12 * i);
			}

			if (NoteList[1] + 12 * i <= upperBound && NoteList[1] + 12 * i >= lowerBound) {
				thirdNotes.add(NoteList[1] + 12 * i);
			}

			if (NoteList[2] + 12 * i <= upperBound && NoteList[2] + 12 * i >= lowerBound) {
				fifthNotes.add(NoteList[2] + 12 * i);
			}
		}

		allPossibilities.addAll(rootNotes);
		allPossibilities.addAll(thirdNotes);
		allPossibilities.addAll(fifthNotes);

		// "inversion" decides which note to be bass note
		List<Integer> bass = null;
		if (inversion == 0)
			bass = rootNotes;
		else if (inversion == 1)
			bass = thirdNotes;
		else if (inversion == 2)
			bass = fifthNotes;

		// Get all possible combinations of 4 notes
		for (int n1 : bass) {
			if (n1 >= lowerBound && n1 <= upperBound) {
				for (int n2 : allPossibilities) {
					if (n2 > n1 && n2 <= upperBound) {
						for (int n3 : allPossibilities) {
							if (n3 > n2 && n3 <= upperBound) {
								for (int n4 : allPossibilities) {
									if (n4 > n3 && n4 <= upperBound) {
										List<Integer> check = new ArrayList<Integer>();
										check.add(n1 % 12);
										check.add(n2 % 12);
										check.add(n3 % 12);
										check.add(n4 % 12);
										Set<Integer> set = new HashSet<Integer>(check);
										if (set.size() == check.size() - 1 && n2 - n1 <= 24 && n3 - n2 <= 12
												&& n4 - n3 <= 12) {
											int[] chord = new int[4];
											chord[0] = n1;
											chord[1] = n2;
											chord[2] = n3;
											chord[3] = n4;
											rawList.add(chord);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return rawList;
	}

	// Similar to previous one
	public List<int[]> getRawSeventhChords(int[] NoteList, int inversion, int lowerBound, int upperBound) {

		List<Integer> rootNotes = new ArrayList<Integer>();
		List<Integer> thirdNotes = new ArrayList<Integer>();
		List<Integer> fifthNotes = new ArrayList<Integer>();
		List<Integer> seventhNotes = new ArrayList<Integer>();
		List<Integer> allPossibilities = new ArrayList<Integer>();
		List<int[]> rawList = new ArrayList<int[]>();

		for (int i = 0; i < 10; i++) {

			if (NoteList[0] + 12 * i <= upperBound && NoteList[0] + 12 * i >= lowerBound) {
				rootNotes.add(NoteList[0] + 12 * i);
			}

			if (NoteList[1] + 12 * i <= upperBound && NoteList[1] + 12 * i >= lowerBound) {
				thirdNotes.add(NoteList[1] + 12 * i);
			}

			if (NoteList[2] + 12 * i <= upperBound && NoteList[2] + 12 * i >= lowerBound) {
				fifthNotes.add(NoteList[2] + 12 * i);
			}

			if (NoteList[3] + 12 * i <= upperBound && NoteList[3] + 12 * i >= lowerBound) {
				seventhNotes.add(NoteList[3] + 12 * i);
			}

		}

		allPossibilities.addAll(rootNotes);
		allPossibilities.addAll(thirdNotes);
		allPossibilities.addAll(fifthNotes);
		allPossibilities.addAll(seventhNotes);

		List<Integer> bass = null;
		if (inversion == 0)
			bass = rootNotes;
		else if (inversion == 1)
			bass = thirdNotes;
		else if (inversion == 2)
			bass = fifthNotes;
		else if (inversion == 3)
			bass = seventhNotes;

		for (int n1 : bass) {
			if (n1 >= lowerBound && n1 <= upperBound) {
				for (int n2 : allPossibilities) {
					if (n2 > n1 && n2 <= upperBound) {
						for (int n3 : allPossibilities) {
							if (n3 > n2 && n3 <= upperBound) {
								for (int n4 : allPossibilities) {
									if (n4 > n3 && n4 <= upperBound) {
										List<Integer> check = new ArrayList<Integer>();
										check.add(n1 % 12);
										check.add(n2 % 12);
										check.add(n3 % 12);
										check.add(n4 % 12);
										Set<Integer> set = new HashSet<Integer>(check);
										if (set.size() == 4 && n2 - n1 <= 24 && n3 - n2 <= 12 && n4 - n3 <= 12) {
											int[] chord = new int[4];
											chord[0] = n1;
											chord[1] = n2;
											chord[2] = n3;
											chord[3] = n4;
											rawList.add(chord);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return rawList;
	}
}
