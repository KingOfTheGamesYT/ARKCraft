package com.arkcraft.common.creativetabs;

import com.arkcraft.init.ARKCraftItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ARKBlueprintTab extends ARKTabBase {
	public ARKBlueprintTab() {
		super("tabARKBlueprints");
	}

	@Override
	public ItemStack createIcon() {
		ItemStack is = new ItemStack(ARKCraftItems.blueprint);
		is.setTagCompound(new NBTTagCompound());
		is.getTagCompound().setShort("engram", (short) 14);
		return is;
	}
}
