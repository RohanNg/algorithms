package eightPuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Immutable data type represent a board of dimension n-by-n containing n^2
 * integer between 0 and n^2-1, where 0 represents blank space, and integer
 * other than 0 represent blocks. Every integer must exist only once. The GOAL
 * board of this board is n-by-n array with all the number in incremental order
 * except 0, which is in the last place.
 * 
 * <pre>
 *             For example the goal board of the 3-by-3 board is
 *            	1	2	3
 *            	4	5	6
 *            	7	8	0
 * </pre>
 * 
 * @author rohan
 *
 */
public class Board {
	private final int[][] board;
	private final int dim;
	private int row0;
	private int col0;
	private int hamming;
	private int manhattan;
	// RI
	// row0, col0 in range [0,dim-1]

	// protect rep exposure strategy:
	// dim, row0, col0 is private immutable, and never be reassigned after
	// constructor
	// board is created created in constructor with defensive copying, and is
	// not exposed

	/**
	 * @param board
	 *            n-by-n array containing n^2 integer between 0 and n^2-1, where
	 *            0 represents blank space, and integer other than 0 represent
	 *            blocks.
	 * 
	 * @throws IllegalArgumentException
	 *             if the the array block is not of dimension n-by-n
	 */
	public Board(int[][] board) {
		dim = board.length;
		this.board = new int[dim][dim];
		// defensive copying, any calculating manhattan and hamming
		try {
			hamming = 0;
			manhattan = 0;
			for (int row = 0; row < dim; row++) {
				for (int col = 0; col < dim; col++) {
					int value = board[row][col];

					if (value != 0) {
						manhattan += Math.abs((value - 1) / dim - row) + Math.abs((value - 1) % dim - col);
					} else {
						row0 = row;
						col0 = col;
					}
					if (board[row][col] != (row * dim + col + 1))
						hamming++;
					this.board[row][col] = value;
				}
			}
			hamming--;
			// when row = col = dim - 1, and constructor spec is
			// adhered to, the condition must fail.
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("A n-by-n array block must be given");
		}
	}

	/**
	 * Get dimension of the Board
	 * 
	 * @return an integer n meaning the board is of dimension n-by-n
	 */
	public int dimension() {
		return dim;
	}

	/**
	 * Get number of block out of place. 0 is not a block. A block is out of
	 * place if it's not in the place it should be in the goal BOAD. For example
	 * 
	 * <pre>
	 * 8 1 3 1 2 3 1 2 3 4 5 6 7 8 4 0 2 4 5 6 ---------------------- 7 6 5 7 8
	 * 0 1 1 0 0 1 1 0 1 initial goal Hamming = 5
	 * 
	 * <Pre>
	 * 
	 * @return the number of block out of place in this board
	 */
	public int hamming() {
		return hamming;
	}

	/**
	 * Calculate sum of Manhattan distance of every block. The Manhattan
	 * distance of a block is the distance between its position in this board
	 * and its position in the goal board. For example:
	 * 
	 * <pre>
	 *  8  1  3        1  2  3     1  2  3  4  5  6  7  8
	 *  4  0  2        4  5  6     ----------------------
	 *  7  6  5        7  8  0     1  2  0  0  2  2  0  3
	 *
	 *	initial          goal       Manhattan = 10
	 *
	 * </pre>
	 * 
	 * @return
	 */
	public int manhattan() {
		return manhattan;
	}

	/**
	 * Check if the board is identical to the goal board of its dimension
	 * 
	 * @return true if this board is identical to the goal board of its
	 *         dimension.
	 */
	public boolean isGoal() {
		return hamming() == 0;
	}

	/**
	 * Get the twin of this board. The twin of this board is created by
	 * exchanging any pair of adjacent block
	 * 
	 * @return the board obtained by exchanging one pair of adjacent block.
	 */
	public Board twin() {
		while (true) {
			int row = StdRandom.uniform(dim);
			int col = StdRandom.uniform(dim);
			if (board[row][col] != 0) {
				if ((row + 1 < dim) && board[row + 1][col] != 0)
					return createBoardByExchange(row, col, row + 1, col);
				if ((row > 0) && board[row - 1][col] != 0)
					return createBoardByExchange(row, col, row - 1, col);
				if ((col + 1 < dim) && board[row][col + 1] != 0)
					return createBoardByExchange(row, col, row, col + 1);
				if ((col > 0) && board[row][col - 1] != 0)
					return createBoardByExchange(row, col, row, col - 1);
			}
		}
	}

	/**
	 * Create a new board having two site on this board swapped. A site on this
	 * board is eigther a block or number 0.
	 * 
	 * @param row1
	 * @param col1
	 * @param row2
	 * @param col2
	 * @return a new board having two site on this board swapped
	 */
	private Board createBoardByExchange(int row1, int col1, int row2, int col2) {
		exchange(row1, col1, row2, col2);
		Board board = new Board(this.board);
		exchange(row2, col2, row1, col1);
		return board;
	}

	private void exchange(int row1, int col1, int row2, int col2) {
		int copy = board[row1][col1];
		board[row1][col1] = board[row2][col2];
		board[row2][col2] = copy;
	}

	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result + Arrays.deepHashCode(board);
	// return result;
	// }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		return true;
	}

	public Iterable<Board> neighbors() {
		return new NeighborBoard(board, row0, col0, dim);
	}

	private class NeighborBoard implements Iterable<Board> {
		private List<Board> neighbors;

		/**
		 * 
		 * @param board
		 * @param row0
		 *            row position of item 0
		 * @param col0
		 *            col position of item 0
		 * @param dim
		 *            dimension of the board
		 */
		public NeighborBoard(int[][] board, int row0, int col0, int dim) {
			neighbors = new ArrayList<>();
			assert board[row0][col0] == 0;
			// move 0 to bottom
			if (row0 < dim - 1) {
				neighbors.add(createBoardByExchange(row0 + 1, col0, row0, col0));
			}
			// move 0 to top
			if (row0 > 0) {
				neighbors.add(createBoardByExchange(row0 - 1, col0, row0, col0));
			}
			// move 0 to the left
			if (col0 > 0) {
				neighbors.add(createBoardByExchange(row0, col0 - 1, row0, col0));
			}
			// move 0 to the right
			if (col0 < dim - 1) {
				neighbors.add(createBoardByExchange(row0, col0 + 1, row0, col0));
			}
		}

		@Override
		public Iterator<Board> iterator() {
			return neighbors.iterator();
		}

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(dim).append("\n");
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				sb.append(" ").append(board[row][col]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		int[] a = new int[] { 0, 1, 3 };
		int[] b = new int[] { 4, 5, 6 };
		int[] c = new int[] { 8, 7, 2 };
		int[][] board = new int[][] { a, b, c };
		Board myboard = new Board(board);
		Board myBoard1 = new Board(board);
		System.out.println("Is two board equal: " + myBoard1.equals(myboard));
		System.out.println("Manhattan: " + myboard.manhattan());
		System.out.println("Hamming: " + myboard.hamming());
		System.out.println("Board: \n" + myboard);

		System.out.println("Neighbor board: ");
		Iterator<Board> it = myboard.neighbors().iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}

		System.out.println("Is goal:" + myboard.isGoal());
	}
}
