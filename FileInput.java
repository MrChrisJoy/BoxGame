import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * @author AM
 * Last Edited 24/05/2017		:	Antony.J
 */
public final class FileInput extends Application {
	File file;
	public void start(Stage stage) throws FileNotFoundException {
		final FileChooser fileChooser = new FileChooser();

		String userDirectoryString = System.getProperty("user.dir");
		userDirectoryString += "/gameFiles";
		File userDirectory = new File(userDirectoryString);
		
		fileChooser.setInitialDirectory(userDirectory);
		
		file = fileChooser.showOpenDialog(stage);
	}
	
	public File getFile(){
		return file;
	}
}