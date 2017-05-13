import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Sprite extends StackPane {
	Sprite(CellType type, double x, double y, double width, double height) {
		// create rectangle
		Rectangle rectangle = new Rectangle(width, height);
		Circle circle = new Circle(width);
		circle.setStroke(Color.WHITE);
		rectangle.setStrokeWidth(1);
		//rectangle.setStroke(Color.WHITE);
		
		if(type.equals(CellType.floor)){
			rectangle.setFill(Color.WHITE);
		}else if(type.equals(CellType.box)) {
			rectangle.setFill(Color.BROWN);
		}else if(type.equals(CellType.goal)) {
			rectangle.setFill(Color.YELLOW);
		}else if(type.equals(CellType.player)) {
			rectangle.setFill(Color.BLUE);
		}else if(type.equals(CellType.wall)) {
			rectangle.setFill(Color.GREY);
		}
		


		setTranslateX(x);
		setTranslateY(y);
					
		getChildren().addAll(rectangle);

	}

}