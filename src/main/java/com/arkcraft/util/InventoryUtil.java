package com.arkcraft.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class InventoryUtil {
	public static boolean add(ItemStack stack, IInventory inventory) {
		// first try adding to filled slots
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack in = inventory.getStackInSlot(i);
			if (!in.isEmpty()) {
				if (in.getItem() == stack.getItem()) {
					if (in.getCount() + stack.getCount() < in.getMaxStackSize()) {
						in.grow(stack.getCount());
						return true;
					} else {
						stack.shrink(in.getMaxStackSize() - in.getCount());
						in.setCount(in.getMaxStackSize());
						if (stack.getCount() <= 0) return true;
					}
				}
			}
		}
		// then try empty slots
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack in = inventory.getStackInSlot(i);
			if (in == null) {
				inventory.setInventorySlotContents(i, stack);
				return true;
			}
		}
		return false;
	}

	public static void addOrDrop(ItemStack stack, IInventory inventory, BlockPos pos, World world) {
		if (!add(stack, inventory)) world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(),
				stack));
	}

	public static void writeToNBT(NBTTagCompound nbt, IInventory inventory) {
		NBTTagList inv = new NBTTagList();
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack s = inventory.getStackInSlot(i);
			NBTTagCompound ns = new NBTTagCompound();
			ns.setBoolean("null", s == null);
			if (s != null) s.writeToNBT(ns);
			inv.appendTag(ns);
		}
		nbt.setTag("inventory", inv);
	}

	public static void readFromNBT(NBTTagCompound nbt, IInventory inventory) {
		NBTTagList inv = nbt.getTagList("inventory", NBT.TAG_COMPOUND);
		for (int i = 0; i < inv.tagCount(); i++) {
			NBTTagCompound ns = inv.getCompoundTagAt(i);
			if (!ns.getBoolean("null")) inventory.setInventorySlotContents(i, new ItemStack(ns));
		}
	}
}
