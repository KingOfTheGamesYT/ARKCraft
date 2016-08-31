package com.uberverse.arkcraft.rework.gui;

import java.io.IOException;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.rework.container.ContainerEngramCrafting;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public abstract class GUIEngramCrafting extends GUIScrollable
{
	public static final ResourceLocation buttons = new ResourceLocation(ARKCraft.MODID, "textures/gui/buttons.png");

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
		craft = new GuiTexturedButton(buttonCounter++, guiLeft + getC1ButtonX(), guiTop + getC1ButtonY(), getC1ButtonWidth(), getC1ButtonHeight(),
				getC1ButtonResource(), getC1ButtonU(), getC1ButtonV());
		craftall = new GuiTexturedButton(buttonCounter++, guiLeft + getCAButtonX(), guiTop + getCAButtonY(), getCAButtonWidth(), getCAButtonHeight(),
				getCAButtonResource(), getCAButtonU(), getCAButtonV());
		this.buttonList.add(craft);
		this.buttonList.add(craftall);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		System.out.println("act");
		if (button == craft) mc.playerController.sendEnchantPacket(0, 0);
		else if (button == craftall) mc.playerController.sendEnchantPacket(0, 1);
		else super.actionPerformed(button);
	}

	public abstract int getC1ButtonX();

	public abstract int getC1ButtonY();

	public int getC1ButtonU()
	{
		return 0;
	}

	public int getC1ButtonV()
	{
		return 42;
	}

	public int getC1ButtonWidth()
	{
		return 32;
	}

	public int getC1ButtonHeight()
	{
		return 14;
	}

	public ResourceLocation getC1ButtonResource()
	{
		return buttons;
	}

	public abstract int getCAButtonX();

	public abstract int getCAButtonY();

	public int getCAButtonU()
	{
		return 0;
	}

	public int getCAButtonV()
	{
		return 0;
	}

	public int getCAButtonWidth()
	{
		return 47;
	}

	public int getCAButtonHeight()
	{
		return 14;
	}

	public ResourceLocation getCAButtonResource()
	{
		return buttons;
	}

}
