package com.uberverse.arkcraft.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityStoneArrow extends EntityArkArrow
{
	public EntityStoneArrow(World worldIn)
	{
		super(worldIn);
		this.setDamage(3);
	}

	public EntityStoneArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		this.setDamage(3);
	}

	public EntityStoneArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float speed, float accuracy)
	{
		super(worldIn, shooter, target, speed, accuracy);
		this.setDamage(3);
	}

	public EntityStoneArrow(World worldIn, EntityLivingBase shooter, float speed)
	{
		super(worldIn, shooter, speed);
		this.setDamage(3);
	}
}