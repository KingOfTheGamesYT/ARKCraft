package com.uberverse.arkcraft.rework.itemquality;

import java.util.List;

import com.uberverse.arkcraft.I18n;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public interface Qualitable
{
	public static final String qualityKey = "itemquality";

	public static ItemQuality get(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt.hasKey(qualityKey)) return ItemQuality.get(nbt.getByte(qualityKey));
		return null;
	}

	public static ItemStack set(ItemStack stack, ItemQuality q)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setByte(qualityKey, q.id);
		return stack;
	}

	public default String getItemStackDisplayName(ItemStack stack)
	{
		return get(stack).toFormattedString() + getItemStackDisplayNameAppendage(stack);
	}

	public default void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		ItemStack stack = new ItemStack(item);
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		set(stack, ItemQuality.PRIMITIVE);
		itemList.add(stack);
		if (tab == null)
		{
			stack = new ItemStack(item);
			set(stack, ItemQuality.RAMSCHACKLE);
			itemList.add(stack);
			stack = new ItemStack(item);
			set(stack, ItemQuality.APPRENTICE);
			itemList.add(stack);
			stack = new ItemStack(item);
			set(stack, ItemQuality.JOURNEYMAN);
			itemList.add(stack);
			stack = new ItemStack(item);
			set(stack, ItemQuality.MASTERCRAFT);
			itemList.add(stack);
			stack = new ItemStack(item);
			set(stack, ItemQuality.ASCENDANT);
			itemList.add(stack);
		}
	}

	public String getItemStackDisplayNameAppendage(ItemStack stack);

	public static enum ItemQuality
	{
		PRIMITIVE(0.8D, EnumChatFormatting.RESET),
		RAMSCHACKLE(1, EnumChatFormatting.GREEN),
		APPRENTICE(1.3, EnumChatFormatting.BLUE),
		JOURNEYMAN(1.5, EnumChatFormatting.DARK_PURPLE),
		MASTERCRAFT(1.7, EnumChatFormatting.YELLOW),
		ASCENDANT(2.0, EnumChatFormatting.RED);

		public final double durabilityMultiplier;
		private static byte idCounter = 0;
		public final byte id;
		private final EnumChatFormatting color;

		private ItemQuality(double durabilityMultiplier, EnumChatFormatting color)
		{
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
