package com.uberverse.arkcraft.common.item;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ARKCraftFeces extends Item 
{
//	public int decayTime;
	
	public ARKCraftFeces()
	{
		this.setMaxStackSize(1);
		this.setCreativeTab(ARKCraft.tabARK);
//		this.decayTime = decayTime;
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
	/*
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
	public int getMaxDecayTime(ItemStack stack){
	//	return decayTime;
	//}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		tooltip.add("Decomposes in " + ((getMaxDamage() - itemStack.getItemDamage()) / 20) + " seconds");
	}
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged || (oldStack != null && newStack != null && (oldStack.getItem() != newStack.getItem()));
	}

	@Override
	public void decayTick(IInventory inventory, int itemSlot, double decayModifier, ItemStack stack) {
		if(stack.getMetadata() > (getMaxDecayTime(stack) * 20)){
			inventory.setInventorySlotContents(itemSlot, null);
		}else{
			stack.setItemDamage(MathHelper.floor_double(stack.getMetadata() + (20 * decayModifier)));
		}
	} */
}
