package com.uberverse.arkcraft.client.book.lib.button;

import org.lwjgl.opengl.GL11;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class PageButton extends GuiButton
{

	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/info_book_left.png");
	private final boolean nextPage;

	public PageButton(int id, int x, int y, boolean nextPage)
	{
		super(id, x, y, 16, 11, "");
		this.nextPage = nextPage;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		if (this.visible)
		{
			/* boolean for hover */
			boolean hover = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(texture);
			int x = 7;
			int y = 187;

			if (hover)
			{
				x += 16;
			}

			if (this.nextPage)
			{
				y += 11;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, x, y, 16, 11);
		}
	}
}
