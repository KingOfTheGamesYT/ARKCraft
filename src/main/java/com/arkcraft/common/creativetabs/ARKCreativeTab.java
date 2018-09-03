package com.arkcraft.common.creativetabs;

import com.arkcraft.init.ARKCraftItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;

public class ARKCreativeTab extends ARKTabBase {
	public ARKCreativeTab() {
		super("tabARKCraft");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(ARKCraftItems.info_book);
	}
}
