package com.arkcraft.client.book.pages;

import com.arkcraft.client.book.GuiInfoBook;
import com.arkcraft.client.book.lib.Page;
import com.arkcraft.client.book.lib.SmallFontRenderer;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;

/**
 * @author Archiving This page is for crafting recipes that also have a block
 *         model.
 */
public class PageCraftingBlock extends Page
{

    public Block result;
    public String title;
    public String text;
    public String recipeImage;

    @Override
    public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiInfoBook book)
    {
        if (result != null) {

        }

        if (title != null) {
            if (canTranslate)
                title = I18n.format(title);

        }

        if (text != null) {

        }

        if (recipeImage != null) {

        }

    }
}
