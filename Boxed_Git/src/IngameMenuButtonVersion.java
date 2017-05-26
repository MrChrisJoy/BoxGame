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

	Parent createContent() {
		Pane root = new Pane();

		RoundButton menu = new RoundButton("MENU");
		RoundButton restart = new RoundButton("RESTART");
		RoundButton exit = new RoundButton("EXIT");

		HBox menuBox = new HBox(10, menu, restart, exit);

		root.getChildren().add(menuBox);
		return root;

	}

	public static class RoundButton extends Parent {
		public RoundButton(String name) {
			Button roundButton = new Button(name);
			roundButton.setId(name);
			roundButton.setOnAction(myHandler);
			roundButton.setStyle("-fx-background-radius: 5em; " + "-fx-min-width: 30px; " + "-fx-min-height: 30px; "
					+ "-fx-max-width: 30px; " + "-fx-max-height: 30px;");
			getChildren().add(roundButton);
		}
	}

	final static EventHandler<ActionEvent> myHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(final ActionEvent event) {
			Button x = (Button) event.getSource();
			switch (x.getId()) {
			case "MENU":
				BoxSystem gui = new BoxSystem();
				try {
					gui.turnOnMenu();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case "RESTART":
				System.out.println(1);
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