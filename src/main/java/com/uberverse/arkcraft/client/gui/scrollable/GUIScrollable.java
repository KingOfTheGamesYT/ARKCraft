package com.uberverse.arkcraft.client.gui.scrollable;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.gui.GUIArkContainer;
import com.uberverse.arkcraft.common.container.scrollable.ContainerScrollable;
import com.uberverse.arkcraft.common.container.scrollable.IContainerScrollable;
import com.uberverse.arkcraft.common.network.ScrollGui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public abstract class GUIScrollable extends GUIArkContainer implements IGuiScrollable
{
	public static final ResourceLocation scrollButtonCreativeResource = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	public static final int scrollButtonCreativeWidth = 12;
	public static final int scrollButtonCreativeHeight = 15;
	public static final int scrollButtonCreativeU = 232;
	public static final int scrollButtonCreativeV = 0;
	public static final int scrollButtonCreativeDisabledU = scrollButtonCreativeU + scrollButtonCreativeWidth;
	public static final int scrollButtonCreativeDisabledV = scrollButtonCreativeV;

	public GUIScrollable(ContainerScrollable container)
	{
		super(container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		// Scroll button
		mc.getTextureManager().bindTexture(getScrollButtonResource());

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int y = guiTop + getScrollBarStartY() + getActualScrollPosition();

		this.drawTexturedModalRect(guiLeft + getScrollBarStartX(), y, canScroll() ? getScrollButtonU() : getScrollButtonDisabledU(),
				canScroll() ? getScrollButtonV() : getScrollButtonDisabledV(), getScrollButtonWidth(), getScrollButtonHeight());
	}

	private boolean scrollClicked;

	private void scroll(float newScroll)
	{
		((IContainerScrollable) this.inventorySlots).scroll(newScroll);
		ARKCraft.modChannel.sendToServer(new ScrollGui(newScroll));
	}

	private void scroll(int newScroll)
	{
		((IContainerScrollable) this.inventorySlots).scroll(newScroll);
		ARKCraft.modChannel.sendToServer(new ScrollGui(newScroll));
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
			scroll(scrollAmount);
			scrollPosition = ((ContainerScrollable) inventorySlots).getRelativeScrollingOffset();
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseButton == 0)
		{
			if (canScroll() && mc.thePlayer.inventory.getCurrentItem() == null)
			{
				if (isPointInRegion(getScrollBarStartX(), (int) (getScrollBarStartY() + getActualScrollPosition()), getScrollButtonWidth(),
						getScrollButtonHeight(), mouseX, mouseY))
				{
					scrollClicked = true;
				}
				if (isPointInRegion(getScrollBarStartX(), getScrollBarStartY(), getScrollButtonWidth(),
						getScrollBarEndY() + getScrollButtonHeight() - getScrollBarStartY(), mouseX, mouseY))
				{
					adjustScrollFromMouseY(mouseY);
				}
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
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

	private float scrollPosition = 0;

	@Override
	public float getScrollPosition()
	{
		return scrollPosition;
	}

	public int getActualScrollPosition()
	{
		return (int) ((float) (getScrollBarEndY() - getScrollBarStartY() - 17) * getScrollPosition());
	}

	private void adjustScrollFromMouseY(int mouseY)
	{
		int top = guiTop + getScrollBarStartY();
		int bot = guiTop + getScrollBarEndY();

		this.scrollPosition = ((float) (mouseY - top) - 7.5F) / ((float) (bot - top) - 15.0F);
		this.scrollPosition = MathHelper.clamp_float(this.scrollPosition, 0.0F, 1.0F);
		scroll(scrollPosition);
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

	public int getScrollButtonWidth()
	{
		return scrollButtonCreativeWidth;
	}

	public int getScrollButtonHeight()
	{
		return scrollButtonCreativeHeight;
	}

	public int getScrollButtonU()
	{
		return scrollButtonCreativeU;
	}

	public int getScrollButtonV()
	{
		return scrollButtonCreativeV;
	}

	public int getScrollButtonDisabledU()
	{
		return scrollButtonCreativeDisabledU;
	}

	public int getScrollButtonDisabledV()
	{
		return scrollButtonCreativeDisabledV;
	}

	public ResourceLocation getScrollButtonResource()
	{
		return scrollButtonCreativeResource;
	}

	public abstract int getScrollBarStartX();

	public abstract int getScrollBarStartY();

	public abstract int getScrollBarEndY();

}
