package com.uberverse.arkcraft.client.book.lib;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.book.core.BookDocument;
import com.uberverse.arkcraft.client.book.lib.deserializers.PageDeserializer;
import com.uberverse.lib.LogHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BookParser {

	//This is different from BookClient.readManual(), since this is more general (it only registers a page deserializer)
	public static BookDocument parseJSON(GsonBuilder gBuilder, String dest) {
		gBuilder = new GsonBuilder();
		gBuilder.registerTypeAdapter(IPage.class, new PageDeserializer());
		Gson target = gBuilder.create();
		ResourceLocation loc = new ResourceLocation(ARKCraft.MODID, dest);
		try {
			LogHelper.info("Trying to find JSON file......");
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
			Reader reader = new InputStreamReader(stream, "UTF-8");
			/* This line tries to deserialize the whole JSON file into a BookDocument object. */
			BookDocument doc = target.fromJson(reader, BookDocument.class);
			return doc;
		}
		catch(Exception e) {
			LogHelper.error("Failed to parse the JSON!");
			e.printStackTrace();
			
		}
		
		//If unsuccessful, return null.
		return null;
	}
	
}
