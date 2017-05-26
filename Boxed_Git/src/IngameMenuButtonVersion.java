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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IngameMenuButtonVersion {
	private Pane root;

	public IngameMenuButtonVersion(EventHandler<MouseEvent> menuHandler, EventHandler<MouseEvent> instructionHandler) {
		root = new Pane();

		HBox menuBox = new HBox(10,  new RoundButton("MENU", menuHandler), new RoundButton("MENU", instructionHandler));

		root.getChildren().add(menuBox);
	}
	

	public Parent getGroup() {
		return root;
	}
	
	public static class RoundButton extends Parent {
		public RoundButton(String name, EventHandler<MouseEvent> handler) {
			Button roundButton = new Button(name);
			roundButton.setId(name);
			roundButton.setOnMouseClicked(handler);
			roundButton.setStyle("-fx-background-radius: 5em; " + "-fx-min-width: 30px; " + "-fx-min-height: 30px; "
					+ "-fx-max-width: 30px; " + "-fx-max-height: 30px;");
			getChildren().add(roundButton);
		}
	}
}