package com.uberverse.arkcraft.common.item;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author tom5454
 * @author Lewis_McReu
 */
public interface IDecayable
{
	default void decayTick(IInventory inventory, int slotId, double decayModifier, ItemStack stack)
	{
		if (shouldRemove(stack, decayModifier)) inventory.setInventorySlotContents(slotId, null);
	}

	public default boolean shouldRemove(ItemStack stack, double decayModifier)
	{
		long decayStart = getDecayStart(stack);
		return decayStart >= 0 && getRemovalTime(stack, decayModifier) < ARKCraft.proxy.getWorldTime();
	}

	public default long getRemovalTime(ItemStack stack, double decayModifier)
	{
		return (long) (getDecayStart(stack) + getDecayTime(stack) * decayModifier);
	}

	public default long getDecayStart(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("decayStart") : -1;
	}

	public default void setDecayStart(ItemStack stack, long decayStart)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setLong("decayStart", decayStart);
	}

	public default long getDecayTime(ItemStack stack)
	{
		return getMaxDecayTime(stack) * stack.stackSize;
	}

	public default long getDecayTimeLeft(ItemStack stack, double decayModifier)
	{
		return getRemovalTime(stack, decayModifier) - ARKCraft.proxy.getWorldTime();
	}

	public long getMaxDecayTime(ItemStack stack);
}
