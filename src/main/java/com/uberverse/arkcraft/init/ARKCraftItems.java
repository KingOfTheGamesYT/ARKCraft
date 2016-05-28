package com.uberverse.arkcraft.init;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.item.ARKCraftItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKCraftItems
{
	public static ARKCraftItem crystal;

	public static void init()
	{
		crystal = new ARKCraftItem("crystal");

		register(crystal);
	}

	private static void register(Item item)
	{
		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
	}

	public static void registerRenderers()
	{
		registerItemRenderer(crystal);
	}

	public static void registerItemRenderer(Item item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
				new ModelResourceLocation(
						ARKCraft.MODID + ":" + item.getUnlocalizedName().substring(5),
						"inventory"));
	}
}