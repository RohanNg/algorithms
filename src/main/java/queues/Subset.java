package queues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
	public static void main(String[] args){
		
		int k = Integer.parseInt(args[0]);
		RandomizedQueue<String> ranQueue = new RandomizedQueue<>();
				
		while(!StdIn.isEmpty()) {
			ranQueue.enqueue(StdIn.readString());
		}
		
		for(int x = 1 ; x <= k ; x ++ ){
			StdOut.println(ranQueue.dequeue());
		}
	}
}
