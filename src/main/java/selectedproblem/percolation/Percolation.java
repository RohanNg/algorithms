package selectedproblem.percolation;

public interface Percolation {
	void open(int row, int col);
	boolean isOpen(int row, int col);
	boolean isFull(int row, int col);
	boolean percolates();
}
