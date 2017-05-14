import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class Generator implements IGenerator {
	public static Random random = new Random();
	
	private State start;
	private State goal;
	private State curr;
	private List<State> next;
	
	private int movedBox;
	
	private Vector2 playerPosition;
	private Set<Vector2> floor;
	private Set<Vector2> edge;
	
	private EventHandler<ActionEvent> positionHandler;
	private EventHandler<ActionEvent> boxHandler;
	private EventHandler<ActionEvent> winHandler;
	private EventHandler<ActionEvent> loseHandler;
	
	
	public void moveCharacter(int dir) {
		Vector2 newPos = Vector2.add(playerPosition, Vector2.directions[dir]);
		for(int i=0; i<curr.getBoxLocations().length; i++) {
			if(curr.getBoxLocation(i).equals(newPos)){
				for(State s : next) {
					if(s.getMovedBox() == i && s.getMovedDirection() == dir && floor.contains(s.getMovedBoxLocation())) {
						curr = s;
						next = curr.getStates(false);
						playerPosition = newPos;
						movedBox = curr.getMovedBox();
						
						positionHandler.handle(null);
						boxHandler.handle(null);
						
						if(!s.boxHasNextState(false, floor, i) && !hasGoal(s.getMovedBoxLocation())) {
							//TODO sometimes box is only blocked by other boxes -> no lose
							loseHandler.handle(null);
						} else if(hasGoal(s.getMovedBoxLocation())) {
							for(Vector2 box : curr.getBoxLocations()) if(!hasGoal(box)) return;
							winHandler.handle(null);
						}					
						return;
					}
				}
				return;
			}
		}
		if(floor.contains(newPos)) {
			playerPosition = newPos;
			positionHandler.handle(null);
		}
	}
	
	public void generateLevel(int numBoxes, int numIterations, int difficulty, long seed) {
		random.setSeed(seed);
		generateLevel(numBoxes, numIterations, difficulty);
	}
	
	public void generateLevel(int numBoxes, int numIterations, int difficulty) {
		floor = new HashSet<Vector2>();	
		edge = new HashSet<Vector2>();	
		goal = new State(randomPositions(numBoxes));
		start = goal;
		for(int i=0; i<numIterations; i++) {
			State temp = start.getRandomState(floor, true, difficulty);
			if(temp == null) break;
			start = temp;
		}
		
		for(Vector2 v : floor) {
			for(Vector2 dir : Vector2.directions) {
				Vector2 adjacent = Vector2.add(v, dir);
				if(!floor.contains(adjacent)) edge.add(adjacent); 
			}
		}
		
		curr = start;
		next = start.getStates(false);
		playerPosition = start.getPlayerLocation();
		movedBox = curr.getMovedBox();
	}
	
	public int getBoxMovedBox() {
		return movedBox;
	}
	
	public Vector2[] getBoxLocations() {
		return curr.getBoxLocations();
	}
	
	public Vector2 getPlayerLocation() {
		return playerPosition;
	}
	
	public Set<Vector2> getFloor() {
		return floor;
	}
	
	public Vector2[] getGoals() {
		return goal.getBoxLocations();
	}
	
	public boolean hasGoal(Vector2 g) {
		for(Vector2 v : goal.getBoxLocations()) if(v.equals(g)) return true;
		return false;
	}
	
	public Set<Vector2> getEdges() {
		return edge;
	}
	
	private Vector2[] randomPositions(int num) {
		Vector2[] positions = new Vector2[num];
		int[] xRandom = randomSequence(1, num + 1);
		int[] yRandom = randomSequence(1, num + 1);
		for(int i=0; i<positions.length; i++) positions[i] = new Vector2(xRandom[i], yRandom[i]);
		return positions;
	}
	
	static public int[] randomSequence(int start, int length) {
		int[] array = new int[length];
		for(int i=0; i<array.length; i++) array[i] = start + i;
	    for (int i = array.length - 1; i > 0; i--) {
	      int index = Generator.random.nextInt(i + 1);
	      int a = array[index];
	      array[index] = array[i];
	      array[i] = a;
	    }
	    return array;
	}

	public void setOnPlayerMove(EventHandler<ActionEvent> eventHandler) {
		positionHandler = eventHandler;
	}

	public void setOnBoxMove(EventHandler<ActionEvent> eventHandler) {
		boxHandler = eventHandler;
	}
	
	public void setOnWin(EventHandler<ActionEvent> eventHandler) {
		winHandler = eventHandler;
	}
	
	public void setOnLose(EventHandler<ActionEvent> eventHandler) {
		loseHandler = eventHandler;
	}

	public void undo() {
		if(!curr.equals(start)) {
			movedBox = curr.getMovedBox();
			
			curr = curr.getParent();
			next = curr.getStates(false);
			playerPosition = curr.getPlayerLocation();
			
			positionHandler.handle(null);
			boxHandler.handle(null);
		}
	}
}
