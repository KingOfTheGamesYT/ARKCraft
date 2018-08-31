package com.arkcraft.common.item;

import com.arkcraft.util.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFertilizer extends ARKCraftItem {
	private final long fertilizingTime;

	public ItemFertilizer(long fertilizingTime) {
		super();
		this.fertilizingTime = fertilizingTime * 20;
		setMaxStackSize(1);
	}

	public static long getFertilizingValueLeft(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("fertilizer") : 0;
	}

	public static void setFertilizingValueLeft(ItemStack stack, long f) {
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setLong("fertilizer", f);
	}

	public long getFertilizingTime() {
		return fertilizingTime;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		ItemStack s = new ItemStack(this);
		setFertilizingValueLeft(s, fertilizingTime);
		subItems.add(s);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		long value = getFertilizingValueLeft(stack) / 20;
		tooltip.add(I18n.format("arkcraft.tooltip.fertilizer", value + fertilizingTime * (stack.getCount() - 1)));
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		setFertilizingValueLeft(stack, fertilizingTime);
	}
}
