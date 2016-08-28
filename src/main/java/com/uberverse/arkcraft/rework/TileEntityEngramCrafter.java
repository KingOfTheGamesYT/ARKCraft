package com.uberverse.arkcraft.rework;

import java.util.Iterator;
import java.util.Queue;

import com.uberverse.arkcraft.rework.EngramManager.Engram;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants.NBT;

public abstract class TileEntityEngramCrafter extends TileEntity
		implements IInventory, IUpdatePlayerListBox
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
	public void update()
	{
		if (progress > 0) progress--;
		else if (!craftingQueue.isEmpty())
		{
			CraftingOrder c = craftingQueue.peek();
			progress = c.getEngram().getCraftingTime();
			c.getEngram().consume(this);
		}
		if (progress == 0 && !craftingQueue.isEmpty())
		{
			CraftingOrder c = craftingQueue.peek();
			c.decreaseCount(1);
			Item i = c.getEngram().getItem();
			int amount = c.getEngram().getAmount();
			ItemStack out = new ItemStack(i, amount);
			addOrDrop(out);
			if (c.count == 0) craftingQueue.remove();
		}

		 System.out.println("queue");
		 for (CraftingOrder c : craftingQueue)
		 {
		 System.out.println("id: " + c.getEngram().getId() + " , count: " +
		 c.getCount());
		 }
	}

	public boolean add(ItemStack stack)
	{
		for (int i = 0; i < inventory.length; i++)
		{
			if (inventory[i] != null)
			{
				ItemStack in = inventory[i];
				if (in.getItem() == stack.getItem())
				{
					if (in.stackSize + stack.stackSize < in
							.getMaxStackSize()) in.stackSize += stack.stackSize;
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
				inventory[i] = stack;
				return true;
			}
		}
		return false;
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

	public void addOrDrop(ItemStack stack)
	{
		if (!add(stack)) worldObj.spawnEntityInWorld(
				new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack));
	}

	private boolean startCraft(short engramId, int amount)
	{
		if (amount > 0)
		{
			Engram e = EngramManager.instance().getEngram(engramId);
			Iterator<CraftingOrder> it = craftingQueue.iterator();
			while (it.hasNext())
			{
				CraftingOrder c = it.next();
				if (c.getEngram().equals(e))
				{
					c.increaseCount(amount);
					return true;
				}
			}
			return craftingQueue.add(new CraftingOrder(e, amount));
		}
		return false;
	}

	public boolean startCraftAll(short engramId)
	{
		return startCraft(engramId, EngramManager.instance().getEngram(engramId)
				.getCraftableAmount(this) - getCraftingAmount(engramId));
	}

	public boolean startCraft(short engramId)
	{
		if (EngramManager.instance().getEngram(engramId).canCraft(this,
				1 + getCraftingAmount(engramId))) return startCraft(engramId, 1);
		return false;
	}

	private int getCraftingAmount(short engramId)
	{
		Engram e = EngramManager.instance().getEngram(engramId);
		for (CraftingOrder c : craftingQueue)
		{
			if (c.getEngram().equals(e)) return c.getCount();
		}
		return 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		progress = compound.getInteger("progress");

		NBTTagList inventory = compound.getTagList("inventory", NBT.TAG_COMPOUND);
		for (int i = 0; i < inventory.tagCount(); i++)
		{
			NBTTagCompound n = inventory.getCompoundTagAt(i);
			if (n.getBoolean("null")) this.inventory[i] = null;
			else ItemStack.loadItemStackFromNBT(n);
		}

		NBTTagList queue = compound.getTagList("queue", NBT.TAG_COMPOUND);
		for (int i = 0; i < inventory.tagCount(); i++)
		{
			NBTTagCompound n = queue.getCompoundTagAt(i);
			if (n.getBoolean("load")) this.craftingQueue.add(new CraftingOrder(
					EngramManager.instance().getEngram(n.getShort("id")), n.getInteger("count")));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		compound.setInteger("progress", progress);

		NBTTagList l = new NBTTagList();
		for (ItemStack s : inventory)
		{
			NBTTagCompound n = new NBTTagCompound();
			n.setBoolean("null", true);
			if (s != null)
			{
				s.writeToNBT(n);
				n.setBoolean("null", false);
				l.appendTag(n);
				continue;
			}
			l.appendTag(n);
		}
		compound.setTag("inventory", l);

		NBTTagList l2 = new NBTTagList();
		for (CraftingOrder c : craftingQueue)
		{
			int count = c.getCount();
			short id = c.getEngram().getId();
			System.out.println("wrote " + id + " " + count);
			NBTTagCompound n = new NBTTagCompound();
			n.setShort("id", id);
			n.setInteger("count", count);
			n.setBoolean("load", true);
			l2.appendTag(n);
		}
		compound.setTag("queue", l2);
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

	public class CraftingOrder
	{
		private Engram engram;
		private int count;

		public CraftingOrder(Engram engram, int count)
		{
			this.engram = engram;
			this.count = count;
		}

		public CraftingOrder(Engram engram)
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

		public void setCount(int count)
		{
			this.count = count;
		}

		public void decreaseCount(int decrease)
		{
			count -= decrease;
		}

		public void increaseCount(int increase)
		{
			count += increase;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof CraftingOrder) return ((CraftingOrder) obj).getEngram()
					.equals(engram);
			else if (obj instanceof Engram) return ((Engram) obj).equals(engram);
			return false;
		}
	}
}
