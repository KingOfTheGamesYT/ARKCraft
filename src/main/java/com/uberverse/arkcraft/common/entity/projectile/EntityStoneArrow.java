package com.uberverse.arkcraft.common.entity.projectile;

import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityStoneArrow extends EntityProjectile1
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