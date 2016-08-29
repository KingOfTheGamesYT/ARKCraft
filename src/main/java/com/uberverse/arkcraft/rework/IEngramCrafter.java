package com.uberverse.arkcraft.rework;

import java.util.Iterator;
import java.util.Queue;

import com.uberverse.arkcraft.rework.EngramManager.Engram;

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
			Queue<CraftingOrder> craftingQueue = getCraftingQueue();
			if (getProgress() > 0)
			{
				decreaseProgress();
				syncProgress();
			}
			else if (!craftingQueue.isEmpty())
			{
				CraftingOrder c = craftingQueue.peek();
				setProgress(c.getEngram().getCraftingTime());
				c.getEngram().consume(getIInventory());
				sync();
			}
			if (getProgress() == 0 && !craftingQueue.isEmpty())
			{
				CraftingOrder c = craftingQueue.peek();
				c.decreaseCount(1);
				Item i = c.getEngram().getItem();
				int amount = c.getEngram().getAmount();
				ItemStack out = new ItemStack(i, amount);
				addOrDrop(out);
				if (c.getCount() == 0) craftingQueue.remove();
				sync();
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

	public default void addOrDrop(ItemStack stack)
	{
		if (!add(stack)) getWorld().spawnEntityInWorld(new EntityItem(getWorld(),
				getPosition().getX(), getPosition().getY(), getPosition().getZ(), stack));
	}

	@Override
	public default void readFromNBT(NBTTagCompound compound)
	{
		setProgress(compound.getInteger("progress"));

		NBTTagList inventory = compound.getTagList("inventory", NBT.TAG_COMPOUND);
		for (int i = 0; i < inventory.tagCount(); i++)
		{
			NBTTagCompound n = inventory.getCompoundTagAt(i);
			if (n.getBoolean("null")) this.getInventory()[i] = null;
			else ItemStack.loadItemStackFromNBT(n);
		}

		NBTTagList queue = compound.getTagList("queue", NBT.TAG_COMPOUND);
		for (int i = 0; i < inventory.tagCount(); i++)
		{
			NBTTagCompound n = queue.getCompoundTagAt(i);
			if (n.getBoolean("load")) this.getCraftingQueue().add(new CraftingOrder(
					EngramManager.instance().getEngram(n.getShort("id")), n.getInteger("count")));
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
			NBTTagCompound n = new NBTTagCompound();
			n.setShort("id", id);
			n.setInteger("count", count);
			n.setBoolean("load", true);
			l2.appendTag(n);
		}
		compound.setTag("queue", l2);
	}

	default boolean startCraft(short engramId, int amount)
	{
		Queue<CraftingOrder> craftingQueue = getCraftingQueue();
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

	default boolean startCraftAll(short engramId)
	{
		return startCraft(engramId, EngramManager.instance().getEngram(engramId)
				.getCraftableAmount(getIInventory()) - getCraftingAmount(engramId));
	}

	default boolean startCraft(short engramId)
	{
		if (EngramManager.instance().getEngram(engramId).canCraft(getIInventory(),
				1 + getCraftingAmount(engramId))) return startCraft(engramId, 1);
		return false;
	}

	default int getCraftingAmount(short engramId)
	{
		Engram e = EngramManager.instance().getEngram(engramId);
		for (CraftingOrder c : getCraftingQueue())
		{
			if (c.getEngram().equals(e)) return c.getCount();
		}
		return 0;
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
