package com.uberverse.arkcraft.client.book.pages;

import org.lwjgl.opengl.GL11;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.book.GuiInfoBook;
import com.uberverse.arkcraft.client.book.lib.Page;
import com.uberverse.arkcraft.client.book.lib.SmallFontRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * @author Vastatio The title page. It includes 3 strings, a title, text, and an
 *         image path. It draws the title at guiTop+5, the text at guiTop + 105
 *         and the image at guiTop + 25.
 */
public class PageTitle extends Page
{

	public String title;
	public String text;
	public String image;
	public boolean footnote;
	
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate,
			GuiInfoBook book)
	{
		if (image != null)
		{
			ResourceLocation imagePath = new ResourceLocation(ARKCraft.MODID, image);
			if (imagePath != null)
			{
				GL11.glColor4f(1F, 1F, 1F, 1F);
				Minecraft.getMinecraft().getTextureManager().bindTexture(imagePath);
				book.drawTexturedModalRect(guiLeft + (book.guiWidth - 64) / 2, guiTop + 65, 0, 0, 64, 64);
			}

		}

		if (title != null)
		{
			if (canTranslate)
			{
				StatCollector.translateToLocal(title);
			}
			renderer.drawSplitString(EnumChatFormatting.BOLD + "" + EnumChatFormatting.UNDERLINE + title, guiLeft - 8
					+ (book.guiWidth - renderer.getStringWidth(title)) / 2, guiTop + 5, 1000, 0);
		}

		if (text != null)
		{
			if (canTranslate)
			{
				StatCollector.translateToLocal(text);
			}
			renderer.drawSplitString(text, guiLeft - 36 + (renderer.splitStringWidth(text, book.guiWidth)), guiTop
					+ 40, book.guiWidth - 20, 0);
		}
		
		if(footnote) {
			String footnote = "Click ↑ for curseforge page.";
			renderer.drawSplitString(EnumChatFormatting.DARK_RED + footnote, 50 + guiLeft - (book.guiWidth - renderer
					.getStringWidth(footnote)) / 2, guiTop + 150, 1000, 0);
		}
	}

	public String getTitle()
	{
		return title;
	}

	public String getText()
	{
		return text;
	}

	public String getImagePath()
	{
		return image;
	}

}