import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	private int[][] grid;		//represents the state of the board
	private int[][] solnGrid;	//stores the first solved board, to be printed out if needed
	private ArrayList<Spot> SpotList;
	public int[] firstSolution;
	public long timeStart;		//will store time at the start of solve()
	public long timeStop;		//will store time at the end of solve()
	
	
	public class Spot{
		private int row;
		private int col;
		private Set<Integer> setOfValidNums;
		
		/*	Spot():
		 * 	Constructor for Spot.  Given integer x value, y value, and a integer grid representing the
		 * Sudoku board, the constructor will create a Spot instance and set its internal x and y variables
		 * to the given values, and also use the board info to populate its Set of valid number assignments
		 */

		public Spot(int xval, int yval,int[][] board){
			this.row = xval;
			this.col = yval;
			calcValidNums(board);   //populates the internal setOfValidNums
		}
		/*	getSetSize:
		 * 	Returns size of this Spot's set of valid number candidates
		 * 
		 */
		public int getSetSize(){
			return setOfValidNums.size();
		}
		
		/*	setGridValue:
		 * 	Given an int grid and an integer value, set the main grid location corresponding to this Spot to the value of the int. 
		 * 
		 */
		public void setGridValue(int[][] board,int number){
			board[this.row][this.col]=number;
		}
		
		/*	clear:
		 * 	Given an int grid, clears the cell (sets to 0) that corresponds to this Spot's coordinates. 
		 * 
		 */
		public void clear(int[][] board){
			board[this.row][this.col]=0;
		}
		
		
		/*  clearValidNums:
		 * 	Empties out the Spot's set of valid values, in anticipation of recalculating them 
		 * 
		 */
		public void clearValidNums(){
			this.setOfValidNums.removeAll(this.setOfValidNums); 
		}
		
		
		/*  calcValidNums:
		 * 	Figures out what numbers are legal for this Spot, given a board state. 
		 *  Updates the Spot's internal Set of valid numbers.  
		 * 
		 */
		public void calcValidNums(int[][] board){
			Set<Integer> tempSet = new HashSet<Integer>();
			for(int value = 1;value <10;value++){
				if(!RowColContains(value,board) && !BlockContains(value,board)){
					tempSet.add((Integer)value);
				}			
			}
			this.setOfValidNums = tempSet;
		}
		
		/*  RowColContains:
		 * 	Given an int grid, and a value, determines if that value already exists in 
		 * 	the same row or same column as the caller Spot.  Returns true if so, otherwise false.
		 * 
		 */
		public boolean RowColContains(int value, int[][] board){
			for(int i =0; i< board.length;i++){   //parse all rows of the same COLUMN
				if(board[i][this.col]==value){		
					return true;
				}
				if(board[this.row][i]==value){ 	 	//parse all columns of the same ROW
					return true;
				}
			}
			return false;
		}
		
		/*  BlockContains:
		 * 	Given an int grid, and a value, determines if that value already exists in 
		 * 	the same 3x3 "Block" as the caller Spot.  Returns true if so, otherwise false.
		 * 
		 */
		public boolean BlockContains(int value, int[][] board){
			int blockRow = this.row/3;				//this Spot's block's row index (row 0-2 is index 0, row 3-5 is index 1, row 6-8 is index 2)
			int blockCol = this.col/3;				//block's col index....
			int blockRowIndex = blockRow*3;		//starting Row index of your particular block
			int blockColIndex = blockCol*3;		//starting Col index of your particular block
			for(int i =blockRowIndex; i< blockRowIndex+3;i++){   //parse all locations of your 3x3 block
				for(int j = blockColIndex; j<blockColIndex+3;j++){
					if(board[i][j] == value){  //if the desired value already exists within the block...
						return true;			//return true
					}
				}		
			}
			return false;
		}
		
	} //end Spot class definition
	

	/*  printSortedSpotList:
	 * 	Debugging method. Retrieves the sorted Spots from the Spot List of the Sudoku instance, and 
	 * 	prints out the location and number of possible values for each. 
	 */
	public void printSortedSpotList(){
		for(int i=0;i<SpotList.size();i++){
			System.out.println("Spot number "+i+" is located at x: "+SpotList.get(i).row+" y: "+SpotList.get(i).col+" and has this many possibilities: "+SpotList.get(i).getSetSize());
		
		}
	
	}
	
	
	// Created almost done grid
	// (can paste this text into the GUI too)
	public static final int[][] almostGrid = Sudoku.stringsToGrid(
	"0 0 5 0 0 0 0 0 4",
	"0 0 2 0 0 0 0 0 0",
	"7 0 0 0 0 0 1 0 0",
	"0 0 0 0 0 0 6 0 5",
	"0 9 0 8 0 0 2 0 0",
	"0 4 0 1 0 0 9 0 0",
	"0 0 0 0 7 0 3 8 9",
	"0 0 0 0 0 0 0 0 0",
	"8 7 0 0 4 0 0 0 0");
	
	
		
		
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		System.out.println(sudoku); 	
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());	
		
	}


	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {  
		grid = ints;
		solnGrid = new int[grid.length][grid[0].length];
		for(int x=0;x<grid.length;x++){				//copies the initial grid into the solution grid, the holes will be filled in later once we solve
			System.arraycopy(this.grid[x], 0, this.solnGrid[x], 0, this.grid[0].length);
		}
		
		SpotList = new ArrayList<Spot>();
		generateSpots(grid, SpotList);
		SpotList = sortSpotList(SpotList);
		firstSolution = new int[SpotList.size()];  //default value is 0 in java
		
		
	}
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search. Also sets the time variables so getElapsed() 
	 * can return the time spent in the method
	 */
	public int solve() {
		timeStart = System.currentTimeMillis();
		int solutionCount = recurseSolve(grid,0,0);  //start with the first Spot in the Spot List, no solutions thus far.
		timeStop = System.currentTimeMillis();
		return solutionCount;	
	}
	
	/*
	 *  recurseSolve:
	 * 	Called by its wrapper function solve(), which is a method in the Sudoku class. Given the current grid state, and an int array 
	 *  to carry the first solution found, and the index of the Spot to be examined (from the SpotList) recompute the Spot's possible values, then try all the Spot's 
	 *  possible values as a starting move, and pass forward an updated board to the recursion function.
	 *  The base case should be when you reach a spot that doesn't have any possible values (in which case you failed to find a solution)
	 *  or if you reach the last Spot and can find a single value that is valid (in which case, you found a solution). 
	 *  After getting the returned results, the function clears the grid location corresponding to the Spot it is working with, 
	 *  so that it doesn't leave traces that will screw up other recursion computations. 
	 *  
	 *  Also passes a "solutions found thus far" variable to check if we've found 100 solutions yet. If we have,
	 *  then we will stop recursing and just return 0's.  This limits the solution count to 100. 
	 */
	
	private int recurseSolve(int[][] modBoard, int spotIndex, int solnsThusFar){
		int totalSoln=0;	
		Spot currentSpot = SpotList.get(spotIndex);		//retrieve the spot we are going to examine
		currentSpot.clearValidNums();       //clear out the old valid numbers since the board has been modified
		currentSpot.calcValidNums(modBoard);    //recalculate valid nums given a modified/updated board

		//case 0: Reached last Spot to be checked, and only 1 valid number
		if((spotIndex==(SpotList.size()-1)) && (currentSpot.getSetSize()==1)){
			if(firstSolution[spotIndex]==0){  //if array location is 0, means it has not been written by anyone else yet, so we can be the first to update it!
				Iterator<Integer> itr = currentSpot.setOfValidNums.iterator();
				firstSolution[spotIndex]=(int)itr.next();   //write the one sole valid answer to the solution array cell corresponding to this Spot (the very last one)
			}
			return 1;
		}
		
		//case 1: Bad ending - last Spot and does not have a single answer....
		else if(spotIndex == (SpotList.size()-1)){    		
			return 0;
		}
		//at this point we definitely are not at the last Spot
		
		//case 2: no valid numbers possible for this Spot: 
		if(currentSpot.getSetSize()==0){		
			return 0;
		}		

		//general case:  at this point, you are not the last Spot and you have at least 1 possible number you can try
		Iterator<Integer> valueIter = currentSpot.setOfValidNums.iterator();
		while(valueIter.hasNext() && ((solnsThusFar+totalSoln)<100)){   			//try all of this Spot's possible values!			
			int tryValue = valueIter.next();	
			int nextIndex = spotIndex+1;
			currentSpot.setGridValue(modBoard, tryValue);			//modify the board to try one of the valid values
			int recurseReturn = recurseSolve(modBoard, nextIndex,(solnsThusFar + totalSoln));
			if((recurseReturn>0) && (firstSolution[spotIndex]==0)){    //check if you got some solutions back.  If so, then check if the solution array for this Spot has been filled  If not, you're the first!  
				firstSolution[spotIndex]= tryValue;      //put your value in there
			}
			totalSoln = totalSoln + recurseReturn;				
		}	
		currentSpot.clear(modBoard);      //remove traces of this Spot's modification.  
		return totalSoln;
	}
	
	/*
	 *  getSolutionText:
	 * 	Outputs the first solution found, in string form, if any. First uses the finalSolution array,
	 *  along with the Spot List, to fill in the locations in the solnGrid specified by the Spot List with the numbers 
	 *  found in the finalSolution array.  It then converts the int[][] format into a string.  If no solution was found,
	 *  or solve hasn't been run yet, the solution grid is a replica of the original puzzle grid. 
	 */
	public String getSolutionText() {	
		for(int i=0;i<SpotList.size();i++){
			SpotList.get(i).setGridValue(solnGrid, firstSolution[i]);			
		}		
		return SolnToString(); 
	}
	
	/*
	 *  toString:
	 * 	Overrides default toString.  Converts an int[][] grid into a text string.
	 */
	@Override
	public String toString() {
		String eol = System.getProperty("line.separator");
		String output = "";
		for(int i=0; i<grid.length;i++){
			for(int j=0;j<grid[0].length;j++){
				output = output + grid[i][j] + " ";				
			}
			output = output + eol;			
		}
		return output;
	}
	
	/*
	 *  SolnToString:
	 * 	Returns the text form of the solved grid.
	 */
	public String SolnToString() {
		String eol = System.getProperty("line.separator");
		String output = "";
		for(int i=0; i<solnGrid.length;i++){
			for(int j=0;j<solnGrid[0].length;j++){
				output = output + solnGrid[i][j] + " ";				
			}
			output = output + eol;			
		}
		return output;
	}
	
	/*
	 *  getElapsed:
	 * 	Returns time the solve took to compute
	 */
	public long getElapsed() {
		return (timeStop-timeStart); 
	}
	
	/*
	 *  generateSpots:
	 * 	Fills the Sudoku object's SpotList with spots that correspond
	 *  to the blank (aka has value 0) spaces in the grid. Intended for use only during constructor call.
	 */
	private ArrayList<Spot> generateSpots(int[][] board, ArrayList<Spot> SpotArray){
		for(int row=0;row<SIZE;row++){
			for(int col=0;col<SIZE;col++){
				if(grid[row][col] ==0){
					Spot newSpot = new Spot(row,col,grid);
					SpotArray.add(newSpot);				
				}
			}
		}
		return SpotArray;
	}
	
	/*
	 *  sortSpotList:
	 * 	Sorts the given array of Spots in ascending order based on the size of their internal Sets.
	 *  Uses a simple Bubble Sort algorithm since the data is limited in size
	 */
	private ArrayList<Spot> sortSpotList(ArrayList<Spot> SpotArray){
		if(SpotArray.size()<=1 || SpotArray == null){
			return SpotArray;
		}
		int swapflag=1;
		while(swapflag ==1){   //keep going until no more swaps
			swapflag =0;    //by default indicates no swaps were made.  Only changes if a swap triggers. 
			for(int i=0;i<SpotArray.size()-1;i++){   //parse item and its higher index neighbor, thus the loop index only goes up till 2nd to last index
				if(SpotArray.get(i).getSetSize()>SpotArray.get(i+1).getSetSize()){  //if earlier index has bigger set then next index, switch them
					Spot temp = SpotArray.get(i);
					SpotArray.set(i, SpotArray.get(i+1));
					SpotArray.set(i+1, temp);
					swapflag =1;     //set flag high to show a swap occurred
				}
			}
		}	
		return SpotArray;
	}	
}
