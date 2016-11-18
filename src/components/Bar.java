package components;

public class Bar {

	private String chordName;
	private int upperBound;
	private int lowerBound;
	private int[] chord;

	private int[] preDecidedNotes;

	public Bar(String chordName, int upperBound, int lowerBound) {
		this.chordName = chordName;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.chord = new int[4];
	}

	public void preDecide(int[] preDecidedNotes) {
		this.preDecidedNotes = preDecidedNotes;
	}

}