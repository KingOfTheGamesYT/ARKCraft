package com.arkcraft;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class BlueprintGenerator
{
	static PrintWriter p;

	public static void main(String[] args)
	{
		String[] strings = new String[] { "stone_pick", "campfire", "stone_hatchet", "spear", "cloth_legs",
				"cloth_chest", "cloth_boots", "cloth_helm", "slingshot", "mortar_and_pestle", "spark_powder",
				"narcotics", "cementing_paste", "gunpowder", "spy_glass", "compost_bin", "small_crop_plot",
				"hide_chest", "hide_legs", "hide_boots", "hide_helm", "refining_forge", "smithy", "metal_pick",
				"metal_hatchet", "pike", "fur_boots", "fur_helm", "fur_legs", "fur_chest", "chitin_legs",
				"chitin_chest", "chitin_helm", "simple_pistol", "simple_bullet", "scope", "sickle", "chitin_boots",
				"longneck_rifle", "simple_rifle_ammo", "shotgun", "simple_shotgun_ammo", "medium_crop_plot",
				"large_crop_plot", "refertilizer", "polymer", "electronics", "absorbent_substrate", "flak_boots",
				"flak_chest", "flak_helm", "flak_legs", "advanced_bullet", "water_jar", "waterskin" };
		for (String name : strings)
		{
			File f = new File("src/main/resources/assets/arkcraft/models/item/blueprint/" + name + ".json");
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
			value("arkcraft:items/blueprint/" + name);
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