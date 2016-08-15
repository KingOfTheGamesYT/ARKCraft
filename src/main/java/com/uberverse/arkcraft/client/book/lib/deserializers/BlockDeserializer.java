package com.uberverse.arkcraft.client.book.lib.deserializers;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.uberverse.arkcraft.client.book.core.PageData;
import com.uberverse.lib.LogHelper;

import net.minecraft.block.Block;

public class BlockDeserializer implements JsonDeserializer<Block> {

	@Override
	public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		JsonObject obj = json.getAsJsonObject();
		try {
			LogHelper.info("Trying to Deserialize blocks.");
			Class<? extends Block> block = PageData.getBlock(obj.get("name").getAsString());
			return context.deserialize(json, block);
		}
		catch(JsonParseException e) {
			e.printStackTrace();
			LogHelper.error("Failed to deserialize blocks!");
		}
		
		return null;
	}
}
