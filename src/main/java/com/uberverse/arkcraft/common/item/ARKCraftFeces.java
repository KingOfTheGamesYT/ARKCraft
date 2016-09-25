package com.uberverse.arkcraft.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ARKCraftFeces extends ARKCraftUtility implements IDecayable
{
	
	private long decayTime;

	public ARKCraftFeces(long decayTime)
	{
		this.decayTime = decayTime;
	}

	@SuppressWarnings("rawtypes")
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer playerIn,
			List tooltip, boolean advanced)
	{
		IDecayable.super.addInformation(itemStack, playerIn, tooltip, advanced);
	}

	@Override
	public long getDecayTime(ItemStack stack) 
	{
		return decayTime;
	}
}
