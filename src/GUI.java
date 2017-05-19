import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class GUI extends Application{
	private GenerateWareHouse wareHouse;
	private int numBoxes = 2;
	private int size;
	private int sizeOfGame = 500;

	@Override
	public void start(Stage stage) throws Exception {
		float numCells = (float) ((numBoxes*2) / 0.7) ;
		size = (int)Math.ceil(Math.sqrt(numCells));
		System.out.println("size of Box -> " + size);
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
				case U:
					validMove = wareHouse.movePlayer(key.getCode());
					break;
				default:
					break;
				}

				if(validMove){
					wareHouse.getMaze().printMaze();
					changeToImage(wareHouse.getMaze().getMaze(), pane);	
				}
				
				if(wareHouse.isWin()) {	
					System.out.println("WON");
				}
			}
			
		});

		changeToImage(wareHouse.getMaze().getMaze(), pane);
		stage.setScene(scene);
		stage.show();
	}

	public void changeToImage2(CellOfMaze[][] level, Pane pane) {
		for (int y = 0; y < level.length; y++) {
			for (int x = 0; x < level[0].length; x++) {
				Sprite block = new Sprite(level[x][y].getStatus(), y * (sizeOfGame / level.length),
						x * (sizeOfGame / level[0].length), sizeOfGame / level.length, sizeOfGame / level[0].length);
				pane.getChildren().addAll(block);
			}
		}

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
