package com.uberverse.arkcraft.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWaterJar extends ItemWaterContainer{

	public ItemWaterJar(int maxWaterValue) 
	{
		super(maxWaterValue);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (worldIn.getTotalWorldTime() % 10 == 0 && !worldIn.isRemote)
		{
			int waterValue = getWaterValueLeft(stack);
			
			if(isRaining((EntityPlayer) entityIn, worldIn))
			{
				if(waterValue != maxWaterValue)
				setWaterValueLeft(stack, ++waterValue);
			}
		}		
	}

}
