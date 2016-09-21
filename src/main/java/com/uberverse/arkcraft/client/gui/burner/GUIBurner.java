package com.uberverse.arkcraft.client.gui.burner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uberverse.arkcraft.client.gui.GUIArkContainer;
import com.uberverse.arkcraft.common.burner.IBurner;
import com.uberverse.arkcraft.common.container.burner.ContainerBurner;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public abstract class GUIBurner extends GUIArkContainer
{
	public static final ResourceLocation flame =
			new ResourceLocation("textures/gui/container/furnace.png");;
	public static final int flameU = 176;
	public static final int flameV = 0;
	public static final int flameSize = 14;

	public GUIBurner(ContainerBurner container)
	{
		super(container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		// draw the flame
		Minecraft.getMinecraft().getTextureManager().bindTexture(flame);
		if (((ContainerBurner) inventorySlots).getBurner().isBurning())
			drawTexturedModalRect(guiLeft + getFlamePosX(),
					guiTop + getFlamePosY(), flameU, flameV, flameSize,
					flameSize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		// If the mouse is over the burn time indicator add the burn time
		// indicator hovering text
		if (onFlame(mouseX, mouseY))
		{
			List<String> hoveringText = new ArrayList<String>();
			IBurner b = ((ContainerBurner) inventorySlots).getBurner();
			// TODO Localization!
			if (b.isBurning())
			{
				hoveringText.add("Fuel Time:");
				hoveringText.add(b.getBurningTicks() / 20 + "s");
				hoveringText.add(EnumChatFormatting.RED + "Press to douse");
			}
			else
			{
				hoveringText.add(EnumChatFormatting.RED + "Press to ignite");
			}
			drawHoveringText(hoveringText, mouseX - guiLeft,
					mouseY - guiTop + 25, fontRendererObj);
		}
	}

	private boolean onFlame(int mouseX, int mouseY)
	{
		return isPointInRegion(getFlamePosX(), getFlamePosY(), flameSize,
				flameSize, mouseX, mouseY);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
			throws IOException
	{
		if (onFlame(mouseX, mouseY))
		{
			mc.playerController.sendEnchantPacket(inventorySlots.windowId, 0);
			return;
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public abstract int getFlamePosX();

	public abstract int getFlamePosY();
}
