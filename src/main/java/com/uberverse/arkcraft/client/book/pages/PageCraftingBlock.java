package com.uberverse.arkcraft.client.book.pages;

import com.uberverse.arkcraft.client.book.GuiInfoBook;
import com.uberverse.arkcraft.client.book.lib.Page;
import com.uberverse.arkcraft.client.book.lib.SmallFontRenderer;

import net.minecraft.block.Block;

/**
 * 
 * @author Archiving
 * This page is for crafting recipes that also have a block model.
 * 
 */
public class PageCraftingBlock extends Page {

	public Block result;
	public String title;
	public String text;
	public String recipeImage;
	
	
	@Override
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiInfoBook book) {
		if(result != null) {
			
		}
	
	}
}
