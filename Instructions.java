
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Instructions extends Application {

	public static void main (String args) {
		
		launch (args);
	}

	@Override
	public void start(Stage stage) throws Exception {
	
		Scene scene = new Scene(createIns());
		stage.setScene(scene);
		stage.show();
		
	}
	
	
	Parent createIns() {
		
		Pane root = new Pane ();
		root.setPrefSize(500,500);
		
		// new Image(url)
		Image image = new Image("file:instructions.png");
		BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true), BackgroundSize.DEFAULT);

		root.setBackground(new Background(background));
		
		return root;
	}
	
	
}
