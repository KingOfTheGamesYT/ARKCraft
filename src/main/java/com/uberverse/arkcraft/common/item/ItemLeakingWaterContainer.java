package com.uberverse.arkcraft.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemLeakingWaterContainer extends ItemWaterContainer
{
	public ItemLeakingWaterContainer(int maxWaterValue)
	{
		super(maxWaterValue);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (!worldIn.isRemote && !isRaining((EntityPlayer) entityIn, worldIn) && worldIn.getTotalWorldTime() % 432 == 0)
			setWaterValueLeft(stack, MathHelper.clamp(getWaterValueLeft(stack) - 1, 0, maxWaterValue));
	}
}
