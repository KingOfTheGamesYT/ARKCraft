package com.uberverse.arkcraft.common.entity.projectile;

import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
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

	public EntityStoneArrow(World worldIn, EntityLivingBase shooter, float speed)
	{
		super(worldIn, shooter, speed);
		this.setDamage(3);
	}
	
	@Override
	public ItemStack getPickupItem()
	{
		return new ItemStack(ARKCraftRangedWeapons.stone_arrow, 1);
	}
}