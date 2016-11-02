package com.uberverse.arkcraft.common.entity;

import com.uberverse.arkcraft.common.data.WeaponDamageSource;
import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySpear extends EntityProjectile
{
	public EntitySpear(World world)
	{
		super(world);
	}

	public EntitySpear(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
	}

	public EntitySpear(World world, EntityLivingBase entityliving, float speed)
	{
		super(world, entityliving, speed);
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

	@Override
	public ItemStack getPickupItem()
	{
		return new ItemStack(ARKCraftItems.spear, 1);
	}
	
	@Override
	public void gunRange()
	{		
	}
}