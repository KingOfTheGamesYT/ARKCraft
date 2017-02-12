package com.uberverse.arkcraft.client.book.pages;

import com.uberverse.arkcraft.client.book.GuiInfoBook;
import com.uberverse.arkcraft.client.book.lib.Page;
import com.uberverse.arkcraft.client.book.lib.SmallFontRenderer;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class PageChapter extends Page
{

	public String title;
	public String[] sections;
	// public String[] images;

	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate,
			GuiInfoBook book)
	{
		if (title != null)
		{
			if (canTranslate)
			{
				StatCollector.translateToLocal(title);
			}
			renderer.drawSplitString(EnumChatFormatting.BOLD + "" + EnumChatFormatting.UNDERLINE + title, guiLeft
					+ (book.guiWidth - renderer.getStringWidth(title)) / 2, guiTop + 5, 1000, 0);

		}

		if (sections != null)
		{
			for (int i = 0; i < sections.length; i++)
			{
				if (canTranslate) StatCollector.translateToLocal(sections[i]);
				renderer.drawSplitString(sections[i], guiLeft + (book.guiWidth - renderer.getStringWidth(sections[i]))
						/ 2, guiTop + 50 + i * ((book.guiHeight / sections.length) / 2), 1000, 0);
			}
		}

	}
}
