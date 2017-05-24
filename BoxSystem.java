import javafx.application.Application;
import javafx.stage.Stage;

public class BoxSystem extends Application{
	private Menu m = new Menu();

	
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		m.start(primaryStage);
	}
}
