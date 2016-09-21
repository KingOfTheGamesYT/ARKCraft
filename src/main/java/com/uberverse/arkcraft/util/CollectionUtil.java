/**
 * 
 */
package com.uberverse.arkcraft.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author Lewis_McReu
 */
public class CollectionUtil
{
	@SafeVarargs
	public static <E> List<E> filter(Collection<E> col, Predicate<E>... p)
	{
		List<E> out = new ArrayList<>();

		Predicate<E> predicate = combine(p);
		if (predicate == null) return out;

		for (E e : col)
		{
			if (predicate.test(e)) out.add(e);
		}

		return out;
	}

	/**
	 * @param container
	 * @param filter
	 *            the filter collection
	 * @return a list only containing the values from container that are also
	 *         contained in filter
	 */
	public static <T> List<T> filterContains(Collection<T> container,
			Collection<T> filter)
	{
		return filter(container, (T t) -> {
			return filter.contains(t);
		});
	}

	@SafeVarargs
	public static <E, T> List<E> filter(Collection<E> col, T filter,
			Function<E, T> converter, Predicate<T>... predicates)
	{
		List<E> out = new ArrayList<>();

		Predicate<T> predicate = combine(predicates);
		if (predicate == null) return out;

		for (E e : col)
		{
			if (predicate.test(converter.apply(e))) out.add(e);
		}

		return out;
	}

	@SafeVarargs
	public static <T> Predicate<T> combine(Predicate<T>... predicates)
	{
		Predicate<T> p = null;
		for (Predicate<T> pr : predicates)
		{
			if (p == null) p = pr;
			else p = p.and(pr);
		}
		return p;
	}

	public static <E, T> E find(Collection<E> col, Function<E, T> converter,
			T sought)
	{
		List<E> l = filter(col, (E e) -> converter.apply(e).equals(sought));
		return l.isEmpty() ? null : l.get(0);
	}

	public static <E> void process(Collection<E> col, Consumer<E> consumer)
	{
		for (E e : col)
		{
			consumer.accept(e);
		}
	}

	public static <E, T> List<T> convert(Collection<E> col,
			Function<E, T> converter)
	{
		List<T> l = new ArrayList<>();

		for (E e : col)
			l.add(converter.apply(e));
		return l;
	}

	public static <E> List<E> adapt(Collection<E> col, UnaryOperator<E> adapter)
	{
		List<E> l = new ArrayList<>();

		for (E e : col)
			l.add(adapter.apply(e));
		return l;
	}

	public static Integer[] convert(int[] arr)
	{
		Integer[] o = new Integer[arr.length];

		for (int i = 0; i < arr.length; i++)
			o[i] = Integer.valueOf(arr[i]);
		return o;
	}

	public static Boolean[] convert(boolean[] arr)
	{
		Boolean[] o = new Boolean[arr.length];

		for (int i = 0; i < arr.length; i++)
			o[i] = Boolean.valueOf(arr[i]);
		return o;
	}

	public static Byte[] convert(byte[] arr)
	{
		Byte[] o = new Byte[arr.length];

		for (int i = 0; i < arr.length; i++)
			o[i] = Byte.valueOf(arr[i]);
		return o;
	}

	public static Long[] convert(long[] arr)
	{
		Long[] o = new Long[arr.length];

		for (int i = 0; i < arr.length; i++)
			o[i] = Long.valueOf(arr[i]);
		return o;
	}

	public static Short[] convert(short[] arr)
	{
		Short[] o = new Short[arr.length];

		for (int i = 0; i < arr.length; i++)
			o[i] = Short.valueOf(arr[i]);
		return o;
	}
}
