package com.uberverse.arkcraft.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

public abstract class EntityArkArrow extends EntityArrow
{
	public EntityArkArrow(World worldIn)
	{
		super(worldIn);
	}

	public EntityArkArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn);
		this.setPosition(x, y, z);
	}

	public EntityArkArrow(World worldIn, EntityLivingBase shooter, float speed)
	{
		super(worldIn, shooter, speed);
	}

	public EntityArkArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float speed, float inaccuracy)
	{
		super(worldIn, target, target, speed, inaccuracy);
	}
}
