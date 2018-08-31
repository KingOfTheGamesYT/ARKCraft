package com.arkcraft.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ARKCraftFeces extends ItemFertilizer implements IDecayable {
	private long decayTime;

	public ARKCraftFeces(long fertilizingTime, long decayTime) {
		super(fertilizingTime);
		this.decayTime = decayTime;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		super.getSubItems(tab, subItems);
		for (Object o : subItems)
			IDecayable.setDecayStart((ItemStack) o, -1);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		IDecayable.super.addInformation(stack, tooltip);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		decayTick(((EntityPlayer) entityIn).inventory, itemSlot, 1, stack, worldIn);
	}

	@Override
	public long getDecayTime(ItemStack stack) {
		return decayTime;
	}
}
