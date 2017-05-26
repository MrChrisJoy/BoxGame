
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Theme extends Application {

	private int themeItem = 0;
	private VBox themeBox;
	private int themeNum = 1;
	private static final Font FONT = Font.font("", FontWeight.BOLD, 18);
	
	public static void main (String args) {
		
		launch (args);
	}

	@Override
	public void start(Stage stage) throws Exception {

			
			//		stage.initModality(Modality.APPLICATION_MODAL);
			//		stage.initOwner(stage);
					Parent themeRoot = createTheme();
					Scene themeScene = new Scene (themeRoot);
					themeScene.setOnKeyPressed(themeEvent -> {
						if (themeEvent.getCode() == KeyCode.UP) {
								getThemeItem(themeItem).setActive(false);
								getThemeItem(--themeItem).setActive(true);
						}

						if (themeEvent.getCode() == KeyCode.DOWN) {
							if (themeItem < themeBox.getChildren().size() - 1) {
								getThemeItem(themeItem).setActive(false);
								getThemeItem(++themeItem).setActive(true);
							}	

						}

						if (themeEvent.getCode() == KeyCode.ENTER) {
							
							switch (getThemeItem(themeItem).getMenuName()) {
								
							case "THEME 1" :
						//		System.out.println("Theme set to 1");
								setThemeNum(1);
								break;
								
							case "THEME 2" :
							//	System.out.println("Theme set to 2");
								setThemeNum(2);
								break;
								
							case "THEME 3" :
								
								setThemeNum(3);
								break;
								
							case "THEME 4" :
								
								setThemeNum(4);
								break;	
							
							case "RETURN" :
								try {
									
									stage.close();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
							
						}
					});
				//	System.out.println("how many");
						stage.setScene(themeScene);
						stage.show();
						
		
	}
	Parent createTheme() {
		
		Pane root = new Pane ();
		root.setPrefSize(500,500);
		
		// new Image(url)
		Image image = new Image("http://i.imgur.com/AGTcz1a.jpg");
		BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true), BackgroundSize.DEFAULT);

		
		MenuItem t1 = new MenuItem("THEME 1");
		MenuItem t2 = new MenuItem("THEME 2");
		MenuItem t3 = new MenuItem("THEME 3");
		MenuItem t4 = new MenuItem("THEME 4");
		MenuItem back = new MenuItem("RETURN");

		themeBox = new VBox(10, t1, t2, t3, t4, back);
		themeBox.setAlignment(Pos.TOP_CENTER);
		themeBox.setTranslateX(360);
		themeBox.setTranslateY(300);

	//	getThemeItem(0).setActive(true);
		root.setBackground(new Background(background));
		root.getChildren().addAll(themeBox);
		
		return root;
	}
	private MenuItem getMenuItem(int index) {
		if (index < 0)
			return null;
		return (MenuItem) themeBox.getChildren().get(index);
	}

	private static class MenuItem extends HBox {
		private Text text;
		private String name;

		public MenuItem(String name) {

			// constructor for distance between sprite and text

			super(15);
			this.name = name;
			setAlignment(Pos.CENTER_LEFT);

			text = new Text(name);
			text.setFont(FONT);

			getChildren().addAll(text);
			setActive(false);

		}
		public void setActive(boolean b) {
			text.setFill(b ? Color.WHITE : Color.BISQUE);
		}
		
		public String getMenuName() {
			return name;
		}

	}
	private MenuItem getThemeItem(int index) {
		if (index < 0)
			return null;
		return (MenuItem) themeBox.getChildren().get(index);
	}

	public void setThemeItem(int themeItem) {
		this.themeItem = themeItem;
	}

	public int getThemeNum() {
		return themeNum;
	}

	public void setThemeNum(int themeNum) {
		this.themeNum = themeNum;
	}
	
}
