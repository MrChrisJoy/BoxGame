
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
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

public class IngameMenu extends Application {
	private static final Font FONT = Font.font("", FontWeight.BOLD, 20);

	final FileChooser fileChooser = new FileChooser();
	private VBox menuBox;
	private int currentItem = 0;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setAlwaysOnTop(true);
		Scene scene = new Scene(createContent());
		stage.setScene(scene);

		stage.show();
	}

	public Parent createContent() {
		StackPane root = new StackPane();
		root.setPrefSize(200, 300);

		MenuItem exit = new MenuItem("EXIT");
		MenuItem menu = new MenuItem("MENU");
		MenuItem sandbox = new MenuItem("SANDBOX MODE");
		MenuItem options = new MenuItem("OPTIONS");

		menuBox = new VBox(30, menu, sandbox, options, exit);
		menuBox.setAlignment(Pos.CENTER);
		getMenuItem(0).setActive(true);

		root.getChildren().addAll(menuBox);

		return root;
	}

	private static class MenuItem extends HBox {
		private Text text;
		private String name;

		public MenuItem(String name) {

			// constructor for distance between sprite and text
			super(15);
			this.name = name;
			setAlignment(Pos.CENTER_LEFT);

			text = new Text(name);
			text.setFont(FONT);

			getChildren().addAll(text);
			setActive(false);

		}

		public String getMenuName() {
			return name;
		}

		public void setActive(boolean b) {
			text.setFill(b ? Color.WHITE : Color.BISQUE);
		}
	}

	public void moveItem(int code) {
		if(code == 1) {
			if (currentItem > 0) {
				getMenuItem(currentItem).setActive(false);
				getMenuItem(--currentItem).setActive(true);

			}
		}

		if (code == -1) {
			if (currentItem < menuBox.getChildren().size() - 1) {
				getMenuItem(currentItem).setActive(false);
				getMenuItem(++currentItem).setActive(true);

			}
		}

		if (code == 0) {
			switch (getMenuItem(currentItem).getMenuName()) {

			case "OPTIONS":
				break;

			case "EXIT":
				System.exit(0);
			}

		}
	}

	private MenuItem getMenuItem(int index) {
		if (index < 0)
			return null;
		return (MenuItem) menuBox.getChildren().get(index);
	}
}