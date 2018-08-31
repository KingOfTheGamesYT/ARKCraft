package com.arkcraft.common.item.ammo;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.entity.projectile.EntityArkArrow;
import com.arkcraft.common.entity.projectile.EntityProjectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemArrow extends Item
{
	public ItemArrow()
	{
		this.setCreativeTab(ARKCraft.tabARK);
	}

    public EntityProjectile createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
        EntityArkArrow entitytippedarrow = new EntityArkArrow(worldIn, shooter);
        entitytippedarrow.setPotionEffect(stack);
        return entitytippedarrow;
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.EntityPlayer player)
    {
        int enchant = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, bow);
        return enchant <= 0 ? false : this.getClass() == ItemArrow.class;
    }
}
