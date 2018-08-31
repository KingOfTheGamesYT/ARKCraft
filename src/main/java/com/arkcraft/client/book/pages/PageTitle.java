package com.arkcraft.client.book.pages;

import com.arkcraft.ARKCraft;
import com.arkcraft.client.book.GuiInfoBook;
import com.arkcraft.client.book.lib.Page;
import com.arkcraft.client.book.lib.SmallFontRenderer;
import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

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
    public String footnote;

    public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiInfoBook book)
    {
        if (image != null) {
            ResourceLocation imagePath = new ResourceLocation(ARKCraft.MODID, image);
            if (imagePath != null) {
                GL11.glColor4f(1F, 1F, 1F, 1F);
                Minecraft.getMinecraft().getTextureManager().bindTexture(imagePath);
                book.drawTexturedModalRect(guiLeft + (book.guiWidth - 64) / 2, guiTop + 65, 0, 0, 64, 64);
            }

        }

        if (title != null) {
            if (canTranslate) {
                title = I18n.format(title);
            }
            renderer.drawSplitString(ChatFormatting.BOLD + "" + ChatFormatting.UNDERLINE + title, guiLeft - 16 + (book.guiWidth - renderer.getStringWidth(title)) / 2, guiTop + 5, 1000, 0);
        }

        if (text != null) {
            if (canTranslate) {
                text = I18n.format(text);
            }
            renderer.drawSplitString(text, guiLeft - 36 + (renderer.splitStringWidth(text, book.guiWidth)), guiTop + 40, book.guiWidth - 5, 0);
        }

        if (footnote != null) {
            renderer.drawSplitString(ChatFormatting.DARK_RED + footnote, guiLeft + 48 - (book.guiWidth - renderer.getStringWidth(footnote)) / 2, guiTop + 150, 1000, 0);
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