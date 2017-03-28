package com.uberverse.arkcraft.common.item.ranged;

import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.projectile.EntityProjectile;
import com.uberverse.arkcraft.common.item.attachments.NonSupporting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemShotgun extends ItemRangedWeapon implements NonSupporting
{
	public ItemShotgun()
	{
		super("shotgun", 200, 2, "simple_shotgun_ammo", 1, 0, 6F, 15F, 14, 5, 4F, 7F, true);
	}

	@Override
	public int getReloadDuration()
	{
		return (int) (ModuleItemBalance.WEAPONS.SHOTGUN_RELOAD * 20.0);
	}

	/*
	 * @Override public void effectReloadDone(ItemStack stack, World world,
	 * EntityPlayer player) { world.playSoundAtEntity(player,
	 * "random.door_close", 0.8F, 1.0F / (this.getItemRand() .nextFloat() * 0.2F
	 * + 0.0F)); }
	 */
	/*
	 * @Override public void soundCharge(ItemStack stack, World world,
	 * EntityPlayer player) { world.playSoundAtEntity(player, ARKCraft.MODID +
	 * ":" + "shotgun_reload", 0.7F, 0.9F / (getItemRand().nextFloat() * 0.2F +
	 * 0.0F)); }
	 */

	@Override
	public void fire(ItemStack stack, World world, EntityPlayer player, int timeLeft)
	{
		if (!world.isRemote)
		{
			for (int i = 0; i < this.getAmmoConsumption() * 10; i++)
			{
				EntityProjectile projectile = createProjectile(stack, world, player);
				if (projectile != null)
				{
					applyProjectileEnchantments(projectile, stack);
					world.spawnEntity(projectile);
				}
			}
		}
		afterFire(stack, world, player);
	}
}
