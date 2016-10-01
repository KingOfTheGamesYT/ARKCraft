package com.uberverse.arkcraft.client.book.lib;

import com.uberverse.arkcraft.client.book.GuiInfoBook;

/**
 * @author Archiving A class to act as a rendering interface. This is a base
 *         class. Specific pages extend this class to connect a JSON object to
 *         rendering information in Minecraft.
 */
public class Page implements IPage
{

	public Page()
	{}

	@Override
	public void draw(int guiLeft, int guiRight, int mouseX, int mouseY, SmallFontRenderer renderer,
			boolean canTranslate, GuiInfoBook guiScreen)
	{}

	@Override
	public boolean equals(Object o)
	{
		return o == this;
	}

}
