package selectedproblem.percolation;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class PercolationWith2UF implements Percolation{
	private WeightedQuickUnionUF pecoTopBot, pecoTop;
	private final boolean[] siteStatus; // 0 mean blocked site, 1 mean open site
	private final int gridSize, virtualTopSiteId = 0, virtualBottomSiteId;
	public PercolationWith2UF(int n){
		if(n <= 0){
			throw new IllegalArgumentException("n must be positive");
		}
		virtualBottomSiteId = n*n+1;
		gridSize = n;
		pecoTopBot = new WeightedQuickUnionUF(n*n+2);
		pecoTop = new WeightedQuickUnionUF(n*n+1);
		siteStatus = new boolean[n*n+1];
	}
	private void validate(int row, int col) throws IndexOutOfBoundsException{
		 if(row < 1 || row > gridSize || col < 1 || col > gridSize){
			 throw new IndexOutOfBoundsException("Invalid input for coordinate of site in the grid");
		 }
	 }
	private int mapToId(int row, int col){
		validate(row, col);
		return (row-1)*gridSize + col;
	}
	public void open(int row, int col){
		validate(row, col);
		int id = mapToId(row, col);
		if(siteStatus[id]) { // the site has already been opened
			return;
		} else {
			siteStatus[id] = true;
			if( col > 1 && siteStatus[mapToId(row, col-1)]){ // check if the site on the left exists and is open
				pecoTopBot.union(id, mapToId(row, col-1));
				pecoTop.union(id, mapToId(row, col-1));
			} 
			if (row > 1 && siteStatus[mapToId(row-1, col)]){
				pecoTopBot.union(id, mapToId(row-1, col));
				pecoTop.union(id, mapToId(row-1, col));
			}
			if (col < gridSize && siteStatus[mapToId(row, col+1)]){
				pecoTopBot.union(id, mapToId(row, col+1));
				pecoTop.union(id, mapToId(row, col + 1));
			} 
			if (row < gridSize && siteStatus[mapToId(row+1, col)]){
				pecoTopBot.union(id, mapToId(row+1, col));
				pecoTop.union(id, mapToId(row + 1, col) );
			}
			if(row == 1){
				pecoTopBot.union(id, virtualTopSiteId);
				pecoTop.union(id, virtualTopSiteId);
			}
			if(row == gridSize) {
				pecoTopBot.union(id, virtualBottomSiteId);
			}
		}
	}
	public boolean isOpen(int row, int col){
		validate(row, col);
		return siteStatus[mapToId(row, col)];
	}
	
	public boolean isFull(int row, int col){
		validate(row, col);
		return pecoTop.connected(mapToId(row, col), virtualTopSiteId);
	}
	public boolean percolates(){
		return pecoTopBot.connected(virtualTopSiteId, virtualBottomSiteId);
	}
}
