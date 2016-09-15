package com.uberverse.arkcraft.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AbstractItemStack implements Comparable<AbstractItemStack>
{
	public final Item item;
	private int amount;
	public final int meta;

	public AbstractItemStack(Item item, int amount, int meta)
	{
		super();
		this.item = item;
		this.amount = amount;
		this.meta = meta;
	}

	public AbstractItemStack(Item item, int amount)
	{
		this(item, amount, 0);
	}

	public AbstractItemStack(Item item)
	{
		this(item, 1, 0);
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}
	
	public int getAmount()
	{
		return amount;
	}

	public boolean matches(AbstractItemStack i)
	{
		return i.item == item && i.meta == meta;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof AbstractItemStack ? compareTo((AbstractItemStack) obj) == 0 : false;
	}

	@Override
	public int compareTo(AbstractItemStack o)
	{
		return o.item != item ? item.getUnlocalizedName().compareTo(o.item.getUnlocalizedName()) : meta - o.meta;
	}

	public ItemStack[] toItemStack()
	{
		@SuppressWarnings("deprecation")
		int lim = item.getItemStackLimit();
		ItemStack[] out = new ItemStack[amount / lim + (amount % lim > 0 ? 1 : 0)];
		int am = amount;
		int i = 0;
		while (am > 0)
		{
			ItemStack o = new ItemStack(item, 0, meta);
			if (am >= lim)
			{
				o.stackSize = lim;
				am -= lim;
			}
			else
			{
				o.stackSize = am;
				am = 0;
			}
			out[i] = o;
			i++;
		}
		return out;
	}

	public ItemStack toSingleItemStack()
	{
		return new ItemStack(item, amount, meta);
	}

	public static AbstractItemStack fromItemStack(ItemStack i)
	{
		return new AbstractItemStack(i.getItem(), i.stackSize, i.getMetadata());
	}

	public AbstractItemStack copy()
	{
		return new AbstractItemStack(item, amount, meta);
	}
}
