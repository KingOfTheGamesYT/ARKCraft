package com.uberverse.arkcraft.common.handlers.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.uberverse.arkcraft.common.handlers.ARKCraftingManager;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;

/**
 * @author wildbill22 Notes about adding recipes: 1) If a block has meta data:
 *         a) If not enter in ItemStack as 3rd param, it is set to 0 b) If
 *         entered, then just a stack with that meta will match c) If
 *         ARKShapelessRecipe.ANY (32767) is used, all the different meta types
 *         for the block will match 2) Do not have two recipes for the same
 *         ItemStack, only the first will be used
 */
public class PlayerCraftingManager extends ARKCraftingManager
{

	private static PlayerCraftingManager instance = null;

	public static PlayerCraftingManager getInstance()
	{
		if (instance == null)
		{
			instance = new PlayerCraftingManager();
		}
		return instance;
	}

	public PlayerCraftingManager()
	{
		super();
		instance = this;
	}

	public static void registerPlayerCraftingRecipes()
	{

		// Compost Bin
		getInstance().addShapelessRecipe(
				new ItemStack(Item.getItemFromBlock(ARKCraftBlocks.compost_bin), 1),
			//	new ItemStack(ARKCraftItems.wood, 50), new ItemStack(ARKCraftItems.thatch, 15),
				new ItemStack(ARKCraftItems.fiber, 12));

	}
}
