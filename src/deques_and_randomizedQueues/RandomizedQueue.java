package deques_and_randomizedQueues;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] items;
	private int size = 0;
	
	public RandomizedQueue() {
		items = (Item[]) new Object[2];
	}
	
	public boolean isEmpty(){
		return size == 0;
	}
	public int size(){
		return size;
	}
	
	private void resize(int capacity){
		Item[] copy = (Item[]) new Object[capacity];
		for(int i = 0; i < size ; i++){
			copy[i] = items[i];
		}
		items = copy;
	}
	
	public void enqueue(Item item){
		if (item == null) throw new NullPointerException("Cannon enqueue null element!");
		if ( size == items.length) resize(2*items.length);
		items[size++] = item;
	}
	
	public Item dequeue() {
		if (isEmpty()) throw new NoSuchElementException("Queue is empty");
		int ranIndex = StdRandom.uniform(size);
		Item item = items[ranIndex];
		if(ranIndex != (size - 1)) items[ranIndex] = items[size-1];
		items[size-1] = null;
		size--;
		if(size > 0 && size == items.length/4) resize(items.length/2);
		return item;
	}

	public Item sample() {
		if (isEmpty()) throw new NoSuchElementException("Queue is empty");
		return items[StdRandom.uniform(size)];
	}
	@Override
	public Iterator<Item> iterator() {
		return new RandomIterator<>(items, size-1);
	}
	
	private class RandomIterator<Item> implements Iterator<Item>{
		
		private final Item[] items;
		private int i;				// index of the last item in the array
		public RandomIterator(Item[] items, int i) {
			this.i = i;
			assert i < items.length;
			if(i < 0) { this.items = null; return; } // case iterate an empty array
			this.items = copy(items, i);
			StdRandom.shuffle(this.items, 0, i);
		}
		
		private Item[] copy(Item[] items, int i){
			Item[] copy = (Item[]) new Object[i+1];
			for(int x = 0; x <= i ; x++){
				copy[x] = items[x];
			}
			return copy;
		}
		
	    public void remove()      { throw new UnsupportedOperationException();  }
		@Override
		public boolean hasNext() 	{ return i >= 0; 		}
		@Override
		public Item next() 			{ 
			if (!hasNext()) throw new NoSuchElementException();
			return items[i--]; 	}
	}
	
	public static void main(String[] args){
//		RandomizedQueue<String> rq = new RandomizedQueue<>();
//		while(true) {
//			String item = StdIn.readString();
//			if(item.equals("quit")) break;
//			else if(!item.equals("-")) rq.enqueue(item);
//			else if (!rq.isEmpty() )	StdOut.print(rq.dequeue() + " ");
//		}
//		StdOut.println("(" + rq.size() + " left on queue)");
//		Iterator<String> it;
//		for(int i = 0 ; i <= 5; i ++){
//			it = rq.iterator();
//			System.out.println("\nTesting iterator: " +  i + " th test: ");
//			while(it.hasNext()){
//				System.out.print(it.next());
//			}
//		}
		
		RandomizedQueue<Integer> rq = new RandomizedQueue<>();
		for(int i = 0; i < 10; i++){
			rq.enqueue(i);
		}
		Iterator<Integer> it = rq.iterator();
		int count = 0;
		while(it.hasNext()){
			count ++;
			System.out.println(it.next());
		}
		
		System.out.println("Count: " + count);
	}
}
