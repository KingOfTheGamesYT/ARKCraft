package com.uberverse.arkcraft.common.entity.projectile;

import com.uberverse.arkcraft.common.entity.ITranquilizer;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author ?, Lewis_McReu
 */
public class EntityStone extends EntityThrowable implements ITranquilizer
{
	public EntityStone(World w)
	{
		super(w);
	}

	public EntityStone(World w, EntityLivingBase base)
	{
		super(w, base);
	}

	@Override
	public int getTorpor()
	{
		return 10;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		/* Damage on impact */
		float dmg = 2;
		if (result.entityHit != null)
		{
			result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), dmg);
			applyTorpor(result.entityHit);
		}

		for (int i = 0; i < 4; i++)
		{
			this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		}
		if (this.world.isRemote)
		{
			this.setDead();
		}
	}
}