package com.uberverse.arkcraft.rework;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiTexturedButton extends GuiButton
{
	private ResourceLocation resource;

	private int resourceX, resourceY;

	public GuiTexturedButton(int buttonId, int x, int y, int widthIn, int heightIn, ResourceLocation resource, int resourceX, int resourceY)
	{
		super(buttonId, x, y, widthIn, heightIn, "");
		this.resource = resource;
		this.resourceX = resourceX;
		this.resourceY = resourceY;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(resource);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (!enabled)
		{
			this.drawTexturedModalRect(this.xPosition, this.yPosition, resourceX,
					resourceY + height * 2, this.width, this.height);
			return;
		}
		if (mousePressed(mc, mouseX, mouseY))
		{
			this.drawTexturedModalRect(this.xPosition, this.yPosition, resourceX,
					resourceY + height, this.width, this.height);
			return;
		}
		this.drawTexturedModalRect(xPosition, yPosition, resourceX, resourceY, width, height);
	}
}
