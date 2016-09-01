package com.uberverse.arkcraft.rework.engram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.uberverse.arkcraft.I18n;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;
import com.uberverse.arkcraft.rework.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.rework.itemquality.Qualitable;
import com.uberverse.arkcraft.rework.itemquality.Qualitable.ItemQuality;

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
		// Player
		// lvl 1
		instance().registerEngram(new Engram("stone_pick", ARKCraftItems.stone_pick, 0, 1, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.wood, 1, ARKCraftItems.stone, 1, ARKCraftItems.thatch, 10)));

		// lvl 2
		instance().registerEngram(new Engram("campfire", Item.getItemFromBlock(ARKCraftBlocks.campfire), 3, 2, 10, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.wood, 2, ARKCraftItems.stone, 16, ARKCraftItems.thatch, 12, ARKCraftItems.flint, 1)));
		instance().registerEngram(new Engram("stone_hatchet", ARKCraftItems.stone_hatchet, 3, 2, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.wood, 1, ARKCraftItems.thatch, 10, ARKCraftItems.flint, 1)));
		instance().registerEngram(new Engram("spear", ARKCraftItems.spear, 3, 2, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.wood, 8, ARKCraftItems.fiber, 12, ARKCraftItems.flint, 2)));
		instance().registerEngram(
				new Engram("cloth_legs", ARKCraftItems.cloth_legs, 3, 2, 5, EngramType.PLAYER, new EngramRecipe(ARKCraftItems.fiber, 50)));
		instance().registerEngram(
				new Engram("cloth_chest", ARKCraftItems.cloth_chest, 3, 2, 5, EngramType.PLAYER, new EngramRecipe(ARKCraftItems.fiber, 40)));

		// lvl 3
		instance().registerEngram(new Engram("cloth_boots", ARKCraftItems.cloth_boots, 3, 3, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.fiber, 25, ARKCraftItems.hide, 6)));
		instance().registerEngram(
				new Engram("cloth_helm", ARKCraftItems.cloth_helm, 3, 3, 5, EngramType.PLAYER, new EngramRecipe(ARKCraftItems.fiber, 10)));

		// lvl 5
		instance().registerEngram(new Engram("slingshot", ARKCraftRangedWeapons.slingshot, 6, 5, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.fiber, 20, ARKCraftItems.hide, 1, ARKCraftItems.wood, 5)));
		instance().registerEngram(new Engram("mortar_and_pestle", Item.getItemFromBlock(ARKCraftBlocks.pestle), 6, 5, 10, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.stone, 65, ARKCraftItems.hide, 15)));
		instance().registerEngram(new Engram("spark_powder", ARKCraftItems.spark_powder, 2, 3, 5, 1, EngramType.MORTAR_AND_PESTLE,
				new EngramRecipe(ARKCraftItems.flint, 2, ARKCraftItems.stone, 1)));
		instance().registerEngram(new Engram("narcotics", ARKCraftItems.narcotics, 6, 5, 5, EngramType.MORTAR_AND_PESTLE,
				new EngramRecipe(ARKCraftItems.narcoBerry, 5, ARKCraftItems.spoiled_meat, 1)));

		// lvl 10
		instance().registerEngram(new Engram("cementing_paste", ARKCraftItems.cementing_paste, 3, 10, 5, EngramType.MORTAR_AND_PESTLE,
				new EngramRecipe(ARKCraftItems.chitin, 4, ARKCraftItems.stone, 8),
				new EngramRecipe(ARKCraftItems.keratin, 4, ARKCraftItems.stone, 8)));
		instance().registerEngram(new Engram("gunpowder", ARKCraftItems.gunpowder, 2, 10, 1, EngramType.MORTAR_AND_PESTLE,
				new EngramRecipe(ARKCraftItems.spark_powder, 1, ARKCraftItems.chitin, 1)));
		instance().registerEngram(new Engram("spy_glass", ARKCraftItems.spy_glass, 2, 10, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.wood, 5, ARKCraftItems.hide, 10, ARKCraftItems.fiber, 10, ARKCraftItems.crystal, 2)));
		instance().registerEngram(new Engram("small_crop_plot", Item.getItemFromBlock(ARKCraftBlocks.crop_plot.getDefaultState().getBlock()), 9, 10,
				10, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.stone, 25, ARKCraftItems.wood, 20, ARKCraftItems.fiber, 15, ARKCraftItems.thatch, 10)));

		// lvl 15
		instance().registerEngram(new Engram("hide_chest", ARKCraftItems.hide_chest, 6, 15, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.hide, 20, ARKCraftItems.fiber, 8)));
		instance().registerEngram(new Engram("hide_legs", ARKCraftItems.hide_legs, 9, 15, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.hide, 25, ARKCraftItems.fiber, 8)));

		// lvl 20
		instance().registerEngram(new Engram("hide_boots", ARKCraftItems.hide_boots, 7, 20, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.hide, 20, ARKCraftItems.fiber, 8)));
		instance().registerEngram(new Engram("hide_helm", ARKCraftItems.hide_helm, 9, 20, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.hide, 25, ARKCraftItems.fiber, 8)));
		instance().registerEngram(
				new Engram("refining_forge", Item.getItemFromBlock(ARKCraftBlocks.refining_forge), 21, 20, 10, EngramType.PLAYER, new EngramRecipe(
						ARKCraftItems.stone, 125, ARKCraftItems.wood, 20, ARKCraftItems.fiber, 40, ARKCraftItems.flint, 5, ARKCraftItems.hide, 65)));

		// lvl 25
		instance().registerEngram(new Engram("smithy", Item.getItemFromBlock(ARKCraftBlocks.smithy), 16, 25, 10, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.stone, 50, ARKCraftItems.wood, 30, ARKCraftItems.metal_ingot, 5, ARKCraftItems.hide, 20)));

		instance().registerEngram(new Engram("metal_pick", ARKCraftItems.metal_pick, 6, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal_ingot, 1, ARKCraftItems.wood, 1, ARKCraftItems.hide, 10)));
		instance().registerEngram(new Engram("metal_hatchet", ARKCraftItems.metal_hatchet, 6, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal_ingot, 8, ARKCraftItems.wood, 1, ARKCraftItems.hide, 10)));
		instance().registerEngram(new Engram("pike", ARKCraftItems.pike, 10, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal_ingot, 10, ARKCraftItems.wood, 10, ARKCraftItems.hide, 20)));
		instance().registerEngram(new Engram("fur_boots", ARKCraftItems.fur_boots, 12, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.pelt, 48, ARKCraftItems.metal_ingot, 8, ARKCraftItems.hide, 6, ARKCraftItems.fiber, 4)));
		instance().registerEngram(new Engram("fur_helm", ARKCraftItems.fur_helm, 14, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.pelt, 56, ARKCraftItems.metal_ingot, 10, ARKCraftItems.hide, 7, ARKCraftItems.fiber, 3)));
		instance().registerEngram(new Engram("fur_legs", ARKCraftItems.fur_legs, 16, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.pelt, 96, ARKCraftItems.metal_ingot, 16, ARKCraftItems.hide, 12, ARKCraftItems.fiber, 5)));
		instance().registerEngram(new Engram("fur_chest", ARKCraftItems.fur_chest, 16, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.pelt, 80, ARKCraftItems.metal_ingot, 13, ARKCraftItems.hide, 10, ARKCraftItems.fiber, 4)));
		// instance().registerEngram(new Engram("medium_crop_plot",ItemCropPlot.getByNameOrId(("tile.crop_plot.medium")), 12, 25, 10, EngramType.PLAYER,
		// new EngramRecipe(ARKCraftItems.stone, 50, ARKCraftItems.wood, 40, ARKCraftItems.fiber, 30, ARKCraftItems.thatch, 20)));

		// lvl 30
		instance().registerEngram(new Engram("chitin_legs", ARKCraftItems.chitin_legs, 15, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.chitin, 25, ARKCraftItems.hide, 12, ARKCraftItems.fiber, 5)));
		instance().registerEngram(new Engram("chitin_chest", ARKCraftItems.chitin_chest, 18, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.chitin, 20, ARKCraftItems.hide, 10, ARKCraftItems.fiber, 4)));
		instance().registerEngram(new Engram("chitin_helm", ARKCraftItems.chitin_helm, 18, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.chitin, 15, ARKCraftItems.hide, 7, ARKCraftItems.fiber, 3)));
		instance().registerEngram(new Engram("simple_pistol", ARKCraftRangedWeapons.simple_pistol, 15, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal, 60, ARKCraftItems.hide, 15, ARKCraftItems.wood, 5)));
		instance().registerEngram(new Engram("simple_bullet", ARKCraftRangedWeapons.simple_bullet, 6, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal, 1, ARKCraftItems.gunpowder, 6)));
		instance().registerEngram(new Engram("scope", ARKCraftRangedWeapons.scope, 13, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal, 40, ARKCraftItems.stone, 5, ARKCraftItems.crystal, 20)));
		instance().registerEngram(new Engram("sickle", ARKCraftItems.metal_sickle, 12, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal, 18, ARKCraftItems.wood, 4, ARKCraftItems.hide, 16)));

		// lvl 35
		instance().registerEngram(new Engram("chitin_boots", ARKCraftItems.chitin_boots, 15, 35, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.chitin, 12, ARKCraftItems.hide, 6, ARKCraftItems.fiber, 4)));
		instance().registerEngram(new Engram("longneck_rifle", ARKCraftRangedWeapons.longneck_rifle, 18, 35, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal, 95, ARKCraftItems.hide, 25, ARKCraftItems.wood, 20)));
		instance().registerEngram(new Engram("simple_rifle_ammo", ARKCraftRangedWeapons.simple_rifle_ammo, 6, 35, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal, 2, ARKCraftItems.gunpowder, 12)));
		// instance().registerEngram(new Engram("medium_crop_plot",ItemCropPlot.getByNameOrId(("tile.crop_plot.medium")), 12, 25, 10, EngramType.PLAYER,
		// new EngramRecipe(ARKCraftItems.stone, 50, ARKCraftItems.wood, 40, ARKCraftItems.fiber, 30, ARKCraftItems.thatch, 20)));
		instance().registerEngram(new Engram("shotgun", ARKCraftRangedWeapons.shotgun, 18, 35, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal, 80, ARKCraftItems.hide, 25, ARKCraftItems.wood, 20)));
		instance().registerEngram(new Engram("simple_shotgun_ammo", ARKCraftRangedWeapons.simple_shotgun_ammo, 6, 35, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal, 1, ARKCraftItems.gunpowder, 3, ARKCraftRangedWeapons.simple_bullet, 3)));

		// lvl 40
		// instance().registerEngram(new Engram("silencer",ARKCraftRangedWeapons.silencer, 13, 40, 10, EngramType.SMITHY,
		// new EngramRecipe(ARKCraftItems.metal, 50, ARKCraftItems.stone, 5, ARKCraftItems.crystal, 20)));
	}

	private Map<String, Engram> engramMap;
	private Set<Engram> engrams;

	private EngramManager()
	{
		engrams = new TreeSet<>();
		engramMap = new HashMap<>();
	}

	public Set<Engram> getEngrams()
	{
		return engrams;
	}

	public boolean registerEngram(Engram engram)
	{
		if (engrams.add(engram))
		{
			engramMap.put(engram.name, engram);
			return true;
		}
		return false;
	}

	public Engram getEngram(short id)
	{
		if (id >= 0) return (Engram) engrams.toArray()[id];
		return null;
	}

	public Engram getEngram(String name)
	{
		return engramMap.get(name);
	}

	public List<Engram> getUnlockedEngrams(EntityPlayer player)
	{
		final Collection<Short> col = ARKPlayer.get(player).getUnlockedEngrams();
		final List<Engram> out = new ArrayList<>();
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

	public List<Engram> getUnlockedEngramsOfType(EntityPlayer player, final EngramType type)
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

	public boolean canPlayerLearn(EntityPlayer player, short engramId)
	{
		Engram e = getEngram(engramId);
		ARKPlayer p = ARKPlayer.get(player);
		return e != null && !p.getUnlockedEngrams().contains(engramId) && p.getLevel() >= e.getLevel() && p.getEngramPoints() >= e.getPoints();
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
			this.craftingTime = craftingTime * 20;
			this.type = type;
			for (EngramRecipe r : recipes)
				addRecipe(r);
		}

		public Engram(String name, Item item, int points, int level, int craftingTime, EngramType type, EngramRecipe... recipes)
		{
			this(name, item, 1, points, level, craftingTime, type, recipes);
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

		public String getTitle()
		{
			return I18n.translate("engram." + getName() + ".title");
		}

		public String getDescription()
		{
			return I18n.translate("engram." + getName() + ".description");
		}

		public boolean isQualitable()
		{
			return getItem() instanceof Qualitable;
		}

		public boolean canCraft(IInventory inventory)
		{
			return canCraft(inventory, null);
		}

		public boolean canCraft(IInventory inventory, int amount)
		{
			return canCraft(inventory, amount, null);
		}

		public boolean canCraft(IInventory inventory, ItemQuality quality)
		{
			return canCraft(inventory, 1, quality);
		}

		// TODO implement ItemQuality modifiers (also checks to see if something can have quality
		public boolean canCraft(IInventory inventory, int amount, ItemQuality quality)
		{
			Map<Item, Integer> map = convertIInventoryToMap(inventory);
			while (amount > 0)
			{
				boolean found = false;
				for (EngramRecipe r : this.recipes)
				{
					if (r.canCraft(map))
					{
						r.consume(map);
						amount--;
						found = true;
						if (amount <= 0) return true;
					}
				}
				if (!found) return false;
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

		public Collection<EngramRecipe> getRecipes()
		{
			return recipes;
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
					return;
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
		
		public Map<Item, Integer> getItems()
		{
			return items;
		}

		void consume(IInventory inv)
		{
			boolean[] consumed = new boolean[items.size()];
			int[] yetFound = new int[items.size()];
			Arrays.fill(consumed, false);
			Arrays.fill(yetFound, 0);

			Item[] items = this.items.keySet().toArray(new Item[0]);
			Integer[] required = this.items.values().toArray(new Integer[0]);

			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack s = inv.getStackInSlot(i);
				if (s != null)
				{
					for (int j = 0; j < items.length; j++)
					{
						if (s.getItem() == items[j])
						{
							if (!consumed[j])
							{
								int stillRequired = required[j] - yetFound[j];
								if (s.stackSize > stillRequired)
								{
									s.stackSize -= stillRequired;
									required[j] = 0;
								}
								else
								{
									required[j] -= s.stackSize;
									inv.setInventorySlotContents(i, null);
								}
							}
							break;
						}
					}
				}
			}
		}

		public void consume(Map<Item, Integer> map)
		{
			// changed items reference to map : correct ? TODO
			for (Item i : map.keySet())
			{
				map.put(i, map.getOrDefault(i, 0) - items.get(i));
			}
		}

		public boolean canCraft(Map<Item, Integer> map)
		{
			for (Item i : items.keySet())
			{
				int required = items.get(i);
				int available = map.getOrDefault(i, 0);
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
					out = r.items.containsKey(e.getKey()) ? r.items.get(e.getKey()) == e.getValue() : false;
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
