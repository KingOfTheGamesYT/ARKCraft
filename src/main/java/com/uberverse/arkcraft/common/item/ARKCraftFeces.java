package com.uberverse.arkcraft.common.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ARKCraftFeces extends Item
{

	public ARKCraftFeces()
	{
		this.setMaxStackSize(1);
	}

	// seconds that this fertilizer will grow a crop
	public static int getItemGrowTime(ItemStack itemStack)
	{
		return itemStack.getMaxDamage() - itemStack.getItemDamage();
	}

	/**
	 * allows items to add custom lines of information to the mouseover
	 * description
	 *
	 * @param tooltip
	 *            All lines to display in the Item's tooltip. This is a List of
	 *            Strings.
	 * @param advanced
	 *            Whether the setting "Advanced tooltips" is enabled
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		tooltip.add("Decomposes in " + ((getMaxDamage() - itemStack.getItemDamage()) / 20) + " seconds");
	}
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(stack.getMetadata() > getMaxDamage(stack)){
			entityIn.getInventory()[itemSlot] = null;
		}else{
			stack.setItemDamage(stack.getMetadata()+1);
		}
	}
}
