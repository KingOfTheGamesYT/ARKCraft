package com.uberverse.arkcraft.rework.engram;

import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;

import com.uberverse.arkcraft.rework.engram.EngramManager.Engram;
import com.uberverse.arkcraft.rework.itemquality.Qualitable;
import com.uberverse.arkcraft.rework.itemquality.Qualitable.ItemQuality;
import com.uberverse.arkcraft.rework.util.NBTable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public interface IEngramCrafter extends NBTable
{
	public default void update()
	{
		if (!getWorld().isRemote)
		{
			if ((getWorld().getTotalWorldTime() % 20) == 0)
			{
				Queue<CraftingOrder> craftingQueue = getCraftingQueue();
				if (getProgress() > 0)
				{
					decreaseProgress();
					if (getProgress() == 0 && !craftingQueue.isEmpty())
					{
						CraftingOrder c = craftingQueue.peek();
						c.decreaseCount(1);
						Item i = c.getEngram().getItem();
						int amount = c.getEngram().getAmount();
						ItemStack out = new ItemStack(i, amount);
						if (c.isQualitable()) Qualitable.set(out, c.getItemQuality());
						addOrDrop(out);
						if (c.getCount() == 0) craftingQueue.remove();
						sync();
						return;
					}
					syncProgress();
				}
				else if (!craftingQueue.isEmpty())
				{
					CraftingOrder c = craftingQueue.peek();
					while (c != null)
					{
						if (!c.canCraft(getConsumedInventory()))
						{
							craftingQueue.poll();
							c = craftingQueue.peek();
						}
						else break;
					}
					if (c != null)
					{
						setProgress(c.getEngram().getCraftingTime());
						c.getEngram().consume(getConsumedInventory());
					}
					sync();
				}
			}
		}
	}

	public default void decreaseProgress()
	{
		setProgress(getProgress() - 1);
	}

	public default boolean add(ItemStack stack)
	{
		ItemStack[] inventory = getInventory();
		for (int i = 0; i < inventory.length; i++)
		{
			if (inventory[i] != null)
			{
				ItemStack in = inventory[i];
				if (in.getItem() == stack.getItem())
				{
					if (in.stackSize + stack.stackSize < in.getMaxStackSize()) in.stackSize += stack.stackSize;
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

	public default void addOrDrop(ItemStack stack)
	{
		if (!add(stack))
			getWorld().spawnEntityInWorld(new EntityItem(getWorld(), getPosition().getX(), getPosition().getY(), getPosition().getZ(), stack));
	}

	@Override
	public default void readFromNBT(NBTTagCompound compound)
	{
		setProgress(compound.getInteger("progress"));

		NBTTagList inventory = compound.getTagList("inventory", NBT.TAG_COMPOUND);
		for (int i = 0; i < inventory.tagCount(); i++)
		{
			NBTTagCompound n = inventory.getCompoundTagAt(i);
			this.getIInventory().setInventorySlotContents(i, n.getBoolean("null") ? null : ItemStack.loadItemStackFromNBT(n));
		}

		NBTTagList queue = compound.getTagList("queue", NBT.TAG_COMPOUND);
		for (int i = 0; i < inventory.tagCount(); i++)
		{
			NBTTagCompound n = queue.getCompoundTagAt(i);
			if (n.getBoolean("load")) this.getCraftingQueue().add(new CraftingOrder(EngramManager.instance().getEngram(n.getShort("id")),
					n.getInteger("count"), ItemQuality.get(n.getByte("quality"))));
		}
	}

	@Override
	public default void writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("progress", getProgress());

		NBTTagList l = new NBTTagList();
		for (ItemStack s : getInventory())
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
		for (CraftingOrder c : getCraftingQueue())
		{
			int count = c.getCount();
			short id = c.getEngram().getId();
			byte q = c.getItemQuality().id;
			NBTTagCompound n = new NBTTagCompound();
			n.setShort("id", id);
			n.setInteger("count", count);
			n.setByte("quality", q);
			n.setBoolean("load", true);
			l2.appendTag(n);
		}
		compound.setTag("queue", l2);
	}

	default boolean startCraft(short engramId, int amount, ItemQuality quality)
	{
		Queue<CraftingOrder> craftingQueue = getCraftingQueue();
		if (amount > 0)
		{
			Engram e = EngramManager.instance().getEngram(engramId);
			Iterator<CraftingOrder> it = craftingQueue.iterator();
			while (it.hasNext())
			{
				CraftingOrder c = it.next();
				if (c.getEngram().equals(e) && (!c.isQualitable() || c.getItemQuality().equals(quality))
						&& c.canCraft(getConsumedInventory(), c.getCount() + amount))
				{
					c.increaseCount(amount);
					return true;
				}
			}
			if (e.canCraft(getConsumedInventory(), amount, quality))
			{
				craftingQueue.add(new CraftingOrder(e, amount, quality));
				return true;
			}
		}
		return false;
	}

	default boolean startCraftAll(short engramId, ItemQuality quality)
	{
		return startCraft(engramId, EngramManager.instance().getEngram(engramId).getCraftableAmount(getIInventory()) - getCraftingAmount(engramId),
				quality);
	}

	default boolean startCraft(short engramId)
	{
		return startCraft(engramId, 1, ItemQuality.PRIMITIVE);
	}

	default boolean startCraftAll(short engramId)
	{
		return startCraft(engramId, EngramManager.instance().getEngram(engramId).getCraftableAmount(getIInventory()) - getCraftingAmount(engramId),
				ItemQuality.PRIMITIVE);
	}

	default boolean startCraft(short engramId, ItemQuality quality)
	{
		return startCraft(engramId, 1, quality);
	}

	public IInventory getConsumedInventory();

	default int getCraftingAmount(short engramId)
	{
		Engram e = EngramManager.instance().getEngram(engramId);
		for (CraftingOrder c : getCraftingQueue())
		{
			if (c.getEngram().equals(e)) return c.getCount();
		}
		return 0;
	}

	public default boolean isCrafting()
	{
		return !getCraftingQueue().isEmpty();
	}

	public default int getField(int id)
	{
		Queue<CraftingOrder> craftingQueue = getCraftingQueue();
		switch (id)
		{
			case 0:
				return getProgress();
			default:
				int t = (id - 1) / 2;
				CraftingOrder c = craftingQueue.toArray(new CraftingOrder[0])[t];
				return (t % 2) == 1 ? c.getCount() : c.getEngram().getId();
		}
	}

	public default void setField(int id, int value)
	{
		Queue<CraftingOrder> craftingQueue = getCraftingQueue();
		switch (id)
		{
			case 0:
				setProgress(value);
				break;
			default:
				int t = (id - 1) / 2;
				CraftingOrder[] q = craftingQueue.toArray(new CraftingOrder[0]);
				CraftingOrder c = q[t];
				if (c == null)
				{
					if ((t % 2) == 1) q[t] = new CraftingOrder(null, value);
					else q[t] = new CraftingOrder(EngramManager.instance().getEngram((short) value));
					craftingQueue.clear();
					Collections.addAll(craftingQueue, q);
				}
				else
				{
					if ((t % 2) == 1) c.setCount(value);
					else c.setEngram(EngramManager.instance().getEngram((short) value));
				}
		}
	}

	public default int getFieldCount()
	{
		return 1 + getCraftingQueue().size() * 2;
	}

	public void syncProgress();

	public void sync();

	public IInventory getIInventory();

	public ItemStack[] getInventory();

	public int getProgress();

	public void setProgress(int progress);

	public BlockPos getPosition();

	public World getWorld();

	public Queue<CraftingOrder> getCraftingQueue();
}
