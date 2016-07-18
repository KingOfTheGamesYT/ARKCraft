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
	
	public static int getAsInt(EntityPlayer player) 
	{
		ItemStack stack;
		int weight = 0;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++)
		{
			if(player.inventory.getStackInSlot(i) != null)
			{
				stack = player.inventory.getStackInSlot(i);
				for(int j = 0; j < stack.stackSize; j++)
				{
					weight += WeightsConfig.getConfig().get(Configuration.CATEGORY_GENERAL, stack.getItem().getUnlocalizedName().substring(5, stack.getItem().getUnlocalizedName().length()), 0).getInt();
				}
			}
		}
		return weight;
	}
	
	public static int getWeight(ItemStack stack)
	{
		return WeightsConfig.getConfig().get(Configuration.CATEGORY_GENERAL, stack.getItem().getUnlocalizedName().substring(5, stack.getItem().getUnlocalizedName().length()), 0).getInt();
	}

}
