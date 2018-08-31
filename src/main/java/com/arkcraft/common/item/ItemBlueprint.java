package com.arkcraft.common.item;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.engram.EngramManager;
import com.arkcraft.common.engram.EngramManager.Engram;
import com.arkcraft.common.item.Qualitable.ItemQuality;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlueprint extends ARKCraftItem {
	public ItemBlueprint() {
		super();
		hasSubtypes = true;
		setMaxStackSize(1);
		setCreativeTab(ARKCraft.tabARKBlueprints);
	}

	public static Engram getEngram(ItemStack stack) {
		if (!stack.hasTagCompound()) return null;
		short id = stack.getTagCompound().getShort("engram");
		return id < EngramManager.instance().getEngrams().size() && id >= 0 ? EngramManager.instance().getEngram(id)
				: null;
	}

	public static ItemQuality getItemQuality(ItemStack stack) {
		if (!stack.hasTagCompound()) return null;
		return ItemQuality.get(stack.getTagCompound().getByte("itemQuality"));
	}

	public static String getColor(ItemStack stack) {
		ItemQuality q = getItemQuality(stack);
		return q != null ? q.color.toString() : "";
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (Engram e : EngramManager.instance().getEngrams()) {
			if (e.hasBlueprint()) {
				ItemStack s = new ItemStack(this);
				if (!s.hasTagCompound()) s.setTagCompound(new NBTTagCompound());
				s.getTagCompound().setShort("engram", e.getId());
				if (e.isQualitable()) {
					s.getTagCompound().setByte("itemQuality", ItemQuality.PRIMITIVE.id);
				}
				items.add(s);
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return getColor(stack) + super.getItemStackDisplayName(stack);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		Engram e = getEngram(stack);
		if (e != null) {
			tooltip.add(getColor(stack) + e.getTitle());
			if (e.isQualitable()) {
				ItemQuality q = getItemQuality(stack);
				tooltip.add(q.toFormattedString());
			}
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
