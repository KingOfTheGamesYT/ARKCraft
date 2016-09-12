package com.uberverse.arkcraft.common.creativetabs;

import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ARKBlueprintTab extends ARKTabBase
{
	public ARKBlueprintTab()
	{
		super("tabARKBlueprints");
	}

	@Override
	public Item getTabIconItem()
	{
		return ARKCraftItems.blueprint;
	}

	@Override
	public ItemStack getIconItemStack()
	{
		ItemStack is = new ItemStack(ARKCraftItems.blueprint);
		is.setTagCompound(new NBTTagCompound());
		is.getTagCompound().setShort("engram", (short) 14);
		return is;
	}
}
