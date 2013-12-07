package sagma.core.data.structures;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class CircularQueue<E> implements Queue<E> {
	private int head = 0;
	private int tail = 0;
	private int count = 0;
	private int size;
	private Object[] objects;
	
	public CircularQueue() {
		this(16);
	}
	
	public CircularQueue(int size) {
		this.size = size;
		objects = new Object[size];
	}

	@Override
	public boolean add(E a) {
		if (count >= size) {
			expand();
		}
		if (++tail >= size) tail -= size;
		objects[tail] = a;
		count++;
		return true;
	}

	private void expand() {
		Object[] newObjects = new Object[size<<=1];
		System.arraycopy(objects, 0, newObjects, 0, count);
		objects = newObjects;
	}

	@Override
	public boolean addAll(Collection<? extends E> a) {
		for (E o : a) add(o);
		return true;
	}

	@Override
	public void clear() {
		count = 0;
		head = 0;
		tail = 0;
		objects = null;
		objects = new Object[size];
	}

	@Override
	public boolean contains(Object o) {
		for (int i = head; i < size; i++) {
			if (objects[i].equals(o)) return true;
		}
		for (int i = 0; i < head; i++) {
			if (objects[i].equals(o)) return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object a : c) {
			if (!contains(a)) return false;
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			Object[] array = toArray();
			private int sizeOfArray = array.length;
			private int counterOfArray = 0;
			
			@Override
			public boolean hasNext() {
				return counterOfArray < sizeOfArray;
			}

			@SuppressWarnings("unchecked")
			@Override
			public E next() {
				return (E)array[counterOfArray++];
			}

			@Override
			public void remove() {
				throw new RuntimeException("Removing in a Queue? Really? Not good.");
			}
			
		};
	}

	@Override
	public boolean remove(Object o) {
		throw new RuntimeException("Really? Removing in a queue? I hope you meant remove() because remove(Object) is silly");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException("Really? Removing in a queue? Not good.");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException("Really? Removing in a queue? Not good.");
	}

	@Override
	public int size() {
		return count;
	}

	@Override
	public Object[] toArray() {
		Object[] o = new Object[count];
		int counter = 0;
		for (int i = head; i < size; i++) {
			Object obj = objects[i];
			if (obj != null)
				o[counter++] = obj;
		}
		for (int i = 0; i < head; i++) {
			Object obj = objects[i];
			if (obj != null)
				o[counter++] = obj;
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		T[] o = (T[])new Object[count];
		int counter = 0;
		for (int i = head; i < size; i++) {
			Object obj = objects[i];
			if (obj != null)
				o[counter++] = (T)obj;
		}
		for (int i = 0; i < head; i++) {
			Object obj = objects[i];
			if (obj != null)
				o[counter++] = (T)obj;
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E element() {
		if (count == 0) throw new RuntimeException("Queue is empty!");
		return (E)objects[head];
	}

	@Override
	public boolean offer(E e) {
		return add(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E peek() {
		if (count == 0) return null;
		return (E)objects[head];
	}

	@Override
	public E poll() {
		if (count == 0) return null;
		return remove();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E remove() {
		if (count == 0) throw new RuntimeException("Queue is empty!");
		
		E o = (E)objects[head];
		objects[head] = null;
		if (++head >= size) head -= size;
		
		return o;
	}

	
}
