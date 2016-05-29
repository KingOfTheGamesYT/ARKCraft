package com.uberverse.arkcraft.client.proxy;

import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftWeapons;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void init()
	{
		registerRenderers();
	}
	
	private void registerRenderers()
	{
		ARKCraftItems.registerRenderers();
	//	ARKCraftBlocks.registerRenderers();	
		for (Entry<String, Item> i : ARKCraftWeapons.allItems.entrySet())
		{
			registerItemTexture(i.getValue(), 0, i.getKey());
		}	
	}
	
	public void registerItemTexture(final Item item, final String name)
	{
		registerItemTexture(item, 0, name);
	}

	public void registerItemTexture(final Item item, int meta, final String name)
	{
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(
						item,
						meta,
						new ModelResourceLocation(ARKCraft.MODID + ":" + name,
								"inventory"));
		ModelBakery.addVariantName(item, ARKCraft.MODID + ":" + name);
	//	ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(ARKCraft.MODID + ":" + name,
	//			"inventory"));;
	}
}