/**
 * 
 */
package com.uberverse.arkcraft.common.entity.data;

import com.uberverse.arkcraft.common.config.WeightsConfig;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

/**
 * @author ERBF
 *
 */

public class CalcPlayerWeight
{

	public static double getAsDouble(EntityPlayer player)
	{
		ItemStack stack;
		double weight = 0;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++)
		{
			if (player.inventory.getStackInSlot(i) != null)
			{
				stack = player.inventory.getStackInSlot(i);
				weight += WeightsConfig.getConfig()
						.get(Configuration.CATEGORY_GENERAL,
								stack.getItem().getUnlocalizedName().substring(5,
										stack.getItem().getUnlocalizedName().length()),
								0.5)
						.getDouble() * stack.stackSize;
			}
		}
		return weight;
	}

	public static double getWeight(ItemStack stack)
	{
		return WeightsConfig.getConfig()
				.get(Configuration.CATEGORY_GENERAL, stack.getItem().getUnlocalizedName()
						.substring(5, stack.getItem().getUnlocalizedName().length()), 0.5)
				.getDouble();
	}

}
