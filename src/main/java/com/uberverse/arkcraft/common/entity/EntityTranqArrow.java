package com.uberverse.arkcraft.common.entity;

import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityTranqArrow extends EntityArkArrow implements ITranquilizer
{
	public EntityTranqArrow(World worldIn)
	{
		super(worldIn);
		this.setDamage(1);
	}

	public EntityTranqArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		this.setDamage(1);
	}

	public EntityTranqArrow(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy, double damage, int range)
	{
		super(worldIn, shooter, speed, inaccuracy, damage, range);
		this.setDamage(1);
	}

	@Override
	public int getTorpor()
	{
		return ModuleItemBalance.WEAPONS.TRANQ_ARROW_TORPOR_TIME;
	}
	
	@Override
	public ItemStack getPickupItem()
	{
		return new ItemStack(ARKCraftRangedWeapons.tranq_arrow, 1);
	}
}