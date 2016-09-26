package com.uberverse.arkcraft.common.burner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.CollectionUtil;

import net.minecraft.item.Item;

/**
 * @author Lewis_McReu
 */
public class BurnerManager
{
	private static BurnerManager instance = new BurnerManager();

	public static BurnerManager instance()
	{
		return instance;
	}

	public static void init()
	{
		// Fuels
		instance.registerFuel(
				new BurnerFuel(ARKCraftItems.wood, 600, ARKCraftItems.charcoal,
						BurnerType.CAMPFIRE, BurnerType.REFINING_FORGE));
		instance.registerFuel(new BurnerFuel(ARKCraftItems.thatch, 140,
				BurnerType.CAMPFIRE, BurnerType.REFINING_FORGE));
		instance.registerFuel(new BurnerFuel(ARKCraftItems.spark_powder, 1200,
				BurnerType.CAMPFIRE, BurnerType.REFINING_FORGE));

		// Recipes
		// TODO add more recipes and patch times of the existing recipes
		instance.registerRecipe(new BurnerRecipe(ARKCraftItems.metal_ingot, 1,
				400, BurnerType.REFINING_FORGE, ARKCraftItems.metal, 2));
		instance.registerRecipe(new BurnerRecipe(ARKCraftItems.gasoline, 5,
				600, BurnerType.REFINING_FORGE, ARKCraftItems.oil, 3, ARKCraftItems.hide, 5));
		instance.registerRecipe(new BurnerRecipe(ARKCraftItems.meat_cooked, 1,
				400, BurnerType.CAMPFIRE, ARKCraftItems.meat_raw, 1));
		instance.registerRecipe(new BurnerRecipe(ARKCraftItems.primemeat_cooked,
				1, 400, BurnerType.CAMPFIRE, ARKCraftItems.primemeat_raw, 1));
	}

	private Set<BurnerRecipe> recipes;
	private Set<BurnerFuel> fuels;

	private BurnerManager()
	{
		recipes = new TreeSet<>();
		fuels = new HashSet<>();
	}

	public Set<BurnerRecipe> getEngrams()
	{
		return recipes;
	}

	public boolean registerRecipe(BurnerRecipe recipe)
	{
		return recipes.add(recipe);
	}

	public boolean registerFuel(BurnerFuel fuel)
	{
		return fuels.add(fuel);
	}

	public Collection<BurnerRecipe> getRecipes()
	{
		return recipes;
	}

	public Collection<BurnerRecipe> getRecipes(BurnerType type)
	{
		return CollectionUtil.filter(recipes,
				(BurnerRecipe r) -> r.type == type);
	}

	public Collection<BurnerFuel> getFuels()
	{
		return fuels;
	}

	public Collection<BurnerFuel> getFuels(BurnerType type)
	{
		return CollectionUtil.filter(fuels,
				(BurnerFuel f) -> f.isValidBurner(type));
	}

	public boolean isValidFuel(Item item, BurnerType type)
	{
		return !CollectionUtil.filter(fuels,
				(BurnerFuel f) -> f.item == item && f.isValidBurner(type))
				.isEmpty();
	}

	public BurnerFuel getFuel(Item item)
	{
		return CollectionUtil.find(fuels, (BurnerFuel f) -> f.item, item);
	}

	public static class BurnerFuel
	{
		private final Item item;
		private final int burnTime;
		private final Item output;
		private final int outputAmount;
		private final Collection<BurnerType> supportedBurnerTypes;

		public BurnerFuel(Item item, int burnTime, Item output, int outputAmount, BurnerType... supportedBurnerTypes)
		{
			this.item = item;
			this.burnTime = burnTime;
			this.output = output;
			this.outputAmount = outputAmount;
			this.supportedBurnerTypes = Arrays.asList(supportedBurnerTypes);
		}

		public BurnerFuel(Item item, int burnTime, Item output, BurnerType... supportedBurnerTypes)
		{
			this(item, burnTime, output, 1, supportedBurnerTypes);
		}

		public BurnerFuel(Item item, int burnTime, BurnerType... supportedBurnerTypes)
		{
			this(item, burnTime, null, 0, supportedBurnerTypes);
		}

		public int getBurnTime()
		{
			return burnTime;
		}

		public boolean hasOutput()
		{
			return output != null;
		}

		public Item getOutput()
		{
			return output;
		}

		public int getOutputAmount()
		{
			return outputAmount;
		}

		public boolean isValidBurner(BurnerType type)
		{
			return supportedBurnerTypes.contains(type);
		}
	}

	public static class BurnerRecipe implements Comparable<BurnerRecipe>
	{
		private static short idCounter = 0;
		private final short id;
		private final Item item;
		/**
		 * craftingTime is a base value to possibly be adjusted by the
		 * machine/player actually doing the crafting
		 */
		private final int amount, craftingTime;
		private final BurnerType type;
		private final Map<Item, Integer> items;

		public BurnerRecipe(Item item, int amount, int craftingTime, BurnerType type, Object... objects)
		{
			id = idCounter++;
			this.item = item;
			this.amount = amount;
			this.craftingTime = craftingTime;
			this.type = type;
			this.items = new HashMap<>();
			if (objects.length % 2 == 0)
			{
				for (int i = 0; i < objects.length; i += 2)
				{
					Object o1 = objects[i];
					Object o2 = objects[i + 1];
					if (o1 instanceof Item && o2 instanceof Integer)
					{
						Item it = (Item) o1;
						Integer am = (Integer) o2;
						this.addItem(it, am);
					}
				}
			}
		}

		private void addItem(Item i, int amount)
		{
			if (!this.items.containsKey(i)) this.items.put(i, amount);
			else this.items.put(i, items.get(i) + amount);
		}

		/**
		 * @return the id
		 */
		public short getId()
		{
			return id;
		}

		/**
		 * @return the item
		 */
		public Item getItem()
		{
			return item;
		}

		/**
		 * @return the amount
		 */
		public int getAmount()
		{
			return amount;
		}

		/**
		 * @return the craftingTime
		 */
		public int getCraftingTime()
		{
			return craftingTime;
		}

		/**
		 * @return the type
		 */
		public BurnerType getType()
		{
			return type;
		}

		/**
		 * @return the items
		 */
		public Map<Item, Integer> getItems()
		{
			return items;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof BurnerRecipe)
				return ((BurnerRecipe) obj).item == item;
			return false;
		}

		@Override
		public int compareTo(BurnerRecipe o)
		{
			return id - o.id;
		}
	}

	public static enum BurnerType
	{
		REFINING_FORGE, CAMPFIRE;
	}
}
