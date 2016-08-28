package com.uberverse.arkcraft.rework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.uberverse.arkcraft.common.entity.data.ARKPlayer;
import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EngramManager
{
	private static EngramManager instance = new EngramManager();

	public static EngramManager instance()
	{
		return instance;
	}

	public static void init()
	{
		instance().registerEngram(new Engram("stone_pick", ARKCraftItems.stone_pick, 0, 1, 10,
				EngramType.SMITHY, new EngramRecipe(ARKCraftItems.wood, 1, ARKCraftItems.stone, 1,
						ARKCraftItems.thatch, 10)));
	}

	private Set<Engram> engrams;

	private EngramManager()
	{
		engrams = new HashSet<>();
	}

	public Set<Engram> getEngrams()
	{
		return engrams;
	}

	public boolean registerEngram(Engram engram)
	{
		return engrams.add(engram);
	}

	public Engram getEngram(short id)
	{
		return (Engram) engrams.toArray()[id];
	}

	public List<Engram> getUnlockedEngrams(EntityPlayer player)
	{
		Collection<Short> col = ARKPlayer.get(player).learnedEngrams();
		List<Engram> out = new ArrayList<>();
		engrams.forEach(new Consumer<Engram>()
		{
			@Override
			public void accept(Engram t)
			{
				if (col.contains(t.id)) out.add(t);
			}
		});

		return out;
	}

	public List<Engram> getUnlockedEngramsOfType(EntityPlayer player, EngramType type)
	{
		List<Engram> in = getUnlockedEngrams(player);
		in.removeIf(new Predicate<Engram>()
		{
			@Override
			public boolean test(Engram t)
			{
				return t.type != type;
			}
		});
		return in;
	}

	public static class Engram implements Comparable<Engram>
	{
		private static short idCounter = 0;
		private short id;
		private String name;
		private Item item;
		private int amount, points, level, craftingTime;
		private EngramType type;
		private Collection<EngramRecipe> recipes;

		private Engram()
		{
			this.recipes = new HashSet<>();
			this.id = idCounter++;
		}

		public Engram(String name, Item item, int amount, int points, int level, int craftingTime, EngramType type, EngramRecipe... recipes)
		{
			this();
			this.name = name;
			this.item = item;
			this.amount = amount;
			this.points = points;
			this.level = level;
			this.craftingTime = craftingTime;
			this.type = type;
			for (EngramRecipe r : recipes)
				addRecipe(r);
		}

		public Engram(String name, Item item, int points, int level, int craftingTime, EngramType type, EngramRecipe... recipes)
		{
			this(name, item, 1, points, craftingTime, level, type, recipes);
		}

		public short getId()
		{
			return id;
		}

		public Item getItem()
		{
			return item;
		}

		public int getAmount()
		{
			return amount;
		}

		public String getName()
		{
			return name;
		}

		public int getPoints()
		{
			return points;
		}

		public int getLevel()
		{
			return level;
		}

		public int getCraftingTime()
		{
			return craftingTime;
		}

		public boolean canCraft(IInventory inventory)
		{
			return canCraft(inventory, 1);
		}

		public boolean canCraft(IInventory inventory, int amount)
		{
			Map<Item, Integer> map = convertIInventoryToMap(inventory);
			while (amount > 0)
			{
				for (EngramRecipe r : this.recipes)
				{
					if (r.canCraft(map))
					{
						r.consume(map);
						amount--;
						if (amount <= 0) return true;
					}
				}
			}
			return false;
		}

		public int getCraftableAmount(IInventory inventory)
		{
			Map<Item, Integer> map = convertIInventoryToMap(inventory);
			int amount = 0;
			while (amount > 0)
			{
				for (EngramRecipe r : this.recipes)
				{
					if (r.canCraft(map))
					{
						r.consume(map);
						amount++;
					}
				}
			}
			return amount;
		}

		public EngramType getType()
		{
			return type;
		}

		private boolean addRecipe(EngramRecipe recipe)
		{
			return recipes.add(recipe);
		}

		@Override
		public boolean equals(Object obj)
		{
			return obj instanceof Engram ? ((Engram) obj).name.equals(name) : false;
		}

		@Override
		public int compareTo(Engram o)
		{
			return id - o.id;
		}

		public static Map<Item, Integer> convertIInventoryToMap(IInventory inv)
		{
			Map<Item, Integer> out = new HashMap<>();
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack s = inv.getStackInSlot(i);
				if (s != null)
				{
					if (!out.containsKey(s.getItem()))
					{
						out.put(s.getItem(), s.stackSize);
						continue;
					}
					out.put(s.getItem(), out.get(s.getItem()) + s.stackSize);
				}
			}

			return out;
		}

		public void consume(IInventory inv)
		{
			Map<Item, Integer> map = convertIInventoryToMap(inv);
			for (EngramRecipe recipe : recipes)
			{
				if (recipe.canCraft(map))
				{
					recipe.consume(inv);
					break;
				}
			}
		}
	}

	public static class EngramRecipe
	{
		private Map<Item, Integer> items;

		private EngramRecipe()
		{
			this.items = new HashMap<>();
		}

		void consume(IInventory inv)
		{
			boolean[] consumed = new boolean[items.size()];
			int[] yetFound = new int[items.size()];
			Arrays.fill(consumed, false);
			Arrays.fill(yetFound, 0);

			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack s = inv.getStackInSlot(i);
				if (s != null)
				{
					for (int j = 0; j < items.size(); j++)
					{
						Entry<Item, Integer> e = (Entry<Item, Integer>) items.entrySet()
								.toArray()[j];
						if (s.getItem() == e.getKey())
						{
							if (!consumed[j])
							{
								if (s.stackSize > e.getValue() - yetFound[j]) s.stackSize -= e
										.getValue() - yetFound[j];
								else
								{
									inv.setInventorySlotContents(i, null);
								}
							}
							break;
						}
					}
				}
			}
		}

		public EngramRecipe(Object... objects)
		{
			this();
			if (objects.length % 2 == 0)
			{
				for (int i = 0; i < objects.length; i += 2)
				{
					Object o1 = objects[i];
					Object o2 = objects[i + 1];
					if (o1 instanceof Item && o2 instanceof Integer)
					{
						Item item = (Item) o1;
						Integer amount = (Integer) o2;
						this.addItem(item, amount);
					}
				}
			}
		}

		public void consume(Map<Item, Integer> map)
		{
			for (Item i : items.keySet())
			{
				items.put(i, map.getOrDefault(i, 0) - items.get(i));
			}
		}

		public boolean canCraft(Map<Item, Integer> map)
		{
			for (Item i : items.keySet())
			{
				int required = items.get(i);
				int available = items.getOrDefault(i, 0);
				if (required > available) return false;
			}
			return true;
		}

		private void addItem(Item i, int amount)
		{
			if (!this.items.containsKey(i)) this.items.put(i, amount);
			else this.items.put(i, items.get(i) + amount);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof EngramRecipe)
			{
				EngramRecipe r = (EngramRecipe) obj;
				boolean out = true;
				for (Entry<Item, Integer> e : items.entrySet())
				{
					out = r.items.containsKey(
							e.getKey()) ? r.items.get(e.getKey()) == e.getValue() : false;
					if (!out) return out;
				}
				return out;
			}
			return false;
		}
	}

	public enum EngramType
	{
		PLAYER, SMITHY, MORTAR_AND_PESTLE, FABRICATOR;
	}
}
