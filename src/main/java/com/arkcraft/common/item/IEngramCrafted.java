package com.arkcraft.common.item;

import com.arkcraft.common.engram.EngramManager;
import com.arkcraft.common.engram.EngramManager.Engram;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IEngramCrafted
{
	public default Engram getEngram(ItemStack stack)
	{
		if (!stack.hasTagCompound()) return null;
		return EngramManager.instance().getEngram(stack.getTagCompound().getShort("engram"));
	}

	public default void setEngram(ItemStack stack, Engram engram)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setShort("engram", engram.getId());
	}
}
