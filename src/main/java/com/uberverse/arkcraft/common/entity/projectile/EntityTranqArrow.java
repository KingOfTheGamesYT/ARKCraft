package com.uberverse.arkcraft.common.entity.projectile;

import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.ITranquilizer;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityTranqArrow extends EntityProjectile implements ITranquilizer
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

	public EntityTranqArrow(World worldIn, EntityLivingBase shooter, float speed)
	{
		super(worldIn, shooter, speed);
		this.setDamage(1);
	}

	@Override
	public int getTorpor()
	{
		return ModuleItemBalance.WEAPONS.TRANQ_ARROW_TORPOR_TIME;
	}
	
	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ARKCraftRangedWeapons.tranq_arrow, 1);
	}
}