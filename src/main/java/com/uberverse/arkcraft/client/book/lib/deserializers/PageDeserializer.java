package com.uberverse.arkcraft.client.book.lib.deserializers;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.uberverse.arkcraft.client.book.core.PageData;
import com.uberverse.arkcraft.client.book.lib.Page;
import com.uberverse.lib.LogHelper;

public class PageDeserializer implements JsonDeserializer<Page>
{

	@Override
	public Page deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		JsonObject obj = json.getAsJsonObject();
		try
		{
			LogHelper.info("Trying to deserialize Pages.");
			Class<? extends Page> page = PageData.getBookPage(obj.get("type").getAsString());
			return context.deserialize(obj, page);
		}
		catch (JsonParseException e)
		{
			e.printStackTrace();
			LogHelper.error("Failed to deserialize pages!");
		}
		return null;
	}

}
