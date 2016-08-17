package com.uberverse.arkcraft.client.book.pages;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.book.GuiInfoBook;
import com.uberverse.arkcraft.client.book.lib.Page;
import com.uberverse.arkcraft.client.book.lib.SmallFontRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class PageChapter extends Page {

	public String title;
	public String image;
	public String link;
	public String text;
	public int buttonID;
	
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiInfoBook book) {
		 if(title != null) {
			 if(canTranslate) StatCollector.translateToLocal(title);
			 renderer.drawString(EnumChatFormatting.UNDERLINE + title, guiLeft + (book.guiWidth - renderer.getStringWidth(title)) / 2, guiTop + 35, 0);
		 }
		 
		 if(text != null && link != null) {
			 if(canTranslate) StatCollector.translateToLocal(text);
		 }
		 
		 if(image != null) {
			 ResourceLocation imagePath = new ResourceLocation(ARKCraft.MODID, image);
	            if (imagePath != null)
	            {
	                Minecraft.getMinecraft().getTextureManager().bindTexture(imagePath);
	            }
	            book.drawTexturedModalRect(guiLeft + (book.guiWidth - 64) / 2, guiTop + 55, 0, 0, 64, 64);
		 }
		 
	}
	

    public String getTitle() { return title; }

    public String getText() { return text; }

    public String getImagePath() { return image; }
	
    public String getLink() { return link; }
    
}
