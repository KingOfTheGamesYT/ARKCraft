package com.uberverse.arkcraft.common.item.ranged;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.item.ammo.ItemArrow;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemWoodenBow extends ItemBow
{
	public ItemWoodenBow()
	{
		setCreativeTab(ARKCraft.tabARK);
	}

	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		net.minecraftforge.event.entity.player.ArrowNockEvent event =
				new net.minecraftforge.event.entity.player.ArrowNockEvent(playerIn, itemStackIn);
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return event.result;

		if (playerIn.capabilities.isCreativeMode)
		{
			playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		}
		else
		{
			for (int i = 0; i < playerIn.inventory.getSizeInventory(); i++)
			{
				ItemStack stack = playerIn.inventory.getStackInSlot(i);

				if (stack != null && stack.getItem() instanceof ItemArrow)
				{
					if (stack.getItem() == ARKCraftRangedWeapons.stone_arrow)
					{
						setArrowType(stack, "stone");
					}
					else if (stack.getItem() == ARKCraftRangedWeapons.metal_arrow)
					{
						setArrowType(stack, "metal");
					}
					else if (stack.getItem() == ARKCraftRangedWeapons.tranq_arrow)
					{
						setArrowType(stack, "tranq");
					}
					playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
					break;
				}
			}
		}
		return itemStackIn;
	}

	public static String getArrowType(ItemStack stack)
	{
		if (!stack.hasTagCompound()) setArrowType(stack, "none");
		return stack.getTagCompound().getString("arrowtype");
	}

	public static void setArrowType(ItemStack stack, String s)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("arrowtype", s);
	}

	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
	{
		int j = this.getMaxItemUseDuration(stack) - timeLeft;
		//// net.minecraftforge.event.entity.player.ArrowLooseEvent event =
		//// new net.minecraftforge.event.entity.player.ArrowLooseEvent(playerIn, stack, j);
		//// if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return;
		// j = event.charge;

		boolean flag = playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(
				Enchantment.infinity.effectId, stack) > 0 || !getArrowType(stack).equals("none");

		if (flag)
		{
			System.out.println(getArrowType(stack));
			float f = (float) j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if ((double) f < 0.1D) { return; }

			if (f > 1.0F)
			{
				f = 1.0F;
			}

			EntityArrow entityarrow = new EntityArrow(worldIn, playerIn, f * 2.0F);

			if (f == 1.0F)
			{
				entityarrow.setIsCritical(true);
			}

			int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);

			if (k > 0)
			{
				entityarrow.setDamage(entityarrow.getDamage() + (double) k * 0.5D + 0.5D);
			}

			int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);

			if (l > 0)
			{
				entityarrow.setKnockbackStrength(l);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
			{
				entityarrow.setFire(100);
			}

			stack.damageItem(1, playerIn);
			worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f
					* 0.5F);

			entityarrow.canBePickedUp = 2;

			if (getArrowType(stack).equals("stone"))
			{
				System.out.println("remove");
				playerIn.inventory.consumeInventoryItem(ARKCraftRangedWeapons.stone_arrow);
			}
			else if (getArrowType(stack).equals("metal"))
			{
				playerIn.inventory.consumeInventoryItem(ARKCraftRangedWeapons.metal_arrow);
			}
			else if (getArrowType(stack).equals("tranq"))
			{
				playerIn.inventory.consumeInventoryItem(ARKCraftRangedWeapons.tranq_arrow);
			}
			setArrowType(stack, "");

			playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);

			if (!worldIn.isRemote)
			{
				System.out.println("shoot");
				worldIn.spawnEntityInWorld(entityarrow);
			}
		}
	}
}
