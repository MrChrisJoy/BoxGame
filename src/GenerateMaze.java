import java.util.ArrayList;
import java.util.Random;

public class GenerateMaze {
	private CellOfMaze[][] maze;
	private int size;
	private ArrayList<CellOfMaze> blocksList;
	
	
	public GenerateMaze() {
		blocksList = new ArrayList<CellOfMaze>();
	}
	
	/**
	 * Creates a maze of default complexity using MazeBlocks to store important info
	 */
	public CellOfMaze[][] createMaze(int diff){
		int s = diff;	
		size = 2*s+1;
		//set to "1" or " "
		maze = new CellOfMaze[size][size];		
		//create a raw maze
		int blocksNeed = (s)*(s);		
		for (int r=0;r<size;r++){
			for(int c=0;c<size;c++){
				CellOfMaze mazeblock = new CellOfMaze(c,r);
				maze[c][r] = mazeblock;			
			}
		}
		
		Random RG = new Random();
	
		CellOfMaze startMazeBlock = maze[RG.nextInt(s)*2+1][RG.nextInt(s)*2+1];
		//add visited block into list
		blocksList.add(startMazeBlock);
		startMazeBlock.setStatus(CellType.player);
		blocksNeed --;
		
		while(blocksNeed != 0){
			int i = RG.nextInt(blocksList.size());
			CellOfMaze randomBlock = blocksList.get(i);
			int nextx = 0;
			int nexty = 0;
			boolean decide = RG.nextBoolean();
			if(decide){
				nextx = randomBlock.getX()+RG.nextInt(2)*4-2;
				nexty = randomBlock.getY();
			}else{
				nextx = randomBlock.getX();
				nexty = randomBlock.getY()+RG.nextInt(2)*4-2;
			}
			if(nextx>0 && nextx<size && nexty>0 && nexty<size){
				CellOfMaze nextBlock = maze[nextx][nexty];
				if(blocksList.indexOf(nextBlock) == -1){
					//add this new block into list
					blocksList.add(nextBlock);
					nextBlock.setStatus(CellType.floor);
					blocksNeed --;
					//add the path between this block and previous block
					maze[(nextx+randomBlock.getX())/2][(nexty+randomBlock.getY())/2].setStatus(CellType.floor);
				}				
			}		
			
		}
		return maze;
	}

	
	/**
	 * Prints the maze to STDOUTe
	 */
	public void printMaze(){
		String status = "";
		for (int r = 0;r<size;r++){
			String line = "";
			for (int c= 0;c<size;c++){
				if(maze[c][r].getStatus().equals(CellType.floor)) {
					status = " ";
				}else if(maze[c][r].getStatus().equals(CellType.wall)){
					status = "#";
				}else if(maze[c][r].getStatus().equals(CellType.box)){
					status = "B";
				}else if(maze[c][r].getStatus().equals(CellType.goal)) {
					status = "G";
				}else if(maze[c][r].getStatus().equals(CellType.test)) {
					status = " ";
				}else if(maze[c][r].getStatus().equals(CellType.fault)) {
					status = "X";
				}else if(maze[c][r].getStatus().equals(CellType.player)) {
					status = "P";
				}
				line  = line+status;
			}
			System.out.println(line);	
		}	
	}
	
	public CellOfMaze[][] getMaze(){
		return maze;
	}
	
	public void setMaze(CellOfMaze[][] maze){
		this.maze  = maze;
	}

	public void setMazeSize(int size) {
		this.size = size;
	}
	public int getMazeSize() {
		return size;
	}

}
