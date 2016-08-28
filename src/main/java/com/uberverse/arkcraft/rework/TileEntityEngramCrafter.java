package com.uberverse.arkcraft.rework;

import java.util.Queue;

import com.uberverse.arkcraft.rework.EngramManager.Engram;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public abstract class TileEntityEngramCrafter extends TileEntity
		implements IInventory, IUpdatePlayerListBox
{
	private ItemStack[] inventory;

	private Queue<Entry> craftingQueue;

	private String name;

	public TileEntityEngramCrafter(int size, String name)
	{
		inventory = new ItemStack[size];
		this.name = name;
		craftingQueue = new FixedSizeQueue<>(5);
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub

	}

	private boolean startCraft(short engramId, int amount)
	{
		// TODO
		return false;
	}

	public boolean startCraftAll(short engramId)
	{
		// TODO
		while (EngramManager.instance().getEngram(engramId).canCraft(this))
			startCraft(engramId, 1);
		return true;
	}

	public boolean startCraft(short engramId)
	{
		// TODO
		if (EngramManager.instance().getEngram(engramId)
				.canCraft(this)) return startCraft(engramId, 1);
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		// TODO Auto-generated method stub
		super.readFromNBT(compound);
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		// TODO Auto-generated method stub
		super.writeToNBT(compound);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean hasCustomName()
	{
		return true;
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText(name);
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return inventory[index].splitStack(count);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return inventory[index];
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory[index] = stack;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
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
		for (int i = 0; i < inventory.length; i++)
			inventory[i] = null;
	}

	public static class Entry
	{
		private Engram engram;
		private int count;

		public Entry(Engram engram, int count)
		{
			this.engram = engram;
			this.count = count;
		}

		public Entry(Engram engram)
		{
			this(engram, 1);
		}

		public int getCount()
		{
			return count;
		}

		public Engram getEngram()
		{
			return engram;
		}

		public void increaseCount(int increase)
		{
			count += increase;
		}
	}
}
