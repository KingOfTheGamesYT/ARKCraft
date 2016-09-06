/**
 * 
 */
package com.uberverse.arkcraft.common.burner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.uberverse.arkcraft.common.burner.BurnerManager.BurnerFuel;
import com.uberverse.arkcraft.common.burner.BurnerManager.BurnerRecipe;
import com.uberverse.arkcraft.common.burner.BurnerManager.BurnerType;
import com.uberverse.arkcraft.util.CollectionUtil;
import com.uberverse.arkcraft.util.IInventoryAdder;
import com.uberverse.arkcraft.util.NBTable;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author Lewis_McReu
 */
public interface IBurner extends IInventoryAdder, NBTable
{
	public default void update()
	{
		updateBurning();
		World world = getWorldIA();
		if (!world.isRemote)
		{
			Collection<BurnerRecipe> possibleRecipes = BurnerManager.instance().getRecipes(getBurnerType());
			if (this.isBurning() && possibleRecipes.size() > 0)
			{
				Map<BurnerRecipe, Integer> activeRecipes = getActiveRecipes();

				Iterator<Entry<BurnerRecipe, Integer>> it = activeRecipes.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<BurnerRecipe, Integer> e = it.next();
					if (!possibleRecipes.contains(e.getKey()))
					{
						it.remove();
					}
				}
				for (BurnerRecipe r : possibleRecipes)
				{
					if (!activeRecipes.containsKey(r)) activeRecipes.put(r, 0);
				}

				updateCookTimes();

				sync();
			}
			else clearActiveRecipes();
		}
		world.checkLightFor(EnumSkyBlock.BLOCK, getPosition());
	}

	public default void updateCookTimes()
	{
		Iterator<Entry<BurnerRecipe, Integer>> it = getActiveRecipes().entrySet().iterator();
		while (it.hasNext())
		{
			Entry<BurnerRecipe, Integer> e = it.next();
			e.setValue(e.getValue() + 1);
			if (e.getValue() == e.getKey().getCraftingTime())
			{
				List<ItemStack> l = new ArrayList<>();

				Iterator<Entry<Item, Integer>> iter = e.getKey().getItems().entrySet().iterator();
				while (iter.hasNext())
				{
					Entry<Item, Integer> entry = iter.next();
					Item item = entry.getKey();
					int val = entry.getValue();
					while (val > item.getItemStackLimit())
					{
						l.add(new ItemStack(item, item.getItemStackLimit()));
						val -= item.getItemStackLimit();
					}
					l.add(new ItemStack(item, val));
				}

				for (ItemStack s : l)
				{
					addOrDrop(s);
				}
				it.remove();
			}
		}
	}

	public default void clearActiveRecipes()
	{
		getActiveRecipes().clear();
	}

	public default boolean updateIsBurning(boolean burning)
	{
		setBurning(burning);
		updateBurning();
		if (burning != isBurning()) sync();
		return isBurning();
	}

	public default void updateBurning()
	{
		if (isBurning())
		{
			int burningTicks = getBurningTicks();
			if (burningTicks > 0)
			{
				burningTicks--;
				setBurningTicks(getBurningTicks() - 1);
			}
			if (burningTicks <= 0)
			{
				if (!burn())
				{
					setBurning(false);
				}
			}
		}
		else
		{
			setBurningTicks(0);
			setCurrentFuel(null);
		}
	}

	public default boolean burn()
	{
		BurnerFuel fuel = getCurrentFuel();
		if (fuel != null)
		{
			if (!getWorldIA().isRemote)
			{
				ItemStack s = new ItemStack(fuel.getOutput());
				addOrDrop(s);
			}
			setCurrentFuel(null);
		}
		IInventory inventory = getIInventory();
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null && BurnerManager.instance().isValidFuel(stack.getItem(), getBurnerType()))
			{
				if (!getWorldIA().isRemote)
				{
					stack.stackSize--;
					if (stack.stackSize == 0) inventory.setInventorySlotContents(i, null);
				}
				setCurrentFuel(BurnerManager.instance().getFuel(stack.getItem()));
				setBurningTicks(getBurningTicks() + BurnerManager.instance().getFuel(stack.getItem()).getBurnTime());
				return true;
			}
		}
		return false;
	}

	public void setBurning(boolean burning);

	public int getBurningTicks();

	public void setBurningTicks(int burningTicks);

	public BurnerType getBurnerType();

	public boolean isBurning();

	public BurnerFuel getCurrentFuel();

	public void setCurrentFuel(BurnerFuel fuel);

	public Map<BurnerRecipe, Integer> getActiveRecipes();

	public void sync();

	@Override
	public default void readFromNBT(NBTTagCompound compound)
	{
		setBurning(compound.getBoolean("burning"));
		setBurningTicks(compound.getInteger("burningTicks"));

		NBTTagList inventory = compound.getTagList("inventory", NBT.TAG_COMPOUND);

		Collection<NBTTagCompound> out = new ArrayList<>();
		for (int i = 0; i < inventory.tagCount(); i++)
			out.add(inventory.getCompoundTagAt(i));

		List<ItemStack> in = CollectionUtil.convert(out, (NBTTagCompound n) -> {
			return n.getBoolean("null") ? null : ItemStack.loadItemStackFromNBT(n);
		});

		for (int i = 0; i < getInventory().length; i++)
			getInventory()[i] = in.get(i);
	}

	@Override
	public default void writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("burningTicks", getBurningTicks());
		compound.setBoolean("burning", isBurning());

		NBTTagList inventory = new NBTTagList();

		for (NBTTagCompound nbt : CollectionUtil.convert(Arrays.asList(getInventory()), (ItemStack s) -> {
			NBTTagCompound n = new NBTTagCompound();
			n.setBoolean("null", true);
			if (s != null)
			{
				s.writeToNBT(n);
				n.setBoolean("null", false);
			}
			return n;
		}))
			inventory.appendTag(nbt);

		compound.setTag("inventory", inventory);
	}

	public ItemStack[] getInventory();
}
