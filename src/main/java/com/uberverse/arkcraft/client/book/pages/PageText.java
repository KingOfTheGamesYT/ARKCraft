package com.uberverse.arkcraft.client.book.pages;

import com.uberverse.arkcraft.client.book.GuiInfoBook;
import com.uberverse.arkcraft.client.book.lib.Page;
import com.uberverse.arkcraft.client.book.lib.SmallFontRenderer;

import net.minecraft.client.resources.I18n;

public class PageText extends Page
{

    public String text;

    @Override
    public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiInfoBook book)
    {

        if (text != null) {
            if (canTranslate) {
                text = I18n.format(text);
            }
            renderer.drawSplitString(text, guiLeft - 53 + renderer.splitStringWidth(text, (book.guiWidth)), guiTop + 20, book.guiWidth - 30, 0);
        }
    }

}
