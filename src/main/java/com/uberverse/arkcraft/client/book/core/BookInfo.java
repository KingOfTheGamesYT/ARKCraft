package com.uberverse.arkcraft.client.book.core;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.book.proxy.BookClient;
import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class BookInfo
{

	public BookData bd = new BookData();

	public BookInfo()
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		bd = createBook(bd, ARKCraftItems.info_book.getUnlocalizedName(),
				EnumChatFormatting.GOLD + "Knowledge is Power",
				side == Side.CLIENT ? BookClient.document : null,
				"textures/items/info_book.png");
	}

	public BookData createBook(BookData data, String unlocalizedName,
			String tooltip, BookDocument document, String itemImage)
	{
		data.unlocalizedName = unlocalizedName;
		data.modid = ARKCraft.MODID;
		data.itemImage = new ResourceLocation(data.modid, itemImage);
		data.document = document;
		data.tooltip = tooltip;
		BookDataStore.addBookData(data);
		return data;
	}

}
