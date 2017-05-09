import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GUI extends Application {

	private static final int sizeOfGame = 500;
	private Generator generator;
	private BlockType[][] level;
	//private Button buttonGenerate = new Button("Generate");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		generator = new Generator();
		level = generator.getEnvironment();
		// pane auto resizes the children while group cannot
		Pane pane = new Pane();
		Scene scene = new Scene(pane, sizeOfGame, sizeOfGame);
//		buttonGenerate.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//
//			public void handle(ActionEvent arg0) {
//				buttonGenerate.setVisible(true);
//				generator.generateLevel();
//				level = generator.getEnvironment();
//				
//
//			}
//
//		});
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
				Vector2 movedTo = null;
				switch (key.getCode()) {
				case LEFT:
					movedTo = generator.moveCharacter(0);
					break;
				case UP:
					movedTo = generator.moveCharacter(1);
					break;
				case RIGHT:
					movedTo = generator.moveCharacter(2);
					break;
				case DOWN:
					movedTo = generator.moveCharacter(3);
					break;
				case ENTER:
					generator.generateLevel(5, 20, 100);
					level = generator.getEnvironment();
					changeToImage(level, pane);
				default:
					break;

				}

				if (movedTo != null) {
					changeToImage(level, pane);
				}
			}

		});

		changeToImage(level, pane);
		// System.out.println(level.length);
		// System.out.println(level[0].length);
		//pane.getChildren().addAll(buttonGenerate);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * my representation may or may not be upside down compared to the numbered
	 * system.out.print directions for up and down were swapped in the generator
	 * class. need clarifications on this.
	 * 
	 * @author Jzhang
	 */

	public void changeToImage(BlockType[][] level, Pane pane) {
		for (int x = 0; x < level.length; x++) {
			for (int y = 0; y < level[0].length; y++) {
				Sprite block = new Sprite(level[x][level[0].length - y - 1].ordinal(), x * (sizeOfGame / level.length),
						y * (sizeOfGame / level[0].length), sizeOfGame / level.length, sizeOfGame / level[0].length);
				pane.getChildren().addAll(block);
			}
		}

	}
}