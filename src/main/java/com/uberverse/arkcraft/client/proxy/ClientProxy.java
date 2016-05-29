package com.uberverse.arkcraft.client.proxy;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void init()
	{
		registerRenderers();
	}
	
	private void registerRenderers()
	{
		for (Entry<String, Item> i : ARKCraftItems.allItems.entrySet())
		{
			registerItemTexture(i.getValue(), 0, i.getKey());
		}	
		for (Map.Entry<String, Block> e : ARKCraftBlocks.allBlocks.entrySet())
		{
			String name = e.getKey();
			Block b = e.getValue();
			registerBlockTexture(b, name);
		}
	}
	
	public void registerBlockTexture(final Block block, final String blockName)
	{
		registerBlockTexture(block, 0, blockName);
	}
	
	public void registerBlockTexture(final Block block, int meta, final String blockName)
	{
		registerItemTexture(Item.getItemFromBlock(block), meta, blockName);
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
			//	"inventory"));;
	}
}