package com.uberverse.arkcraft.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityRocketPropelledGrenade extends EntityProjectile
{
	public float explosionRadius = 4F;

	public EntityRocketPropelledGrenade(World world)
	{
		super(world);
	}

	public EntityRocketPropelledGrenade(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
	}

	public EntityRocketPropelledGrenade(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy, double damage, int range)
	{
		super(worldIn, shooter, speed, inaccuracy, damage, range);
	}

	@Override
	public float getGravity()
	{
		return 0.0035F;
	}

	@Override
	public float getAirResistance()
	{
		return 0.99F;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		double amount = 16D;
		for (int i1 = 1; i1 < amount; i1++)
		{
			world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (motionX * i1) / amount, posY + (motionY
					* i1) / amount, posZ + (motionZ * i1) / amount, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void onEntityHit(Entity entity)
	{
		explode();
	}

	@Override
	public void onGroundHit(RayTraceResult movingobjectposition)
	{
		explode();
	}

	private final void explode()
	{
		if (world.isRemote) return;
		this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius, true);
		this.setDead();
	}
}