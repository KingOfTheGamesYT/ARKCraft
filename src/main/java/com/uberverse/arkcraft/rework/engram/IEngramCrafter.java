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
			//if ((getWorld().getTotalWorldTime() % 20) == 0)
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
						if (c.getCount() == 0)
						{
							craftingQueue.remove();
						}
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
						setCraftingDuration(c.getEngram().getCraftingTime());
						c.getEngram().consume(getConsumedInventory());
					}
					sync();
				}
			}
		}
	}

	public default double getRelativeProgress()
	{
		if (getCraftingDuration() == 0) return 0;
		return (double) (getCraftingDuration() - getProgress()) / (double) getCraftingDuration();
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
		setCraftingDuration(compound.getInteger("duration"));

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
			if (n.getBoolean("load"))
			{
				CraftingOrder c = new CraftingOrder(EngramManager.instance().getEngram(n.getShort("id")), n.getInteger("count"));
				if (c.isQualitable()) c.setItemQuality(ItemQuality.get(n.getByte("quality")));
				this.getCraftingQueue().add(c);
			}
		}
	}

	@Override
	public default void writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("progress", getProgress());
		compound.setInteger("duration", getCraftingDuration());

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
			NBTTagCompound n = new NBTTagCompound();
			n.setShort("id", c.getEngram().getId());
			n.setInteger("count", c.getCount());
			if (c.isQualitable()) n.setByte("quality", c.getItemQuality().id);
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
			case 1:
				return getCraftingDuration();
			default:
				int t = (id - 2) / 3;
				CraftingOrder c = craftingQueue.toArray(new CraftingOrder[0])[t];
				return (t % 3) == 1 ? c.getCount() : (t % 3) == 2 ? c.getItemQuality().id : c.getEngram().getId();
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
			case 1:
				setCraftingDuration(value);
				break;
			default:
				int t = (id - 2) / 3;
				CraftingOrder[] q = craftingQueue.toArray(new CraftingOrder[0]);
				CraftingOrder c = q[t];
				if (c == null)
				{
					if ((t % 3) == 1) q[t] = new CraftingOrder(null, value);
					else if ((t % 3) == 2) q[t] = new CraftingOrder(null, 0, ItemQuality.get((byte) value));
					else q[t] = new CraftingOrder(EngramManager.instance().getEngram((short) value));
					craftingQueue.clear();
					Collections.addAll(craftingQueue, q);
				}
				else
				{
					if ((t % 3) == 1) c.setCount(value);
					else if ((t % 3) == 2) c.setItemQuality(ItemQuality.get((byte) value));
					else c.setEngram(EngramManager.instance().getEngram((short) value));
				}
				break;
		}
	}

	public default int getFieldCount()
	{
		return 2 + getCraftingQueue().size() * 3;
	}

	public void syncProgress();

	public void sync();

	public IInventory getIInventory();

	public ItemStack[] getInventory();

	public int getProgress();

	public void setProgress(int progress);

	public int getCraftingDuration();

	public void setCraftingDuration(int craftingDuration);

	public BlockPos getPosition();

	public World getWorld();

	public Queue<CraftingOrder> getCraftingQueue();
}
