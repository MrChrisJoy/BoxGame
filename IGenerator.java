import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface IGenerator {
	public void moveCharacter(int dir);
	public void generateLevel(int numBoxes, int numIterations, int difficulty, long seed);
	public void generateLevel(int numBoxes, int numIterations, int difficulty);
	public int getBoxMovedBox();
	public Vector2[] getBoxLocations();
	public Vector2 getPlayerLocation();
	public Set<Vector2> getFloor();
	public Vector2[] getGoals();
	public boolean hasGoal(Vector2 g);
	public Set<Vector2> getEdges();
	public void undo();
	public long getSeed();
	public void setOnPlayerMove(EventHandler<ActionEvent> eventHandler);
	public void setOnBoxMove(EventHandler<ActionEvent> eventHandler);
	public void setOnWin(EventHandler<ActionEvent> eventHandler);
	public void setOnLose(EventHandler<ActionEvent> eventHandler);
}
