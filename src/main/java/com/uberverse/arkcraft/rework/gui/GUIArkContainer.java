package com.uberverse.arkcraft.rework.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GUIArkContainer extends GuiContainer
{
	public GUIArkContainer(Container container)
	{
		super(container);
		xSize = getBackgroundWidth();
		ySize = getBackgroundHeight();
		guiLeft = (Minecraft.getMinecraft().displayWidth - xSize) / 2;
		guiTop = (Minecraft.getMinecraft().displayHeight - ySize) / 2;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(getBackgroundResource());
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	public abstract ResourceLocation getBackgroundResource();

	public abstract int getBackgroundWidth();

	public abstract int getBackgroundHeight();

}
