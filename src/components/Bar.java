package components;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfugue.player.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import algorithms.Filter;
import algorithms.FourPartHarmony;
import algorithms.Interpreter;
import algorithms.ListGetter;

public class Bar {

	private Label barNumber;
	private ObservableList<Chord> rawChords;
	private ObservableList<Chord> rangeTempChords;
	private int[] rawNotes;
	public Chord chosenResult;
	private Button choose;
	private TableView<Chord> resultShowcaseTable;
	private BorderPane tablePage;
	private CheckBox isAugFourth;
	private CheckBox play;
	private Stage chordSort;
	private Scene chordEdit;
	private Group imagePane;

	private ListGetter noteList;
	private FourPartHarmony fph;
	private Interpreter interpreter;
	private Player player;
	private Filter filter;

	private ComboBox<String> chordRootComboBox;
	private ComboBox<String> ChordtypeComboBox;
	private ComboBox<String> inversionBox;
	private ComboBox<String> duration;
	private boolean inversionBoxExists;
	private boolean tableExists;
	private boolean chooseExists;
	private boolean durationExists;

	private int barNum;

	private Canvas canvas;
	private File file;
	private Image image;
	private GraphicsContext gc;

	private VBox container;
	private HBox rootNote;
	private HBox chordType;

	private boolean inSharp;

	private Slider lowerBoundSlider;
	private Slider upperBoundSlider;

	private Bar next;
	private Bar prev;

	public Bar(int barNumber, boolean inSharp) throws IOException {

		this.lowerBoundSlider = new Slider(21, 108, 47);
		this.upperBoundSlider = new Slider(21, 108, 100);

		this.canvas = new Canvas(626, 55);
		this.file = new File("piano.png");
		this.image = new Image(file.toURI().toString());
		this.gc = canvas.getGraphicsContext2D();
		this.setCanvas(canvas, image);

		this.imagePane = new Group();

		this.inSharp = inSharp;
		this.barNum = barNumber;

		this.next = null;
		this.prev = null;

		this.chosenResult = null;

		this.inversionBoxExists = false;
		this.chooseExists = false;
		this.durationExists = false;

		this.isAugFourth = new CheckBox("三全音算增四度");
		this.play = new CheckBox("选中时弹奏和弦");
		this.play.setSelected(true);

		this.duration = new ComboBox<String>(FXCollections.observableArrayList(new String[] { "全音符", "二分音符", "四分音符" }));
		this.setDefaultDuration();

		this.noteList = new ListGetter();
		this.fph = new FourPartHarmony();
		this.interpreter = new Interpreter();
		this.player = new Player();
		this.filter = new Filter();

		this.resultShowcaseTable = new TableView<>();

		this.tablePage = new BorderPane();

		this.barNumber = new Label(Integer.toString(barNumber));
		this.rawChords = FXCollections.observableArrayList();
		this.rangeTempChords = FXCollections.observableArrayList();

		this.container = new VBox();
		this.rootNote = new HBox();
		this.chordType = new HBox();

		if (inSharp) {
			chordRootComboBox = new ComboBox<String>(FXCollections.observableArrayList(
					new String[] { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" }));
			this.rootNote.getChildren().addAll(chordRootComboBox);
		} else {
			chordRootComboBox = new ComboBox<String>(FXCollections.observableArrayList(
					new String[] { "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B" }));
			this.rootNote.getChildren().addAll(chordRootComboBox);
		}

		this.inversionBox = new ComboBox<String>();

		chordRootComboBox.valueProperty().addListener(e -> {
			if (!this.ChordtypeComboBox.getSelectionModel().isEmpty()) {
				if (ChordtypeComboBox.getSelectionModel().getSelectedIndex() < 6) {
					this.inversionBox = new ComboBox<String>(
							FXCollections.observableArrayList(new String[] { "原位", "第1转位", "第2转位" }));
				} else {
					this.inversionBox = new ComboBox<String>(
							FXCollections.observableArrayList(new String[] { "原位", "第1转位", "第2转位", "第3转位" }));
				}
				if (this.inversionBoxExists) {
					container.getChildren().set(3, this.inversionBox);
				}
				startResult();
				if (!this.inversionBoxExists)
					container.getChildren().addAll(inversionBox);

				inversionBoxExists = true;
			}
		});

		ChordtypeComboBox = new ComboBox<String>(this.noteList.getChordList());

		ChordtypeComboBox.valueProperty().addListener(e -> {
			if (!this.chordRootComboBox.getSelectionModel().isEmpty()) {

				if (ChordtypeComboBox.getSelectionModel().getSelectedIndex() < 6) {
					this.inversionBox = new ComboBox<String>(
							FXCollections.observableArrayList(new String[] { "原位", "第1转位", "第2转位" }));
				} else {
					this.inversionBox = new ComboBox<String>(
							FXCollections.observableArrayList(new String[] { "原位", "第1转位", "第2转位", "第3转位" }));
				}
				if (this.inversionBoxExists) {
					container.getChildren().set(3, this.inversionBox);
				}
				startResult();
				if (!this.inversionBoxExists)
					container.getChildren().addAll(inversionBox);
				inversionBoxExists = true;
			}
		});

		chordType.getChildren().addAll(ChordtypeComboBox);
		container.getChildren().addAll(this.barNumber, rootNote, chordType);

	}

	@SuppressWarnings("unchecked")
	public void startResult() {
		inversionBox.valueProperty().addListener(e -> {
			try {

				if (!this.tableExists) {
					this.rawNotes = noteList.getNotesList(chordRootComboBox.getValue(), ChordtypeComboBox.getValue());

					List<int[]> chords;

					if (ChordtypeComboBox.getSelectionModel().getSelectedIndex() < 6) {
						chords = fph.getRawTriads(rawNotes, inversionBox.getSelectionModel().getSelectedIndex());

					} else {
						chords = fph.getRawSeventhOrNinthChords(rawNotes,
								inversionBox.getSelectionModel().getSelectedIndex());
					}

					for (int[] tempChord : chords) {

						Chord chord = new Chord(this.interpreter.numberToLetter(tempChord[0], this.inSharp),
								this.interpreter.numberToLetter(tempChord[1], this.inSharp),
								this.interpreter.numberToLetter(tempChord[2], this.inSharp),
								this.interpreter.numberToLetter(tempChord[3], this.inSharp), tempChord);

						this.rawChords.add(chord);
					}

					// Filter
					if (this.prev != null && this.prev.chosenResult != null) {
						this.filter.parralelFilter(this.rawChords, this.prev.chosenResult);
						this.filter.concurrentFilter(this.rawChords, this.prev.chosenResult);
						this.filter.exteriorHiddenParralelFilterToPrev(this.rawChords, this.prev.chosenResult);

					}

					if (this.next != null && this.next.chosenResult != null) {
						this.filter.exteriorHiddenParralelFilterToNext(this.rawChords, this.next.chosenResult);
						this.filter.concurrentFilter(this.rawChords, this.next.chosenResult);
						this.filter.parralelFilter(this.rawChords, this.next.chosenResult);
					}

					if (this.rawNotes[1] == 3 || this.rawNotes[1] == 4)
						this.filter.thirdRepetitonFilter(this.rawChords, this.rawNotes[1]);

					for (Chord chord : this.filter.rangeFilter(this.rawChords, (int) lowerBoundSlider.getValue(),
							(int) upperBoundSlider.getValue())) {
						this.rangeTempChords.add(chord);
					}

					if (!this.durationExists) {
						this.container.getChildren().addAll(duration);
						this.durationExists = true;
					}

					if (!this.chooseExists) {
						choose = new Button("请选择和弦");
						this.container.getChildren().addAll(choose);
						this.chooseExists = true;

						choose.setOnAction(c -> {
							this.chordSort.show();
						});
					}

					// When a chord is chosen
					resultShowcaseTable.setRowFactory(tv -> {
						TableRow<Chord> row = new TableRow<>();
						row.setOnMouseClicked(event -> {
							if (event.getClickCount() == 1 && (!row.isEmpty())) {
								Chord chord = row.getItem();
								String chordName = chord.getBass() + "+" + chord.getTenor() + "+" + chord.getAlto()
										+ "+" + chord.getSoprano();
								this.chosenResult = chord;
								choose.setText(chordName);

								canvas = new Canvas(626, 55);
								file = new File("piano.png");
								image = new Image(file.toURI().toString());
								gc = canvas.getGraphicsContext2D();
								setCanvas(canvas, image);
								imagePane.getChildren().set(0, canvas);
								this.render();

								if (this.play.isSelected())
									play(chord);

							}
						});
						return row;
					});

					TableColumn<Chord, String> bass = new TableColumn<>("低音");
					bass.setCellValueFactory(new PropertyValueFactory<>("bass"));

					TableColumn<Chord, String> tenor = new TableColumn<>("次中音");
					tenor.setCellValueFactory(new PropertyValueFactory<>("tenor"));

					TableColumn<Chord, String> alto = new TableColumn<>("中音");
					alto.setCellValueFactory(new PropertyValueFactory<>("alto"));

					TableColumn<Chord, String> soprano = new TableColumn<>("高音");
					soprano.setCellValueFactory(new PropertyValueFactory<>("soprano"));

					resultShowcaseTable.setItems(this.rangeTempChords);
					resultShowcaseTable.getColumns().addAll(bass, tenor, alto, soprano);

					// Sort buttons
					Button deleteClose = new Button("去除紧密排列");
					deleteClose.setOnMouseClicked(filter -> {
						ObservableList<Chord> temp = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							temp.add(chord);
						this.filter.deleteCloseFilter(temp, this.isAugFourth.isSelected());
						resultShowcaseTable.setItems(temp);

						rangeTempChords = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							rangeTempChords.add(chord);
					});

					Button deleteOpen = new Button("去除开放排列");
					deleteOpen.setOnAction(filter -> {
						ObservableList<Chord> temp = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							temp.add(chord);
						this.filter.deleteOpenFilter(temp, this.isAugFourth.isSelected());
						resultShowcaseTable.setItems(temp);

						rangeTempChords = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							rangeTempChords.add(chord);
					});

					Button onlyClose = new Button("只要紧密排列");
					onlyClose.setOnMouseClicked(filter -> {
						ObservableList<Chord> temp = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							temp.add(chord);

						resultShowcaseTable.setItems(this.filter.onlyCloseFilter(temp, this.isAugFourth.isSelected()));

						rangeTempChords = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							rangeTempChords.add(chord);
					});

					Button onlyOpen = new Button("只要开放排列");
					onlyOpen.setOnMouseClicked(filter -> {
						ObservableList<Chord> temp = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							temp.add(chord);

						resultShowcaseTable.setItems(this.filter.onlyOpenFilter(temp, this.isAugFourth.isSelected()));

						rangeTempChords = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							rangeTempChords.add(chord);
					});

					Button noJumpToPrev = new Button("去除对前超过跳进音程");
					noJumpToPrev.setOnAction(filter -> {
						if (this.prev != null && this.prev.chosenResult != null) {
							ObservableList<Chord> temp = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								temp.add(chord);
							this.filter.JumpFilter(temp, this.prev.chosenResult);
							resultShowcaseTable.setItems(temp);

							rangeTempChords = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								rangeTempChords.add(chord);
						}
					});

					Button noJumpToNext = new Button("去除对后超过跳进音程");
					noJumpToNext.setOnAction(filter -> {
						if (this.next != null && this.next.chosenResult != null) {
							ObservableList<Chord> temp = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								temp.add(chord);
							this.filter.JumpFilter(temp, this.next.chosenResult);
							resultShowcaseTable.setItems(temp);

							rangeTempChords = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								rangeTempChords.add(chord);
						}
					});

					Button connectBassToPrev = new Button("对前低音连接");
					connectBassToPrev.setOnAction(filter -> {
						if (this.prev != null && this.prev.chosenResult != null) {
							ObservableList<Chord> temp = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								temp.add(chord);
							this.filter.connectPartFilter(temp, this.prev.chosenResult, 0);
							resultShowcaseTable.setItems(temp);

							rangeTempChords = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								rangeTempChords.add(chord);
						}
					});

					Button connectBassToNext = new Button("对后低音连接");
					connectBassToNext.setOnAction(filter -> {
						if (this.next != null && this.next.chosenResult != null) {
							ObservableList<Chord> temp = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								temp.add(chord);
							this.filter.connectPartFilter(temp, this.next.chosenResult, 0);
							resultShowcaseTable.setItems(temp);

							rangeTempChords = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								rangeTempChords.add(chord);
						}
					});

					Button connectTenorToPrev = new Button("对前次中音连接");
					connectTenorToPrev.setOnAction(filter -> {
						if (this.prev != null && this.prev.chosenResult != null) {
							ObservableList<Chord> temp = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								temp.add(chord);
							this.filter.connectPartFilter(temp, this.prev.chosenResult, 1);
							resultShowcaseTable.setItems(temp);

							rangeTempChords = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								rangeTempChords.add(chord);
						}
					});

					Button connectTenorToNext = new Button("对后次中音连接");
					connectTenorToNext.setOnAction(filter -> {
						if (this.next != null && this.next.chosenResult != null) {
							ObservableList<Chord> temp = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								temp.add(chord);
							this.filter.connectPartFilter(temp, this.next.chosenResult, 1);
							resultShowcaseTable.setItems(temp);

							rangeTempChords = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								rangeTempChords.add(chord);
						}
					});

					Button connectAltoToPrev = new Button("对前中音连接");
					connectAltoToPrev.setOnAction(filter -> {
						if (this.prev != null && this.prev.chosenResult != null) {
							ObservableList<Chord> temp = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								temp.add(chord);
							this.filter.connectPartFilter(temp, this.prev.chosenResult, 2);
							resultShowcaseTable.setItems(temp);

							rangeTempChords = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								rangeTempChords.add(chord);
						}
					});

					Button connectAltoToNext = new Button("对后中音连接");
					connectAltoToNext.setOnAction(filter -> {
						if (this.next != null && this.next.chosenResult != null) {
							ObservableList<Chord> temp = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								temp.add(chord);
							this.filter.connectPartFilter(temp, this.next.chosenResult, 2);
							resultShowcaseTable.setItems(temp);

							rangeTempChords = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								rangeTempChords.add(chord);
						}
					});

					Button connectSopranoToPrev = new Button("对前高音连接");
					connectSopranoToPrev.setOnAction(filter -> {
						if (this.prev != null && this.prev.chosenResult != null) {
							ObservableList<Chord> temp = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								temp.add(chord);
							this.filter.connectPartFilter(temp, this.prev.chosenResult, 3);
							resultShowcaseTable.setItems(temp);

							rangeTempChords = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								rangeTempChords.add(chord);
						}
					});

					Button connectSopranoToNext = new Button("对后高音连接");
					connectSopranoToNext.setOnAction(filter -> {
						if (this.next != null && this.next.chosenResult != null) {
							ObservableList<Chord> temp = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								temp.add(chord);
							this.filter.connectPartFilter(temp, this.next.chosenResult, 3);
							resultShowcaseTable.setItems(temp);

							rangeTempChords = FXCollections.observableArrayList();
							for (Chord chord : resultShowcaseTable.getItems())
								rangeTempChords.add(chord);
						}
					});

					Button noMinor2nd = new Button("去除小二度");
					noMinor2nd.setOnAction(filter -> {

						ObservableList<Chord> temp = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							temp.add(chord);
						this.filter.minor2ndFilter(temp);
						resultShowcaseTable.setItems(temp);

						rangeTempChords = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							rangeTempChords.add(chord);

					});

					Button restore = new Button("还原");

					restore.setOnAction(e3 -> {

						this.rangeTempChords.clear();

						for (Chord chord : this.filter.rangeFilter(this.rawChords, (int) lowerBoundSlider.getValue(),
								(int) upperBoundSlider.getValue())) {
							this.rangeTempChords.add(chord);
						}

						resultShowcaseTable.setItems(this.rangeTempChords);
					});

					VBox list1 = new VBox();
					VBox list2 = new VBox();
					HBox list3 = new HBox();

					Label connect = new Label("连接筛选");
					Label interval = new Label("音程筛选");

					list1.getChildren().addAll(connect, connectBassToNext, connectBassToPrev, connectTenorToNext,
							connectTenorToPrev, connectAltoToNext, connectAltoToPrev, connectSopranoToNext,
							connectSopranoToPrev, noMinor2nd);
					list2.getChildren().addAll(interval, deleteClose, deleteOpen, onlyClose, onlyOpen, noJumpToPrev,
							noJumpToNext);
					list3.getChildren().addAll(this.isAugFourth, this.play, restore);

					TilePane sortFunctions = new TilePane();
					sortFunctions.getChildren().addAll(list1, list2);

					Label lowerBoundLabel = new Label("下限：");
					Label upperBoundLabel = new Label("上限：");
					Label lowerBoundValue = new Label("A0");
					Label upperBoundValue = new Label("C8");
					lowerBoundSlider.setOrientation(Orientation.VERTICAL);
					upperBoundSlider.setOrientation(Orientation.VERTICAL);

					lowerBoundSlider.valueProperty().addListener(a -> {
						lowerBoundValue.textProperty()
								.setValue(interpreter.numberToLetter((int) lowerBoundSlider.getValue(), this.inSharp));
						ObservableList<Chord> temp = this.filter.rangeFilter(this.rawChords,
								(int) lowerBoundSlider.getValue(), (int) upperBoundSlider.getValue());
						resultShowcaseTable.setItems(temp);
					});

					upperBoundSlider.valueProperty().addListener(a -> {
						upperBoundValue.textProperty()
								.setValue(interpreter.numberToLetter((int) upperBoundSlider.getValue(), this.inSharp));
						ObservableList<Chord> temp = this.filter.rangeFilter(this.rawChords,
								(int) lowerBoundSlider.getValue(), (int) upperBoundSlider.getValue());
						resultShowcaseTable.setItems(temp);
					});

					HBox range = new HBox();
					range.getChildren().addAll(lowerBoundLabel, lowerBoundValue, lowerBoundSlider, upperBoundLabel,
							upperBoundValue, upperBoundSlider);

					this.imagePane.getChildren().add(canvas);

					this.tablePage.setLeft(resultShowcaseTable);
					this.tablePage.setCenter(sortFunctions);
					this.tablePage.setRight(range);

					TilePane bottom = new TilePane();
					bottom.getChildren().addAll(list3);

					this.tablePage.setBottom(bottom);
					this.tablePage.setTop(imagePane);

					this.chordSort = new Stage();
					this.chordSort.setTitle("第" + this.barNum + "小节");
					this.chordEdit = new Scene(tablePage, 626, 558);
					this.chordSort.setResizable(false);
					this.chordSort.setScene(chordEdit);
					this.chordSort.show();

					this.tableExists = true;

					// If table exists
				} else {
					List<int[]> chords;

					if (ChordtypeComboBox.getSelectionModel().getSelectedIndex() < 6) {
						chords = fph.getRawTriads(rawNotes, inversionBox.getSelectionModel().getSelectedIndex());

					} else {
						chords = fph.getRawSeventhOrNinthChords(rawNotes,
								inversionBox.getSelectionModel().getSelectedIndex());
					}

					this.rawChords = FXCollections.observableArrayList();
					this.rangeTempChords = FXCollections.observableArrayList();

					for (int[] tempChord : chords) {

						Chord chord = new Chord(this.interpreter.numberToLetter(tempChord[0], this.inSharp),
								this.interpreter.numberToLetter(tempChord[1], this.inSharp),
								this.interpreter.numberToLetter(tempChord[2], this.inSharp),
								this.interpreter.numberToLetter(tempChord[3], this.inSharp), tempChord);

						this.rawChords.add(chord);
					}

					// Filter
					if (this.prev != null && this.prev.chosenResult != null) {
						this.filter.parralelFilter(this.rawChords, this.prev.chosenResult);
						this.filter.concurrentFilter(this.rawChords, this.prev.chosenResult);
						this.filter.exteriorHiddenParralelFilterToPrev(this.rawChords, this.prev.chosenResult);

					}

					if (this.next != null && this.next.chosenResult != null) {
						this.filter.exteriorHiddenParralelFilterToNext(this.rawChords, this.next.chosenResult);
						this.filter.concurrentFilter(this.rawChords, this.next.chosenResult);
						this.filter.parralelFilter(this.rawChords, this.next.chosenResult);
					}

					if (this.rawNotes[1] == 3 || this.rawNotes[1] == 4)
						this.filter.thirdRepetitonFilter(this.rawChords, this.rawNotes[1]);

					for (Chord chord : this.filter.rangeFilter(this.rawChords, (int) lowerBoundSlider.getValue(),
							(int) upperBoundSlider.getValue())) {
						this.rangeTempChords.add(chord);
					}

					resultShowcaseTable.setItems(this.rangeTempChords);

				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

	}

	private void setCanvas(Canvas canvas, Image img) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public String[] resultShowCase(List<int[]> results) {

		String[] resultShowCase = new String[results.size()];

		for (int i = 0; i < results.size(); i++) {
			String combo = "";
			for (int j = 0; j < results.get(i).length; j++)
				if (j != 3)
					combo += interpreter.numberToLetter(results.get(i)[j], this.inSharp) + "+";
				else {
					combo += interpreter.numberToLetter(results.get(i)[j], this.inSharp);
				}
			resultShowCase[i] = combo;
		}
		return resultShowCase;
	}

	public void play(Chord chord) {

		if (chord != null) {

			String toPlay = "";

			int duration = this.duration.getSelectionModel().getSelectedIndex();
			if (duration == 0)

				toPlay += chord.getBass() + "w+" + chord.getTenor() + "w+" + chord.getAlto() + "w+" + chord.getSoprano()
						+ "w";

			else if (duration == 1)

				toPlay += chord.getBass() + "+" + chord.getTenor() + "+" + chord.getAlto() + "+" + chord.getSoprano();

			else if (duration == 2)

				toPlay += chord.getBass() + "q+" + chord.getTenor() + "q+" + chord.getAlto() + "q+" + chord.getSoprano()
						+ "q";

			this.player.play(toPlay);
		}
	}

	public void render() {

		this.gc.setFill(Color.RED);

		for (int i : this.chosenResult.getNotes()) {
			int position = i % 12;
			if (position == 1 || position == 3 || position == 6 || position == 8 || position == 10)
				this.gc.fillOval(getPosition(i), 21, 5, 5);
			else
				this.gc.fillOval(getPosition(i), 45, 5, 5);
		}
	}

	public String getChosenRoot() {
		if (!this.chordRootComboBox.getSelectionModel().isEmpty())
			return this.chordRootComboBox.getValue();
		else
			return "null";
	}

	public String getChosenType() {
		if (!this.ChordtypeComboBox.getSelectionModel().isEmpty())
			return this.ChordtypeComboBox.getValue();
		else
			return "null";
	}

	public String getInversion() {
		if (!this.inversionBox.getSelectionModel().isEmpty())
			return this.inversionBox.getSelectionModel().getSelectedIndex() + "";
		else
			return "null";
	}

	public String getDuration() {
		if (!this.duration.getSelectionModel().isEmpty())
			return this.duration.getSelectionModel().getSelectedIndex() + "";
		else
			return "null";
	}

	public String getResult() {
		if (this.chosenResult != null)
			return this.choose.getText();
		else
			return "null";
	}

	public void setRoot(String root) {

		if (!(root.equals("null")))
			this.chordRootComboBox.getSelectionModel().select(root);
	}

	public void setType(String type) {
		if (!(type.equals("null")))
			this.ChordtypeComboBox.getSelectionModel().select(type);
	}

	public void setInversion(String inversion) {
		if (!(inversion.equals("null")))
			this.inversionBox.getSelectionModel().select(Integer.parseInt(inversion));
	}

	public void setDuration(String duration) {
		if (!(duration.equals("null")))
			this.duration.getSelectionModel().select(Integer.parseInt(duration));
	}

	public void setChosenResult(String chosenResult) {

		if (!(chosenResult.equals("null"))) {
			String[] chordContent = chosenResult.split("\\+");
			int[] notes = new int[4];

			for (int i = 0; i < 4; i++) {
				System.out.println(notes[i]);
				notes[i] = this.interpreter.letterToNumber(chordContent[i]);
			}

			Chord chord = new Chord(chordContent[0], chordContent[1], chordContent[2], chordContent[3], notes);

			this.chosenResult = chord;
			this.choose.setText(chosenResult);
			this.resultShowcaseTable.getSelectionModel().select(chord);
		}

	}

	public VBox getContainer() {
		return this.container;
	}

	public List<Chord> getResults() {
		return this.rawChords;
	}

	public void setNext(Bar next) {
		this.next = next;
	}

	public Bar getNext() {
		return this.next;
	}

	public void setPrev(Bar prev) {
		this.prev = prev;
	}

	public Bar getPrev() {
		return this.prev;
	}

	public void setDefaultDuration() {
		this.duration.getSelectionModel().select(0);
	}

	public boolean isInSharp() {
		return this.inSharp;
	}

	public void closeWindow() {
		if (this.tableExists)

			this.chordSort.close();
	}

	public int getPosition(int note) {
		switch (note) {
		case 21:
			return 5;
		case 22:
			return 10;
		case 23:
			return 17;
		case 24:
			return 28;
		case 25:
			return 34;
		case 26:
			return 40;
		case 27:
			return 46;
		case 28:
			return 52;
		case 29:
			return 64;
		case 30:
			return 70;
		case 31:
			return 76;
		case 32:
			return 82;
		case 33:
			return 88;
		case 34:
			return 94;
		case 35:
			return 100;
		case 36:
			return 112;
		case 37:
			return 118;
		case 38:
			return 124;
		case 39:
			return 130;
		case 40:
			return 136;
		case 41:
			return 148;
		case 42:
			return 154;
		case 43:
			return 160;
		case 44:
			return 166;
		case 45:
			return 172;
		case 46:
			return 178;
		case 47:
			return 184;
		case 48:
			return 196;
		case 49:
			return 202;
		case 50:
			return 208;
		case 51:
			return 214;
		case 52:
			return 220;
		case 53:
			return 232;
		case 54:
			return 238;
		case 55:
			return 244;
		case 56:
			return 250;
		case 57:
			return 256;
		case 58:
			return 262;
		case 59:
			return 268;
		case 60:
			return 280;
		case 61:
			return 286;
		case 62:
			return 292;
		case 63:
			return 298;
		case 64:
			return 304;
		case 65:
			return 316;
		case 66:
			return 322;
		case 67:
			return 328;
		case 68:
			return 334;
		case 69:
			return 340;
		case 70:
			return 346;
		case 71:
			return 352;
		case 72:
			return 364;
		case 73:
			return 370;
		case 74:
			return 376;
		case 75:
			return 382;
		case 76:
			return 388;
		case 77:
			return 400;
		case 78:
			return 406;
		case 79:
			return 412;
		case 80:
			return 418;
		case 81:
			return 424;
		case 82:
			return 430;
		case 83:
			return 436;
		case 84:
			return 448;
		case 85:
			return 454;
		case 86:
			return 460;
		case 87:
			return 466;
		case 88:
			return 472;
		case 89:
			return 484;
		case 90:
			return 490;
		case 91:
			return 496;
		case 92:
			return 502;
		case 93:
			return 508;
		case 94:
			return 514;
		case 95:
			return 520;
		case 96:
			return 532;
		case 97:
			return 538;
		case 98:
			return 544;
		case 99:
			return 550;
		case 100:
			return 556;
		case 101:
			return 568;
		case 102:
			return 574;
		case 103:
			return 580;
		case 104:
			return 586;
		case 105:
			return 592;
		case 106:
			return 598;
		case 107:
			return 604;
		case 108:
			return 616;
		default:
			return 0;
		}
	}

}