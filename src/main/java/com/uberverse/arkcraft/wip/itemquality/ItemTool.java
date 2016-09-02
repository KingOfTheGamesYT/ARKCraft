package com.uberverse.arkcraft.wip.itemquality;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemTool extends Item implements Qualitable, ToolTipped
{
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return Qualitable.super.getItemStackDisplayName(stack);
	}

	@Override
	public String getItemStackDisplayNameAppendage(ItemStack stack)
	{
		return super.getItemStackDisplayName(stack);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		// TODO Auto-generated method stub
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
