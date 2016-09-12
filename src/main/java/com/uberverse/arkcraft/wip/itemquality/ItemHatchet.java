package com.uberverse.arkcraft.wip.itemquality;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract class ItemHatchet extends ItemToolBase
{
	public ItemHatchet(int baseDurability, double baseBreakSpeed, double baseDamage)
	{
		super(baseDurability, baseBreakSpeed, baseDamage, ItemType.TOOL, ToolType.HATCHET);
	}
	
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
	{
		// TODO Auto-generated method stub
		return super.onEntitySwing(entityLiving, stack);
	}
}
