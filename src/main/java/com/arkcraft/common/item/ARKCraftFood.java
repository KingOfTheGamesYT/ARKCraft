package com.arkcraft.common.item;

import com.arkcraft.ARKCraft;
import com.arkcraft.init.ARKCraftItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Vastatio
 * @author tom5454
 * @author Lewis_McReu
 */
public class ARKCraftFood extends ItemFood implements IDecayable {
	public int decayTime;
	private PotionEffect[] effects;

	public ARKCraftFood(int healAmount, float sat, boolean fav, boolean alwaysEdible, int decayTime, PotionEffect... effects) {
		super(healAmount, sat, fav);
		if (alwaysEdible) setAlwaysEdible();
		this.effects = effects;
		this.decayTime = decayTime * 20;
		this.setCreativeTab(ARKCraft.tabARK);
	}

	public static ItemStack getSeedForBerry(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKCraftSeed) {
				if (stack.getItem() == ARKCraftItems.amarBerrySeed) {
					return new ItemStack(ARKCraftItems.amarBerry);
				}
				if (stack.getItem() == ARKCraftItems.azulBerrySeed) {
					return new ItemStack(ARKCraftItems.azulBerry);
				}
				if (stack.getItem() == ARKCraftItems.mejoBerrySeed) {
					return new ItemStack(ARKCraftItems.mejoBerry);
				}
				if (stack.getItem() == ARKCraftItems.narcoBerrySeed) {
					return new ItemStack(ARKCraftItems.narcoBerry);
				}
				if (stack.getItem() == ARKCraftItems.tintoBerrySeed) {
					return new ItemStack(ARKCraftItems.tintoBerry);
				}
			}
		}
		return null;
	}

	// Seconds that this food will tame a Dino
	public static int getItemFeedTime(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKCraftFood) {
				if (stack.getItem() == ARKCraftItems.meat_cooked) {
					return 25;
				}
				if (stack.getItem() == ARKCraftItems.meat_raw) {
					return 10;
				}
				if (stack.getItem() == ARKCraftItems.primemeat_cooked) {
					return 50;
				}
				if (stack.getItem() == ARKCraftItems.primemeat_raw) {
					return 25;
				}
			}
		}
		return 0;
	}

	// TODO replace! ITranquilizer
	// Seconds that this food will keep a dino unconscious
	public static int getItemTorporTime(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKCraftFood) {
				if (stack.getItem() == ARKCraftItems.narcoBerry) {
					return 25;
				}
			}
		}
		return 0;
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		for (int i = 0; i < effects.length; i++)
			if (!worldIn.isRemote && effects[i] != null && effects[i].getPotion() != null) player.addPotionEffect(
					new PotionEffect(this.effects[i].getPotion(), this.effects[i].getDuration(), this.effects[i]
							.getAmplifier(), this.effects[i].getIsAmbient(), this.effects[i].doesShowParticles()));
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		ItemStack s = new ItemStack(this);
		IDecayable.setDecayStart(s, ARKCraft.proxy().getWorldTime());
		items.add(s);
	}

	@Override
	public long getDecayTime(ItemStack stack) {
		return decayTime;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		IDecayable.super.addInformation(stack, tooltip);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || (oldStack != null && newStack != null && (oldStack.getItem() != newStack.getItem()));
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		decayTick(((EntityPlayer) entityIn).inventory, itemSlot, 1, stack, worldIn);
	}
}
