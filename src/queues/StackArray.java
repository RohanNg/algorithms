/**
 * @author Nguyen Hai Dang
 */
package queues;

public class StackArray<Item> {
	private Item[] items;
	private int N = 0;
	
	public StackArray(){
		items = (Item[]) new Object[1];
		printArrays();
	}
	
	public boolean isEmpty(){
		return N == 0;
	}
	public void push(Item item){
		if( N == items.length) resize(2*items.length);
		items[N++] = item;
		printArrays();
	}
	public Item pop(){
		Item item = items[--N];
		items[N] = null;
		if(N > 0 && N == items.length/4) resize(items.length/2);
		printArrays();
		return item;
	}
	private void resize(int capacity){
		Item[] copy = (Item[]) new Object[capacity];
		for(int i = 0; i < N ; i++){
			copy[i] = items[i];
		}
		items = copy;
	}
	private void printArrays(){
		System.out.print("\nCurrent Array: ");
		 for(Item item : items){
			 System.out.print(item + " ");
		 }
	}
	public static void main(String[] args){
		StackArray<String> stack = new StackArray<>();
		stack.push("a");
		stack.push("b");
		stack.push("c");
		stack.push("d");
		stack.push("e");
		stack.pop();
		stack.pop();
		stack.pop();
	}
}
