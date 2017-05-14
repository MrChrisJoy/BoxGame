import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class GUI extends Application{
	private GenerateWareHouse wareHouse;
	private int numBoxes = 4;
	private int size = 7;
	private int sizeOfGame = 500;

	@Override
	public void start(Stage stage) throws Exception {
		wareHouse = new GenerateWareHouse(size,numBoxes);
		wareHouse.generateBoxPath();
	 	wareHouse.addPathsWareHouse();
	 	wareHouse.buildWearHouse();
		Pane pane = new Pane();
		Scene scene = new Scene(pane, sizeOfGame, sizeOfGame); // randomly assigned size
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
				boolean validMove = false;
				switch (key.getCode()) {
				case LEFT:
					validMove = wareHouse.movePlayer(key.getCode());
					break;
				case RIGHT:
					validMove = wareHouse.movePlayer(key.getCode());
					break;
				case UP:
					validMove = wareHouse.movePlayer(key.getCode());
					break;
				case DOWN:
					validMove = wareHouse.movePlayer(key.getCode());
					break;
				default:
					break;
				}
				
				if(validMove){
					wareHouse.getMaze().printMaze();
					changeToImage(wareHouse.getMaze().getMaze(), pane);	
				}
			}

		});
		
		changeToImage(wareHouse.getMaze().getMaze(), pane);	
		stage.setScene(scene);
		stage.show();
	}

	public void changeToImage(CellOfMaze[][] level, Pane pane) {
		for (int y = 0; y < level.length; y++) {
			for (int x = 0; x < level[0].length; x++) {
				Sprite block = new Sprite(level[x][y].getStatus(), x * (sizeOfGame / level.length),
						y * (sizeOfGame / level[0].length), sizeOfGame / level.length, sizeOfGame / level[0].length);
				pane.getChildren().addAll(block);
			}
		}

	}
	public static void main(String[] args){
		launch(args);
	}
}
