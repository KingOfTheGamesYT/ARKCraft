package com.uberverse.arkcraft.deprecated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CampfireCraftingManager
{
	public static void registerCampfireRecipes()
	{
		registerCookedMeat();
		registerCookedPrimeMeat();
		registerFuels();
	}

	public static void registerFuels()
	{
		registerFuel(ARKCraftItems.wood, 600);
		registerFuel(ARKCraftItems.thatch, 140);
		registerFuel(ARKCraftItems.spark_powder, 1200);
	}

	private static void registerCookedMeat()
	{
		CampfireRecipe r = new CampfireRecipe("cooked_meat");
		r.addInputItems(ARKCraftItems.meat_raw);
		r.setOutputItem(ARKCraftItems.meat_cooked);
		r.setBurnTime(1);
		registerRecipe(r.toString(), r);
	}

	private static void registerCookedPrimeMeat()
	{
		CampfireRecipe r = new CampfireRecipe("cooked_prime_meat");
		r.addInputItems(ARKCraftItems.primemeat_raw);
		r.setOutputItem(ARKCraftItems.primemeat_cooked);
		r.setBurnTime(1);
		registerRecipe(r.toString(), r);
	}

	private static Map<String, CampfireRecipe> recipes = new HashMap<String, CampfireRecipe>();
	private static Map<Item, Integer> fuels = new HashMap<Item, Integer>();

	public static boolean registerFuel(Item i, int burnTime)
	{
		return fuels.put(i, new Integer(burnTime)) == null;
	}

	public static void registerRecipe(String id, CampfireRecipe recipe)
	{
		recipes.put(id, recipe);
	}

	public static Map<String, CampfireRecipe> getRecipes()
	{
		return recipes;
	}

	public static boolean isValidFuel(Item i)
	{
		return fuels.containsKey(i);
	}

	public static int getBurnTime(Item i)
	{
		return fuels.get(i);
	}

	public static List<CampfireRecipe> findPossibleRecipes(IInventory inv)
	{
		List<CampfireRecipe> list = new ArrayList<CampfireRecipe>();
		for (CampfireRecipe r : recipes.values())
		{
			List<Item> inputs = new ArrayList<Item>(r.getInput());
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack s = inv.getStackInSlot(i);
				if (s != null)
				{
					ItemStack stack = s.copy();
					Item item = stack.getItem();
					while (stack.stackSize > 0)
					{
						if (inputs.remove(item)) stack.stackSize--;
						else break;
					}
				}
			}
			if (inputs.size() == 0) list.add(r);
		}

		return list;
	}

	public static CampfireRecipe getCampfireRecipe(String id)
	{
		return recipes.get(id);
	}

}
