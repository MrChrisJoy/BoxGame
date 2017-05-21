import java.util.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public final class FileInputArrayList extends Application {

	private BlockType blockEnum[] = BlockType.values();
	private BlockType[][] levelArray;
	private ArrayList<ArrayList<BlockType>> level = new ArrayList<ArrayList<BlockType>>();
	private static final double sizeOfGameWidth = 900;
	private static final double sizeOfGameHeight = 600;
	private Scanner fileSc;
	private GUI g = new GUI();

	public void start(Stage stage) throws FileNotFoundException {
		Pane rootPane = new Pane();
		Scene scene = new Scene(rootPane, sizeOfGameWidth, sizeOfGameHeight);
		final FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(stage);

		if (file != null) {
			System.out.println(file);
			level = readFile(file, level);
		}
		levelArray = listToArray(level);
		printLevel();

		g.changeToImage(levelArray, rootPane);
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

	private ArrayList<ArrayList<BlockType>> readFile(File file, ArrayList<ArrayList<BlockType>> level)
			throws FileNotFoundException {
		int z = 0;
		int x = 0;
		int index = 0;
		String token;
		String line;
		char cIndex;
		level.add(new ArrayList<BlockType>());

		try {
			fileSc = new Scanner(new FileReader(file));
			while (fileSc.hasNext()) {
				line = fileSc.nextLine();
				Scanner lineSc = new Scanner(line);
				token = lineSc.next();
				while (z < token.length()) {

					cIndex = token.charAt(z);

					switch (cIndex) {

					case '0':
						index = 0;
						break;
					case '1':
						index = 1;
						break;
					case '2':
						index = 2;
						break;
					case '3':
						index = 3;
						break;
					case '4':
						index = 4;
						break;

					}

					level.get(x).add(blockEnum[index]);

					z++;
				}

				x++;
				level.add(new ArrayList<BlockType>());
				lineSc.close();
				z = 0;

			}

		} catch (FileNotFoundException e) {
		} finally {
			if (fileSc != null)
				fileSc.close();
		}
		level.remove(level.size()-1);
		return level;
	}

	private BlockType[][] listToArray(ArrayList<ArrayList<BlockType>> level) {

		BlockType[][] levelArray = new BlockType[level.size()][];
		for (int i = 0; i < level.size(); i++) {
			ArrayList<BlockType> row = level.get(i);
			levelArray[i] = row.toArray(new BlockType[row.size()]);
		}
		return levelArray;
	}

	private void printLevel() {
		for (int x = 0; x < levelArray.length; x++) {
			for (int y = 0; y < levelArray[0].length; y++) {
				System.out.print(levelArray[x][y].ordinal());
			}
			System.out.println();

		}
		System.out.println();
	}
}