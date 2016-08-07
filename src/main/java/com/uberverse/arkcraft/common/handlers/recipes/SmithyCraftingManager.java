package com.uberverse.arkcraft.common.handlers.recipes;

import net.minecraft.item.ItemStack;

import com.uberverse.arkcraft.common.handlers.ARKCraftingManager;
import com.uberverse.arkcraft.init.ARKCraftItems;

/**
 * @author wildbill22 Notes about adding recipes: 1) If a block has meta data:
 *         a) If not enter in ItemStack as 3rd param, it is set to 0 b) If
 *         entered, then just a stack with that meta will match c) If
 *         ARKShapelessRecipe.ANY (32767) is used, all the different meta types
 *         for the block will match 2) Do not have two recipes for the same
 *         ItemStack, only the first will be used
 */
public class SmithyCraftingManager extends ARKCraftingManager {

	private static SmithyCraftingManager instance = null;

	public SmithyCraftingManager() {
		super();
		instance = this;
	}

	public static SmithyCraftingManager getInstance() {
		if (instance == null) {
			instance = new SmithyCraftingManager();
		}
		return instance;
	}

	public static void registerSmithyCraftingRecipes() {
		//getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.grenade, 1), new ItemStack(Items.gunpowder, 15));
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.metal_pick, 1), new ItemStack(ARKCraftItems.metal, 45), new ItemStack(ARKCraftItems.wood, 30));
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.metal_hatchet, 1), new ItemStack(ARKCraftItems.metal, 45), new ItemStack(ARKCraftItems.wood, 30));
	}
}
