package com.uberverse.arkcraft.common.entity;

import com.uberverse.arkcraft.common.data.WeaponDamageSource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class EntityArkArrow extends EntityProjectile
{
	public EntityArkArrow(World world)
	{
		super(world);
	}

	public EntityArkArrow(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
	}

	public EntityArkArrow(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy, double damage, int range)
	{
		super(worldIn);
		this.shootingEntity = shooter;

		this.canBePickedUp = 0;
		this.damage = damage;
		this.range = range;
		this.setSize(0.05F, 0.05F);
		this.setLocationAndAngles(shooter.posX, shooter.posY + (double) shooter.getEyeHeight(), shooter.posZ,
				shooter.rotationYaw, shooter.rotationPitch);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(
				this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(
				this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		setThrowableHeading(motionX, motionY, motionZ, speed*1.2f, inaccuracy);
	}

	@Override
	public void onEntityHit(Entity entity)
	{
		float damage = 15F;
		DamageSource damagesource = null;
		if (shootingEntity == null)
		{
			damagesource = WeaponDamageSource.causeThrownDamage(this, this);
		}
		else
		{
			damagesource = WeaponDamageSource.causeThrownDamage(this, shootingEntity);
		}
		if (entity.attackEntityFrom(damagesource, damage))
		{
			if (entity instanceof EntityLivingBase && worldObj.isRemote)
			{
				((EntityLivingBase) entity).setArrowCountInEntity(((EntityLivingBase) entity).getArrowCountInEntity()
						+ 1);
			}
			applyEntityHitEffects(entity);
			playHitSound();
			setDead();
		}
		else
		{
			bounceBack();
		}
	}

	@Override
	public void onGroundHit(MovingObjectPosition movingobjectposition)
	{
		applyGroundHitEffects(movingobjectposition);
	}

	@Override
	public void playHitSound()
	{
		worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.9F));
	}

	@Override
	public float getGravity()
	{
		return 0.03F;
	}

	@Override
	public int getMaxArrowShake()
	{
		return 4;
	}
}