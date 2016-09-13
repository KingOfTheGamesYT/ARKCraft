package com.uberverse.arkcraft;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Generator
{
	static PrintWriter p;

	public static void main(String[] args)
	{
		String[] strings = new String[] { "stone_pick", "campfire", "stone_hatchet", "spear", "cloth_legs", "cloth_chest", "cloth_boots",
				"cloth_helm", "slingshot", "mortar_and_pestle", "spark_powder", "narcotics", "cementing_paste", "gunpowder", "spy_glass",
				"small_crop_plot", "hide_chest", "hide_legs", "hide_boots", "hide_helm", "refining_forge", "smithy", "metal_pick", "metal_hatchet",
				"pike", "fur_boots", "fur_helm", "fur_legs", "fur_chest", "chitin_legs", "chitin_chest", "chitin_helm", "simple_pistol",
				"simple_bullet", "scope", "sickle", "chitin_boots", "longneck_rifle", "simple_rifle_ammo", "shotgun", "simple_shotgun_ammo",
				"medium_crop_plot", "large_crop_plot" };
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
			value("builtin/generated");
			separator();
			name("textures");
			start();
			name("layer0");
			value("arkcraft:items/blueprint/" + name);
			end();
			separator();
			name("display");
			start();

			name("thirdperson");
			start();
			name("rotation");
			value(new int[] { -90, 0, 0 });
			separator();
			name("translation");
			value(new int[] { 0, 1, -3 });
			separator();
			name("scale");
			value(new double[] { 0.55, 0.55, 0.55 });
			end();
			separator();
			name("firstperson");
			start();
			name("rotation");
			value(new int[] { 0, -135, 25 });
			separator();
			name("translation");
			value(new int[] { 0, 4, 2 });
			separator();
			name("scale");
			value(new double[] { 1.7, 1.7, 1.7 });
			end();

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