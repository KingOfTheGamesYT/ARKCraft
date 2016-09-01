package com.uberverse.arkcraft.rework.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class FixedSizeQueue<E> implements Queue<E>
{
	private LinkedList<E> queue;
	private int size;

	public FixedSizeQueue(int size)
	{
		queue = new LinkedList<>();
		this.size = size;
	}

	@Override
	public int size()
	{
		return queue.size();
	}

	@Override
	public boolean isEmpty()
	{
		return queue.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return queue.contains(o);
	}

	@Override
	public Iterator<E> iterator()
	{
		return queue.iterator();
	}

	@Override
	public Object[] toArray()
	{
		return queue.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return queue.toArray(a);
	}

	@Override
	public boolean remove(Object o)
	{
		return queue.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return queue.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		if ((c.size() + size()) <= size) return queue.addAll(c);
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return queue.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return queue.retainAll(c);
	}

	@Override
	public void clear()
	{
		queue.clear();
	}

	@Override
	public boolean add(E e)
	{
		if (size() == size) return false;
		return queue.add(e);
	}

	@Override
	public boolean offer(E e)
	{
		if (size() == size) return false;
		return queue.offer(e);
	}

	@Override
	public E remove()
	{
		return queue.remove();
	}

	@Override
	public E poll()
	{
		return queue.poll();
	}

	@Override
	public E element()
	{
		return queue.element();
	}

	@Override
	public E peek()
	{
		return queue.peek();
	}
}
