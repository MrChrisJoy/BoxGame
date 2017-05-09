import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Generator {
	public static final Vector2[] directions = {new Vector2(-1, 0), new Vector2(0, 1), new Vector2(1, 0), new Vector2(0, -1)};
	public static final Random random = new Random();
	
	private BlockType[][] level;
	private List<State> next;
	private int minX, maxX, minY, maxY;
	private Vector2 playerPosition;
	private Vector2[] goals;
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Generator b = new Generator();
	}
	
	public Generator() {
		generateLevel(5, 20, 100);
	}
	
	public Vector2 moveCharacter(int dir) {
		Vector2 newPos = Vector2.add(playerPosition, directions[dir]);
			switch(getLevelPosition(newPos)) {
			case goal : 
			case floor : 
				setLevelPosition(newPos, BlockType.player);
				setLevelPosition(playerPosition, Arrays.asList(goals).contains(playerPosition) ? BlockType.goal : BlockType.floor);
				return (playerPosition = newPos);
			case box : 
				if(getLevelPosition(newPos).equals(BlockType.box)) {
					for(State s : next) {
						if(s.getPlayerLocation().equals(newPos) && s.getMovedBoxLocation().equals(Vector2.add(newPos, directions[dir])) && getLevelPosition(s.getMovedBoxLocation()) != BlockType.wall) {
							next = s.getStates(false);
							setLevelPosition(newPos, BlockType.player);
							setLevelPosition(s.getMovedBoxLocation(), BlockType.box);
							setLevelPosition(playerPosition, Arrays.asList(goals).contains(playerPosition) ? BlockType.goal : BlockType.floor);
							return (playerPosition = newPos);
						}
					}
				}
			default: return null;
		}
	}
	
	public void generateLevel(int numBoxes, int numIterations, int difficulty) {
		Set<Vector2> traversed = new HashSet<Vector2>();
		goals = randomPositions(numBoxes);
				
		State s = new State(goals);
		for(int i=0; i<numIterations && s != null; i++) {
			State n = s.getRandomState(traversed, true, difficulty);
			if(n == null) break;
			s = n;
		}
		
		//Get boolean bounding region for boxes with border depth 1
		minX = maxX = s.getPlayerLocation().x;
		minY = maxY = s.getPlayerLocation().y;
		for(State curr = s; curr != null; curr = curr.getParent()) {
			for(Vector2 box : curr.getBoxLocations()) {
				if(box.x < minX) minX = box.x;
				if(box.x > maxX) maxX = box.x;
				if(box.y < minY) minY = box.y;
				if(box.y > maxY) maxY = box.y;
			}
		}
		
		for(Vector2 floor : traversed) {
			if(floor.x < minX) minX = floor.x;
			if(floor.x > maxX) maxX = floor.x;
			if(floor.y < minY) minY = floor.y;
			if(floor.y > maxY) maxY = floor.y;
		}
		
		minX--;
		minY--;
		maxX++;
		maxY++;
		
		int paddedSize = Math.max(Math.abs(maxX - minX)+1, Math.abs(maxY - minY)+1);
		
		//Create 2D array with default false and add fence-post error
		level = new BlockType[paddedSize][paddedSize];
		
		for(Vector2 v : traversed) setLevelPosition(v, BlockType.floor);
		for(Vector2 v : goals) setLevelPosition(v, BlockType.goal);
		for(Vector2 box : s.getBoxLocations()) setLevelPosition(box, BlockType.box);
		setLevelPosition(s.getPlayerLocation(), BlockType.player);
		
		for(int y=level[0].length-1; y>=0; y--) for(int x=0; x<level.length; x++) if(level[x][y] == null) level[x][y] = BlockType.wall;
		
		next = s.getStates(false);
		playerPosition = s.getPlayerLocation();
		printLevel();
	}
	
	public BlockType[][] getEnvironment() {
		return level;
	}
	
	private void setLevelPosition(Vector2 v, BlockType b) {
		level[v.x - minX][v.y - minY] = b;
	}
	
	private BlockType getLevelPosition(Vector2 v) {
		return level[v.x - minX][v.y - minY];
	}
	
	public void printLevel() {
		for(int y=level[0].length-1; y>=0; y--) {
			for(int x=0; x<level.length; x++) {
				System.out.print(level[x][y].ordinal());
			}
			System.out.println();
		}
	}
	
	private Vector2[] randomPositions(int num) {
		Vector2[] positions = new Vector2[num];
		int[] xRandom = randomSequence(1, num );
		int[] yRandom = randomSequence(1, num);
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
}
