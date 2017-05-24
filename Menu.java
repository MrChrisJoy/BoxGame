
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

//import Menu.MenuItem;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * 
 * Last Edited 24/05/2017	12:35		:	Antony.J
 *
 */
public class Menu extends Application {

	public static void main(String[] args) {
		launch(args);
	}


	private static final Font FONT = Font.font("", FontWeight.BOLD, 18); 
//	final FileChooser fileChooser = new FileChooser();
	private VBox menuBox;
	private VBox themeBox;
	// navigator of the menu
	private int currentItem = 0;
	private int themeItem = 0;
	private Stage stage;
	GUI gui = new GUI();
	
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(createContent());

		scene.setOnKeyPressed(event -> {
			int temp;
			if (event.getCode() == KeyCode.UP) {
				if (currentItem > 0) {
					temp = currentItem;
					getMenuItem(currentItem - 1).setPrevActive(false);
					getMenuItem(currentItem).setActive(false);
					getMenuItem(--currentItem).setActive(true);
					getMenuItem(temp).setPrevActive(true);
					// depends on how many choices there are on menu
					if (temp <= 2)
						getMenuItem(++temp).setPrevActive(false);

				}
			}

			if (event.getCode() == KeyCode.DOWN) {
				if (currentItem < menuBox.getChildren().size() - 1) {
					temp = currentItem;
					getMenuItem(currentItem + 1).setPrevActive(false);
					getMenuItem(currentItem).setActive(false);
					getMenuItem(++currentItem).setActive(true);
					getMenuItem(temp).setPrevActive(true);
					if (temp >= 1)
						getMenuItem(--temp).setPrevActive(false);

				}
			}

			if (event.getCode() == KeyCode.ENTER) {
				switch (getMenuItem(currentItem).getMenuName()) {
				case "PLAY":
					
					gui.setG("PLAY");
					try {						
						gui.start(stage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				case "SANDBOX MODE":
					gui.setG("USER");
					try {
						gui.start(stage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
					break;

				case "OPTIONS":
					
					try {
					
			//		stage.initModality(Modality.APPLICATION_MODAL);
			//		stage.initOwner(stage);
					Parent themeRoot = createTheme();
					Scene themeScene = new Scene (themeRoot);
					
					themeScene.setOnKeyPressed(themeEvent -> {
						int themeTemp;
						if (themeEvent.getCode() == KeyCode.UP) {
							if (themeItem > 0) {
								themeTemp = themeItem;
								getThemeItem(themeItem - 1).setPrevActive(false);
								getThemeItem(themeItem).setActive(false);
								getThemeItem(--themeItem).setActive(true);
								getThemeItem(themeTemp).setPrevActive(true);
								// depends on how many choices there are on menu
								if (themeTemp <= 2)
									getMenuItem(++themeTemp).setPrevActive(false);

							}
						}

						if (themeEvent.getCode() == KeyCode.DOWN) {
							if (themeItem < themeBox.getChildren().size() - 1) {
								themeTemp = themeItem;
								getThemeItem(themeItem + 1).setPrevActive(false);
								getThemeItem(themeItem).setActive(false);
								getThemeItem(++themeItem).setActive(true);
								getThemeItem(themeTemp).setPrevActive(true);
								if (themeTemp >= 1)
									getThemeItem(--themeTemp).setPrevActive(false);

							}
						}

						if (themeEvent.getCode() == KeyCode.ENTER) {
							
							switch (getThemeItem(themeItem).getMenuName()) {
								
							case "THEME 1" :
								System.out.println("Theme set to 1");
								gui.setThemeNum(1);
								break;
								
							case "THEME 2" :
								System.out.println("Theme set to 2");
								gui.setThemeNum(2);
								break;
								
							case "THEME 3" :
								
								gui.setThemeNum(3);
								break;
								
							case "THEME 4" :
								
								gui.setThemeNum(4);
								break;
								
							
							case "RETURN" :
								try {
									
									start(stage);
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
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
					
				break;

				case "EXIT":
					System.exit(0);
				}
			}
		});	
		stage.setScene(scene);

		stage.show();
	}
	
	
	private Parent createTheme() {
		
		Pane root = new Pane ();
		root.setPrefSize(900,600);
		
		// new Image(url)
		Image image = new Image("http://i.imgur.com/AGTcz1a.jpg");
		BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true), BackgroundSize.DEFAULT);

//		ContentFrame frame = new ContentFrame(createMiddleContent());

//		HBox hbox = new HBox(frame);
		// space inbetween frames
//		hbox.setTranslateX(120);
//		hbox.setTranslateY(50);

		MenuItem t1 = new MenuItem("THEME 1");
		MenuItem t2 = new MenuItem("THEME 2");
		MenuItem t3 = new MenuItem("THEME 3");
		MenuItem t4 = new MenuItem("THEME 4");
		MenuItem back = new MenuItem("RETURN");

		themeBox = new VBox(10, t1, t2, t3, t4, back);
		themeBox.setAlignment(Pos.TOP_CENTER);
		themeBox.setTranslateX(360);
		themeBox.setTranslateY(300);

//		Text about = new Text("COMP2911 \nMenu Prototype");
//		about.setTranslateX(50);
//		about.setTranslateY(500);
//		about.setFill(Color.WHITE);
//		about.setFont(FONT);
//		about.setOpacity(0.2);

		getThemeItem(0).setActive(true);
		root.setBackground(new Background(background));
		root.getChildren().addAll(themeBox);
		
		return root;
	}
	// main layout of the menu with UI
	private Parent createContent() {
		Pane root = new Pane();
		root.setPrefSize(900, 600);

		// new Image(url)
		Image image = new Image("http://www.relumination.com/wp-content/uploads/2016/07/warehouse-led.jpg");
		BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true), BackgroundSize.DEFAULT);

		ContentFrame frame = new ContentFrame(createMiddleContent());

		HBox hbox = new HBox(frame);
		// space inbetween frames
		hbox.setTranslateX(120);
		hbox.setTranslateY(50);

		MenuItem exit = new MenuItem("EXIT");
		MenuItem play = new MenuItem("PLAY");
		MenuItem sandbox = new MenuItem("SANDBOX MODE");
		MenuItem options = new MenuItem("OPTIONS");

		menuBox = new VBox(10, play, sandbox, options, exit);
		menuBox.setAlignment(Pos.TOP_CENTER);
		menuBox.setTranslateX(360);
		menuBox.setTranslateY(300);

		Text about = new Text("COMP2911 \nMenu Prototype");
		about.setTranslateX(50);
		about.setTranslateY(500);
		about.setFill(Color.WHITE);
		about.setFont(FONT);
		about.setOpacity(0.2);

		getMenuItem(0).setActive(true);
		root.setBackground(new Background(background));
		root.getChildren().addAll(hbox, menuBox, about);
		return root;
	}

	// middle panel content
	private Node createMiddleContent() {
		String title = "Box Pusher";
		HBox letters = new HBox(0);
		letters.setAlignment(Pos.CENTER);
		for (int i = 0; i < title.length(); i++) {
			Text letter = new Text(title.charAt(i) + "");
			letter.setFont(FONT);
			letter.setFill(Color.WHITE);
			letters.getChildren().add(letter);

			TranslateTransition tt = new TranslateTransition(Duration.seconds(2), letter);
			tt.setDelay(Duration.millis(i * 50));
			tt.setToY(-25);
			tt.setAutoReverse(true);
			tt.setCycleCount(TranslateTransition.INDEFINITE);
			tt.play();
		}

		return letters;
	}

	private MenuItem getMenuItem(int index) {
		if (index < 0)
			return null;
		return (MenuItem) menuBox.getChildren().get(index);
	}
	private MenuItem getThemeItem(int index) {
		if (index < 0)
			return null;
		return (MenuItem) themeBox.getChildren().get(index);
	}

	// pane with smoothed out edges
	// took this design from the internet
	private static class ContentFrame extends StackPane {
		public ContentFrame(Node content) {
			setAlignment(Pos.CENTER);

			Rectangle frame = new Rectangle(200, 200);
			frame.setArcWidth(25);
			frame.setArcHeight(25);
			frame.setStroke(Color.WHITESMOKE);

			getChildren().addAll(frame, content);
		}
	}

	private static class MenuItem extends HBox {

		private Box bbLeft = new Box(40, 40, "PLAYER");
		private Box boxLeft = new Box(40, 40, "BOX");
		private Text text;
		private String name;
		
		public MenuItem(String name) {

			// constructor for distance between sprite and text
			super(15);
			this.name = name;
			setAlignment(Pos.CENTER_LEFT);

			text = new Text(name);
			text.setFont(FONT);

			getChildren().addAll(bbLeft, boxLeft, text);
			setPrevActive(false);
			setActive(false);

		}

		public String getMenuName() {
			return name;
		}

		public void setPrevActive(boolean b) {
			bbLeft.setVisible(b);
		}

		public void setActive(boolean b) {
			boxLeft.managedProperty().bind(boxLeft.visibleProperty());
			boxLeft.setVisible(b);
			text.setFill(b ? Color.WHITE : Color.BISQUE);
		}
	}

	private static class Box extends Parent {
		Rectangle r;

		public Box(int x, int y, String type) {
			r = new Rectangle(x, y);
			r.setStroke(Color.WHITE);
			if (type.equals("BOX")) {
				r.setFill(Color.BROWN);
			} else if (type.equals("PLAYER"))
				r.setFill(Color.BLUE);

			getChildren().add(r);
		}

	}

	

}