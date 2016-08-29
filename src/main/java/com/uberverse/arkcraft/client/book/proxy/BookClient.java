package com.uberverse.arkcraft.client.book.proxy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.book.core.BookDocument;
import com.uberverse.arkcraft.client.book.core.BookInfo;
import com.uberverse.arkcraft.client.book.core.PageData;
import com.uberverse.arkcraft.client.book.lib.Page;
import com.uberverse.arkcraft.client.book.lib.SmallFontRenderer;
import com.uberverse.arkcraft.client.book.lib.deserializers.BlockDeserializer;
import com.uberverse.arkcraft.client.book.lib.deserializers.PageDeserializer;
import com.uberverse.arkcraft.client.book.pages.PageChapter;
import com.uberverse.arkcraft.client.book.pages.PageTitle;
import com.uberverse.lib.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BookClient extends BookCommon
{

	public static Minecraft mc;
	public static SmallFontRenderer fontRenderer;
	public static BookDocument document;
	public static BookInfo bookInfo;

	public void init()
	{
		bookInfo = new BookInfo();
		mc = Minecraft.getMinecraft();
		fontRenderer = new SmallFontRenderer(mc.gameSettings,
				new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
		registerPageClasses();
		registerBlockClasses();

		GsonBuilder gBuilder = bookInfo.bd.getGsonBuilder();
		gBuilder.registerTypeAdapter(Page.class, new PageDeserializer());
		gBuilder.registerTypeAdapter(Block.class, new BlockDeserializer());
		String language = mc.getLanguageManager().getCurrentLanguage().getLanguageCode();
		BookDocument document_cl = readManual(gBuilder, "dossier/" + language + "/dossier.json");
		document = document_cl != null ? document_cl : readManual(gBuilder,
				"dossier/en_US/dossier.json");
		if (document != null) bookInfo.bd.document = document;
	}

	public BookDocument readManual(GsonBuilder gBuilder, String dest)
	{
		Gson gson = gBuilder.create();
		ResourceLocation loc = new ResourceLocation(ARKCraft.MODID, dest);
		try
		{
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(loc)
					.getInputStream();
			Reader reader = new InputStreamReader(stream, "UTF-8");
			BookDocument doc = gson.fromJson(reader, BookDocument.class);
			return doc;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void registerPageClasses()
	{
		LogHelper.info("Regsitering Page Classes.");
		PageData.addBookPage("title", PageTitle.class);
		PageData.addBookPage("chapter", PageChapter.class);
	}

	@Override
	public void registerBlockClasses()
	{
		LogHelper.info("Regsitering Block Classes.");
	}

}
