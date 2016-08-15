package com.uberverse.arkcraft.client.book.core;

import com.google.common.collect.HashBiMap;
import com.uberverse.lib.LogHelper;

/**
 * 
 * @author Archiving
 * Get your hot, fresh Book Data instances here! Only a method a pop!
 * 
 */
public class BookDataStore {

	private static HashBiMap<String, BookData> data = HashBiMap.create();
	
	public static void addBookData(BookData bd) {
		LogHelper.info("Adding a new BookData instance with the name: " + bd.getUnlocalizedName());
		data.put(bd.getUnlocalizedName(), bd);
	}
	
	public static BookData getBookDataFromName(String unlocalizedName) {
		LogHelper.info("Getting a BookData instance with the name: " + unlocalizedName);
		return data.get(unlocalizedName);
	}
	
}
