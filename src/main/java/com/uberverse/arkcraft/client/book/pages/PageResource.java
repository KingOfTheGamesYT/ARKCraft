package com.uberverse.arkcraft.client.book.pages;

import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.book.GuiInfoBook;
import com.uberverse.arkcraft.client.book.lib.Page;
import com.uberverse.arkcraft.client.book.lib.SmallFontRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class PageResource extends Page {

	public String displayImage;
	public String title;
	public String information;
	
	@Override
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate,
			GuiInfoBook book) {
		
		if(title != null) {
			if(canTranslate) {
				StatCollector.translateToLocal(title);
			}
			renderer.drawSplitString(ChatFormatting.BOLD + "" + ChatFormatting.UNDERLINE + title, guiLeft - 8
					+ (book.guiWidth - renderer.getStringWidth(title)) / 2, guiTop + 5, 1000, 0);
		}
		
		if(displayImage != null) {
			ResourceLocation imagePath = new ResourceLocation(ARKCraft.MODID, displayImage);
			if(imagePath != null) {
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				Minecraft.getMinecraft().getTextureManager().bindTexture(imagePath);
				book.drawTexturedModalRect(guiLeft + 5, guiTop + 20, 0, 0, 196, 128);
			}
		}
		
		if(information != null) {
			if(canTranslate) {
				StatCollector.translateToLocal(information);
			}
			renderer.drawSplitString(information, guiLeft - 36 + (renderer.splitStringWidth(information, book.guiWidth)), guiTop
					+ 40, book.guiWidth - 20, 0);
		}
	}
	
}
