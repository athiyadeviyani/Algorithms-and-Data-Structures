package matcher;

import java.util.*;

public class Queue {

	private Vector<Integer> content;
	
	public Queue() {
		this.content = new Vector<Integer>();
	}
	
	public Queue(Collection<Integer> integerCollection) {
		this.content = new Vector<Integer>();
		for (Integer i : integerCollection)
			content.addElement(i);
	}
	
	public boolean equals(Queue other) {
		return content.equals(other.content);
	}
	
	public void enqueue(Integer offset) {
		content.addElement(offset);
	}
	
	public Integer dequeue() throws QueueUnderflowException {
		if (content.isEmpty())
			throw new QueueUnderflowException();
		else {
			Integer firstElement = content.firstElement();
			content.remove(0);
			return firstElement;
		}
	}
	
	public String toString() {
		String result = "[";
		int contentSize = content.size();
		for (int index = 0; index < contentSize; index++) {
			if (index != 0)
				result += ", ";
			result += content.get(index).toString();
		}
		result += "]";
		return result;
	}
	
}