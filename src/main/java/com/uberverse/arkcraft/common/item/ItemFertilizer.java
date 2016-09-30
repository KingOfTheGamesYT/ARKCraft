package com.uberverse.arkcraft.common.item;

import java.util.List;

import com.uberverse.arkcraft.util.I18n;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemFertilizer extends ARKCraftItem
{
	private final long fertilizingTime;

	public ItemFertilizer(long fertilizingTime)
	{
		super();
		this.fertilizingTime = fertilizingTime;
		setMaxStackSize(1);
	}

	public long getFertilizingTime()
	{
		return fertilizingTime;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		ItemStack s = new ItemStack(this);
		setFertilizingValueLeft(s, fertilizingTime);
		subItems.add(s);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		long value = getFertilizingValueLeft(stack);
		tooltip.add(I18n.format("arkcraft.tooltip.fertilizer", value + fertilizingTime * (stack.stackSize - 1)));
	}

	public static long getFertilizingValueLeft(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("fertilizer") : 0;
	}

	public static void setFertilizingValueLeft(ItemStack stack, long f)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setLong("fertilizer", f);
	}
}
