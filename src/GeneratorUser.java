import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * 
 * @author AM
 *
 */
public class GeneratorUser implements IGenerator {
	private UserGrid userGrid;
	
	
	private int movedBox;
	private int prevDir;
	private boolean boxMoved;
	private Vector2 playerPosition;
	Vector2[] boxLocations;
	Vector2[] goalLocations;
	
	private Set<Vector2> floor;
	private Set<Vector2> edge;
	
	private EventHandler<ActionEvent> positionHandler;
	private EventHandler<ActionEvent> boxHandler;
	private EventHandler<ActionEvent> winHandler;
//	private EventHandler<ActionEvent> loseHandler;
	
	public GeneratorUser() {
		prevDir = 5; // define undefine Dir
		boxMoved = false;
		userGrid = new UserGrid();
		
	}
	
	//
	//  	RELATED TO WEAREHOUSE GRID
	//
	 
	@Override
	public void generateLevel() {
		userGrid.createUserDefinedGrid();
		boxLocations = userGrid.getBoxLoctions();
		goalLocations = userGrid.getGoals();
	}
	
	@Override
	public Vector2 getPlayerLocation() {
		playerPosition = userGrid.getPlayerLocation();
		return playerPosition;
	}
	
	@Override
	public Set<Vector2> getFloor() {
		this.floor = userGrid.getFloorSet();
		return floor;
	}
	
	@Override
	public Set<Vector2> getEdges() {
		this.edge = userGrid.getEdgesSet();
		return edge;
	}
	
	@Override
	public Vector2[] getGoals() {
		return goalLocations;
	}
	
	@Override
	public Vector2[] getBoxLocations() {
		return boxLocations;
	}
	
	//
	//  	GET MOVED LOCATIONS
	//
	
	
	@Override
	public void moveCharacter(int dir) {
		prevDir = dir;
		
		Vector2 newPos = Vector2.add(playerPosition, Vector2.directions[dir]);

		if(floor.contains(newPos) && !userGrid.getUserDefinedGrid()[newPos.x][newPos.y].getBlockType().equals(BlockType.box)) {
			userGrid.getUserDefinedGrid()[playerPosition.x][playerPosition.y].setBlockType(BlockType.floor);
			userGrid.getUserDefinedGrid()[newPos.x][newPos.y].setBlockType(BlockType.player);
			playerPosition = newPos;
			positionHandler.handle(null);
			boxMoved = false;
			
		}else {		
			for(int i=0; i < boxLocations.length; i++) {
				if(boxLocations[i].equals(newPos)){
					Vector2 newBoxPos = Vector2.add(boxLocations[i], Vector2.directions[dir]);
					if(floor.contains(newBoxPos) && !userGrid.getUserDefinedGrid()[newBoxPos.x][newBoxPos.y].getBlockType().equals(BlockType.box)) {
						userGrid.getUserDefinedGrid()[playerPosition.x][playerPosition.y].setBlockType(BlockType.floor);
						userGrid.getUserDefinedGrid()[newPos.x][newPos.y].setBlockType(BlockType.player);
						userGrid.getUserDefinedGrid()[newBoxPos.x][newBoxPos.y].setBlockType(BlockType.box);
						playerPosition = newPos;
						movedBox = i;
						boxLocations[i] = newBoxPos;
						positionHandler.handle(null);
						boxHandler.handle(null);
						boxMoved = true;
					}
					int numWins = 0;
					for(Vector2 goal: goalLocations) {					
						if(userGrid.getUserDefinedGrid()[goal.x][goal.y].getBlockType().equals(BlockType.box)) {
							numWins++;
						}
					}
					if(userGrid.getGoals().length == numWins) {
						winHandler.handle(null);
					}
				}
			}		
		}	
	}
	
	
	@Override
	public void undo() {
		if(getOppositeDir() == 5) {
			System.out.println("no undo");
			positionHandler.handle(null);
		}else {
			Vector2 newPos = Vector2.add(playerPosition, Vector2.directions[getOppositeDir()]);
			if(!boxMoved) {
				userGrid.getUserDefinedGrid()[playerPosition.x][playerPosition.y].setBlockType(BlockType.floor);
				userGrid.getUserDefinedGrid()[newPos.x][newPos.y].setBlockType(BlockType.player);
				playerPosition = newPos;
				positionHandler.handle(null);
				prevDir = 5;  // assigning to be undefined dirr
			}else {
				Vector2 newBoxPos = Vector2.add(boxLocations[this.getBoxMovedBox()], Vector2.directions[getOppositeDir()]);
				userGrid.getUserDefinedGrid()[boxLocations[this.getBoxMovedBox()].x][boxLocations[this.getBoxMovedBox()].y].setBlockType(BlockType.floor);
				userGrid.getUserDefinedGrid()[playerPosition.x][playerPosition.y].setBlockType(BlockType.floor);
				userGrid.getUserDefinedGrid()[newPos.x][newPos.y].setBlockType(BlockType.player);
				userGrid.getUserDefinedGrid()[newBoxPos.x][newBoxPos.y].setBlockType(BlockType.box);
				playerPosition = newPos;
				boxLocations[this.getBoxMovedBox()] = newBoxPos;
				positionHandler.handle(null);
				boxHandler.handle(null);
				prevDir = 5;  // assigning to be undefined dirr
			}
		}
	}
	
	@Override
	public int getBoxMovedBox() {
		return movedBox;
	}
	
	
	@Override
	public boolean hasGoal(Vector2 g) {
		Vector2[] goalLocs = getGoals();
		for(Vector2 goal: goalLocs) {
			if(goal.equals(g))
				return true;
		}
		return false;
	}
	

	
	//
	//  	RELATED TO MOVEMENT IN GRID
	//

	@Override
	public void setOnPlayerMove(EventHandler<ActionEvent> eventHandler) {
		positionHandler = eventHandler;
	}
	
	@Override
	public void setOnBoxMove(EventHandler<ActionEvent> eventHandler) {
		boxHandler = eventHandler;
	}
	
	@Override
	public void setOnWin(EventHandler<ActionEvent> eventHandler) {
		winHandler = eventHandler;
	}
	
	@Override
	public void setOnLose(EventHandler<ActionEvent> eventHandler) {
		//loseHandler = eventHandler;
	}
	
	
	
	private int getOppositeDir() {
		int oppDir = 5; // assigning undefined Direction
		switch(prevDir){
			case Vector2.LEFT:
				oppDir = Vector2.RIGHT;
				break;
			case Vector2.RIGHT:
				oppDir = Vector2.LEFT;
				break;
			case Vector2.UP:
				oppDir = Vector2.DOWN;
				break;
			case Vector2.DOWN:
				oppDir = Vector2.UP;
				break;
			default :
				break;
		}
		return oppDir;
	}

	
	//
	//  PROBABLY NOT NEED THEM, SINCE THESE ARENT NECESSARY FOR USER 
	//
	@Override
	public void generateLevel(int numBoxes, int numIterations, int difficulty, long seed) {
		// TODO Auto-generated method stub
	}
	@Override
	public void generateLevel(int numBoxes, int numIterations, int difficulty) {
		// TODO Auto-generated method stub		
	}

}
