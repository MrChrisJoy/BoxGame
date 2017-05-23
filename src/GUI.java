import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 * Last Edited 24/05/2017		:	Antony.J
 *
 */
public class GUI extends Application {
	private final int sceneScale = 100;
	private final Vector2 sceneDimensions = new Vector2(500, 500);
	private final int cameraHeight = 10;
	private final float cameraAngle = 20;
	private final float edgeHeight = 20;
	
	private String type = "";
	private boolean canPlay = true;
	
	private boolean userGen = false;
	private Box[] boxes;
	private Group playerGroup;
	private IGenerator g;
	
	IngameMenu im = new IngameMenu();
	Parent root = im.createContent();
	
	private PhongMaterial box;
	private PhongMaterial floor;
	private PhongMaterial goal;
	private PhongMaterial player;
	private PhongMaterial edge;
	
	private TranslateTransition cameraTransition;
	private FadeTransition textTransition;
	private Timeline playerTransition;
	private Rotate rotation = new Rotate();
	
	private PerspectiveCamera camera;
	private Group level;
	private Text text;
	
	private CachedLevels cachedLevels;
	private Text time;
	private final DateFormat formatter = new SimpleDateFormat("mm:ss");
	
	private Timer timer;
	private boolean settingRecord;
	private Timeline timeline;
	private Record record;
	private long currTime;
	private long startTime;
	
	@Override 
	public void start(Stage stage) throws IOException {
		text = new Text("");
		text.setFill(Color.WHITE);
		text.setFont(new Font(20));
		textTransition = new FadeTransition();
		textTransition.setNode(text);
		textTransition.setDuration(Duration.millis(2000));
		textTransition.setInterpolator(Interpolator.EASE_IN);
		textTransition.setFromValue(1);
		textTransition.setToValue(0);
		
		time = new Text();
		time.setFont(new Font(50));
		time.setFill(Color.WHITE);
		time.setTranslateY(200);
		time.setTextAlignment(TextAlignment.LEFT);
		time.setVisible(false);
		Notification notification = new Notification();
		cachedLevels = new CachedLevels("Guest07");
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				  if(!settingRecord) {
					  Record r = cachedLevels.getRecord();
					  if(r != null && !r.equals(record)) {
						  record = r;
						  notification.showAlert(record.opponent + " challenged you");
					  }
				  }
			  }
			}, 10000, 10000);
		
		notification.onClick(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent m) {
				if(!settingRecord ) { // is the user is playing his generated game, then desable this
					newLevel(record.seed);
					text.setText("");
					settingRecord = true;
					time.setVisible(true);
					notification.hideAlert();
					
					currTime = record.time;
			        timeline = new Timeline();
			        timeline.setCycleCount(Timeline.INDEFINITE);
			        timeline.getKeyFrames().add(
			        		new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
			                    public void handle(ActionEvent event) {
			                    	Date date = new Date(currTime -= 10);
			                        time.setText(formatter.format(date));
			                        if (currTime < 10) {
			                        	currTime = 0;
			                            timeline.stop();
			                            lost();
			                        }
			                      }
			                }));
			        timeline.playFromStart();
				}
			}
		});
		
		camera = new PerspectiveCamera(false);
		camera.setTranslateZ(-sceneScale * cameraHeight);
		camera.setRotationAxis(new Point3D(1, 0, 0));
		camera.setRotate(cameraAngle);
		cameraTransition = new TranslateTransition();
		cameraTransition.setNode(camera);
		cameraTransition.setDuration(Duration.millis(400));
		cameraTransition.setInterpolator(Interpolator.EASE_OUT);
		
		level = new Group();
		setupMaterials();		
		
		newLevel();
		
		//Group 3D nodes into a sub-scene with perspective camera
		Group root3D = new Group(camera, text, level);
		SubScene subScene = new SubScene(root3D, sceneDimensions.x, sceneDimensions.y, true, SceneAntialiasing.DISABLED);
		subScene.setCamera(camera);
		subScene.setFill(Color.BLACK);
		
		//Group sub-scene and 2D elements into stack pane
		StackPane pane = new StackPane();
		pane.getChildren().addAll(subScene, notification, time, text);
		
		//Create main scene
		Scene scene = new Scene(pane);
		
		setupKeyEvents(scene, stage, pane);
		setupSwipeEvents(scene);
		
		
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
		
		stage.setTitle("Project");
		stage.setScene(scene); 
		stage.show(); 
	}
	
	@Override
	public void stop() {
		timer.cancel();
	}
	
	private void won() {
		if(settingRecord) {
			timeline.stop();
			text.setText((cachedLevels.setRecord(record.seed, record.time - currTime) ? "You beat " : "You lost to ") + record.opponent);
			time.setVisible(false);
			record = null;
		} else {
			text.setText("You " + (cachedLevels.setRecord(g.getSeed(), System.currentTimeMillis() - startTime) ? "set a record!" : "won!"));
		}
		newLevel();
	}
	
	private void lost() {
		if(settingRecord) {
			timeline.stop();
			text.setText("You lost to " + record.opponent);
			time.setVisible(false);
			
			cachedLevels.setRecord(record.seed, record.time);
			record = null;
			
		} else {
			text.setText("You lost!");
		}
		newLevel();
	}
	
	private void newLevel() {
		if(getType().equals("PLAY")){

			g = new Generator();
			getG().generateLevel(5, 20, 100);
		}
		if(getType().equals("USER")){

			g = new GeneratorUser();
			getG().generateLevel();
		}
		drawLevel();
	}
	
	private void newLevel(long seed) {
		g = new Generator();
		getG().generateLevel(5, 20, 100, seed);
		drawLevel();
	}
	
	public void drawLevel() {
		settingRecord = false;
		startTime = System.currentTimeMillis();
		
		createPlayerGroup(g.getPlayerLocation());
		createBoxes();
		level.getChildren().clear();
		level.getChildren().add(playerGroup);
		level.getChildren().addAll(boxes);
		addFloor(level);
		addGoals(level);
		addEdges(level);
		centreCamera(g.getPlayerLocation());
		
		textTransition.playFromStart();
	}
	
	private void centreCamera(Vector2 pos) {
		cameraTransition.stop();
		cameraTransition.setToX(sceneScale * pos.x - sceneDimensions.x/2.0 * camera.getScaleX());
		cameraTransition.setToY(sceneScale * pos.y - sceneDimensions.y/2.0 * camera.getScaleY() + (cameraAngle*cameraHeight*sceneScale)/(90 - cameraAngle));
		cameraTransition.play();
	}
	
	
	private void createPlayerGroup(Vector2 start) {
		Box p = new Box(sceneScale, sceneScale, sceneScale); 
		p.setMaterial(player);
		
		PointLight l = new PointLight();
	    l.setColor(Color.LIGHTGOLDENRODYELLOW);
	    l.setTranslateZ(-sceneScale);
	    
	    playerGroup = new Group(p);
	    playerGroup.setTranslateX(sceneScale * start.x);
	    playerGroup.setTranslateY(sceneScale * start.y);
	    
		playerTransition = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(rotation.angleProperty(), -90)), new KeyFrame(Duration.millis(150), new KeyValue(rotation.angleProperty(), 0)));
	    
	}
	
	public void createBoxes() {
		Vector2[] positions = g.getBoxLocations();
		boxes = new Box[positions.length];
		for(int i=0; i<boxes.length; i++) {
			boxes[i] = new Box(sceneScale, sceneScale, sceneScale);
			boxes[i].setTranslateX(sceneScale * positions[i].x);
			boxes[i].setTranslateY(sceneScale * positions[i].y);
			boxes[i].setMaterial(box);
			boxes[i].setDrawMode(DrawMode.FILL); 
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
			t.setTranslateZ(sceneScale/1.5);
			root.getChildren().add(t);
		}
	}
	
	private void addEdges(Group root) {
		for(Vector2 v : g.getEdges()) {
			Box b = new Box(sceneScale, sceneScale, sceneScale*edgeHeight);
			b.setTranslateX(sceneScale * v.x);
			b.setTranslateY(sceneScale * v.y);
			b.setTranslateZ(sceneScale * (edgeHeight - 1)/2);
			b.setMaterial(edge);
			b.setDrawMode(DrawMode.FILL);
			root.getChildren().add(b);
		}
	}
	
	private void movePlayer(Vector2 newPos, Camera c) {
		Vector2 dir = new Vector2(newPos.x - (int)playerGroup.getTranslateX()/sceneScale, newPos.y - (int)playerGroup.getTranslateY()/sceneScale);
		
		rotation.setAxis(new Point3D(dir.y, -dir.x, 0));
		rotation.setPivotZ(sceneScale/2);
		rotation.setPivotY(-dir.y * sceneScale/2);
		rotation.setPivotX(-dir.x * sceneScale/2);
	    
	    playerGroup.getTransforms().remove(rotation);
	    playerGroup.setTranslateX(newPos.x * sceneScale);
		playerGroup.setTranslateY(newPos.y* sceneScale);
	    playerGroup.getTransforms().add(rotation);
	    
		playerTransition.playFromStart();
		centreCamera(newPos);
	}
	
	private void moveBox(int id, Vector2 newPos) {
		boxes[id].setTranslateX(sceneScale * newPos.x);
		boxes[id].setTranslateY(sceneScale * newPos.y);
	}
	
	private void setupMaterials() {
		box = new PhongMaterial();
		box.setSpecularColor(new Color(0.05, 0.05, 0.05, 1));
		box.setDiffuseMap(new Image("https://www.filterforge.com/filters/9452.jpg"));
		box.setBumpMap(new Image("https://www.filterforge.com/filters/9452-normal.jpg"));
		floor = new PhongMaterial();
		floor.setSpecularColor(Color.DIMGRAY);
		floor.setDiffuseMap(new Image("https://www.filterforge.com/filters/10767.jpg"));
		floor.setBumpMap(new Image("https://www.filterforge.com/filters/10767-normal.jpg"));
		player = new PhongMaterial(Color.DIMGRAY);
		goal = new PhongMaterial(Color.GREY);
		goal.setSpecularColor(Color.SLATEGRAY);
		goal.setDiffuseMap(new Image("https://www.filterforge.com/filters/10767.jpg")); //11075
		goal.setBumpMap(new Image("https://www.filterforge.com/filters/10767-normal.jpg"));
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
						g.moveCharacter(Vector2.DOWN);
						break;
					case RIGHT:
						g.moveCharacter(Vector2.RIGHT);
						break;
					case DOWN:
						g.moveCharacter(Vector2.UP);
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
	
	private String getType(){
		return type;
	}
	
	private IGenerator getG(){
		return g;
	}	
	
	
	private void setupSwipeEvents(Scene scene) {
		scene.setOnSwipeDown(new EventHandler<SwipeEvent>() {
			@Override
			public void handle(SwipeEvent event) {
				g.moveCharacter(Vector2.DOWN);
			}
		});
		
		scene.setOnSwipeUp(new EventHandler<SwipeEvent>() {
			@Override
			public void handle(SwipeEvent event) {
				g.moveCharacter(Vector2.UP);
			}
		});
		
		scene.setOnSwipeLeft(new EventHandler<SwipeEvent>() {
			@Override
			public void handle(SwipeEvent event) {
				g.moveCharacter(Vector2.LEFT);
			}
		});
		
		scene.setOnSwipeRight(new EventHandler<SwipeEvent>() {
			@Override
			public void handle(SwipeEvent event) {
				g.moveCharacter(Vector2.RIGHT);
			}
		});
	}
}
