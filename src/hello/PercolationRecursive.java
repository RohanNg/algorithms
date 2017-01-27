   package percolation;

public class PercolationRecursive implements Percolation {
	private final int[] states; 
	private final int gridSize;
	private boolean isPercolate;
	public PercolationRecursive(int n){
		if( n<= 0){
			throw new IllegalArgumentException("Percolation system must is n*n grid where n is a positive integer");
		}
		gridSize = n;
		states = new int[n*n + 1];
		states[0] = 2;
		isPercolate = false;
	}
	private void validate(int row, int col) throws IndexOutOfBoundsException{
		 if(row < 1 || row > gridSize || col < 1 || col > gridSize){
			 throw new IndexOutOfBoundsException("Invalid input for coordinate of site in the grid");
		 }
	}
	
	private int map(int row, int col){
		return (row-1)*gridSize + col;
	}
	private boolean isValid(int row, int col){
		return !(row < 1 || row > gridSize || col < 1 || col  > gridSize);
	}
	
	public void open(int row, int col){
		validate(row, col);
		if(states[map(row, col)] != 0) return;
		if(row == 1 || (isValid(row - 1, col) && states[map(row - 1, col)] == 2)
					|| (isValid(row + 1, col) && states[map(row + 1, col)] == 2)
					|| (isValid(row, col + 1) && states[map(row, col + 1)] == 2)
					|| (isValid(row, col - 1) && states[map(row, col - 1)] == 2)){
			states[map(row,col)] = 2;
			if(row == gridSize) isPercolate = true;
			handleToTheBottom(row, col);
			handleToTheLeft(row, col);
			handleToTheRight(row, col);
			handleToTheTop(row, col);
		} else {
			states[map(row, col)] = 1;
		}
	}
	private void handleToTheLeft(int row, int col){
		int currRow = row, currCol = col - 1;
		if(isValid(currRow, currCol) && states[map(currRow, currCol)] == 1){
			states[map(currRow, currCol)] = 2;
			handleToTheBottom(currRow, currCol);
			handleToTheLeft(currRow, currCol);
			handleToTheTop(currRow, currCol);
		}
	}
	private void handleToTheRight(int row, int col){
		int currRow = row, currCol = col + 1;
		if(isValid(currRow, currCol) && states[map(currRow, currCol)] == 1){
			states[map(currRow, currCol)] = 2;
			handleToTheBottom(currRow, currCol);
			handleToTheRight(currRow, currCol);
			handleToTheTop(currRow, currCol);
		}
	}
	private void handleToTheTop(int row, int col){
		int currRow = row - 1, currCol = col;
		if(isValid(currRow, currCol) && states[map(currRow, currCol)] == 1){
			states[map(currRow, currCol)] = 2;
			handleToTheTop(currRow, currCol);
			handleToTheLeft(currRow, currCol);
			handleToTheRight(currRow, currCol);
		}
		
	}
	private void handleToTheBottom(int row, int col){
		int currRow = row + 1, currCol = col;
		if(isValid(currRow, currCol) && states[map(currRow, currCol)] == 1){
			states[map(currRow, currCol)] = 2;
			if(currRow == gridSize) isPercolate = true;
			handleToTheBottom(currRow, currCol);
			handleToTheLeft(currRow, currCol);
			handleToTheRight(currRow, currCol);
		}
	}
	public boolean isOpen(int row, int col){
		validate(row, col);
		int state = states[map(row, col)];
		return state == 1 || state == 2;
	}
	public boolean isFull(int row, int col){
		validate(row, col);
		return states[(map(row, col))] == 2;
	}
	public boolean percolates(){
		return isPercolate;
	}
}
