import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class KeyListner extends Application{
	private GenerateWareHouse wareHouse;
	private int numBoxes = 4;
	private int size = 7;

	@Override
	public void start(Stage stage) throws Exception {
		wareHouse = new GenerateWareHouse(size,numBoxes);
		wareHouse.generateBoxPath();
	 	wareHouse.addPathsWareHouse();
	 	wareHouse.buildWearHouse();
		Pane pane = new Pane();
		Scene scene = new Scene(pane, size+1, size+1); // randomly assigned size
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
				switch (key.getCode()) {
				case LEFT:
					wareHouse.movePlayer(key.getCode()).printMaze();;
					break;
				case RIGHT:
					wareHouse.movePlayer(key.getCode()).printMaze();;
					break;
				case UP:
					wareHouse.movePlayer(key.getCode()).printMaze();;
					break;
				case DOWN:
					wareHouse.movePlayer(key.getCode()).printMaze();;
					break;
				default:
					break;
				}
			}

		});
		
		stage.setScene(scene);
		stage.show();
	}

	
	public static void main(String[] args){
		launch(args);
	}
}
