import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Notification extends Group {
	private TranslateTransition tt = new TranslateTransition();
	private Text t = new Text();
	private ImageView i = new ImageView();
	private final Vector2 in = new Vector2(400, 200);
	private final Vector2 out = new Vector2(150, 200);
	private final Vector2 mid = new Vector2(295, 200);
	
	public Notification() {
		tt.setNode(this);
		tt.setDuration(Duration.millis(2000));
		tt.setInterpolator(Interpolator.EASE_BOTH);
		t.setX(50);
		t.setY(20);
		t.setFill(Color.BLACK);
		t.setStroke(Color.WHITE);
		
		i.setImage(new Image("https://lh3.googleusercontent.com/3Ofcop0iBUBDcxHk_-fB3-Y9xaeIv9Tt6Mvvnv6W8085GbqUrJiLOZR35yLpaR3VqTR1ocG9YSwCVvt5MkeN3mA=s0"));
		i.setPreserveRatio(true);
		i.setFitWidth(40);
		
		
		this.getChildren().addAll(i, t);
		this.setTranslateX(in.x);
		this.setTranslateY(in.y);
		
		tt.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(tt.getToX() == out.x) {
					tt.setToX(mid.x);
					tt.play();
				}
			}
		});
	}
	
	public void onClick(EventHandler<MouseEvent> onClick) {
		i.setOnMouseClicked(onClick);
	}
	
	public void showAlert(String text) {
		t.setText(text);
		tt.setToX(out.x);
		tt.playFromStart();
	}
	
	public void hideAlert() {
		tt.setToX(in.x);
		tt.playFromStart();
	}
}
