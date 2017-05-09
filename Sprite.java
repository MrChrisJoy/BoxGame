import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Sprite extends StackPane {
	Sprite(int blockNum, double x, double y, double width, double height) {
		// create rectangle
		Rectangle rectangle = new Rectangle(width, height);
		Circle circle = new Circle(width);
		circle.setStroke(Color.WHITE);
		rectangle.setStrokeWidth(1);
		//rectangle.setStroke(Color.WHITE);
		switch (blockNum) {
		case 1:
			blockNum = 1;
			rectangle.setFill(Color.WHITE);
			break;
		case 2:
			blockNum = 2;
			rectangle.setFill(Color.BROWN);
			break;
		case 3:
			blockNum = 3;
			rectangle.setFill(Color.YELLOW);
			break;
		case 4:
			blockNum = 4;
			rectangle.setFill(Color.BLUE);
			break;
		case 5:
			blockNum = 0;
			rectangle.setFill(Color.GREY);
		default:
			//everything outside of the maze is a wall
			rectangle.setFill(Color.GREY);
			break;

			
		}
		// set position
					setTranslateX(x);
					setTranslateY(y);
					
		getChildren().addAll(rectangle);

	}

}