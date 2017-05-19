import java.io.File;
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {
	private final int sceneScale = 100;
	private final Vector2 sceneDimensions = new Vector2(500, 500);
	private final int cameraHeight = 10;
	private final Image img = new Image("diffuse_box.jpg");
	private final Image img4 = new Image("diffuse_tile2.jpg");
	private final Image img5 = new Image("fireball2.png");
	private final Image img6 = new Image("golf_hole.png");
	private final Image img7 = new Image("wall.jpeg");
	
	private Box[] boxes;
	private Box playerBox;
	private IGenerator g;
	
	private PhongMaterial box;
	private PhongMaterial floor;
	private PhongMaterial goal;
	private PhongMaterial player;
	private PhongMaterial edge;
	
	private PerspectiveCamera camera;
	private Group level;
	
	public static void main(String args[]){ 
		launch(args); 
	}
	
	@Override 
	public void start(Stage stage) throws IOException {
		//Create global generator
		g = new Generator();
		
		//Create global materials
		setupMaterials();
		
		//Setup camera without fixed eye above level
		camera = new PerspectiveCamera(false);
		camera.setTranslateZ(-sceneScale * cameraHeight);
		
		//Create and fill level group
		level = new Group(); 
		newLevel();
		
		//Create scene with depth buffer enabled
		Scene scene = new Scene(level, sceneDimensions.x, sceneDimensions.y, true);
		scene.setFill(Color.BLACK);
	
	//	edge = new PhongMaterial();
	//	edge.setDiffuseMap(img2);
	//	edge.setSpecularPower(500);
		scene.setCamera(camera);
		
		//Set up input events
		setupKeyEvents(scene);
		
		//Set up engine events
		g.setOnPlayerMove(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				movePlayer(g.getPlayerLocation(), camera);
			}
		});
		g.setOnBoxMove(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				moveBox(g.getBoxMovedBox(), g.getBoxLocations()[g.getBoxMovedBox()]);
			}
		});
		g.setOnWin(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage vic = new Stage();
				vic.initModality(Modality.APPLICATION_MODAL);
				vic.initOwner(stage);
				VBox vbox = new VBox (20);
				vbox.getChildren().add(new Text ("VICTORY"));
				Scene on = new Scene (vbox, 300, 200);
				vic.setScene(on);
				vic.show();
				//newLevel();
				System.out.println("You won");
			}
		});
		g.setOnLose(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				newLevel();
				System.out.println("You lost");
			}
		});
		
		//Display stage
		stage.setScene(scene); 
		stage.show(); 
	}
	
	private void centreCamera(Vector2 pos) {
		//Camera center is top left corner so offset by half screen dimensions
		camera.setTranslateX(sceneScale * pos.x - sceneDimensions.x/2.0 * camera.getScaleX());
		camera.setTranslateY(sceneScale * pos.y - sceneDimensions.y/2.0 * camera.getScaleY());
	}
	
	private void newLevel() {
		//Generate a new level
		g.generateLevel(5, 25, 100);
		
		//Clear previous level group
		level.getChildren().clear();
		
		//Create and cache player and boxes
		createPlayer(g.getPlayerLocation());
		level.getChildren().add(playerBox);
		createBoxes();
		level.getChildren().addAll(boxes);
		
		//Create floors goals and edges
		addFloor(level);
		addGoals(level);
		addEdges(level);
		
		//Position camera
		centreCamera(g.getPlayerLocation());
	}
	
	
	private void createPlayer(Vector2 start) {
		playerBox = new Box(sceneScale, sceneScale, sceneScale); 
		playerBox.setMaterial(player);
		playerBox.setTranslateX(start.x * sceneScale);
		playerBox.setTranslateY(start.y * sceneScale);
	}
	
	public void createBoxes() {
		Vector2[] positions = g.getBoxLocations();
		boxes = new Box[positions.length];
		for(int i=0; i<boxes.length; i++) {
			boxes[i] = new Box(sceneScale, sceneScale, sceneScale);
			boxes[i].setTranslateX(sceneScale * positions[i].x);
			boxes[i].setTranslateY(sceneScale * positions[i].y);
			boxes[i].setMaterial(box);
		}
	}
	
	private void addFloor(Group root) {
		for(Vector2 v : g.getFloor()) {
			if(!g.hasGoal(v)) {
				Tile t = new Tile(sceneScale, floor);
				t.setTranslateX(sceneScale * v.x);
				t.setTranslateY(sceneScale * v.y);
				t.setTranslateZ(sceneScale/2);
				root.getChildren().add(t);
			}
		}
	}
	
	private void addGoals(Group root) {
		for(Vector2 v : g.getGoals()) {
			Tile t = new Tile(sceneScale, goal);
			t.setTranslateX(sceneScale * v.x);
			t.setTranslateY(sceneScale * v.y);
			t.setTranslateZ(sceneScale/2);
			root.getChildren().add(t);
		}
	}
	
	private void addEdges(Group root) {
		for(Vector2 v : g.getEdges()) {
			Box b = new Box(sceneScale, sceneScale, sceneScale);
			b.setTranslateX(sceneScale * v.x);
			b.setTranslateY(sceneScale * v.y);
			b.setMaterial(edge);
			b.setDrawMode(DrawMode.FILL);
			root.getChildren().add(b);
		}
	}
	
	private void movePlayer(Vector2 newPos, Camera c) {
		playerBox.setTranslateX(sceneScale * newPos.x);
		playerBox.setTranslateY(sceneScale * newPos.y);
		centreCamera(newPos);
	}
	
	private void moveBox(int id, Vector2 newPos) {
		boxes[id].setTranslateX(sceneScale * newPos.x);
		boxes[id].setTranslateY(sceneScale * newPos.y);
	}
	
	private void setupMaterials() {
//		box = new PhongMaterial(Color.SADDLEBROWN);
	//	File file = new File("/System/Library/CoreServices/loginwindow.app/Contents/Resources/LogOut.png");
	//	Image img = new Image("diffuse_box.jpg");
		//ImageView iV = new ImageView(img);
	//	Image img2 = new Image("normal_box.jpg");
	//	ImageView iV2 = new ImageView(img2);
		//Image img3 = new Image("normal_tile.png");
	//	Image img4 = new Image("diffuse_tile2.jpg");
	//	Image img5 = new Image("fireball2.png");
	//	ImageView iV5 = new ImageView(img5);
	//	iV5.setViewport();
	//	Image img6 = new Image("golf_hole.png");
	//	Image img7 = new Image("wall.jpeg");
		
//		Rectangle x = new Rectangle(100,50,100,50);
//		Timeline timeline = new Timeline ();
//		timeline.setCycleCount(Timeline.INDEFINITE);
//		timeline.setAutoReverse(true);
//		KeyValue kv = new KeyValue(x.xProperty(),300);
//		KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
//		timeline.getKeyFrames().add(kf);
//		timeline.play();
	//    player = new PhongMaterial ();
		box = new PhongMaterial();
		box.setDiffuseMap(img);
	//	box.setBumpMap(img2);
		box.setSpecularColor(Color.RED);
		box.setSpecularPower(100);
		floor = new PhongMaterial();
		floor.setDiffuseMap(img4);
	//	floor.setBumpMap(img4);
		floor.setSpecularColor(Color.RED);
		floor.setSpecularPower(100);
		player = new PhongMaterial();
		player.setDiffuseMap(img5);
		goal = new PhongMaterial();
		goal.setDiffuseMap(img6);
		
		
		//If background is black this will give illusion of deep walls
		edge = new PhongMaterial();
		edge.setDiffuseMap(img7);
		edge.setSpecularPower(0);
	}
	
	private void setupKeyEvents(Scene scene) {
		//UP/DOWN reversed since javafx coordinate system has -y axis
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
				switch (key.getCode()) {
				case LEFT: g.moveCharacter(Vector2.LEFT); break;
				case UP: g.moveCharacter(Vector2.DOWN); break;
				case RIGHT: g.moveCharacter(Vector2.RIGHT); break; 
				case DOWN: g.moveCharacter(Vector2.UP) ;break;
				case BACK_SPACE : g.undo(); break;
				case ENTER: newLevel(); break;
				default: break;
				}
			}
		});
	}
}
