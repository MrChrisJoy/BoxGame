import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * 
 * Last Edited 24/05/2017 12:35 : Antony.J
 *
 */
public class Menu {
	private static final Font FONT = Font.font("", FontWeight.BOLD, 18);
	private VBox menuBox;
	private StackPane root;

	// navigator of the menu
	private int currentItem = 0;

	public Menu(EventHandler<MouseEvent> playHandler, EventHandler<MouseEvent> exitHandler) {
		root = new StackPane();
		root.setPrefSize(500, 500);
		MenuItem play = new MenuItem("PLAY", playHandler);
		MenuItem exit = new MenuItem("EXIT", exitHandler);

		menuBox = new VBox(10, play, exit);

		root.getChildren().addAll(menuBox);
	}

	public Parent getGroup() {
		return menuBox;
	}

	private static class MenuItem extends StackPane {
		private Text text;

		public MenuItem(String name, EventHandler<MouseEvent> handler) {
			LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
					new Stop[] { new Stop(0, Color.DARKVIOLET), new Stop(0.1, Color.BLACK), new Stop(0.9, Color.BLACK),
							new Stop(1, Color.DARKVIOLET), });

			Rectangle bg = new Rectangle(200, 30);
			bg.setOpacity(0.4);
			bg.setOnMouseClicked(handler);

			text = new Text(name);
			text.setFill(Color.DARKGREY);
			text.setFont(FONT);

			setAlignment(Pos.CENTER);
			getChildren().addAll(text, bg);

			setOnMouseEntered(event -> {
				bg.setFill(gradient);
				text.setFill(Color.WHITE);
			});

			setOnMouseExited(event -> {
				bg.setFill(Color.BLACK);
				text.setFill(Color.WHITE);
			});

			setOnMousePressed(event -> {
				bg.setFill(Color.DARKVIOLET);
			});

			setOnMouseReleased(event -> {
				bg.setFill(gradient);
			});
		}

	}

}