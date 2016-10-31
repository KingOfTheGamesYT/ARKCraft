package com.uberverse.arkcraft.common.entity;

import com.uberverse.arkcraft.common.config.ModuleItemBalance;

import net.minecraft.entity.EntityLivingBase;
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

	public EntityTranqArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_)
	{
		super(worldIn, shooter, p_i1755_3_, p_i1755_4_, p_i1755_5_);
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
}