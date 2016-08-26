package com.uberverse.arkcraft.common.handlers.recipes;

import com.uberverse.arkcraft.common.handlers.ARKCraftingManager;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;

import net.minecraft.item.ItemStack;

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

		// Spy Glass
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.spy_glass, 1),
				new ItemStack(ARKCraftItems.wood, 5), new ItemStack(ARKCraftItems.hide, 10),
				new ItemStack(ARKCraftItems.fiber, 10),
				new ItemStack(ARKCraftItems.crystal, 2));

		// Scope
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftRangedWeapons.scope, 1),
				new ItemStack(ARKCraftItems.stone, 5), new ItemStack(ARKCraftItems.metal, 40),
				new ItemStack(ARKCraftItems.crystal, 20));

		// Pike
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.pike, 1),
				new ItemStack(ARKCraftItems.metal_ingot, 10), new ItemStack(ARKCraftItems.wood, 10),
				new ItemStack(ARKCraftItems.hide, 20));

		//Metal Sickle
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.metal_sickle),
				new ItemStack(ARKCraftItems.metal_ingot, 18), new ItemStack(ARKCraftItems.wood, 4),
				new ItemStack(ARKCraftItems.hide, 16));
		
		// Metal Pick
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.metal_pick, 1),
				new ItemStack(ARKCraftItems.metal_ingot, 1), new ItemStack(ARKCraftItems.wood, 1),
				new ItemStack(ARKCraftItems.hide, 10));
		// Metal Hatchet
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.metal_hatchet, 1),
				new ItemStack(ARKCraftItems.metal_ingot, 8), new ItemStack(ARKCraftItems.wood, 1),
				new ItemStack(ARKCraftItems.hide, 10));

		// Guns
		// Simple Pistol
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftRangedWeapons.simple_pistol, 1),
				new ItemStack(ARKCraftItems.metal_ingot, 60), new ItemStack(ARKCraftItems.wood, 5),
				new ItemStack(ARKCraftItems.hide, 15));

		// Longneck Rifle
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftRangedWeapons.longneck_rifle, 1),
				new ItemStack(ARKCraftItems.metal_ingot, 95), new ItemStack(ARKCraftItems.wood, 20),
				new ItemStack(ARKCraftItems.hide, 25));

		// Shotgun
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftRangedWeapons.shotgun, 1),
				new ItemStack(ARKCraftItems.metal_ingot, 80), new ItemStack(ARKCraftItems.wood, 20),
				new ItemStack(ARKCraftItems.hide, 25));

		// Crossbow
	//	getInstance().addShapelessRecipe(new ItemStack(ARKCraftRangedWeapons.crossbow, 1),
	//			new ItemStack(ARKCraftItems.metal_ingot, 7), new ItemStack(ARKCraftItems.wood, 10),
	//			new ItemStack(ARKCraftItems.fiber, 35));

		// Bullets
		// Simple Bullet
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftRangedWeapons.simple_bullet, 1),
				new ItemStack(ARKCraftItems.gunpowder, 6), new ItemStack(ARKCraftItems.metal_ingot, 1));
		// Simple Rifle Ammo
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftRangedWeapons.simple_rifle_ammo, 1),
				new ItemStack(ARKCraftItems.gunpowder, 12), new ItemStack(ARKCraftItems.metal_ingot, 2));
		// Simple Shotgun Ammo
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftRangedWeapons.simple_shotgun_ammo, 1),
				new ItemStack(ARKCraftItems.gunpowder, 3), new ItemStack(ARKCraftItems.metal_ingot, 1),
				new ItemStack(ARKCraftRangedWeapons.simple_bullet, 3));
		// Tranquilizer
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftRangedWeapons.tranquilizer, 1),
				new ItemStack(ARKCraftItems.narcotics, 3), new ItemStack(ARKCraftItems.metal_ingot, 2),
				new ItemStack(ARKCraftRangedWeapons.simple_rifle_ammo, 1));
	}
}
