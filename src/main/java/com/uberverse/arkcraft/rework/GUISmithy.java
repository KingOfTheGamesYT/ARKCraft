package com.uberverse.arkcraft.rework;

import java.io.IOException;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.container.scrollable.IContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.IGuiScrollable;
import com.uberverse.arkcraft.common.network.ScrollingMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GUISmithy extends GuiContainer implements IGuiScrollable
{
	public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/smithy_old.png");
	public static final ResourceLocation scrollButton = new ResourceLocation(
			"textures/gui/container/creative_inventory/tabs.png");

	public static final int scrollButtonWidth = 12;
	public static final int scrollButtonHeight = 15;
	public static final int scrollButtonPosX = 232;
	public static final int scrollButtonPosY = 0;
	public static final int scrollButtonPosXDisabled = scrollButtonPosX + 12;
	public static final int scrollButtonStartX = 154;
	public static final int scrollButtonStartY = 18;
	public static final int scrollButtonEndY = 106 - scrollButtonHeight;

	private GuiButton craft, craftall;

	public GUISmithy(EntityPlayer player, TileEntitySmithy tileEntity)
	{
		super(new ContainerSmithy(player, tileEntity));
		xSize = 176;
		ySize = 222;
		guiLeft = (Minecraft.getMinecraft().displayWidth - xSize) / 2;
		guiTop = (Minecraft.getMinecraft().displayHeight - ySize) / 2;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int buttonId = 0;
		craft = new GuiTexturedButton(buttonId++, guiLeft + 84, guiTop + 111, 32, 14, texture, 176,
				42);
		craftall = new GuiTexturedButton(buttonId++, guiLeft + 120, guiTop + 111, 47, 14, texture,
				176, 0);
		this.buttonList.add(craft);
		this.buttonList.add(craftall);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		// Scroll button
		Minecraft.getMinecraft().getTextureManager().bindTexture(scrollButton);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.canScroll())
		{
			drawTexturedModalRect(guiLeft + scrollButtonStartX,
					guiTop + scrollButtonStartY + getScrollPosition(), scrollButtonPosX,
					scrollButtonPosY, scrollButtonWidth, scrollButtonHeight);
		}
		else
		{
			drawTexturedModalRect(guiLeft + scrollButtonStartX,
					(int) (guiTop + scrollButtonStartY + ((IContainerScrollable) this.inventorySlots)
							.getRelativeScrollingOffset() * (scrollButtonStartY - scrollButtonStartX)),
					scrollButtonPosXDisabled, scrollButtonPosY, scrollButtonWidth,
					scrollButtonHeight);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == craft) ((ContainerSmithy) this.inventorySlots).craftOne();
		else if (button == craftall) ((ContainerSmithy) this.inventorySlots).craftAll();
		else super.actionPerformed(button);
	}

	@Override
	public int getScrollPosition()
	{
		return (int) (((IContainerScrollable) this.inventorySlots)
				.getRelativeScrollingOffset() * (scrollButtonEndY - scrollButtonStartY));
	}

	private boolean scrollClicked;

	private void adjustScroll(int scrollAmount)
	{
		((IContainerScrollable) this.inventorySlots).scroll(scrollAmount);
		ARKCraft.modChannel.sendToServer(new ScrollingMessage(scrollAmount));
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseX >= guiLeft + scrollButtonStartX && mouseX <= guiLeft + scrollButtonStartX + scrollButtonWidth && mouseY >= guiTop + scrollButtonStartY && mouseY <= guiTop + scrollButtonEndY + scrollButtonHeight)
		{
			adjustScrollFromMouseY(mouseY);
			scrollClicked = true;
		}
		if (mouseX >= guiLeft + scrollButtonStartX && mouseX <= guiLeft + scrollButtonStartX + scrollButtonWidth && mouseY >= guiTop + scrollButtonStartY + getScrollPosition() && mouseY < guiTop + scrollButtonStartY + getScrollPosition() + scrollButtonHeight)
		{
			scrollClicked = true;
		}
		else super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		if (scrollClicked)
		{
			adjustScrollFromMouseY(mouseY);
		}
		else super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	private void adjustScrollFromMouseY(int mouseY)
	{
		IContainerScrollable inv = (IContainerScrollable) inventorySlots;
		int minScroll = 0;
		int maxScroll = scrollButtonEndY - scrollButtonStartY + scrollButtonHeight;
		int newScrollPos = mouseY - guiTop - scrollButtonStartY - 7;
		if (newScrollPos < minScroll)
		{
			newScrollPos = minScroll;
		}
		else if (newScrollPos > maxScroll)
		{
			newScrollPos = maxScroll;
		}
		int scrollDiv = (scrollButtonEndY - scrollButtonStartY + scrollButtonHeight) / inv
				.getMaxOffset();
		int adjustScroll = newScrollPos / scrollDiv - inv.getScrollingOffset();
		adjustScroll(adjustScroll);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		if (scrollClicked)
		{
			scrollClicked = false;
		}
		else super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public boolean canScroll()
	{
		return ((IContainerScrollable) this.inventorySlots).canScroll();
	}
}
