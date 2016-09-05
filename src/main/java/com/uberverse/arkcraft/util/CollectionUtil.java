/**
 * 
 */
package com.uberverse.arkcraft.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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
	 * @return a list only containing the values from container that are also contained in filter
	 */
	public static <T> List<T> filterContains(Collection<T> container, Collection<T> filter)
	{
		return filter(container, (T t) -> {
			return filter.contains(t);
		});
	}

	@SafeVarargs
	public static <E, T> List<E> filter(Collection<E> col, T filter, Function<E, T> converter, Predicate<T>... predicates)
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
}
