package com.uberverse.arkcraft.common.engram;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;
import com.uberverse.arkcraft.util.CollectionUtil;
import com.uberverse.arkcraft.util.I18n;
import com.uberverse.arkcraft.wip.itemquality.Qualitable;
import com.uberverse.arkcraft.wip.itemquality.Qualitable.ItemQuality;

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
		instance().registerEngram(new Engram("stone_pick", new AbstractItemStack(ARKCraftItems.test), 0, 1, 5, EngramType.PLAYER,
				new EngramRecipe(new AbstractItemStack(ARKCraftItems.wood, 1), new AbstractItemStack(ARKCraftItems.stone, 1),
						new AbstractItemStack(ARKCraftItems.thatch, 10))));

		// lvl 2
		instance().registerEngram(new Engram("campfire", new AbstractItemStack(Item.getItemFromBlock(ARKCraftBlocks.campfire)), 3, 2, 10,
				EngramType.PLAYER, new EngramRecipe(new AbstractItemStack(ARKCraftItems.wood, 2), new AbstractItemStack(ARKCraftItems.stone, 16),
						new AbstractItemStack(ARKCraftItems.thatch, 12), new AbstractItemStack(ARKCraftItems.flint, 1))));
		// TODO from here on replace EngramRecipe constructors with newer (and simpler) AbstractItemStack one
		instance().registerEngram(new Engram("stone_hatchet", new AbstractItemStack(ARKCraftItems.stone_hatchet), 3, 2, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.wood, 1, ARKCraftItems.thatch, 10, ARKCraftItems.flint, 1)));
		instance().registerEngram(new Engram("spear", new AbstractItemStack(ARKCraftItems.spear), 3, 2, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.wood, 8, ARKCraftItems.fiber, 12, ARKCraftItems.flint, 2)));
		instance().registerEngram(new Engram("cloth_legs", new AbstractItemStack(ARKCraftItems.cloth_legs), 3, 2, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.fiber, 50)));
		instance().registerEngram(new Engram("cloth_chest", new AbstractItemStack(ARKCraftItems.cloth_chest), 3, 2, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.fiber, 40)));

		// lvl 3
		instance().registerEngram(new Engram("cloth_boots", new AbstractItemStack(ARKCraftItems.cloth_boots), 3, 3, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.fiber, 25, ARKCraftItems.hide, 6)));
		instance().registerEngram(new Engram("cloth_helm", new AbstractItemStack(ARKCraftItems.cloth_helm), 3, 3, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.fiber, 10)));

		// lvl 5
		instance().registerEngram(new Engram("slingshot", new AbstractItemStack(ARKCraftRangedWeapons.slingshot), 6, 5, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.fiber, 20, ARKCraftItems.hide, 1, ARKCraftItems.wood, 5)));
		instance().registerEngram(new Engram("mortar_and_pestle", new AbstractItemStack(Item.getItemFromBlock(ARKCraftBlocks.pestle)), 6, 5, 10,
				EngramType.PLAYER, new EngramRecipe(ARKCraftItems.stone, 65, ARKCraftItems.hide, 15)));
		instance().registerEngram(new Engram("spark_powder", new AbstractItemStack(ARKCraftItems.spark_powder, 2), 3, 5, 1,
				EngramType.MORTAR_AND_PESTLE, new EngramRecipe(ARKCraftItems.flint, 2, ARKCraftItems.stone, 1)));
		instance().registerEngram(new Engram("narcotics", new AbstractItemStack(ARKCraftItems.narcotics), 6, 5, 5, EngramType.MORTAR_AND_PESTLE,
				new EngramRecipe(ARKCraftItems.narcoBerry, 5, ARKCraftItems.spoiled_meat, 1)));

		// lvl 10
		instance().registerEngram(new Engram("cementing_paste", new AbstractItemStack(ARKCraftItems.cementing_paste), 3, 10, 5,
				EngramType.MORTAR_AND_PESTLE, new EngramRecipe(ARKCraftItems.chitin, 4, ARKCraftItems.stone, 8),
				new EngramRecipe(ARKCraftItems.keratin, 4, ARKCraftItems.stone, 8)));
		instance().registerEngram(new Engram("gunpowder", new AbstractItemStack(ARKCraftItems.gunpowder), 2, 10, 1, EngramType.MORTAR_AND_PESTLE,
				new EngramRecipe(ARKCraftItems.spark_powder, 1, ARKCraftItems.chitin, 1)));
		instance().registerEngram(new Engram("spy_glass", new AbstractItemStack(ARKCraftItems.spy_glass), 2, 10, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.wood, 5, ARKCraftItems.hide, 10, ARKCraftItems.fiber, 10, ARKCraftItems.crystal, 2)));
		instance().registerEngram(
				new Engram("small_crop_plot", new AbstractItemStack(Item.getItemFromBlock(ARKCraftBlocks.crop_plot)), 9, 10, 10, EngramType.PLAYER,
						new EngramRecipe(ARKCraftItems.stone, 25, ARKCraftItems.wood, 20, ARKCraftItems.fiber, 15, ARKCraftItems.thatch, 10)));

		// lvl 15
		instance().registerEngram(new Engram("hide_chest", new AbstractItemStack(ARKCraftItems.hide_chest), 6, 15, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.hide, 20, ARKCraftItems.fiber, 8)));
		instance().registerEngram(new Engram("hide_legs", new AbstractItemStack(ARKCraftItems.hide_legs), 9, 15, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.hide, 25, ARKCraftItems.fiber, 8)));

		// lvl 20
		instance().registerEngram(new Engram("hide_boots", new AbstractItemStack(ARKCraftItems.hide_boots), 7, 20, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.hide, 20, ARKCraftItems.fiber, 8)));
		instance().registerEngram(new Engram("hide_helm", new AbstractItemStack(ARKCraftItems.hide_helm), 9, 20, 5, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.hide, 25, ARKCraftItems.fiber, 8)));
		instance().registerEngram(new Engram("refining_forge", new AbstractItemStack(Item.getItemFromBlock(ARKCraftBlocks.refining_forge)), 21, 20,
				10, EngramType.PLAYER, new EngramRecipe(ARKCraftItems.stone, 125, ARKCraftItems.wood, 20, ARKCraftItems.fiber, 40,
						ARKCraftItems.flint, 5, ARKCraftItems.hide, 65)));

		// lvl 25
		instance().registerEngram(
				new Engram("smithy", new AbstractItemStack(Item.getItemFromBlock(ARKCraftBlocks.smithy)), 16, 25, 10, EngramType.PLAYER,
						new EngramRecipe(ARKCraftItems.stone, 50, ARKCraftItems.wood, 30, ARKCraftItems.metal_ingot, 5, ARKCraftItems.hide, 20)));

		instance().registerEngram(new Engram("metal_pick", new AbstractItemStack(ARKCraftItems.metal_pick), 6, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal_ingot, 1, ARKCraftItems.wood, 1, ARKCraftItems.hide, 10)));
		instance().registerEngram(new Engram("metal_hatchet", new AbstractItemStack(ARKCraftItems.metal_hatchet), 6, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal_ingot, 8, ARKCraftItems.wood, 1, ARKCraftItems.hide, 10)));
		instance().registerEngram(new Engram("pike", new AbstractItemStack(ARKCraftItems.pike), 10, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal_ingot, 10, ARKCraftItems.wood, 10, ARKCraftItems.hide, 20)));
		instance().registerEngram(new Engram("fur_boots", new AbstractItemStack(ARKCraftItems.fur_boots), 12, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.pelt, 48, ARKCraftItems.metal_ingot, 8, ARKCraftItems.hide, 6, ARKCraftItems.fiber, 4)));
		instance().registerEngram(new Engram("fur_helm", new AbstractItemStack(ARKCraftItems.fur_helm), 14, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.pelt, 56, ARKCraftItems.metal_ingot, 10, ARKCraftItems.hide, 7, ARKCraftItems.fiber, 3)));
		instance().registerEngram(new Engram("fur_legs", new AbstractItemStack(ARKCraftItems.fur_legs), 16, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.pelt, 96, ARKCraftItems.metal_ingot, 16, ARKCraftItems.hide, 12, ARKCraftItems.fiber, 5)));
		instance().registerEngram(new Engram("fur_chest", new AbstractItemStack(ARKCraftItems.fur_chest), 16, 25, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.pelt, 80, ARKCraftItems.metal_ingot, 13, ARKCraftItems.hide, 10, ARKCraftItems.fiber, 4)));
		instance().registerEngram(new Engram("medium_crop_plot", new AbstractItemStack(Item.getItemFromBlock(ARKCraftBlocks.crop_plot), 1, 1), 12, 25,
				10, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.stone, 50, ARKCraftItems.wood, 40, ARKCraftItems.fiber, 30, ARKCraftItems.thatch, 20)));

		// lvl 30
		instance().registerEngram(new Engram("chitin_legs", new AbstractItemStack(ARKCraftItems.chitin_legs), 15, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.chitin, 25, ARKCraftItems.hide, 12, ARKCraftItems.fiber, 5)));
		instance().registerEngram(new Engram("chitin_chest", new AbstractItemStack(ARKCraftItems.chitin_chest), 18, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.chitin, 20, ARKCraftItems.hide, 10, ARKCraftItems.fiber, 4)));
		instance().registerEngram(new Engram("chitin_helm", new AbstractItemStack(ARKCraftItems.chitin_helm), 18, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.chitin, 15, ARKCraftItems.hide, 7, ARKCraftItems.fiber, 3)));
		instance().registerEngram(new Engram("simple_pistol", new AbstractItemStack(ARKCraftRangedWeapons.simple_pistol), 15, 30, 10,
				EngramType.SMITHY, new EngramRecipe(ARKCraftItems.metal_ingot, 60, ARKCraftItems.hide, 15, ARKCraftItems.wood, 5)));
		instance().registerEngram(new Engram("simple_bullet", new AbstractItemStack(ARKCraftRangedWeapons.simple_bullet), 6, 30, 10,
				EngramType.SMITHY, new EngramRecipe(ARKCraftItems.metal_ingot, 1, ARKCraftItems.gunpowder, 6)));
		instance().registerEngram(new Engram("scope", new AbstractItemStack(ARKCraftRangedWeapons.scope), 13, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal_ingot, 40, ARKCraftItems.stone, 5, ARKCraftItems.crystal, 20)));
		instance().registerEngram(new Engram("sickle", new AbstractItemStack(ARKCraftItems.metal_sickle), 12, 30, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal_ingot, 18, ARKCraftItems.wood, 4, ARKCraftItems.hide, 16)));

		// lvl 35
		instance().registerEngram(new Engram("chitin_boots", new AbstractItemStack(ARKCraftItems.chitin_boots), 15, 35, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.chitin, 12, ARKCraftItems.hide, 6, ARKCraftItems.fiber, 4)));
		instance().registerEngram(new Engram("longneck_rifle", new AbstractItemStack(ARKCraftRangedWeapons.longneck_rifle), 18, 35, 10,
				EngramType.SMITHY, new EngramRecipe(ARKCraftItems.metal_ingot, 95, ARKCraftItems.hide, 25, ARKCraftItems.wood, 20)));
		instance().registerEngram(new Engram("simple_rifle_ammo", new AbstractItemStack(ARKCraftRangedWeapons.simple_rifle_ammo), 6, 35, 10,
				EngramType.SMITHY, new EngramRecipe(ARKCraftItems.metal_ingot, 2, ARKCraftItems.gunpowder, 12)));
		instance().registerEngram(new Engram("large_crop_plot", new AbstractItemStack(Item.getItemFromBlock(ARKCraftBlocks.crop_plot), 1, 2), 12, 25,
				10, EngramType.PLAYER,
				new EngramRecipe(ARKCraftItems.stone, 100, ARKCraftItems.wood, 80, ARKCraftItems.fiber, 60, ARKCraftItems.thatch, 40)));
		instance().registerEngram(new Engram("shotgun", new AbstractItemStack(ARKCraftRangedWeapons.shotgun), 18, 35, 10, EngramType.SMITHY,
				new EngramRecipe(ARKCraftItems.metal_ingot, 80, ARKCraftItems.hide, 25, ARKCraftItems.wood, 20)));
		instance().registerEngram(
				new Engram("simple_shotgun_ammo", new AbstractItemStack(ARKCraftRangedWeapons.simple_shotgun_ammo), 6, 35, 10, EngramType.SMITHY,
						new EngramRecipe(ARKCraftItems.metal_ingot, 1, ARKCraftItems.gunpowder, 3, ARKCraftRangedWeapons.simple_bullet, 3)));

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
		if (id >= 0 && id < engrams.size()) return (Engram) engrams.toArray()[id];
		return null;
	}

	public Engram getEngram(String name)
	{
		return engramMap.get(name);
	}

	public List<Engram> getUnlockedEngrams(EntityPlayer player)
	{
		final Collection<Short> col = ARKPlayer.get(player).getUnlockedEngrams();
		return CollectionUtil.filter(engrams, (Engram e) -> col.contains(e.id));
	}

	public List<Engram> getUnlockedEngramsOfType(EntityPlayer player, final EngramType type)
	{
		final Collection<Short> ue = ARKPlayer.get(player).getUnlockedEngrams();
		return CollectionUtil.filter(engrams, (Engram e) -> e.type == type && ue.contains(e.id));
	}

	public boolean canPlayerLearn(EntityPlayer player, short engramId)
	{
		Engram e = getEngram(engramId);
		ARKPlayer p = ARKPlayer.get(player);
		return e != null && !p.getUnlockedEngrams().contains(engramId) && p.getLevel() >= e.getLevel() && p.getEngramPoints() >= e.getPoints();
	}

	public Collection<Engram> getBlueprintEngrams()
	{
		return CollectionUtil.filter(getEngrams(), (Engram e) -> e.hasBlueprint);
	}

	public Collection<Engram> getDefaultEngrams()
	{
		return CollectionUtil.filter(getEngrams(), (Engram e) -> e.defaultUnlocked);
	}

	public static class Engram implements Comparable<Engram>
	{
		private static short idCounter = 0;
		private final short id;
		private final String name;
		private final AbstractItemStack output;
		private final int points, level, craftingTime;
		private final EngramType type;
		private final boolean hasBlueprint;
		private final boolean defaultUnlocked;
		private final Collection<EngramRecipe> recipes;

		public Engram(String name, AbstractItemStack output, int points, int level, int craftingTime, EngramType type, boolean hasBlueprint, boolean defaultUnlocked, EngramRecipe... recipes)
		{
			this.recipes = new TreeSet<>();
			this.id = idCounter++;
			this.name = name;
			this.output = output;
			this.points = points;
			this.level = level;
			this.craftingTime = craftingTime * 20;
			this.type = type;
			this.hasBlueprint = hasBlueprint;
			this.defaultUnlocked = defaultUnlocked;
			for (EngramRecipe r : recipes)
			{
				addRecipe(r);
				r.getResourceMultiplier = (ItemQuality q) -> isQualitable() ? q.resourceMultiplier : 1;
			}
		}

		public Engram(String name, AbstractItemStack output, int points, int level, int craftingTime, EngramType type, boolean hasBlueprint, EngramRecipe... recipes)
		{
			this(name, output, points, level, craftingTime, type, hasBlueprint, false, recipes);
		}

		public Engram(String name, AbstractItemStack output, int points, int level, int craftingTime, EngramType type, EngramRecipe... recipes)
		{
			this(name, output, points, level, craftingTime, type, true, false, recipes);
		}

		public boolean hasBlueprint()
		{
			return hasBlueprint;
		}

		public boolean isDefaultUnlocked()
		{
			return defaultUnlocked;
		}

		public short getId()
		{
			return id;
		}

		public AbstractItemStack getOutput()
		{
			return output;
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
			return output.item instanceof Qualitable;
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

		public boolean canCraft(IInventory inventory, int amount, ItemQuality quality)
		{
			Collection<AbstractItemStack> is = convertIInventoryToAbstractInventory(inventory);
			while (amount > 0)
			{
				boolean found = false;
				for (EngramRecipe r : this.recipes)
				{
					if (r.canCraft(is, quality))
					{
						r.consume(is, quality);
						amount--;
						found = true;
						if (amount <= 0) return true;
					}
				}
				if (!found) return false;
			}
			return false;
		}

		public int getCraftableAmount(Collection<AbstractItemStack> inv, ItemQuality quality)
		{
			int amount = 0;
			for (EngramRecipe r : this.recipes)
			{
				while (r.canCraft(inv, quality))
				{
					r.consume(inv, quality);
					amount++;
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

		public static Collection<AbstractItemStack> convertIInventoryToAbstractInventory(IInventory inv)
		{
			Collection<AbstractItemStack> out = Lists.newArrayList();
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack s = inv.getStackInSlot(i);
				if (s != null)
				{
					List<AbstractItemStack> matched =
							CollectionUtil.filter(out, (AbstractItemStack ais) -> ais.item == s.getItem() && ais.meta == s.getMetadata());
					if (!matched.isEmpty()) matched.get(0).amount += s.stackSize;
					else out.add(new AbstractItemStack(s.getItem(), s.stackSize, s.getMetadata()));
				}
			}
			return out;
		}

		public void consume(IInventory inv, ItemQuality quality)
		{
			Collection<AbstractItemStack> is = convertIInventoryToAbstractInventory(inv);
			for (EngramRecipe recipe : recipes)
			{
				if (recipe.canCraft(is, quality))
				{
					recipe.consume(inv, quality);
					return;
				}
			}
		}

		void consume(Collection<AbstractItemStack> inv, ItemQuality quality)
		{
			for (EngramRecipe r : recipes)
			{
				if (r.canCraft(inv, quality))
				{
					r.consume(inv, quality);
					return;
				}
			}
		}
	}

	public static class EngramRecipe implements Comparable<EngramRecipe>
	{
		private final Set<AbstractItemStack> items;
		private Function<ItemQuality, Double> getResourceMultiplier;

		private EngramRecipe()
		{
			this.items = new TreeSet<>();
		}

		public EngramRecipe(AbstractItemStack... stacks)
		{
			this();
			for (AbstractItemStack ais : stacks)
			{
				items.add(ais);
			}
		}

		public EngramRecipe(Object... objects) // TODO stop using this in favour of above
		{
			this();
			for (int i = 0; i < objects.length;)
			{
				Object o1 = objects[i];
				Object o2 = objects[i + 1];
				if (o1 instanceof Item && o2 instanceof Integer)
				{
					Item item = (Item) o1;
					Integer amount = (Integer) o2;

					if (i + 2 < objects.length)
					{
						Object o3 = objects[i + 2];
						if (o3 instanceof Integer)
						{
							Integer meta = (Integer) o3;
							addItem(item, amount, meta);
							i += 3;
							continue;
						}
					}
					this.addItem(item, amount);
					i += 2;
				}
				else
				{
					throw new IllegalArgumentException(
							"Invalid parameters for EngramRecipe. Pairs of Item and Integer or triplets of Item, Integer and Integer are expected.");
				}
			}
		}

		public Collection<AbstractItemStack> getItems()
		{
			return items;
		}

		private double getResourceMultiplier(ItemQuality quality)
		{
			return getResourceMultiplier.apply(quality);
		}

		void consume(IInventory inv, ItemQuality quality)
		{
			boolean[] consumed = new boolean[items.size()];
			int[] yetFound = new int[items.size()];
			Arrays.fill(consumed, false);
			Arrays.fill(yetFound, 0);

			Item[] items = CollectionUtil.convert(this.items, (AbstractItemStack i) -> i.item).toArray(new Item[0]);
			Integer[] required = CollectionUtil.convert(this.items, (AbstractItemStack i) -> (int) (i.amount * getResourceMultiplier(quality)))
					.toArray(new Integer[0]);
			Integer[] meta = CollectionUtil.convert(this.items, (AbstractItemStack i) -> i.meta).toArray(new Integer[0]);

			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack s = inv.getStackInSlot(i);
				if (s != null)
				{
					for (int j = 0; j < items.length; j++)
					{
						if (s.getItem() == items[j] && s.getMetadata() == meta[j])
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

		public void consume(Collection<AbstractItemStack> is, ItemQuality quality)
		{
			for (AbstractItemStack i : items)
				for (AbstractItemStack j : is)
					if (i.matches(j))
					{
						j.amount -= (i.amount * getResourceMultiplier(quality));
						break;
					}
		}

		public boolean canCraft(Collection<AbstractItemStack> is, ItemQuality quality)
		{
			for (AbstractItemStack i : items)
			{
				int required = (int) (i.amount * getResourceMultiplier(quality));
				List<AbstractItemStack> found = CollectionUtil.filter(is, (AbstractItemStack ais) -> ais.matches(i));
				AbstractItemStack inIs = !found.isEmpty() ? found.get(0) : null;
				int available = inIs != null ? inIs.amount : 0;
				if (required > available) return false;
			}
			return true;
		}

		private void addItem(Item i, int amount)
		{
			addItem(i, amount, 0);
		}

		private void addItem(Item i, int amount, int meta)
		{
			List<AbstractItemStack> matched = CollectionUtil.filter(items, (AbstractItemStack ais) -> ais.item == i && ais.meta == meta);
			if (!matched.isEmpty()) matched.get(0).amount += amount;
			else items.add(new AbstractItemStack(i, amount, meta));
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof EngramRecipe)
			{
				EngramRecipe r = (EngramRecipe) obj;

				if (r.items.size() == items.size())
				{
					Iterator<AbstractItemStack> it1 = items.iterator();
					Iterator<AbstractItemStack> it2 = r.items.iterator();
					while (it1.hasNext() && it2.hasNext())
						if (!it1.next().equals(it2.next())) return false;

					return true;
				}
			}
			return false;
		}

		@Override
		public int compareTo(EngramRecipe o)
		{
			if (o.items.size() != items.size()) return items.size() - o.items.size();
			else
			{
				int comp = 0;
				Iterator<AbstractItemStack> it1 = items.iterator();
				Iterator<AbstractItemStack> it2 = o.items.iterator();
				while (it1.hasNext() && it2.hasNext())
					comp = it1.next().compareTo(it2.next());
				if (comp != 0) return comp;
			}
			return 0;
		}
	}

	public enum EngramType
	{
		PLAYER, SMITHY, MORTAR_AND_PESTLE, FABRICATOR;
	}

	public static class AbstractItemStack implements Comparable<AbstractItemStack>
	{
		public final Item item;
		private int amount;
		public final int meta;

		public AbstractItemStack(Item item, int amount, int meta)
		{
			super();
			this.item = item;
			this.amount = amount;
			this.meta = meta;
		}

		public AbstractItemStack(Item item, int amount)
		{
			this(item, amount, 0);
		}

		public AbstractItemStack(Item item)
		{
			this(item, 1, 0);
		}

		public int getAmount()
		{
			return amount;
		}

		public boolean matches(AbstractItemStack i)
		{
			return i.item == item && i.meta == meta;
		}

		@Override
		public boolean equals(Object obj)
		{
			return obj instanceof AbstractItemStack ? compareTo((AbstractItemStack) obj) == 0 : false;
		}

		@Override
		public int compareTo(AbstractItemStack o)
		{
			return o.item != item ? item.getUnlocalizedName().compareTo(o.item.getUnlocalizedName()) : meta - o.meta;
		}

		public ItemStack toItemStack()
		{
			return new ItemStack(item, amount, meta);
		}

		public static AbstractItemStack fromItemStack(ItemStack i)
		{
			return new AbstractItemStack(i.getItem(), i.stackSize, i.getMetadata());
		}
	}
}
