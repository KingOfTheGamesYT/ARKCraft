package com.uberverse.arkcraft.rework;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.uberverse.arkcraft.common.container.scrollable.IContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.IGuiScrollable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public abstract class GUIScrollable extends GUIArkContainer implements IGuiScrollable
{
	public GUIScrollable(ContainerScrollable container)
	{
		super(container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		// Scroll button
		Minecraft.getMinecraft().getTextureManager().bindTexture(getScrollButtonResource());
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.canScroll())
		{
			drawTexturedModalRect(guiLeft + getScrollBarStartX(),
					guiTop + getScrollBarStartY() + getScrollPosition(), getScrollButtonU(),
					getScrollButtonV(), getScrollButtonWidth(), getScrollButtonHeight());
		}
		else
		{
			drawTexturedModalRect(guiLeft + getScrollBarStartX(), guiTop + getScrollBarStartY(),
					getScrollButtonDisabledU(), getScrollButtonDisabledV(), getScrollButtonWidth(),
					getScrollButtonHeight());
		}
	}

	@Override
	public int getScrollPosition()
	{
		return (int) (((IContainerScrollable) this.inventorySlots)
				.getRelativeScrollingOffset() * (getScrollBarEndY() - getScrollBarStartY()));
	}

	private boolean scrollClicked;

	private void setScroll(float newScroll)
	{
		((IContainerScrollable) this.inventorySlots).scroll(newScroll);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		int scrollAmount = Mouse.getEventDWheel();
		if (scrollAmount != 0 && canScroll())
		{
			if (scrollAmount > 0) scrollAmount = -1;
			else if (scrollAmount < 0) scrollAmount = 1;
			setScroll(scrollAmount);
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseButton == 0)
		{
			if (isPointInRegion(getScrollBarStartX(), getScrollBarStartY() + getScrollPosition(),
					getScrollButtonWidth(), getScrollButtonHeight(), mouseX, mouseY))
			{
				scrollClicked = true;
			}
			if (isPointInRegion(getScrollBarStartX(), getScrollBarStartY(), getScrollButtonWidth(),
					getScrollBarEndY() + getScrollButtonHeight() - getScrollBarStartY(), mouseX,
					mouseY))
			{
				adjustScrollFromMouseY(mouseY);
			}
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
		float newScroll = (float) (mouseY - guiTop - getScrollBarStartY() + getScrollButtonHeight() / 2) / (float) (getScrollBarEndY() + getScrollButtonHeight() / 2 - getScrollBarStartY());
		newScroll = MathHelper.clamp_float(newScroll, 0.0F, 1.0F);
		setScroll(newScroll);
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

	public abstract int getScrollButtonWidth();

	public abstract int getScrollButtonHeight();

	public abstract int getScrollButtonU();

	public abstract int getScrollButtonV();

	public abstract int getScrollButtonDisabledU();

	public abstract int getScrollButtonDisabledV();

	public abstract ResourceLocation getScrollButtonResource();

	public abstract int getScrollBarStartX();

	public abstract int getScrollBarStartY();

	public abstract int getScrollBarEndY();

}
