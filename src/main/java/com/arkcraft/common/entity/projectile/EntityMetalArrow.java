package com.arkcraft.common.entity.projectile;

import com.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityMetalArrow extends EntityProjectile
{
	public EntityMetalArrow(World worldIn)
	{
		super(worldIn);
		this.setDamage(5);
	}

	public EntityMetalArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		this.setDamage(5);
	}

	public EntityMetalArrow(World worldIn, EntityLivingBase shooter, float speed)
	{
		super(worldIn, shooter, speed);
		this.setDamage(5);
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ARKCraftRangedWeapons.metal_arrow, 1);
	}
}