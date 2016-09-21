/**
 * 
 */
package com.uberverse.arkcraft.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * @author Lewis_McReu
 */
public interface IInventoryAdder
{
	public default void addOrDrop(ItemStack stack)
	{
		InventoryUtil.addOrDrop(stack, getIInventory(), getPosition(),
				getWorldIA());
	}

	public IInventory getIInventory();

	public BlockPos getPosition();

	public World getWorldIA();

}
