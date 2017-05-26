
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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
public class Menu extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private static final Font FONT = Font.font("", FontWeight.BOLD, 18);
	private VBox menuBox;
	// navigator of the menu
	private int currentItem = 0;


	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(createContent());
		stage.setTitle("Project");
		scene.setOnKeyPressed(event -> {
			int temp;
			if (event.getCode() == KeyCode.UP) {
				if (currentItem > 0) {
					temp = currentItem;
					getMenuItem(currentItem - 1).setPrevActive(false);
					getMenuItem(currentItem).setActive(false);
					getMenuItem(--currentItem).setActive(true);
					getMenuItem(temp).setPrevActive(true);
					// depends on how many choices there are on menu
					if (temp <= 2)
						getMenuItem(++temp).setPrevActive(false);

				}
			}

			if (event.getCode() == KeyCode.DOWN) {
				if (currentItem < menuBox.getChildren().size() - 1) {
					temp = currentItem;
					getMenuItem(currentItem + 1).setPrevActive(false);
					getMenuItem(currentItem).setActive(false);
					getMenuItem(++currentItem).setActive(true);
					getMenuItem(temp).setPrevActive(true);
					if (temp >= 1)
						getMenuItem(--temp).setPrevActive(false);

				}
			}

			if (event.getCode() == KeyCode.ENTER) {
				switch (getMenuItem(currentItem).getMenuName()) {
				case "PLAY":
					GUI gui = new GUI();
					gui.setG("PLAY");
					try {
						gui.start(stage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				case "SANDBOX MODE":
					GUI guiU = new GUI();
					guiU.setG("USER");
					try {
						guiU.start(stage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
					break;

				case "OPTIONS":
					break;

				case "EXIT":
					System.exit(0);
				}
			}
		});

		
		
		
		stage.setScene(scene);
		stage.show();
	}

	// main layout of the menu with UI
	Parent createContent() {
		StackPane root = new StackPane();
		root.setPrefSize(500, 500);

		MenuItem exit = new MenuItem("EXIT");
		MenuItem play = new MenuItem("PLAY");
		MenuItem sandbox = new MenuItem("SANDBOX MODE");
		MenuItem options = new MenuItem("OPTIONS");

		// Button exit = new Button("EXIT");
		// Button play = new Button("PLAY");
		// Button sandbox = new Button("SANDBOX");
		// Button options = new Button("OPTIONS");

		menuBox = new VBox(10, play, sandbox, options, exit);
		menuBox.setTranslateX(360);
		menuBox.setTranslateY(300);
		
		root.setBackground(new Background(new BackgroundFill(Color.BROWN,CornerRadii.EMPTY, Insets.EMPTY)));
		
		
		root.getStyleClass().add("pane");
		root.setOpacity(0.5);
		
		
		Text about = new Text("COMP2911 \nMenu Prototype");
		about.setTranslateX(50);
		about.setTranslateY(500);
		about.setFill(Color.WHITE);
		about.setFont(FONT);
		about.setOpacity(0.2);

		menuBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		getMenuItem(0).setActive(true);
		root.getChildren().addAll(menuBox, about);
		return menuBox;
	}

	private MenuItem getMenuItem(int index) {
		if (index < 0)
			return null;
		return (MenuItem) menuBox.getChildren().get(index);
	}

	private static class MenuItem extends HBox {
		private Box bbLeft = new Box(40, 40, "PLAYER");
		private Box boxLeft = new Box(40, 40, "BOX");
		private Text text;
		private String name;

		public MenuItem(String name) {

			// constructor for distance between sprite and text

			super(15);
			Button button = new Button(name);
			button.setMinWidth(100);
			button.setMaxWidth(100);
			button.setId(name);
			button.setOnAction(myHandler);
			this.name = name;
			setAlignment(Pos.CENTER_LEFT);

			text = new Text(name);
			text.setFont(FONT);

			getChildren().addAll(bbLeft, boxLeft, button);
			setPrevActive(false);
			setActive(false);

		}

		public String getMenuName() {
			return name;
		}

		public void setPrevActive(boolean b) {
			bbLeft.setVisible(b);
		}

		public void setActive(boolean b) {
			boxLeft.managedProperty().bind(boxLeft.visibleProperty());
			boxLeft.setVisible(b);
			text.setFill(b ? Color.WHITE : Color.BISQUE);
		}

		private static class Box extends Parent {
			Rectangle r;

			public Box(int x, int y, String type) {
				r = new Rectangle(x, y);
				r.setStroke(Color.WHITE);
				if (type.equals("BOX")) {
					r.setFill(Color.BROWN);
				} else if (type.equals("PLAYER"))
					r.setFill(Color.BLUE);
				getChildren().add(r);
			}

		}

		final EventHandler<ActionEvent> myHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent event) {
				Button x = (Button) event.getSource();
				switch (x.getId()) {
				case "PLAY":
					GUI gui = new GUI();
					try {
					gui.start(GUI.MenuStage);
					gui.turnOffMenu();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				case "SANDBOX MODE":
					GUI guiU = new GUI();
					guiU.setG("USER");
					try {
						guiU.start(GUI.MenuStage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
					break;

				case "OPTIONS":
					break;

				case "EXIT":
					System.exit(0);
				}
			}
		};

	}

}