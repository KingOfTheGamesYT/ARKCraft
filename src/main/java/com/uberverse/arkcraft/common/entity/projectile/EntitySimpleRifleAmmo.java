package com.uberverse.arkcraft.common.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySimpleRifleAmmo extends EntityProjectile
{

	public EntitySimpleRifleAmmo(World world)
	{
		super(world);
	}

	public EntitySimpleRifleAmmo(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
	}

	public EntitySimpleRifleAmmo(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy, double damage, int range)
	{
		super(worldIn, shooter, speed, inaccuracy, damage, range);
	}

	@Override
	public float getGravity()
	{
		return 0.005F;
	}

	@Override
	public float getAirResistance()
	{
		return 0.98F;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void onGroundHit(MovingObjectPosition movingobjectposition)
	{
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		breakGlass(movingobjectposition);
		this.setDead();
	}
}