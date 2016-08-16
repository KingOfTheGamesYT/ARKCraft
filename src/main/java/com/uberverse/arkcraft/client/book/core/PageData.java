package com.uberverse.arkcraft.client.book.core;

import java.util.HashMap;

import com.uberverse.arkcraft.client.book.lib.Page;

import net.minecraft.block.Block;

/**
 * 
 * @author Archiving
 *
 */
public class PageData {

	public static HashMap<String, Class<? extends Page>> pageClasses = new HashMap<String, Class<? extends Page>>();
	public static HashMap<String, Class<? extends Block>> blockClasses = new HashMap<String, Class<? extends Block>>();
	
	public static void addBookPage(String type, Class<? extends Page> page) {
		pageClasses.put(type, page);
	}
	
	public static void addBlock(String name, Class<? extends Block> block) {
		blockClasses.put(name, block);
	}
	
	
	public static Class<? extends Page> getBookPage(String type) { return pageClasses.get(type); }
	public static Class<? extends Block> getBlock(String name) { return blockClasses.get(name); }
	
}
