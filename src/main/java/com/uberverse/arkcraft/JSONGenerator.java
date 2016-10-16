package com.uberverse.arkcraft;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class JSONGenerator
{
	static PrintWriter p;

	public static void main(String[] args)
	{
		String[] strings = new String[] { "absorbent_substrate", "advanced_bullet", "amar", "amarBerrySeed",
				"angler_gel", "azul", "azulBerrySeed", "ballista_bolt", "berry_bush", "black_pearl", "blood_pack",
				"blueprint", "campfire", "cementing_paste", "charcoal", "chitin_boots", "chitin_chest", "chitin_helm",
				"chitin_legs", "chitin", "cloth_boots", "cloth_chest", "cloth_helm", "cloth_legs", "compost_bin",
				"compound_bow", "crop_plot", "crystal", "dodo_bag", "dodo_egg", "dodo_feather", "dossier",
				"electronics", "engramSpear", "explosive_ball", "fertilizer", "fiber", "flak_boots", "flak_chest",
				"flak_helm", "flak_legs", "flash_light", "flint", "fur_boots", "fur_chest", "fur_helm", "fur_legs",
				"gasoline", "grenade", "gunpowder", "hide_boots", "hide_chest", "hide_helm", "hide_legs", "hide",
				"holo_scope", "human_feces", "info_book", "ironPike", "item_crystal", "keratin", "large_feces", "laser",
				"leech_blood", "meat_cooked", "meat_raw", "medium_feces", "mejo", "mejoBerrySeed", "metal_arrow",
				"metal_ingot", "metal", "mortar_and_pestle", "narco", "narcoBerrySeed", "narcotics", "obsidian", "oil",
				"organic_polymer", "pelt", "pike", "polymer", "primemeat_cooked", "primemeat_raw", "rare_flower",
				"rare_mushroom", "refertilizer", "refining_forge", "rocket_propelled_grenade", "saddle_large",
				"saddle_medium", "saddle_small", "sap", "scope", "silencer", "silica_pearls", "simple_bullet",
				"simple_rifle_ammo", "small_feces", "smithy", "spark_powder", "spoiled_meat", "spy_glass", "stim",
				"stimBerrySeed", "stimulant", "stone_arrow", "stone", "thatch", "tinto", "tintoBerrySeed",
				"tranq_arrow", "tranquilizer", "water_jar", "waterskin", "wood", "wooden_spikes", "woolly_rhino_horn" };

		for (String name : strings)
		{
			File f = new File("src/main/resources/assets/arkcraft/models/item/" + name + ".json");
			if (f.exists()) f.delete();
			try
			{
				f.createNewFile();
				p = new PrintWriter(f);
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}

			start();
			name("parent");
			value("arkcraft:item/base_item");
			separator();
			name("textures");
			start();
			name("layer0");
			value("arkcraft:items/" + name);
			end();
			end();

			p.flush();
			p.close();
		}
	}

	private static void value(double[] ds)
	{
		p.print("[");
		for (int i = 0; i < ds.length; i++)
		{
			p.print(ds[i]);
			if (i < ds.length - 1) separator();
		}
		p.print("]");
	}

	private static void value(int[] is)
	{
		p.print("[");
		for (int i = 0; i < is.length; i++)
		{
			p.print(is[i]);
			if (i < is.length - 1) separator();
		}
		p.print("]");
	}

	public static void start()
	{
		p.print("{");
	}

	public static void end()
	{
		p.print("}");
	}

	public static void name(String name)
	{
		value(name);
		p.print(":");
	}

	public static void value(String value)
	{
		p.print("\"");
		primValue(value);
		p.print("\"");
	}

	public static void primValue(String value)
	{
		p.print(value);
	}

	public static void separator()
	{
		p.print(",");
	}
}