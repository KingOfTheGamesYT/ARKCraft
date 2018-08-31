package com.arkcraft.common.item.ranged;

import com.arkcraft.common.config.ModuleItemBalance;
import com.arkcraft.common.item.attachments.Flashable;
import com.arkcraft.common.item.attachments.Laserable;
import com.arkcraft.common.item.attachments.Scopeable;
import com.arkcraft.common.item.attachments.Silenceable;

public class ItemSimplePistol extends ItemRangedWeapon implements Scopeable, Laserable, Flashable, Silenceable
{
	public ItemSimplePistol()
	{
		super("simple_pistol", 150, 6, "simple_bullet", 1, 1 / 2.1, 5F, 2.5F, 6, 20, 2.5F, 5F, true, false);
	}

	/*
	 * @Override public void soundCharge(ItemStack stack, World world,
	 * EntityPlayer player) { world.playSoundAtEntity(player, ARKCraft.MODID +
	 * ":" + "simple_pistol_reload", 0.7F, 0.9F / (getItemRand().nextFloat() *
	 * 0.2F + 0.0F)); }
	 */

	@Override
	public int getReloadDuration()
	{
		return (int) (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL_RELOAD * 20.0);
	}
}
