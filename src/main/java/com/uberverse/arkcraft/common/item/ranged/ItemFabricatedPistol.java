package com.uberverse.arkcraft.common.item.ranged;

import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.item.attachments.Flashable;
import com.uberverse.arkcraft.common.item.attachments.HoloScopeable;
import com.uberverse.arkcraft.common.item.attachments.Laserable;
import com.uberverse.arkcraft.common.item.attachments.Scopeable;
import com.uberverse.arkcraft.common.item.attachments.Silenceable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

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
}
