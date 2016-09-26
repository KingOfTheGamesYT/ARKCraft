/**
 * 
 */
package com.uberverse.arkcraft.common.arkplayer;

import com.uberverse.arkcraft.common.config.WeightsConfig;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

/**
 * @author ERBF
 */

public class PlayerWeightCalculator
{
	public static double calculateWeight(EntityPlayer player)
	{
		double weight = 0;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++)
		{
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null)
			{
				weight += getWeight(stack);
			}
		}
		return weight;
	}

	public static double getWeight(ItemStack stack)
	{
		return WeightsConfig.getConfig()
				.get(Configuration.CATEGORY_GENERAL,
						stack.getItem().getUnlocalizedName().substring(5), 0.5)
				.getDouble() * stack.stackSize;
	}
}
