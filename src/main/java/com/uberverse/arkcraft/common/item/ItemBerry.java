package com.uberverse.arkcraft.common.item;

import net.minecraft.potion.PotionEffect;

public class ItemBerry extends ARKCraftFood
{
	public ItemBerry(int healAmount, float sat, boolean fav, boolean alwaysEdible, int decayTime, PotionEffect... effects)
	{
		super(healAmount, sat, fav, alwaysEdible, decayTime, effects);
	}
}
