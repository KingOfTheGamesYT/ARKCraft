package com.uberverse.arkcraft.common.item;

import java.util.List;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Vastatio
 * @author tom5454
 * @author Lewis_McReu
 */
public class ARKCraftFood extends ItemFood implements IDecayable
{
	private PotionEffect[] effects;
	public int decayTime;

	public ARKCraftFood(int healAmount, float sat, boolean fav, boolean alwaysEdible, int decayTime, PotionEffect... effects)
	{
		super(healAmount, sat, fav);
		if (alwaysEdible) setAlwaysEdible();
		this.effects = effects;
		this.decayTime = decayTime * 20;
		this.setCreativeTab(ARKCraft.tabARK);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
	{
		super.onFoodEaten(stack, worldIn, player);
		for (int i = 0; i < effects.length; i++)
			if (!worldIn.isRemote && effects[i] != null && effects[i].getPotionID() > 0) player.addPotionEffect(
					new PotionEffect(this.effects[i].getPotionID(), this.effects[i].getDuration(), this.effects[i]
							.getAmplifier(), this.effects[i].getIsAmbient(), this.effects[i].getIsShowParticles()));
	}

	public static ItemStack getSeedForBerry(ItemStack stack)
	{
		if (stack != null)
		{
			if (stack.getItem() instanceof ARKCraftSeed)
			{
				if (stack.getItem() == ARKCraftItems.amarBerrySeed) { return new ItemStack(ARKCraftItems.amarBerry); }
				if (stack.getItem() == ARKCraftItems.azulBerrySeed) { return new ItemStack(ARKCraftItems.azulBerry); }
				if (stack.getItem() == ARKCraftItems.mejoBerrySeed) { return new ItemStack(ARKCraftItems.mejoBerry); }
				if (stack.getItem() == ARKCraftItems.narcoBerrySeed) { return new ItemStack(ARKCraftItems.narcoBerry); }
				if (stack.getItem() == ARKCraftItems.tintoBerrySeed) { return new ItemStack(ARKCraftItems.tintoBerry); }
			}
		}
		return null;
	}

	// Seconds that this food will tame a Dino
	public static int getItemFeedTime(ItemStack stack)
	{
		if (stack != null)
		{
			if (stack.getItem() instanceof ARKCraftFood)
			{
				if (stack.getItem() == ARKCraftItems.meat_cooked) { return 25; }
				if (stack.getItem() == ARKCraftItems.meat_raw) { return 10; }
				if (stack.getItem() == ARKCraftItems.primemeat_cooked) { return 50; }
				if (stack.getItem() == ARKCraftItems.primemeat_raw) { return 25; }
			}
		}
		return 0;
	}

	// TODO replace! ITranquilizer
	// Seconds that this food will keep a dino unconscious
	public static int getItemTorporTime(ItemStack stack)
	{
		if (stack != null)
		{
			if (stack.getItem() instanceof ARKCraftFood)
			{
				if (stack.getItem() == ARKCraftItems.narcoBerry) { return 25; }
			}
		}
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		ItemStack s = new ItemStack(this);
		IDecayable.setDecayStart(s, ARKCraft.proxy.getWorldTime());
		subItems.add(s);
	}

	public long getDecayTime(ItemStack stack)
	{
		return decayTime;
	}

	/**
	 * allows items to add custom lines of information to the mouseover
	 * description
	 *
	 * @param tooltip
	 *            All lines to display in the Item's tooltip. This is a List of
	 *            Strings.
	 * @param advanced
	 *            Whether the setting "Advanced tooltips" is enabled
	 */
	@SuppressWarnings({ "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		// TODO localize!
		IDecayable.super.addInformation(itemStack, playerIn, tooltip, advanced);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged || (oldStack != null && newStack != null && (oldStack.getItem() != newStack.getItem()));
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		decayTick(((EntityPlayer) entityIn).inventory, itemSlot, 1, stack, worldIn);
	}
}
