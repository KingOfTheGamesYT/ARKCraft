package com.arkcraft.common.entity.projectile;

import com.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityStoneArrow extends EntityProjectile
{
	public EntityStoneArrow(World worldIn) 
	{
		super(worldIn);
	}

	@Override
	protected ItemStack getArrowStack() 
	{
		return new ItemStack(ARKCraftRangedWeapons.stone_arrow, 1);
	}
}