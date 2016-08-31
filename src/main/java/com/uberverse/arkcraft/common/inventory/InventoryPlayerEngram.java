/**
 * 
 */
package com.uberverse.arkcraft.common.inventory;

import com.uberverse.arkcraft.common.item.engram.Engram;
import com.uberverse.arkcraft.rework.engram.EngramManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

/**
 * @author ERBF | Aug 10, 2016
 *
 */

public class InventoryPlayerEngram implements IInventory
{

	private final String name = "Engram Inventory";
	private final String tagName = "inventoryPlayerEngram";
	public static int size = 64;

	public InventoryPlayerEngram()
	{
	}

	@Override
	public int getSizeInventory()
	{
		return EngramManager.instance().getEngrams().size();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return new ItemStack(EngramManager.instance().getEngram((short) slot).getItem());
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
	{
	}

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
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	/**
	 * This method doesn't seem to do what it claims to do, as items can still
	 * be left-clicked and placed in the inventory even when this returns false
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		// If you have different kinds of slots, then check them here:
		// if (slot == SLOT_SHIELD && itemstack.getItem() instanceof ItemShield)
		// return true;

		// For now, only ItemUseMana items can be stored in these slots
		return itemstack.getItem() instanceof Engram;
	}

	public void writeToNBT(NBTTagCompound compound)
	{
	}

	public void readFromNBT(NBTTagCompound compound)
	{
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText(name);
	}

}
