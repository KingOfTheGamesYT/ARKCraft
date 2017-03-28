package com.uberverse.arkcraft.common.item.ranged;

import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.item.attachments.Flashable;
import com.uberverse.arkcraft.common.item.attachments.Laserable;
import com.uberverse.arkcraft.common.item.attachments.Scopeable;
import com.uberverse.arkcraft.common.item.attachments.Silenceable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemLongneckRifle extends ItemRangedWeapon implements Scopeable, Silenceable, Laserable, Flashable
{
	public ItemLongneckRifle()
	{
		super("longneck_rifle", 350, 1, "simple_rifle_ammo", 1, 1, 7F, 0F, 16, 200, 2.5F, 5F, true);
	}

	@Override
	public int getReloadDuration()
	{
		return (int) (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE_RELOAD * 20.0);
	}
}
