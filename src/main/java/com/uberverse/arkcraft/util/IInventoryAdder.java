/**
 * 
 */
package com.uberverse.arkcraft.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * @author Lewis_McReu
 */
public interface IInventoryAdder
{
	public default boolean add(ItemStack stack)
	{
		IInventory inventory = getIInventory();
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack in = inventory.getStackInSlot(i);
			if (in != null)
			{
				if (in.getItem() == stack.getItem())
				{
					if (in.stackSize + stack.stackSize < in.getMaxStackSize())
					{
						in.stackSize += stack.stackSize;
						return true;
					}
					else
					{
						stack.stackSize -= in.getMaxStackSize() - in.stackSize;
						in.stackSize = in.getMaxStackSize();
						if (stack.stackSize <= 0) return true;
					}
				}
			}
			else
			{
				inventory.setInventorySlotContents(i, stack);
				return true;
			}
		}
		return false;
	}

	public default void addOrDrop(ItemStack stack)
	{
		if (!add(stack))
			getWorldIA().spawnEntityInWorld(new EntityItem(getWorldIA(), getPosition().getX(), getPosition().getY(), getPosition().getZ(), stack));
	}

	public IInventory getIInventory();

	public BlockPos getPosition();

	public World getWorldIA();

}
