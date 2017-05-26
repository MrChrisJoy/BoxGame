import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ThemeMenu extends Application{
	
	private static final Font FONT = Font.font("", FontWeight.BOLD, 20);
	
	final FileChooser fileChooser = new FileChooser();
	private VBox menuBox;
	private int currentItem = 0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
	}
	
	
}
