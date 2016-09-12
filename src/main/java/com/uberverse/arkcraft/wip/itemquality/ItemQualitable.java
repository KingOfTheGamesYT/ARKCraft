package com.uberverse.arkcraft.wip.itemquality;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ItemQualitable extends Item implements Qualitable
{
	private final int baseDurability;
	protected final ItemType itemType;

	public ItemQualitable(int baseDurability, ItemType type)
	{
		super();
		this.baseDurability = baseDurability;
		this.itemType = type;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return Qualitable.get(stack).toFormattedString() + super.getItemStackDisplayName(stack);
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return (int) (baseDurability + Qualitable.get(stack).multiplierTreshold);
	}

	public double getDurability(ItemStack stack)
	{
		return Qualitable.get(stack).multiplierTreshold * itemType.durabilityModifier * baseDurability;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		ItemStack stack = new ItemStack(item);
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		Qualitable.set(stack, ItemQuality.PRIMITIVE);
		itemList.add(stack);
		if (tab == null)
		{
			stack = new ItemStack(item);
			Qualitable.set(stack, ItemQuality.RAMSCHACKLE);
			itemList.add(stack);
			stack = new ItemStack(item);
			Qualitable.set(stack, ItemQuality.APPRENTICE);
			itemList.add(stack);
			stack = new ItemStack(item);
			Qualitable.set(stack, ItemQuality.JOURNEYMAN);
			itemList.add(stack);
			stack = new ItemStack(item);
			Qualitable.set(stack, ItemQuality.MASTERCRAFT);
			itemList.add(stack);
			stack = new ItemStack(item);
			Qualitable.set(stack, ItemQuality.ASCENDANT);
			itemList.add(stack);
		}
	}

	protected enum ItemType
	{
		TOOL(1.5), WEAPON(1), SADDLE(1.5);

		public final double durabilityModifier;

		private ItemType(double durabilityModifier)
		{
			this.durabilityModifier = durabilityModifier;
		}
	}
}
