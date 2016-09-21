package com.uberverse.arkcraft.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class InventoryUtil
{
	public static boolean add(ItemStack stack, IInventory inventory)
	{
		// first try adding to filled slots
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
		}
		// then try empty slots
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack in = inventory.getStackInSlot(i);
			if (in == null)
			{
				inventory.setInventorySlotContents(i, stack);
				return true;
			}
		}
		return false;
	}

	public static void addOrDrop(ItemStack stack, IInventory inventory,
			BlockPos pos, World world)
	{
		if (!add(stack, inventory))
			world.spawnEntityInWorld(new EntityItem(world, pos.getX(),
					pos.getY(), pos.getZ(), stack));
	}
}
