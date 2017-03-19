package com.uberverse.arkcraft.common.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityAdvancedBullet extends EntityProjectile
{
	public EntityAdvancedBullet(World world)
	{
		super(world);
	}

	public EntityAdvancedBullet(World world, double d, double d1, double d2)
	{
		this(world);
		setPosition(d, d1, d2);
	}

	public EntityAdvancedBullet(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy, double damage, int range)
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

		world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void onGroundHit(RayTraceResult movingobjectposition)
	{
		world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		breakGlass(movingobjectposition);
		this.setDead();
	}

	@Override
	protected ItemStack getArrowStack() {
		// TODO Auto-generated method stub
		return null;
	}

}