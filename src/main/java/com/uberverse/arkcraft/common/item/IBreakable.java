package com.uberverse.arkcraft.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IBreakable extends IEngramCrafted
{
	public default boolean isBroken(ItemStack stack)
	{
		if (!stack.hasTagCompound()) return false;
		return stack.getTagCompound().getBoolean("broken");
	}

	public default void setBroken(ItemStack stack, boolean broken)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setBoolean("broken", broken);
	}

	public default boolean damage(ItemStack toolStack, EntityLivingBase entity)
	{
		if (isBroken(toolStack)) return false;
		toolStack.damageItem(1, entity);
		if (toolStack.getItemDamage() >= toolStack.getMaxDamage())
		{
			setBroken(toolStack, true);
			return false;
		}
		return true;
	}
}
