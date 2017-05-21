import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

public class GUI extends Application {
	private final int sceneScale = 1;
	private final Vector2 sceneDimensions = new Vector2(900, 600);
	private final int cameraHeight = 15;
	private boolean canPlay = true;
	private String type = "";
	
	private Box[] boxes;
	private Box playerBox;
	private IGenerator g = new Generator();
	//private Menu m = new Menu();
	IngameMenu im = new IngameMenu();
	Parent root = im.createContent();
	
	private PhongMaterial box;
	private PhongMaterial floor;
	private PhongMaterial goal;
	private PhongMaterial player;
	private PhongMaterial edge;

	private PerspectiveCamera camera;
	private Group level;

	Timer timer;
	private CachedLevels cachedLevels;
	private Record record;

	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		// Create global generator

		// Check for records every 30 seconds
		cachedLevels = new CachedLevels("Player7765");
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Record r = cachedLevels.getRecord();
				if (r != null && !r.equals(record)) {
					record = r;
					System.out.println(record.opponent + " challenged you");
				}
			}
		}, 30000, 30000);

		// Create camera with fixed eye
		camera = new PerspectiveCamera(true);
		camera.setTranslateZ(-sceneScale * cameraHeight);

		// Create level group to be filled/cleared each generation and download
		// materials
		level = new Group();
		setupMaterials();
		newLevel();

		// Group 3D nodes into a sub-scene with perspective camera
		Group root3D = new Group(camera, level);
		SubScene subScene = new SubScene(root3D, sceneDimensions.x, sceneDimensions.y, true,
				SceneAntialiasing.DISABLED);
		subScene.setCamera(camera);

		// Group sub-scene and 2D elements into stack pane
		Label label = new Label("2d ui on 3d subscene");
		StackPane pane = new StackPane();
		pane.getChildren().addAll(subScene, label);

		// Create main scene
		Scene scene = new Scene(pane);

		// Set up input events
		setupKeyEvents(scene, stage, pane);

		// Set up engine events
		getG().setOnPlayerMove(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				movePlayer(g.getPlayerLocation(), camera);
			}
		});
		getG().setOnBoxMove(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				moveBox(g.getBoxMovedBox(), g.getBoxLocations()[g.getBoxMovedBox()]);
			}
		});
		getG().setOnWin(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				newLevel();
				System.out.println("You won");
			}
		});
		getG().setOnLose(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				newLevel();
				System.out.println("You lost");
			}
		});

		// Display scene
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() {
		timer.cancel();
	}

	private void centreCamera(Vector2 pos) {
		// Camera center is top left corner so offset by half screen dimensions
		camera.setTranslateX(sceneScale * pos.x);
		camera.setTranslateY(sceneScale * pos.y);
	}

	private void newLevel() {
		// Generate a new level
		if(getType().equals("PLAY"));
		getG().generateLevel(5, 20, 100);

		if(getType().equals("USER"));
		getG().generateLevel();
		
		// Clear previous level group
		level.getChildren().clear();

		// Create and cache player and boxes
		createPlayer(g.getPlayerLocation());
		level.getChildren().add(playerBox);
		createBoxes();
		level.getChildren().addAll(boxes);

		// Create floors goals and edges
		addFloor(level);
		addGoals(level);
		addEdges(level);

		// Position camera
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
		for (int i = 0; i < boxes.length; i++) {
			boxes[i] = new Box(sceneScale, sceneScale, sceneScale);
			boxes[i].setTranslateX(sceneScale * positions[i].x);
			boxes[i].setTranslateY(sceneScale * positions[i].y);
			boxes[i].setMaterial(box);
		}
	}

	private void addFloor(Group root) {
		for (Vector2 v : g.getFloor()) {
			if (!g.hasGoal(v)) {
				Tile t = new Tile(sceneScale, floor);
				t.setTranslateX(sceneScale * v.x);
				t.setTranslateY(sceneScale * v.y);
				t.setTranslateZ(sceneScale / 2);
				root.getChildren().add(t);
			}
		}
	}

	private void addGoals(Group root) {
		for (Vector2 v : g.getGoals()) {
			Tile t = new Tile(sceneScale, goal);
			t.setTranslateX(sceneScale * v.x);
			t.setTranslateY(sceneScale * v.y);
			t.setTranslateZ(sceneScale / 2);
			root.getChildren().add(t);
		}
	}

	private void addEdges(Group root) {
		for (Vector2 v : g.getEdges()) {
			Box b = new Box(sceneScale, sceneScale, sceneScale);
			b.setTranslateX(sceneScale * v.x);
			b.setTranslateY(sceneScale * v.y);
			b.setMaterial(edge);
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
		box = new PhongMaterial(Color.SADDLEBROWN);
		floor = new PhongMaterial(Color.WHITE);
		player = new PhongMaterial(Color.RED);
		goal = new PhongMaterial(Color.GREY);

		// If background is black this will give illusion of deep walls
		edge = new PhongMaterial(Color.BLACK);
		edge.setSpecularPower(0);
	}

	private void setupKeyEvents(Scene scene, Stage stage, StackPane pane) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
				if (canPlay == true) {
					switch (key.getCode()) {
					case LEFT:
						g.moveCharacter(Vector2.LEFT);
						break;
					case UP:
						g.moveCharacter(Vector2.UP);
						break;
					case RIGHT:
						g.moveCharacter(Vector2.RIGHT);
						break;
					case DOWN:
						g.moveCharacter(Vector2.DOWN);
						break;
					case BACK_SPACE:
						g.undo();
						break;
					case ENTER:
						newLevel();
						break;
					case ESCAPE:
						try {
							
							pane.getChildren().add(root);
							canPlay = false;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					default:
						break;

					}
				} else if (canPlay == false) {
			
					switch (key.getCode()) {
					case UP:
						try {
							im.moveItem(1, stage);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case DOWN:
						try {
							im.moveItem(-1, stage);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case ENTER:
						try {
							im.moveItem(0, stage);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;

					case ESCAPE:
						pane.getChildren().remove(root);
						canPlay = true;
						break;

					default:
						break;

					}
				}
			}
		});
	}

	public void setG(IGenerator g, String type) {
		this.g = g;
		this.type = type;
		
	}
	
	public String getType(){
		return type;
	}
	
	public IGenerator getG(){
		return g;
	}
	
}
