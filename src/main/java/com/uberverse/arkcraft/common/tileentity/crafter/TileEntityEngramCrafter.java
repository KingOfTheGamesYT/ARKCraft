package com.uberverse.arkcraft.common.tileentity.crafter;

import java.util.Queue;

import com.uberverse.arkcraft.common.engram.CraftingOrder;
import com.uberverse.arkcraft.common.engram.IEngramCrafter;
import com.uberverse.arkcraft.util.FixedSizeQueue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public abstract class TileEntityEngramCrafter extends TileEntity implements IInventory, IUpdatePlayerListBox, IEngramCrafter
{
	private ItemStack[] inventory;

	private Queue<CraftingOrder> craftingQueue;

	private int progress;

	private String name;

	public TileEntityEngramCrafter(int size, String name)
	{
		inventory = new ItemStack[size];
		this.progress = 0;
		this.name = name;
		craftingQueue = new FixedSizeQueue<>(5);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		IEngramCrafter.super.readFromNBT(compound);
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		IEngramCrafter.super.writeToNBT(compound);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return new S35PacketUpdateTileEntity(this.pos, 0, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public boolean receiveClientEvent(int id, int type)
	{
		if (id == 0)
		{
			progress = type;
			return true;
		}
		else return super.receiveClientEvent(id, type);
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
	public void clear()
	{
		for (int i = 0; i < inventory.length; i++)
			inventory[i] = null;
	}

	@Override
	public void update()
	{
		IEngramCrafter.super.update();
	}

	@Override
	public void syncProgress()
	{
		worldObj.addBlockEvent(pos, blockType, 0, progress);
		markDirty();
	}

	@Override
	public void sync()
	{
		worldObj.markBlockForUpdate(pos);
		markDirty();
	}

	@Override
	public IInventory getIInventory()
	{
		return this;
	}

	@Override
	public IInventory getConsumedInventory()
	{
		return this;
	}

	@Override
	public ItemStack[] getInventory()
	{
		return inventory;
	}

	@Override
	public int getProgress()
	{
		return progress;
	}

	@Override
	public void setProgress(int progress)
	{
		this.progress = progress;
	}

	@Override
	public BlockPos getPosition()
	{
		return pos;
	}

	@Override
	public Queue<CraftingOrder> getCraftingQueue()
	{
		return craftingQueue;
	}
	//
	// @Override
	// public int getField(int id)
	// {
	// Queue<CraftingOrder> craftingQueue = getCraftingQueue();
	// switch (id)
	// {
	// case 0:
	// return getProgress();
	// default:
	// int t = (id - 1) / 2;
	// CraftingOrder c = craftingQueue.toArray(new CraftingOrder[0])[t];
	// return (t % 2) == 1 ? c.getCount() : c.getEngram().getId();
	// }
	// }
	//
	// @Override
	// public void setField(int id, int value)
	// {
	// Queue<CraftingOrder> craftingQueue = getCraftingQueue();
	// switch (id)
	// {
	// case 0:
	// setProgress(value);
	// break;
	// default:
	// int t = (id - 1) / 2;
	// CraftingOrder[] q = craftingQueue.toArray(new CraftingOrder[0]);
	// CraftingOrder c = q[t];
	// if (c == null)
	// {
	// if ((t % 2) == 1) q[t] = new CraftingOrder(null, value);
	// else q[t] = new CraftingOrder(EngramManager.instance().getEngram((short) value));
	// craftingQueue.clear();
	// Collections.addAll(craftingQueue, q);
	// }
	// else
	// {
	// if ((t % 2) == 1) c.setCount(value);
	// else c.setEngram(EngramManager.instance().getEngram((short) value));
	// }
	// }
	// }
	//
	// @Override
	// public int getFieldCount()
	// {
	// return 1 + getCraftingQueue().size() * 2;
	// }

	@Override
	public int getField(int id)
	{
		return IEngramCrafter.super.getField(id);
	}

	@Override
	public int getFieldCount()
	{
		return IEngramCrafter.super.getFieldCount();
	}

	@Override
	public void setField(int id, int value)
	{
		IEngramCrafter.super.setField(id, value);
	}
}
