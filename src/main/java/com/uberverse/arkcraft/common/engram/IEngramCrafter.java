package com.uberverse.arkcraft.common.engram;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.engram.EngramManager.Engram;
import com.uberverse.arkcraft.common.entity.IArkLevelable;
import com.uberverse.arkcraft.util.AbstractItemStack;
import com.uberverse.arkcraft.util.IInventoryAdder;
import com.uberverse.arkcraft.util.NBTable;
import com.uberverse.arkcraft.wip.itemquality.Qualitable.ItemQuality;
import com.uberverse.arkcraft.wip.oregen.IExperienceSource;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author Lewis_McReu
 */
public interface IEngramCrafter extends NBTable, IInventoryAdder,
		IExperienceSource
{
	public default void update()
	{
		if (!getWorldIA().isRemote)
		{
			Queue<CraftingOrder> craftingQueue = getCraftingQueue();
			if (isCrafting())
			{
				decreaseProgress();
				if (getProgress() == 0)
				{
					CraftingOrder c = craftingQueue.peek();
					c.decreaseCount(1);
					ItemStack out = c.getEngram().getOutputAsItemStack(c
							.getItemQuality());
					addOrDrop(out);
					grantXP(getLevelable());
					if (c.getCount() == 0)
					{
						craftingQueue.remove();
					}
					sync();
				}
			}
			selectNextCraftingOrder();
		}
	}

	public IArkLevelable getLevelable();

	@Override
	default void grantXP(IArkLevelable leveling)
	{
		CraftingOrder co = getCraftingQueue().peek();
		leveling.addXP(co.getEngram().getExperience() * co
				.getItemQuality().multiplierTreshold);
	}

	public int getTimeOffset();

	public void setTimeOffset(int offset);

	public default void selectNextCraftingOrder()
	{
		if (!isCrafting() && !getCraftingQueue().isEmpty())
		{
			CraftingOrder c = getCraftingQueue().peek();
			while (c != null)
			{
				if (!c.canCraft(getConsumedInventory()))
				{
					getCraftingQueue().poll();
					c = getCraftingQueue().peek();
				}
				else break;
			}
			if (c != null)
			{
				setProgress(c.getEngram().getCraftingTime());
				setTimeOffset((int) (getWorldIA().getTotalWorldTime() % 20));
				c.getEngram().consume(getConsumedInventory(), c
						.getItemQuality());
				sync();
			}
		}
	}

	public default double getRelativeProgress()
	{
		if (getCraftingDuration() == 0) return 0;
		return (double) (getCraftingDuration() - getProgress())
				/ (double) (getCraftingDuration());
	}

	public default void decreaseProgress()
	{
		setProgress(getProgress() - 1);
	}

	@Override
	public default void readFromNBT(NBTTagCompound compound)
	{
		setProgress(compound.getInteger("progress"));

		Queue<CraftingOrder> craftingQueue = getCraftingQueue();

		craftingQueue.clear();

		NBTTagCompound queue = compound.getCompoundTag("queue");

		int[] engrams = queue.getIntArray("engrams");
		int[] counts = queue.getIntArray("counts");
		byte[] qualities = queue.getByteArray("qualities");

		if (engrams.length == counts.length
				&& engrams.length == qualities.length)
		{
			for (int i = 0; i < engrams.length; i++)
			{
				Engram e = EngramManager.instance().getEngram(
						(short) engrams[i]);
				int count = counts[i];
				if (e.isQualitable())
				{
					craftingQueue.add(new CraftingOrder(e, count, ItemQuality
							.get(qualities[i])));
				}
				else craftingQueue.add(new CraftingOrder(e, count));
			}
		}
		else ARKCraft.logger.warn(
				"NBT CraftingQueue was inconsistent in length and could not be loaded.");

		NBTTagList inventory = compound.getTagList("inventory",
				NBT.TAG_COMPOUND);
		for (int i = 0; i < inventory.tagCount(); i++)
		{
			NBTTagCompound n = inventory.getCompoundTagAt(i);
			this.getIInventory().setInventorySlotContents(i, n.getBoolean(
					"null") ? null : ItemStack.loadItemStackFromNBT(n));
		}
	}

	@Override
	public default void writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("progress", getProgress());

		NBTTagList inventory = new NBTTagList();
		for (ItemStack s : getInventory())
		{
			NBTTagCompound n = new NBTTagCompound();
			n.setBoolean("null", true);
			if (s != null)
			{
				s.writeToNBT(n);
				n.setBoolean("null", false);
				inventory.appendTag(n);
				continue;
			}
			inventory.appendTag(n);
		}
		compound.setTag("inventory", inventory);

		NBTTagCompound queue = new NBTTagCompound();

		Queue<CraftingOrder> craftingQueue = getCraftingQueue();

		int size = craftingQueue.size();

		int[] engrams = new int[size], counts = new int[size];
		byte[] qualities = new byte[size];

		int i = 0;

		for (CraftingOrder c : craftingQueue)
		{
			engrams[i] = c.getEngram().getId();
			counts[i] = c.getCount();
			if (c.isQualitable()) qualities[i] = c.getItemQuality().id;
			i++;
		}

		queue.setIntArray("engrams", engrams);
		queue.setIntArray("counts", counts);
		queue.setByteArray("qualities", qualities);

		compound.setTag("queue", queue);
	}

	default boolean startCraft(Engram engram, int amount, ItemQuality quality)
	{
		Queue<CraftingOrder> craftingQueue = getCraftingQueue();
		if (amount > 0)
		{
			if (canCraft(engram, quality, amount))
			{
				Iterator<CraftingOrder> it = craftingQueue.iterator();
				boolean add = false;
				while (it.hasNext())
				{
					CraftingOrder c = it.next();
					if (c.matches(engram, quality))
					{
						c.increaseCount(amount);
						add = true;
					}
				}
				if (!add)
				{
					add = craftingQueue.add(new CraftingOrder(engram, amount,
							quality));
				}
				if (add && !getWorldIA().isRemote)
				{
					selectNextCraftingOrder();
					return true;
				}
			}
		}
		return false;
	}

	default boolean startCraft(Engram engram)
	{
		ItemQuality i = engram.isQualitable() ? ItemQuality.PRIMITIVE : null;
		return startCraft(engram, 1, i);
	}

	default boolean startCraft(Engram engram, ItemQuality quality)
	{
		return startCraft(engram, 1, quality);
	}

	default boolean startCraftAll(Engram engram)
	{
		ItemQuality i = engram.isQualitable() ? ItemQuality.PRIMITIVE : null;
		return startCraftAll(engram, i);
	}

	default boolean startCraftAll(Engram engram, ItemQuality quality)
	{
		return startCraft(engram, getCraftableAmount(engram, quality), quality);
	}

	public default void cancelCraftOne(Engram engram)
	{
		cancelCraftOne(engram, engram.isQualitable() ? ItemQuality.PRIMITIVE
				: null);
	}

	public default void cancelCraftAll(Engram engram)
	{
		cancelCraftAll(engram, engram.isQualitable() ? ItemQuality.PRIMITIVE
				: null);
	}

	public default void cancelCraftOne(Engram engram, ItemQuality itemQuality)
	{
		CraftingOrder c = getCraftingQueue().peek();
		if (c != null && c.matches(engram, itemQuality))
		{
			if (c.getCount() > 1) c.decreaseCount(1);
			return;
		}

		Iterator<CraftingOrder> it = getCraftingQueue().iterator();

		while (it.hasNext())
		{
			CraftingOrder co = it.next();
			if (co.matches(engram, itemQuality))
			{
				co.decreaseCount(1);
				if (co.getCount() == 0) it.remove();
				return;
			}
		}
	}

	public default void cancelCraftAll(Engram engram, ItemQuality itemQuality)
	{
		CraftingOrder c = getCraftingQueue().peek();
		if (c != null && c.matches(engram, itemQuality))
		{
			c.decreaseCount(c.getCount() - 1);
			return;
		}

		Iterator<CraftingOrder> it = getCraftingQueue().iterator();
		while (it.hasNext())
		{
			if (it.next().matches(engram, itemQuality))
			{
				it.remove();
			}
		}
	}

	public default boolean canCraft(Engram engram, ItemQuality itemQuality,
			int amount)
	{
		return getCraftableAmount(engram, itemQuality) >= amount;
	}

	public IInventory getConsumedInventory();

	default int getCraftingAmount(Engram engram, ItemQuality itemQuality)
	{
		for (CraftingOrder c : getCraftingQueue())
		{
			if (c.matches(engram, itemQuality)) return c.getCount();
		}
		return 0;
	}

	public default int getCraftableAmount(Engram engram,
			ItemQuality itemQuality)
	{
		Collection<AbstractItemStack> is = EngramManager.Engram
				.convertIInventoryToAbstractInventory(getConsumedInventory());
		for (CraftingOrder c : getCraftingQueue())
		{
			int i = c.getCount();
			while (i > 0)
			{
				c.getEngram().consume(is, c.getItemQuality());
				i--;
			}
		}

		return engram.getCraftableAmount(is, itemQuality) - getCraftingAmount(
				engram, itemQuality);
	}

	public default boolean isCrafting()
	{
		return !getCraftingQueue().isEmpty() && getProgress() > 0;
	}

	public default int getField(int id)
	{
		switch (id)
		{
			case 0:
				return getProgress();
			default:
				return 0;
		}
	}

	public default void setField(int id, int value)
	{
		switch (id)
		{
			case 0:
				setProgress(value);
				break;
		}
	}

	public default int getFieldCount()
	{
		return 1;
	}

	public void syncProgress();

	public void sync();

	public IInventory getIInventory();

	public ItemStack[] getInventory();

	public int getProgress();

	public void setProgress(int progress);

	public default int getCraftingDuration()
	{
		if (getCraftingQueue().isEmpty()) return 0;
		else return getCraftingQueue().peek().getCraftingDuration();
	}

	public BlockPos getPosition();

	public Queue<CraftingOrder> getCraftingQueue();
}
