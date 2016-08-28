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
		for (int i = 0; i < 50; i++)
			instance().registerEngram(new Engram("stone_pick" + i, ARKCraftItems.stone_pick, 0, 1,
					10, EngramType.SMITHY, new EngramRecipe(ARKCraftItems.wood, 1,
							ARKCraftItems.stone, 1, ARKCraftItems.thatch, 10)));
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
			for (EngramRecipe r : this.recipes)
			{
				if (r.canCraft(inventory)) return true;
			}
			return false;
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
	}

	public static class EngramRecipe
	{
		private Map<Item, Integer> items;

		private EngramRecipe()
		{
			this.items = new HashMap<>();
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

		public boolean canCraft(IInventory inv)
		{
			boolean[] b = new boolean[items.size()];
			int[] c = new int[items.size()];
			Arrays.fill(b, false);
			Arrays.fill(c, 0);
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
							if (!b[j])
							{
								if (s.stackSize < e.getValue() - c[j]) c[j] += s.stackSize;
								else b[j] = true;
							}
							break;
						}
					}
				}
			}
			for (boolean d : b)
			{
				if (!d) return false;
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
