import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BoxSystem extends Application{
	private Menu m = new Menu();
	Parent mRoot = m.createContent();
	private GUI gui = new GUI();
	private Generator gen = new Generator();
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		StackPane pane = new StackPane();
		Scene scene = new Scene(pane);
		firstLevel();
		pane.getChildren().add(mRoot);
		stage.setScene(scene);
		
		
	}
	
	public void firstLevel(){
		gen.generateLevel(5, 20, 100);
		gui.drawLevel();
	}
}
