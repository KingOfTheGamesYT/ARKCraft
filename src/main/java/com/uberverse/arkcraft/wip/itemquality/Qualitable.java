package com.uberverse.arkcraft.wip.itemquality;

import com.uberverse.arkcraft.util.I18n;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public interface Qualitable
{
	public static final String qualityKey = "itemquality";

	public static ItemQuality get(ItemStack stack)
	{
		if (stack.getTagCompound().hasKey(qualityKey)) return ItemQuality.get(stack.getTagCompound().getByte(qualityKey));
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
		PRIMITIVE(1, 1, EnumChatFormatting.RESET),
		RAMSCHACKLE(1.25, 1.33, EnumChatFormatting.GREEN),
		APPRENTICE(2.5, 1.67, EnumChatFormatting.BLUE),
		JOURNEYMAN(4.5, 2, EnumChatFormatting.DARK_PURPLE),
		MASTERCRAFT(7, 2.5, EnumChatFormatting.YELLOW),
		ASCENDANT(10, 3.5, EnumChatFormatting.RED);

		// TODO add other multipliers (f.e. efficiency (for harvesting speed and maybe harvest numbers)) & remove current uses of multiplierTreshold
		public final double multiplierTreshold;
		public final double resourceMultiplier;
		private static byte idCounter = 0;
		public final byte id;
		public final EnumChatFormatting color;

		private ItemQuality(double multiplierTreshold, double resourceMultiplier, EnumChatFormatting color)
		{
			this.multiplierTreshold = multiplierTreshold;
			this.resourceMultiplier = resourceMultiplier;
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
