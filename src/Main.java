import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import org.jfugue.player.Player;
import algorithms.Filter;
import algorithms.FourPartHarmony;
import algorithms.Interpreter;
import algorithms.NoteList;

public class Main extends Application {

	// Button saveAsMidi;
	Stage window;
	Scene scene1, scene2;

	@Override
	public void start(Stage primaryStage) throws Exception {

		window = primaryStage;

		Label label1 = new Label("Welcome to the first scene!");
		Button button1 = new Button("Go to scene 2");
		button1.setOnAction(e -> window.setScene(scene2));

		VBox layout1 = new VBox(20);
		layout1.getChildren().addAll(label1, button1);
		scene1 = new Scene(layout1, 200, 200);

		Button button2 = new Button("Go back to scene 1");
		button2.setOnAction(e -> window.setScene(scene1));

		StackPane layout2 = new StackPane();
		layout2.getChildren().add(button2);
		scene2 = new Scene(layout2, 600, 300);
		
		window.setScene(scene1);
		window.setTitle("Title here");
		window.show();

		// primaryStage.setTitle("四部和声");
		//
		// saveAsMidi = new Button();
		// saveAsMidi.setText("输出为midi");
		// saveAsMidi.setOnAction(e -> {
		// System.out.println("test");
		// });
		//
		// StackPane layout = new StackPane();
		// layout.getChildren().add(saveAsMidi);
		//
		// Scene scene = new Scene(layout, 1600, 900);
		// primaryStage.setScene(scene);
		// primaryStage.show();

		// }
		//
		// @Override
		// public void handle(ActionEvent event) {
		//
		// if (event.getSource() == saveAsMidi) {
		// System.out.println("asldjflaskdjfla;skdf");
		// }
		//
		// }
	}

	public static void main(String[] args) throws NumberFormatException, IOException {

		launch(args);

		// UIPanal ui = new UIPanal("四部和声", 500, 500);
		// ui.setVisible(true);

		// FourPartHarmony fph = new FourPartHarmony();
		//
		// Interpreter interpreter = new Interpreter();
		// NoteList notelist = new NoteList();
		//
		// Filter filter = new Filter();
		//
		// int[] test = notelist.getRawNotes("C", "dom7");
		//
		// List<int[]> testing = fph.getRawTriads(test, 0, 50, 80);
		// System.out.println(testing.size());
		// filter.thirdRepetitonFilter(testing, test[1]);
		// System.out.println(testing.size());
		// Player player = new Player();
		//
		// // player.play("C2ww+C3ww+E3ww+G3ww");
		//
		// for (int[] array : testing) {
		//
		// String chord = "";
		//
		// for (int i = 0; i < array.length; i++) {
		// if (i < array.length - 1) {
		// // System.out.println(i);
		// // System.out.println(interpreter.numberToLetter(array[i],
		// // true));
		// chord += interpreter.numberToLetter(array[i], true) + "+";
		// } else {
		// chord += interpreter.numberToLetter(array[i], true) + "";
		// }
		// }
		// System.out.println(chord);
		// player.play(chord);

		// for (int i : array)

		// System.out.println(i);
		// System.out.print(interpreter.numberToLetter(i, true) + " ");

		// System.out.println();
		// }

	}

}
