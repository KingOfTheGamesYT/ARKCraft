/**
 * 
 */
package com.uberverse.arkcraft.common.inventory;

import com.uberverse.arkcraft.common.engram.EngramManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.text.ITextComponent;

/**
 * @author ERBF | Aug 10, 2016
 * @author Lewis_McReu
 */

public class InventoryEngram implements IInventory
{

	private final String name = "Engram Inventory";
	public static int size = 64;

	@Override
	public int getSizeInventory()
	{
		return EngramManager.instance().getEngrams().size();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		ItemStack output = EngramManager.instance().getEngram((short) slot).getOutputAsItemStack();
		output.stackSize = 1;
		return output;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		setInventorySlotContents(slot, null);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean hasCustomName()
	{
		return name.length() > 0;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public void markDirty()
	{
		/*
		 * for (int i = 0; i < getSizeInventory(); ++i) { if (getStackInSlot(i)
		 * != null && getStackInSlot(i).stackSize == 0) { inventory[i] = null; }
		 * }
		 */
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{}

	@Override
	public void closeInventory(EntityPlayer player)
	{}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return false;
	}

	public void writeToNBT(NBTTagCompound compound)
	{}

	public void readFromNBT(NBTTagCompound compound)
	{}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{}

	@Override
	public ITextComponent getDisplayName()
	{
		return new ITextComponent(name);
	}

}
