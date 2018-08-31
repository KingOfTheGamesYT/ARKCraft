package com.arkcraft.client.gui.block;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.container.block.ContainerCampfire;
import com.arkcraft.client.gui.burner.GUIBurner;

import net.minecraft.util.ResourceLocation;

public class GUICampfire extends GUIBurner
{
	public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/campfire_gui.png");

	public GUICampfire(ContainerCampfire container)
	{
		super(container);
	}

	@Override
	public int getFlamePosX()
	{
		return 26;
	}

	@Override
	public int getFlamePosY()
	{
		return 37;
	}

	@Override
	public ResourceLocation getBackgroundResource()
	{
		return texture;
	}

	@Override
	public int getBackgroundWidth()
	{
		return 176;
	}

	@Override
	public int getBackgroundHeight()
	{
		return 207;
	}
}
