package com.uberverse.arkcraft.rework;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public abstract class GUIEngramCrafting extends GUIScrollable
{
	protected int buttonCounter = 0;
	private GuiButton craft, craftall;

	public GUIEngramCrafting(ContainerEngramCrafting container)
	{
		super(container);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		craft = new GuiTexturedButton(buttonCounter++, guiLeft + getC1ButtonX(),
				guiTop + getC1ButtonY(), getC1ButtonWidth(), getC1ButtonHeight(),
				getC1ButtonResource(), getC1ButtonU(), getC1ButtonV());
		craftall = new GuiTexturedButton(buttonCounter++, guiLeft + getCAButtonX(),
				guiTop + getCAButtonY(), getCAButtonWidth(), getCAButtonHeight(),
				getCAButtonResource(), getCAButtonU(), getCAButtonV());
		this.buttonList.add(craft);
		this.buttonList.add(craftall);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == craft) mc.playerController.sendEnchantPacket(0, 0);
		else if (button == craftall) mc.playerController.sendEnchantPacket(0, 1);
		else super.actionPerformed(button);
	}

	public abstract int getC1ButtonX();

	public abstract int getC1ButtonY();

	public abstract int getC1ButtonU();

	public abstract int getC1ButtonV();

	public abstract int getC1ButtonWidth();

	public abstract int getC1ButtonHeight();

	public abstract ResourceLocation getC1ButtonResource();

	public abstract int getCAButtonX();

	public abstract int getCAButtonY();

	public abstract int getCAButtonU();

	public abstract int getCAButtonV();

	public abstract int getCAButtonWidth();

	public abstract int getCAButtonHeight();

	public abstract ResourceLocation getCAButtonResource();
}
