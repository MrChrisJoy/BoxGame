import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public final class FileInput extends Application {
	File file;
	public void start(Stage stage) throws FileNotFoundException {
		final FileChooser fileChooser = new FileChooser();

		file = fileChooser.showOpenDialog(stage);
	}
	
	public File getFile(){
		return file;
	}
}