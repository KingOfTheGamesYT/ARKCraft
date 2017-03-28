package com.uberverse.arkcraft.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.util.I18n;
import com.uberverse.arkcraft.util.Utils;

/**
 * @author Lewis_McReu
 * @author tom5454
 */
public interface IDecayable
{
	public default void decayTick(IInventory inventory, int slotId, double decayModifier, ItemStack stack, World world)
	{
		if (world.isRemote || world.getTotalWorldTime() % 20 != 0) return;
		decayTick(inventory, slotId, decayModifier, stack);
	}

	default void decayTick(IInventory inventory, int slotId, double decayModifier, ItemStack stack)
	{
		if (getDecayStart(stack) < 0) setDecayStart(stack, ARKCraft.proxy.getWorldTime());
		setDecayModifier(stack, decayModifier);
		if (shouldRemove(stack, decayModifier))
		{
			stack.stackSize--;
			setDecayStart(stack, ARKCraft.proxy.getWorldTime());
			if (stack.stackSize <= 0) inventory.setInventorySlotContents(slotId, null);
		}
	}

	public default boolean shouldRemove(ItemStack stack, double decayModifier)
	{
		long decayStart = getDecayStart(stack);
		return decayStart >= 0 && getRemovalTime(stack, decayModifier) <= ARKCraft.proxy.getWorldTime();
	}

	public default long getRemovalTime(ItemStack stack, double decayModifier)
	{
		return (long) (getDecayStart(stack) + getDecayTime(stack) * decayModifier);
	}

	public static long getDecayStart(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("decayStart") : -1;
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public default void addInformation(ItemStack itemStack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		String toAdd = I18n.translate("arkcraft.decayable.tooltip");
		String ret = Utils.formatTime(getDecayTimeLeft(itemStack, getDecayModifier(itemStack)));
		if(!ret.isEmpty())tooltip.add(toAdd + ret);
	}

	public static double getDecayModifier(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("decayModifier") : 1;
	}

	public static void setDecayModifier(ItemStack stack, double decayModifier)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setDouble("decayModifier", decayModifier);
	}

	public static ItemStack setDecayStart(ItemStack stack, long decayStart)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setLong("decayStart", decayStart);
		return stack;
	}

	public default long getDecayTimeLeft(ItemStack stack, double decayModifier)
	{
		return getDecayStart(stack) > -1 ? getRemovalTime(stack, decayModifier) - ARKCraft.proxy.getWorldTime() : 0;
	}

	public long getDecayTime(ItemStack stack);
}
