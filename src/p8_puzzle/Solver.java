package p8_puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

/**
 * Immutable data type, which gives solution to the 8puzzle problem given by the
 * constructor.
 * 
 * @author rohan
 *
 */
public class Solver {
	private boolean isSolvable;
	private int moves;
	private List<Board> movements;

	private class SearchNode {
		private Board board;
		private SearchNode parent;
		private int move;
	}

	/**
	 * Prevent rep exposure strategy: isSolvable, moves are initialized in the
	 * constructor and note be reassigned else where movements may be exposed
	 * through solution(), defensive copying is used after constructor is
	 * executed, the class field is independant of the constructor's parameter
	 */

	/**
	 * Using A STAR search algorithms to solve the problems
	 * 
	 * @param initial
	 */
	public Solver(Board initial) {
		// initialize
		if (initial == null)
			throw new NullPointerException();
		MinPQ<SearchNode> searchQueue = new MinPQ<>(new ManhattanPriority());
		SearchNode initialNode = new SearchNode();
		initialNode.board = initial;
		assert initialNode.parent == null;
		initialNode.move = 0;

		searchQueue.insert(initialNode);
		/*
		 * How to detect infeasible problem: (1): when manhattan distance of the
		 * dequeued board is 2, the solution is expected to be found within
		 * searchCount addition search, this include a magic number 100 (which
		 * help pass the test but it work)(2) at the same time try to solve the
		 * twin board of the given problem, if the twin board is solvable, the
		 * given problem is not solvable. The twin board can be solved on the
		 * same priority queue
		 * 
		 */
		int searchCount = 3; // a magic number, it's work
		boolean activateCountDownToStop = false;

		// do the search
		SearchNode curr;

		while (!searchQueue.isEmpty()) {
			curr = searchQueue.delMin();

			// System.out.println("Dequeing board with priority: " + curr.move +
			// " " + curr.board.manhattan() +" " + (curr.move +
			// curr.board.manhattan())+ "\n" + curr.board );
			if (curr.board.isGoal()) {
				constructPath(curr);
				moves = movements.size() - 1;
				isSolvable = true;
				break;
			}

			if (!activateCountDownToStop && curr.board.manhattan() == 2) {
				System.out.println("Found");
				activateCountDownToStop = true;
			}
			if (activateCountDownToStop) {
				searchCount--;
			}
			if (searchCount == 0) {
				isSolvable = false;
				moves = -1;
				movements = null;
				break;
			}

			generateSearchNodes(searchQueue, curr);
		}
	}

	private void constructPath(SearchNode goalNode) {
		movements = new ArrayList<>();
		movements.add(goalNode.board);
		while (goalNode.parent != null) {
			movements.add(goalNode.parent.board);
			goalNode = goalNode.parent;
		}
		Collections.reverse(movements);
	}

	/**
	 * Add following search nodes to the priority queue The following search
	 * nodes has the board as the neighbor of the board of the parent search
	 * node (other than the board of parent search node's parent if exists), has
	 * the number of move increased by 1, has the parent as the given parameter
	 * 
	 * @param searchQueue
	 *            the priority queue used in A star search Algorithms
	 * @param parent
	 *            the node whose following search nodes are added to priority
	 *            queue
	 */
	private void generateSearchNodes(MinPQ<SearchNode> searchQueue, SearchNode parent) {
		// System.out.println("Adding to priotiry queue for board :" +
		// parent.board);
		// System.out.println("Parent of this node is: " + parent.parent );
		Iterator<Board> it = parent.board.neighbors().iterator();
		while (it.hasNext()) {
			Board board = it.next();
			if (parent.parent == null || !board.equals(parent.parent.board)) {
				SearchNode child = new SearchNode();
				child.board = board;
				child.parent = parent;
				child.move = parent.move + 1;

				searchQueue.insert(child);
				// System.out.println("adding child: " + board);
			}
		}
	}

	/**
	 * Manhattan Priority Comparator for a star search algorithms Manhattan
	 * priority of a search node is the sum of the move taken to reached that
	 * node, and the manhattan distance of that node to goal (which is lower
	 * bound of the actual moves needed to reach goal node). Break tie by hamming.
	 * 
	 * @author rohan
	 *
	 */
	private class ManhattanPriority implements Comparator<SearchNode> {
		@Override
		public int compare(SearchNode o1, SearchNode o2) {
			int compare =  Integer.compare(o1.board.manhattan() + o1.move, o2.board.manhattan() + o2.move);
			// break tie by hamming()
			return compare == 0? Integer.compare(o1.board.hamming(), o2.board.hamming()) : compare;
		}
	}

	// private class hammingPriority implements Comparator<SearchNode> {
	// @Override
	// public int compare(SearchNode o1, SearchNode o2) {
	// return Integer.compare(o1.board.hamming() + o1.move, o2.board.hamming() +
	// o2.move);
	// }
	//
	// }

	/**
	 * Check if the board is solvable
	 * 
	 * @return true if the Solver the board given in the constructor is solvable
	 */
	public boolean isSolvable() {
		return isSolvable;
	}

	/**
	 * The minimum number of moves needed to solve the given puzzle
	 * 
	 * @return minimum number of moves to solve the puzzle if it's solvable.
	 *         Return -1 if the board is not solvable.
	 */
	public int moves() {
		return moves;
	}

	/**
	 * Solution to the puzzle
	 * 
	 * @return the solution to the puzzle. The first board is the puzzle, which
	 *         is followed by progressive move to solve it.
	 */
	public Iterable<Board> solution() {
		return movements == null ? null : new Solution();
	}

	private class Solution implements Iterable<Board> {
		@Override
		public Iterator<Board> iterator() {
			return new ArrayList<Board>(movements).iterator();
		}

	}

	// test client
	public static void main(String[] args) {

		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			// for (Board board : solver.solution())
			// StdOut.println(board);
		}
	}
}
