package com.uberverse.arkcraft.client.book.core;

import com.google.gson.GsonBuilder;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.book.lib.BookParser;
import com.uberverse.arkcraft.client.book.lib.SmallFontRenderer;

import net.minecraft.util.ResourceLocation;

/***
 * 
 * @author Archiving
 * The Data Information storage box for the book.
 * I like to think that this is the publication agency for the book.
 * It stores and gets the book ready for action.
 * 
 */

public class BookData {

	public GsonBuilder gBuilder = new GsonBuilder();
	public BookDocument document = BookParser.parseJSON(gBuilder, "dossier/en_US/dossier.json");
	public String unlocalizedName = new String();
	public String tooltip = new String();
	public String modid = new String();
	
	public ResourceLocation leftPage = new ResourceLocation(ARKCraft.MODID, "textures/gui/info_book_left.png");
	public ResourceLocation rightPage = new ResourceLocation(ARKCraft.MODID, "textures/gui/info_book_right.png");

	public ResourceLocation itemImage = new ResourceLocation(ARKCraft.MODID, "textures/items/info_book.png");
	
	public SmallFontRenderer fontRenderer;
	public Boolean canTranslate = false;
	
	public BookDocument getBookDocument() {
		return this.document;
	}
	
	public String getUnlocalizedName() { return unlocalizedName; }
	public GsonBuilder getGsonBuilder() { return gBuilder; }
}
