package com.uberverse.arkcraft.common.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ARKCraftFeces extends ItemFertilizer implements IDecayable
{
	private long decayTime;

	public ARKCraftFeces(long fertilizingTime, long decayTime)
	{
		super(fertilizingTime);
		this.setMaxStackSize(1);
		this.decayTime = decayTime;
	}

	@SuppressWarnings("rawtypes")
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		IDecayable.super.addInformation(itemStack, playerIn, tooltip, advanced);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		decayTick(((EntityPlayer) entityIn).inventory, itemSlot, itemSlot, stack);
	}

	@Override
	public long getDecayTime(ItemStack stack)
	{
		return decayTime;
	}
}
