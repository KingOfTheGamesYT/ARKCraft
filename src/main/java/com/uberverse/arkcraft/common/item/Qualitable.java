package com.uberverse.arkcraft.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.uberverse.arkcraft.util.I18n;

public interface Qualitable
{
	public static final String qualityKey = "itemquality";

	public static ItemQuality get(ItemStack stack)
	{
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey(qualityKey)) return ItemQuality.get(stack.getTagCompound().getByte(
				qualityKey));
		return null;
	}

	public static ItemStack set(ItemStack stack, ItemQuality q)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setByte(qualityKey, q.id);
		return stack;
	}

	public static enum ItemQuality
	{
		PRIMITIVE(1, 1, 1, 1, ChatFormatting.RESET),
		RAMSHACKLE(1.25, 1.33, 1.1, 1.1, ChatFormatting.GREEN),
		APPRENTICE(2.5, 1.67, 1.25, 1.25, ChatFormatting.BLUE),
		JOURNEYMAN(4.5, 2, 1.375, 1.375, ChatFormatting.DARK_PURPLE),
		MASTERCRAFT(7, 2.5, 1.5, 1.5, ChatFormatting.YELLOW),
		ASCENDANT(10, 3.5, 2, 2, ChatFormatting.RED);

		// TODO add other multipliers (f.e. efficiency (for harvesting speed and
		// maybe harvest numbers)) & remove current uses of multiplierTreshold
		public final double multiplierTreshold, resourceMultiplier, harvestMultiplier, durabilityMultiplier;
		private static byte idCounter = 0;
		public final byte id;
		public final ChatFormatting color;

		private ItemQuality(double multiplierTreshold, double resourceMultiplier, double harvestMultiplier, double durabilityMultiplier, ChatFormatting color)
		{
			this.multiplierTreshold = multiplierTreshold;
			this.resourceMultiplier = resourceMultiplier;
			this.harvestMultiplier = harvestMultiplier;
			this.durabilityMultiplier = durabilityMultiplier;
			this.color = color;
			id = getNextId();
		}

		private byte getNextId()
		{
			byte out = idCounter;
			idCounter++;
			return out;
		}

		@Override
		public String toString()
		{
			return name().toLowerCase();
		}

		public String toFormattedString()
		{
			return color + I18n.translate("arkcraft.itemquality." + toString());
		}

		public static ItemQuality get(byte id)
		{
			for (ItemQuality q : values())
			{
				if (q.id == id) return q;
			}
			return null;
		}
	}
}
