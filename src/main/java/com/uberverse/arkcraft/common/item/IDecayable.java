package com.uberverse.arkcraft.common.item;

import java.util.List;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.util.I18n;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author tom5454
 * @author Lewis_McReu
 */
public interface IDecayable
{
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
		return decayStart >= 0 && getRemovalTime(stack, decayModifier) < ARKCraft.proxy.getWorldTime();
	}

	public default long getRemovalTime(ItemStack stack, double decayModifier)
	{
		return (long) (getDecayStart(stack) + getDecayTime(stack) * decayModifier);
	}

	public static long getDecayStart(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("decayStart") : -1;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public default void addInformation(ItemStack itemStack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		String toAdd = I18n.translate("arkcraft.decayable.tooltip");
		long seconds = (long) Math.ceil((double) getDecayTimeLeft(itemStack, getDecayModifier(itemStack)) / 20d);
		if (seconds > 0)
		{
			if (seconds > 59)
			{
				long minutes = seconds / 60;
				seconds = seconds % 60;
				if (minutes > 59)
				{
					long hours = minutes / 60;
					minutes = minutes % 60;
					if (hours > 23)
					{
						long days = hours / 24;
						hours = hours % 24;
						toAdd += " " + (days == 1 ? I18n.format("arkcraft.day", days) : I18n.format("arkcraft.days",
								days));
					}
					toAdd += " " + (hours == 1 ? I18n.format("arkcraft.hour", hours) : I18n.format("arkcraft.hours",
							hours));
				}
				toAdd += " " + (minutes == 1 ? I18n.format("arkcraft.minute", minutes) : I18n.format("arkcraft.minutes",
						minutes));
			}
			toAdd += " " + (seconds == 1 ? I18n.format("arkcraft.second", seconds) : I18n.format("arkcraft.seconds",
					seconds));
			tooltip.add(toAdd);
		}
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

	public static void setDecayStart(ItemStack stack, long decayStart)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setLong("decayStart", decayStart);
	}

	public default long getDecayTimeLeft(ItemStack stack, double decayModifier)
	{
		return getDecayStart(stack) > -1 ? getRemovalTime(stack, decayModifier) - ARKCraft.proxy.getWorldTime() : 0;
	}

	public long getDecayTime(ItemStack stack);
}
