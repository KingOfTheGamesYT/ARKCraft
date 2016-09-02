package com.uberverse.arkcraft.wip.itemquality;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ToolTipped
{
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean isAdvanced);
}
