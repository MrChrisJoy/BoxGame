import java.awt.Point;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class FlyIn extends Group {
	private Timeline timeline;
	
	public FlyIn(Node node, Pane pane, Point screenPosition, Duration in, Duration stay, Duration out, double outOffset) {
		int signX = screenPosition.x < 0 ? -1 : 1;
		timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(0, new KeyValue(node.translateXProperty(), screenPosition.x - signX * (pane.getWidth() - node.getWidth))), 
				new KeyFrame(0, new KeyValue(node.translateXProperty(), screenPosition.x))
		);
		
	}
	
	public FlyIn(Node node, Pane pane, Point screenPosition double in) {
		int signX = screenPosition.x < 0 ? -1 : 1;
		timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(0, new KeyValue(node.translateXProperty(), screenPosition.x - signX * (pane.getWidth() - node.getWidth))), 
				new KeyFrame(0, new KeyValue(node.translateXProperty(), screenPosition.x))
		);
	}
	
	private void playFromStart() {
		timeline.play();
	}
}
