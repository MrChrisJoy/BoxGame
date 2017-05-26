import java.io.IOException;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IngameMenuButtonVersion extends Application {
	public static Stage MenuStage;


	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		MenuStage = stage;
		stage.setScene(new Scene(createContent()));
		stage.show();
	}

	static Parent createContent() {
		Pane root = new Pane();

		RoundButton menu = new RoundButton("MENU");
		RoundButton exit = new RoundButton("EXIT");
		RoundButton print1 = new RoundButton("PAUSE");
		RoundButton print2 = new RoundButton("PRINT2");

		HBox menuBox = new HBox(10, menu, exit, print1, print2);

		root.getChildren().add(menuBox);
		return root;

	}

	public static class RoundButton extends Parent {
		RoundButton(String name) {
			Button roundButton = new Button(name);
			roundButton.setId(name);
			roundButton.setOnAction(myHandler);
			roundButton.setStyle("-fx-background-radius: 5em; " + "-fx-min-width: 60px; " + "-fx-min-height: 60px; "
					+ "-fx-max-width: 60px; " + "-fx-max-height: 60px;");
			getChildren().add(roundButton);
		}
	}

	final static EventHandler<ActionEvent> myHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(final ActionEvent event) {
			Button x = (Button) event.getSource();
			switch (x.getId()) {
			case "MENU":
				try {
					Menu m = new Menu();
					Parent mRoot = m.createContent();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case "PAUSE":
				System.out.println(1);
				break;
			case "PRINT2":
				System.out.println(2);
				break;
			case "EXIT":
				System.exit(0);
			}
		}
	};

	public static void main(String[] args) {
		launch(args);
	}

}