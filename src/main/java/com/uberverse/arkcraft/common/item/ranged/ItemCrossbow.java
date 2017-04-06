package com.uberverse.arkcraft.common.item.ranged;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import com.uberverse.arkcraft.common.config.ModuleItemBalance;

public class ItemCrossbow extends ItemRangedWeapon
{
	public ItemCrossbow()
	{
		super("crossbow", 250, 1, "stone_arrow", 1, 2, 1.5F, 2F, 4, 10, 2.5F, 5F, true, true);
	}

	@Override
	public int getReloadDuration()
	{
		return (int) (ModuleItemBalance.WEAPONS.CROSSBOW_RELOAD * 20.0);
	}

	@Override
	public void effectShoot(EntityPlayer player, ItemStack stack, World world, double x, double y, double z, float yaw, float pitch)
	{
		world.playSound(player, x, y, z, SoundEvent.REGISTRY.getObject(new ResourceLocation("random.bow")), SoundCategory.PLAYERS, 1.0F, 1.0F / (this.getItemRand().nextFloat() * 0.4F + 0.8F));
	}

	@Override
	public void effectReloadDone(ItemStack stack, World world, EntityPlayer player)
	{
		world.playSound(player, player.getPosition(), SoundEvent.REGISTRY.getObject(new ResourceLocation("random.click")), SoundCategory.PLAYERS, 0.8F, 1.0F / (this.getItemRand().nextFloat() * 0.4F + 0.4F));
	}
}
