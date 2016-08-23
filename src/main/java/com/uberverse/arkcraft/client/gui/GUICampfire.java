package com.uberverse.arkcraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.container.ContainerInventoryCampfire;
import com.uberverse.arkcraft.common.block.tile.TileInventoryCampfire;
import com.uberverse.arkcraft.common.network.CampfireToggleMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author BubbleTrouble
 *
 */
@SideOnly(Side.CLIENT)
public class GUICampfire extends GuiContainer
{

	// This is the resource location for the background image
	public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/campfire_gui.png");
	public static final ResourceLocation textureFlame = new ResourceLocation(
			"textures/gui/container/furnace.png");

	private TileInventoryCampfire tileEntity;

	public GUICampfire(InventoryPlayer invPlayer, TileInventoryCampfire tileInventoryCampfire)
	{
		super(new ContainerInventoryCampfire(invPlayer, tileInventoryCampfire));

		// Set the width and height of the gui
		xSize = 176;
		ySize = 207;

		this.tileEntity = tileInventoryCampfire;
	}

	// some [x,y] coordinates of graphical elements
	final int COOK_BAR_XPOS = 49;
	final int COOK_BAR_YPOS = 60;
	final int COOK_BAR_ICON_U = 0; // texture position of white arrow icon
	final int COOK_BAR_ICON_V = 207;
	final int COOK_BAR_WIDTH = 80;
	final int COOK_BAR_HEIGHT = 17;

	final int FLAME_XPOS = 26;
	final int FLAME_YPOS = 37;
	final int FLAME_ICON_U = 176; // texture position of flame icon
	final int FLAME_ICON_V = 0;
	final int FLAME_WIDTH = 14;
	final int FLAME_HEIGHT = 14;

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y)
	{
		// Bind the image texture
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureFlame);
		if (tileEntity.isBurning()) drawTexturedModalRect(guiLeft + FLAME_XPOS, guiTop + FLAME_YPOS,
				FLAME_ICON_U, FLAME_ICON_V, FLAME_WIDTH, FLAME_HEIGHT);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		final int LABEL_XPOS = xSize / 2 - fontRendererObj
				.getStringWidth(tileEntity.getDisplayName().getUnformattedText()) / 2;
		final int LABEL_YPOS = 5;
		fontRendererObj.drawString(tileEntity.getDisplayName().getUnformattedText(), LABEL_XPOS,
				LABEL_YPOS, Color.darkGray.getRGB());

		List<String> hoveringText = new ArrayList<String>();

		// If the mouse is over one of the burn time indicator add the burn time
		// indicator hovering text
		if (isInRect(guiLeft + FLAME_XPOS, guiTop + FLAME_YPOS, FLAME_WIDTH, FLAME_HEIGHT, mouseX,
				mouseY))
		{
			hoveringText.add("Fuel Time:");
			hoveringText.add(tileEntity.secondsOfFuelRemaining() + "s");
		}
		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty())
		{
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
		}
		// // You must re bind the texture and reset the colour if you still
		// need to use it after drawing a string
		// Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);// draw the fuel
	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY)
	{
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (isInRect(guiLeft + FLAME_XPOS, guiTop + FLAME_YPOS, FLAME_WIDTH, FLAME_HEIGHT, mouseX,
				mouseY))
		{
			ARKCraft.modLog.info("send");
			ARKCraft.modChannel.sendToServer(new CampfireToggleMessage());
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}