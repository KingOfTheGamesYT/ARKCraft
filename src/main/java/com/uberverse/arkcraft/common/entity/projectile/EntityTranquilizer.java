package com.uberverse.arkcraft.common.entity.projectile;

import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.ITranquilizer;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityTranquilizer extends EntityProjectile1 implements ITranquilizer
{
	public EntityTranquilizer(World world)
	{
		super(world);
	}

	public EntityTranquilizer(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
	}

	public EntityTranquilizer(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy, double damage, int range)
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
	public int getTorpor()
	{
		return ModuleItemBalance.WEAPONS.TRANQ_AMMO_TORPOR_TIME;
	}

	@Override
	protected ItemStack getArrowStack() {
		return null;
	}
}