package com.arkcraft.client.book.pages;

import com.arkcraft.client.book.GuiInfoBook;
import com.arkcraft.client.book.lib.Page;
import com.arkcraft.client.book.lib.SmallFontRenderer;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.resources.I18n;

public class PageChapter extends Page
{

    public String title;
    public String[] sections;
    // public String[] images;

    public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiInfoBook book)
    {
        if (title != null) {
            if (canTranslate) {
                title = I18n.format(title);
            }
            renderer.drawSplitString(ChatFormatting.BOLD + "" + ChatFormatting.UNDERLINE + title, guiLeft + (book.guiWidth - renderer.getStringWidth(title)) / 2, guiTop + 5, 1000, 0);

        }

        if (sections != null) {
            for (int i = 0; i < sections.length; i++) {
                if (canTranslate)
                    sections[i] = I18n.format(sections[i]);
                renderer.drawSplitString(sections[i], guiLeft + (book.guiWidth - renderer.getStringWidth(sections[i])) / 2, guiTop + 50 + i * ((book.guiHeight / sections.length) / 2), 1000, 0);
            }
        }

    }
}
