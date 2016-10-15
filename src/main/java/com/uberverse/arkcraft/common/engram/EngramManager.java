package com.uberverse.arkcraft.common.engram;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.item.Qualitable;
import com.uberverse.arkcraft.common.item.Qualitable.ItemQuality;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;
import com.uberverse.arkcraft.util.AbstractItemStack;
import com.uberverse.arkcraft.util.CollectionUtil;
import com.uberverse.arkcraft.util.I18n;

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
		instance().registerEngram(new Engram("stone_pick", new AbstractItemStack(ARKCraftItems.stonePick), 0, 1, 5,
				EngramType.PLAYER, true, true, 0.4, new EngramRecipe(new AbstractItemStack(ARKCraftItems.wood, 1),
						new AbstractItemStack(ARKCraftItems.stone, 1), new AbstractItemStack(ARKCraftItems.thatch,
								10))));

		// lvl 2
		instance().registerEngram(new Engram("campfire", new AbstractItemStack(Item.getItemFromBlock(
				ARKCraftBlocks.campfire)), 3, 2, 10, EngramType.PLAYER, 3.5, new EngramRecipe(new AbstractItemStack(
						ARKCraftItems.wood, 2), new AbstractItemStack(ARKCraftItems.stone, 16), new AbstractItemStack(
								ARKCraftItems.thatch, 12), new AbstractItemStack(ARKCraftItems.flint, 1))));
		instance().registerEngram(new Engram("stone_hatchet", new AbstractItemStack(ARKCraftItems.stoneHatchet), 3, 2,
				5, EngramType.PLAYER, 0.4, new EngramRecipe(new AbstractItemStack(ARKCraftItems.wood, 1),
						new AbstractItemStack(ARKCraftItems.thatch, 10), new AbstractItemStack(ARKCraftItems.flint,
								1))));
		instance().registerEngram(new Engram("spear", new AbstractItemStack(ARKCraftItems.spear), 3, 2, 5,
				EngramType.PLAYER, 0.6, new EngramRecipe(new AbstractItemStack(ARKCraftItems.wood, 8),
						new AbstractItemStack(ARKCraftItems.fiber, 12), new AbstractItemStack(ARKCraftItems.flint,
								2))));
		instance().registerEngram(new Engram("cloth_legs", new AbstractItemStack(ARKCraftItems.cloth_legs), 3, 2, 5,
				EngramType.PLAYER, 1, new EngramRecipe(new AbstractItemStack(ARKCraftItems.fiber, 50))));
		instance().registerEngram(new Engram("cloth_chest", new AbstractItemStack(ARKCraftItems.cloth_chest), 3, 2, 5,
				EngramType.PLAYER, 0.8, new EngramRecipe(new AbstractItemStack(ARKCraftItems.fiber, 40))));

		// lvl 3
		instance().registerEngram(new Engram("cloth_boots", new AbstractItemStack(ARKCraftItems.cloth_boots), 3, 3, 5,
				EngramType.PLAYER, 0.8, new EngramRecipe(new AbstractItemStack(ARKCraftItems.fiber, 25),
						new AbstractItemStack(ARKCraftItems.hide, 6))));
		instance().registerEngram(new Engram("cloth_helm", new AbstractItemStack(ARKCraftItems.cloth_helm), 3, 3, 5,
				EngramType.PLAYER, 0.6, new EngramRecipe(new AbstractItemStack(ARKCraftItems.fiber, 10))));

		// lvl 5
		instance().registerEngram(new Engram("slingshot", new AbstractItemStack(ARKCraftRangedWeapons.slingshot), 6, 5,
				5, EngramType.PLAYER, 0.95, new EngramRecipe(new AbstractItemStack(ARKCraftItems.fiber, 20),
						new AbstractItemStack(ARKCraftItems.hide, 1), new AbstractItemStack(ARKCraftItems.wood, 5))));
		instance().registerEngram(new Engram("mortar_and_pestle", new AbstractItemStack(Item.getItemFromBlock(
				ARKCraftBlocks.mortarAndPestle)), 6, 5, 10, EngramType.PLAYER, 7.2, new EngramRecipe(
						new AbstractItemStack(ARKCraftItems.stone, 65), new AbstractItemStack(ARKCraftItems.hide,
								15))));
		instance().registerEngram(new Engram("spark_powder", new AbstractItemStack(ARKCraftItems.spark_powder, 2), 3, 5,
				1, EngramType.MORTAR_AND_PESTLE, 0.06, new EngramRecipe(new AbstractItemStack(ARKCraftItems.flint, 2),
						new AbstractItemStack(ARKCraftItems.stone, 1))));
		instance().registerEngram(new Engram("narcotics", new AbstractItemStack(ARKCraftItems.narcotics), 6, 5, 5,
				EngramType.MORTAR_AND_PESTLE, 2, new EngramRecipe(new AbstractItemStack(ARKCraftItems.narcoBerry, 5),
						new AbstractItemStack(ARKCraftItems.spoiled_meat, 1))));

		// lvl 10
		instance().registerEngram(new Engram("cementing_paste", new AbstractItemStack(ARKCraftItems.cementing_paste), 3,
				10, 5, EngramType.MORTAR_AND_PESTLE, 1.2, new EngramRecipe(new AbstractItemStack(ARKCraftItems.chitin,
						4), new AbstractItemStack(ARKCraftItems.stone, 8)), new EngramRecipe(new AbstractItemStack(
								ARKCraftItems.keratin, 4), new AbstractItemStack(ARKCraftItems.stone, 8))));
		instance().registerEngram(new Engram("gunpowder", new AbstractItemStack(ARKCraftItems.gunpowder), 2, 10, 1,
				EngramType.MORTAR_AND_PESTLE, 0.05, new EngramRecipe(new AbstractItemStack(ARKCraftItems.spark_powder,
						1), new AbstractItemStack(ARKCraftItems.charcoal, 1))));
		instance().registerEngram(new Engram("spy_glass", new AbstractItemStack(ARKCraftItems.spy_glass), 2, 10, 5,
				EngramType.PLAYER, 2, new EngramRecipe(new AbstractItemStack(ARKCraftItems.wood, 5),
						new AbstractItemStack(ARKCraftItems.hide, 10), new AbstractItemStack(ARKCraftItems.fiber, 10),
						new AbstractItemStack(ARKCraftItems.crystal, 2))));
		instance().registerEngram(new Engram("small_crop_plot", new AbstractItemStack(Item.getItemFromBlock(
				ARKCraftBlocks.cropPlot)), 9, 10, 10, EngramType.PLAYER, 5.3, new EngramRecipe(new AbstractItemStack(
						ARKCraftItems.stone, 25), new AbstractItemStack(ARKCraftItems.wood, 20), new AbstractItemStack(
								ARKCraftItems.fiber, 15), new AbstractItemStack(ARKCraftItems.thatch, 10))));

		// lvl 15
		instance().registerEngram(new Engram("hide_chest", new AbstractItemStack(ARKCraftItems.hide_chest), 6, 15, 5,
				EngramType.PLAYER, 1.5, new EngramRecipe(new AbstractItemStack(ARKCraftItems.hide, 20),
						new AbstractItemStack(ARKCraftItems.fiber, 8))));
		instance().registerEngram(new Engram("hide_legs", new AbstractItemStack(ARKCraftItems.hide_legs), 9, 15, 5,
				EngramType.PLAYER, 1.4, new EngramRecipe(new AbstractItemStack(ARKCraftItems.hide, 25),
						new AbstractItemStack(ARKCraftItems.fiber, 8))));
		instance().registerEngram(new Engram("compost_bin", new AbstractItemStack(Item.getItemFromBlock(
				ARKCraftBlocks.compostBin)), 6, 15, 5, EngramType.PLAYER, 3.5, new EngramRecipe(new AbstractItemStack(
						ARKCraftItems.wood, 2), new AbstractItemStack(ARKCraftItems.stone, 16), new AbstractItemStack(
								ARKCraftItems.thatch, 12), new AbstractItemStack(ARKCraftItems.flint, 1))));

		// lvl 20
		instance().registerEngram(new Engram("hide_boots", new AbstractItemStack(ARKCraftItems.hide_boots), 7, 20, 5,
				EngramType.PLAYER, 0.7, new EngramRecipe(new AbstractItemStack(ARKCraftItems.hide, 20),
						new AbstractItemStack(ARKCraftItems.fiber, 8))));
		instance().registerEngram(new Engram("hide_helm", new AbstractItemStack(ARKCraftItems.hide_helm), 9, 20, 5,
				EngramType.PLAYER, 0.8, new EngramRecipe(new AbstractItemStack(ARKCraftItems.hide, 25),
						new AbstractItemStack(ARKCraftItems.fiber, 8))));
		instance().registerEngram(new Engram("refining_forge", new AbstractItemStack(Item.getItemFromBlock(
				ARKCraftBlocks.refiningForge)), 21, 20, 10, EngramType.PLAYER, 18.6, new EngramRecipe(
						new AbstractItemStack(ARKCraftItems.stone, 125), new AbstractItemStack(ARKCraftItems.wood, 20),
						new AbstractItemStack(ARKCraftItems.fiber, 40), new AbstractItemStack(ARKCraftItems.flint, 5),
						new AbstractItemStack(ARKCraftItems.hide, 65))));

		// lvl 25
		instance().registerEngram(new Engram("smithy", new AbstractItemStack(Item.getItemFromBlock(
				ARKCraftBlocks.smithy)), 16, 25, 10, EngramType.PLAYER, 6.3, new EngramRecipe(new AbstractItemStack(
						ARKCraftItems.stone, 50), new AbstractItemStack(ARKCraftItems.wood, 30), new AbstractItemStack(
								ARKCraftItems.metal_ingot, 5), new AbstractItemStack(ARKCraftItems.hide, 20))));

		instance().registerEngram(new Engram("metal_pick", new AbstractItemStack(ARKCraftItems.metalPick), 6, 25, 10,
				EngramType.SMITHY, 1, new EngramRecipe(new AbstractItemStack(ARKCraftItems.metal_ingot),
						new AbstractItemStack(ARKCraftItems.wood), new AbstractItemStack(ARKCraftItems.hide, 10))));
		instance().registerEngram(new Engram("metal_hatchet", new AbstractItemStack(ARKCraftItems.metalHatchet), 6, 25,
				10, EngramType.SMITHY, 1.4, new EngramRecipe(new AbstractItemStack(ARKCraftItems.metal_ingot, 8),
						new AbstractItemStack(ARKCraftItems.wood), new AbstractItemStack(ARKCraftItems.hide, 10))));
		instance().registerEngram(new Engram("pike", new AbstractItemStack(ARKCraftItems.pike), 10, 25, 10,
				EngramType.SMITHY, 6, new EngramRecipe(new AbstractItemStack(ARKCraftItems.metal_ingot, 10),
						new AbstractItemStack(ARKCraftItems.wood, 10), new AbstractItemStack(ARKCraftItems.hide, 20))));
		instance().registerEngram(new Engram("fur_boots", new AbstractItemStack(ARKCraftItems.fur_boots), 12, 25, 10,
				EngramType.SMITHY, 0.7, new EngramRecipe(new AbstractItemStack(ARKCraftItems.pelt, 48),
						new AbstractItemStack(ARKCraftItems.metal_ingot, 8), new AbstractItemStack(ARKCraftItems.hide,
								6), new AbstractItemStack(ARKCraftItems.fiber, 4))));
		instance().registerEngram(new Engram("fur_helm", new AbstractItemStack(ARKCraftItems.fur_helm), 14, 25, 10,
				EngramType.SMITHY, 0.87, new EngramRecipe(new AbstractItemStack(ARKCraftItems.pelt, 56),
						new AbstractItemStack(ARKCraftItems.metal_ingot, 10), new AbstractItemStack(ARKCraftItems.hide,
								7), new AbstractItemStack(ARKCraftItems.fiber, 3))));
		instance().registerEngram(new Engram("fur_legs", new AbstractItemStack(ARKCraftItems.fur_legs), 16, 25, 10,
				EngramType.SMITHY, 1.45, new EngramRecipe(new AbstractItemStack(ARKCraftItems.pelt, 96),
						new AbstractItemStack(ARKCraftItems.metal_ingot, 16), new AbstractItemStack(ARKCraftItems.hide,
								12), new AbstractItemStack(ARKCraftItems.fiber, 5))));
		instance().registerEngram(new Engram("fur_chest", new AbstractItemStack(ARKCraftItems.fur_chest), 16, 25, 10,
				EngramType.SMITHY, 1.16, new EngramRecipe(new AbstractItemStack(ARKCraftItems.pelt, 80),
						new AbstractItemStack(ARKCraftItems.metal_ingot, 13), new AbstractItemStack(ARKCraftItems.hide,
								10), new AbstractItemStack(ARKCraftItems.fiber, 4))));
		instance().registerEngram(new Engram("medium_crop_plot", new AbstractItemStack(Item.getItemFromBlock(
				ARKCraftBlocks.cropPlot), 1, 1), 12, 25, 10, EngramType.PLAYER, 10.7, new EngramRecipe(
						new AbstractItemStack(ARKCraftItems.stone, 50), new AbstractItemStack(ARKCraftItems.wood, 40),
						new AbstractItemStack(ARKCraftItems.fiber, 30), new AbstractItemStack(ARKCraftItems.thatch,
								20))));

		// lvl 30
		instance().registerEngram(new Engram("chitin_legs", new AbstractItemStack(ARKCraftItems.chitin_legs), 15, 30,
				10, EngramType.SMITHY, 3.2, new EngramRecipe(new AbstractItemStack(ARKCraftItems.chitin, 25),
						new AbstractItemStack(ARKCraftItems.hide, 12), new AbstractItemStack(ARKCraftItems.fiber, 5))));
		instance().registerEngram(new Engram("chitin_chest", new AbstractItemStack(ARKCraftItems.chitin_chest), 18, 30,
				10, EngramType.SMITHY, 2.6, new EngramRecipe(new AbstractItemStack(ARKCraftItems.chitin, 20),
						new AbstractItemStack(ARKCraftItems.hide, 10), new AbstractItemStack(ARKCraftItems.fiber, 4))));
		instance().registerEngram(new Engram("chitin_helm", new AbstractItemStack(ARKCraftItems.chitin_helm), 18, 30,
				10, EngramType.SMITHY, 1.9, new EngramRecipe(new AbstractItemStack(ARKCraftItems.chitin, 15),
						new AbstractItemStack(ARKCraftItems.hide, 7), new AbstractItemStack(ARKCraftItems.fiber, 3))));
		instance().registerEngram(new Engram("simple_pistol", new AbstractItemStack(
				ARKCraftRangedWeapons.simple_pistol), 15, 30, 10, EngramType.SMITHY, 25.2, new EngramRecipe(
						new AbstractItemStack(ARKCraftItems.metal_ingot, 60), new AbstractItemStack(ARKCraftItems.hide,
								15), new AbstractItemStack(ARKCraftItems.wood, 5))));
		instance().registerEngram(new Engram("simple_bullet", new AbstractItemStack(
				ARKCraftRangedWeapons.simple_bullet), 6, 30, 10, EngramType.SMITHY, 0.4, new EngramRecipe(
						new AbstractItemStack(ARKCraftItems.metal_ingot, 1), new AbstractItemStack(
								ARKCraftItems.gunpowder, 6))));
		instance().registerEngram(new Engram("scope", new AbstractItemStack(ARKCraftRangedWeapons.scope), 13, 30, 10,
				EngramType.SMITHY, 2, new EngramRecipe(new AbstractItemStack(ARKCraftItems.metal_ingot, 40),
						new AbstractItemStack(ARKCraftItems.stone, 5), new AbstractItemStack(ARKCraftItems.crystal,
								20))));
		instance().registerEngram(new Engram("sickle", new AbstractItemStack(ARKCraftItems.metal_sickle), 12, 30, 10,
				EngramType.SMITHY, 1.4, new EngramRecipe(new AbstractItemStack(ARKCraftItems.metal_ingot, 18),
						new AbstractItemStack(ARKCraftItems.wood, 4), new AbstractItemStack(ARKCraftItems.hide, 16))));
		instance().registerEngram(new Engram("water_jar", new AbstractItemStack(ARKCraftItems.water_jar), 12, 30, 10,
				EngramType.SMITHY, 1.4, new EngramRecipe(new AbstractItemStack(ARKCraftItems.cementing_paste, 7),
						new AbstractItemStack(ARKCraftItems.hide, 5), new AbstractItemStack(ARKCraftItems.crystal, 2))));

		// lvl 35
		instance().registerEngram(new Engram("chitin_boots", new AbstractItemStack(ARKCraftItems.chitin_boots), 15, 35,
				10, EngramType.SMITHY, 1.6, new EngramRecipe(new AbstractItemStack(ARKCraftItems.chitin, 12),
						new AbstractItemStack(ARKCraftItems.hide, 6), new AbstractItemStack(ARKCraftItems.fiber, 4))));
		instance().registerEngram(new Engram("longneck_rifle", new AbstractItemStack(
				ARKCraftRangedWeapons.longneck_rifle), 18, 35, 10, EngramType.SMITHY, 82.5, new EngramRecipe(
						new AbstractItemStack(ARKCraftItems.metal_ingot, 95), new AbstractItemStack(ARKCraftItems.hide,
								25), new AbstractItemStack(ARKCraftItems.wood, 20))));
		instance().registerEngram(new Engram("simple_rifle_ammo", new AbstractItemStack(
				ARKCraftRangedWeapons.simple_rifle_ammo), 6, 35, 10, EngramType.SMITHY, 0.8, new EngramRecipe(
						new AbstractItemStack(ARKCraftItems.metal_ingot, 2), new AbstractItemStack(
								ARKCraftItems.gunpowder, 12))));
		instance().registerEngram(new Engram("large_crop_plot", new AbstractItemStack(Item.getItemFromBlock(
				ARKCraftBlocks.cropPlot), 1, 2), 12, 25, 10, EngramType.PLAYER, 21.4, new EngramRecipe(
						new AbstractItemStack(ARKCraftItems.stone, 100), new AbstractItemStack(ARKCraftItems.wood, 80),
						new AbstractItemStack(ARKCraftItems.fiber, 60), new AbstractItemStack(ARKCraftItems.thatch,40))));
		instance().registerEngram(new Engram("shotgun", new AbstractItemStack(ARKCraftRangedWeapons.shotgun), 18, 35,
				10, EngramType.SMITHY, 105.8, new EngramRecipe(new AbstractItemStack(ARKCraftItems.metal_ingot, 80),
						new AbstractItemStack(ARKCraftItems.hide, 25), new AbstractItemStack(ARKCraftItems.wood, 20))));
		instance().registerEngram(new Engram("simple_shotgun_ammo", new AbstractItemStack(
				ARKCraftRangedWeapons.simple_shotgun_ammo), 6, 35, 10, EngramType.SMITHY, 0.3, new EngramRecipe(
						new AbstractItemStack(ARKCraftItems.metal_ingot, 1), new AbstractItemStack(
								ARKCraftItems.gunpowder, 3), new AbstractItemStack(ARKCraftRangedWeapons.simple_bullet,3))));
		instance().registerEngram(new Engram("refertilizer", new AbstractItemStack(ARKCraftItems.refertilizer), 20, 35,
				10, EngramType.SMITHY, 4.41, new EngramRecipe(new AbstractItemStack(ARKCraftItems.rare_mushroom, 1),
						new AbstractItemStack(ARKCraftItems.rare_flower, 1), new AbstractItemStack(ARKCraftItems.spark_powder, 4),
						new AbstractItemStack(ARKCraftItems.fertilizer, 1), new AbstractItemStack(ARKCraftItems.oil, 3))));

		// lvl 40
		instance().registerEngram(new Engram("poylmer", new AbstractItemStack(ARKCraftItems.polymer), 6, 40,
				10, EngramType.FABRICATOR, 7.1, new EngramRecipe(new AbstractItemStack(ARKCraftItems.obsidian, 2),
						new AbstractItemStack(ARKCraftItems.cementing_paste, 2))));
		instance().registerEngram(new Engram("electornics", new AbstractItemStack(ARKCraftItems.electronics), 6, 40,
				10, EngramType.FABRICATOR, 7.1, new EngramRecipe(new AbstractItemStack(ARKCraftItems.silica_pearls, 3),
						new AbstractItemStack(ARKCraftItems.metal_ingot, 1))));
		
		// lvl 45
		instance().registerEngram(new Engram("flak_legs", new AbstractItemStack(ARKCraftItems.flak_legs), 15, 45,
				10, EngramType.SMITHY, 7.1, new EngramRecipe(new AbstractItemStack(ARKCraftItems.metal_ingot, 16),
						new AbstractItemStack(ARKCraftItems.hide, 12), new AbstractItemStack(ARKCraftItems.fiber, 5))));
		instance().registerEngram(new Engram("flak_chest", new AbstractItemStack(ARKCraftItems.flak_chest), 18, 45,
				10, EngramType.SMITHY, 5.78, new EngramRecipe(new AbstractItemStack(ARKCraftItems.metal_ingot, 13),
						new AbstractItemStack(ARKCraftItems.hide, 10), new AbstractItemStack(ARKCraftItems.fiber, 4))));
		// lvl 50
		instance().registerEngram(new Engram("flak_helm", new AbstractItemStack(ARKCraftItems.flak_helm), 20, 50,
				10, EngramType.SMITHY, 4.41, new EngramRecipe(new AbstractItemStack(ARKCraftItems.metal_ingot, 10),
						new AbstractItemStack(ARKCraftItems.hide, 7), new AbstractItemStack(ARKCraftItems.fiber, 3))));
		instance().registerEngram(new Engram("flak_boots", new AbstractItemStack(ARKCraftItems.flak_boots), 16, 50,
				10, EngramType.SMITHY, 3.58, new EngramRecipe(new AbstractItemStack(ARKCraftItems.metal_ingot, 8),
						new AbstractItemStack(ARKCraftItems.hide, 6), new AbstractItemStack(ARKCraftItems.fiber, 4))));
		
		// lvl 85
		instance().registerEngram(new Engram("absorbent_substrate", new AbstractItemStack(ARKCraftItems.absorbent_substrate), 60, 85,
				10, EngramType.FABRICATOR, 4.41, new EngramRecipe(new AbstractItemStack(ARKCraftItems.black_pearl, 8),
						new AbstractItemStack(ARKCraftItems.sap, 8), new AbstractItemStack(ARKCraftItems.oil, 8))));
		
		// instance().registerEngram(new
		// Engram("silencer",ARKCraftRangedWeapons.silencer, 13, 40, 10,
		// EngramType.SMITHY,
		// new EngramRecipe(ARKCraftItems.metal, 50, ARKCraftItems.stone, 5,
		// ARKCraftItems.crystal, 20)));
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
		return new TreeSet<>(engrams);
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
		return e != null && !p.getUnlockedEngrams().contains(engramId) && p.getLevel() >= e.getLevel() && p
				.getEngramPoints() >= e.getPoints();
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
		private final List<EngramRecipe> recipes;
		private final double experience;

		public Engram(String name, AbstractItemStack output, int points, int level, int craftingTime, EngramType type, boolean hasBlueprint, boolean defaultUnlocked, double experience, EngramRecipe... recipes)
		{
			this.recipes = Lists.newArrayList();
			this.id = idCounter++;
			this.name = name;
			this.output = output;
			this.points = points;
			this.level = level;
			this.craftingTime = craftingTime * 20;
			this.type = type;
			this.hasBlueprint = hasBlueprint;
			this.defaultUnlocked = defaultUnlocked;
			this.experience = experience;
			for (EngramRecipe r : recipes)
			{
				addRecipe(r);
				r.getResourceMultiplier = (ItemQuality q) -> isQualitable() ? q.resourceMultiplier : 1;
			}
			Collections.sort(this.recipes);
		}

		public Engram(String name, AbstractItemStack output, int points, int level, int craftingTime, EngramType type, boolean hasBlueprint, double experience, EngramRecipe... recipes)
		{
			this(name, output, points, level, craftingTime, type, hasBlueprint, false, experience, recipes);
		}

		public Engram(String name, AbstractItemStack output, int points, int level, int craftingTime, EngramType type, double experience, EngramRecipe... recipes)
		{
			this(name, output, points, level, craftingTime, type, true, false, experience, recipes);
		}

		public double getExperience()
		{
			return experience;
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

		public ItemStack getOutputAsItemStack()
		{
			return getOutputAsItemStack(ItemQuality.PRIMITIVE);
		}

		public ItemStack getOutputAsItemStack(ItemQuality q)
		{
			ItemStack s = getOutput().toItemStack();
			if (isQualitable()) Qualitable.set(s, q);
			return s;
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
					List<AbstractItemStack> matched = CollectionUtil.filter(out, (
							AbstractItemStack ais) -> ais.item == s.getItem() && ais.meta == s.getMetadata());
					if (!matched.isEmpty()) matched.get(0).setAmount(matched.get(0).getAmount() + s.stackSize);
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
				items.add(ais);
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
			Integer[] required = CollectionUtil.convert(this.items, (AbstractItemStack i) -> (int) (i.getAmount()
					* getResourceMultiplier(quality))).toArray(new Integer[0]);
			Integer[] meta = CollectionUtil.convert(this.items, (AbstractItemStack i) -> i.meta).toArray(
					new Integer[0]);

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
						j.setAmount(j.getAmount() - (int) (i.getAmount() * getResourceMultiplier(quality)));
						break;
					}
		}

		public boolean canCraft(Collection<AbstractItemStack> is, ItemQuality quality)
		{
			for (AbstractItemStack i : items)
			{
				int required = (int) (i.getAmount() * getResourceMultiplier(quality));
				List<AbstractItemStack> found = CollectionUtil.filter(is, (AbstractItemStack ais) -> ais.matches(i));
				AbstractItemStack inIs = !found.isEmpty() ? found.get(0) : null;
				int available = inIs != null ? inIs.getAmount() : 0;
				if (required > available) return false;
			}
			return true;
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
}
