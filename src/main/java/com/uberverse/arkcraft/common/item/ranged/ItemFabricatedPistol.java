package com.uberverse.arkcraft.common.item.ranged;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.item.attachments.Flashable;
import com.uberverse.arkcraft.common.item.attachments.HoloScopeable;
import com.uberverse.arkcraft.common.item.attachments.Laserable;
import com.uberverse.arkcraft.common.item.attachments.Scopeable;
import com.uberverse.arkcraft.common.item.attachments.Silenceable;

public class ItemFabricatedPistol extends ItemRangedWeapon implements Silenceable, Laserable, Scopeable, HoloScopeable,
Flashable
{

	public ItemFabricatedPistol()
	{
		super("fabricated_pistol", 350, 13, "advanced_bullet", 1, 0.2, 6F, 1.4F, 12, 80, 2.5F, 5F, true);
	}

	@Override
	public int getReloadDuration()
	{
		return (int) (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL_RELOAD * 20.0);
	}
	
	@Override
	public void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer, World world)
	{
		float f = entityplayer.isSneaking() ? -0.01F : -0.02F;
		double d = -MathHelper.sin((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F)
				* 3.141593F) * f;
		double d1 = MathHelper.cos((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F)
				* 3.141593F) * f;
		entityplayer.rotationPitch -= entityplayer.isSneaking() ? 2.5F : 5F;
		entityplayer.addVelocity(d, 0, d1);
		//	long i = entityplayer.getServer().getCurrentTimeMillis();

	}
	static int i = 0;

	/*
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		//System.out.println(i);
		if (entityIn instanceof EntityPlayer)
		{
			if(wasfired())
			{
				i++;
				if(i >= 20)
				{
					float f = entityIn.isSneaking() ? -0.01F : -0.02F;
					double d = -MathHelper.sin((entityIn.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F)
							* 3.141593F) * f;
					double d1 = MathHelper.cos((entityIn.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F)
							* 3.141593F) * f;
					entityIn.rotationPitch += entityIn.isSneaking() ? 2.5F : 5F;
					entityIn.addVelocity(d, 0, d1);
					i = 0;
					setfired(false);
				}
			}
		} 
	}*/
}
