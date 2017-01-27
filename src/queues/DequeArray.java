package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DequeArray<Item> implements Iterable<Item>{
	private Item[] items;	 
	private int first;	//index of the item to be inserted to the first of the deck
	private int last;	//index of the item to be inserted to the last of the deck
	
	public DequeArray() {
		items = (Item[]) new Object[1];
		first = 0;
		last = 0;
	}
	private void printArrays(){
		System.out.print("\nCurrent Array: ");
		 for(Item item : items){
			 System.out.print(item + " ");
		 }
	}
	public boolean isEmpty(){
		return last == first;
	}
	
	public int size(){
		if(isEmpty()) return 0;
		return last - first - 1;
	}
	
	private void checkRep(){
		assert (last - first) != 1;
	}
	
	private void resize(int freeCellNumOneSide){
		int numItem = size();
		assert size() != 0;
		Item[] copy = (Item[]) new Object[ numItem + 2*freeCellNumOneSide ];
		for(int i = 0 ; i < numItem; i++){
			copy[freeCellNumOneSide + i] = items[first + i + 1];
		}
		items = copy;
		first = freeCellNumOneSide - 1;
		last = first + numItem + 1;
	}
	
	public void addFirst(Item item){
		if(item == null){
			throw new NullPointerException("Cannot add null item!");
		}
		if(first == -1) resize(size()); // resize + update first, last
		items[first] = item;
		if(size() == 0)  last ++;
		first--;
		printArrays();
	}
	
	public void addLast(Item item){
		if(item == null){
			throw new NullPointerException("Cannot add null item!");
		}

		if(last == items.length) resize(size()); // resize + update first, last
		
		items[last] = item;
		
		if(size() == 0) first --; 	// handle the case where size == 0, first == last
		
		last ++;
		printArrays();
	}
	public Item removeFirst(){
		if(isEmpty()){
			throw new NoSuchElementException("can not remove any thing from empy deque");
		}
		
		Item item = items[first + 1];
		items[++first] = null;
		
		if((last - first) == 1) last --;
		else if(last/size() == 4)	resize(size());
		
		printArrays();
		return item;
	}
	public Item removeLast(){
		if(isEmpty()){
			throw new NoSuchElementException("can not remove any thing from empy deque");
		}
		
		Item item = items[last - 1];
		items[--last] = null;
		
		if((last - first) == 1 ) first ++;
		else if(((items.length - first + 1)/size()) == 4) resize(size());
		
		printArrays();
		return item;
	}
	@Override
	public Iterator<Item> iterator() {
		return new DequeIterator<Item>(items,first + 1, last - 1);
	}
	
	private class DequeIterator<Item> implements Iterator<Item> {
		Item[] items;
		int head, tail, current;
		
		public DequeIterator(Item[] items, int head, int tail) {
			this.items = items;
			this.head = head;
			this.tail = tail;
			this.current = head;
		}

		@Override
		public boolean hasNext() {
			return current <= tail;
		}

		@Override
		public Item next() {
			if(!hasNext()) throw new NoSuchElementException();
			return items[current ++];
		}
		
		public void remove() { throw new UnsupportedOperationException(); }
	}
	
	public static void main(String[] args){
		DequeArray<String> deque = new DequeArray<>();
		System.out.println(deque.size());
//		deque.addFirst("a");
//		deque.addFirst("b");
//		deque.addFirst("c");
//		deque.addFirst("d");
//		deque.addFirst("e");
//		System.out.println(deque.size());
//		deque.removeFirst();
//		deque.removeFirst();
//		deque.removeFirst();
//		deque.removeFirst();
//		deque.addFirst("b");
		deque.addLast("b");
		deque.addLast("c");
		deque.removeLast();
		deque.removeFirst();
//		deque.removeLast();
	//	deque.removeLast();
		deque.addFirst("a");
		deque.addLast("b");
		Iterator<String> it = deque.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
	
}
