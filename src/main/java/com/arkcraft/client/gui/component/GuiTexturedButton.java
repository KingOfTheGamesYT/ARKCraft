package com.arkcraft.client.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author Lewis_McReu
 */
public class GuiTexturedButton extends GuiButton {
	private ResourceLocation resource;

	private int resourceX, resourceY;

	public GuiTexturedButton(int buttonId, int x, int y, int widthIn, int heightIn, ResourceLocation resource, int resourceX, int resourceY) {
		super(buttonId, x, y, widthIn, heightIn, "");
		this.resource = resource;
		this.resourceX = resourceX;
		this.resourceY = resourceY;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(resource);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (!enabled) {
			this.drawTexturedModalRect(this.x, this.y, resourceX, resourceY + height * 2, this.width,
					this.height);
			return;
		}
		if (mousePressed(mc, mouseX, mouseY)) {
			this.drawTexturedModalRect(this.x, this.y, resourceX, resourceY + height, this.width,
					this.height);
			return;
		}
		this.drawTexturedModalRect(x, y, resourceX, resourceY, width, height);
	}
}
