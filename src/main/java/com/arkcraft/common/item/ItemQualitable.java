package com.arkcraft.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public abstract class ItemQualitable extends Item implements Qualitable {
	protected final ItemType itemType;
	private final int baseDurability;

	public ItemQualitable(int baseDurability, ItemType type) {
		super();
		this.baseDurability = baseDurability;
		this.itemType = type;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		ItemQuality q = Qualitable.get(stack);
		return (q != null ? q.toFormattedString() : "") + " " + super.getItemStackDisplayName(stack);
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		ItemQuality q = Qualitable.get(stack);
		return (int) (baseDurability * (q != null ? q.durabilityMultiplier : 0));
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		ItemStack stack = new ItemStack(this);
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		Qualitable.set(stack, ItemQuality.PRIMITIVE);
		items.add(stack);
		if (tab == null) {
			stack = new ItemStack(this);
			Qualitable.set(stack, ItemQuality.RAMSHACKLE);
			items.add(stack);
			stack = new ItemStack(this);
			Qualitable.set(stack, ItemQuality.APPRENTICE);
			items.add(stack);
			stack = new ItemStack(this);
			Qualitable.set(stack, ItemQuality.JOURNEYMAN);
			items.add(stack);
			stack = new ItemStack(this);
			Qualitable.set(stack, ItemQuality.MASTERCRAFT);
			items.add(stack);
			stack = new ItemStack(this);
			Qualitable.set(stack, ItemQuality.ASCENDANT);
			items.add(stack);
		}
	}

	protected enum ItemType {
		TOOL, WEAPON, SADDLE;
	}
}
