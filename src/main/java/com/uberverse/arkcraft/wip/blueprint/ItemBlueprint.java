package com.uberverse.arkcraft.wip.blueprint;

import java.util.List;

import com.uberverse.arkcraft.common.engram.EngramManager;
import com.uberverse.arkcraft.common.engram.EngramManager.Engram;
import com.uberverse.arkcraft.common.item.ARKCraftItem;
import com.uberverse.arkcraft.wip.itemquality.Qualitable.ItemQuality;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemBlueprint extends ARKCraftItem
{
	public ItemBlueprint()
	{
		super();
		hasSubtypes = true;
		setMaxStackSize(1);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		for (Engram e : EngramManager.instance().getEngrams())
		{
			ItemStack s = new ItemStack(this);
			if (!s.hasTagCompound()) s.setTagCompound(new NBTTagCompound());
			s.getTagCompound().setShort("engram", e.getId());
			if (e.isQualitable())
			{
				s.getTagCompound().setByte("itemQuality", ItemQuality.PRIMITIVE.id);
			}
			subItems.add(s);
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return getColor(stack) + super.getItemStackDisplayName(stack);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		Engram e = getEngram(stack);
		if (e != null)
		{
			tooltip.add(getColor(stack) + e.getTitle());
			if (e.isQualitable())
			{
				ItemQuality q = getItemQuality(stack);
				tooltip.add(q.toFormattedString());
			}
		}
		super.addInformation(stack, playerIn, tooltip, advanced);
	}

	public static Engram getEngram(ItemStack stack)
	{
		short id = stack.getTagCompound().getShort("engram");
		return id < EngramManager.instance().getEngrams().size() && id >= 0 ? EngramManager.instance().getEngram(id) : null;
	}

	public static ItemQuality getItemQuality(ItemStack stack)
	{
		return ItemQuality.get(stack.getTagCompound().getByte("itemQuality"));
	}

	public static String getColor(ItemStack stack)
	{
		ItemQuality q = getItemQuality(stack);
		return q != null ? q.color.toString() : "";
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
}
