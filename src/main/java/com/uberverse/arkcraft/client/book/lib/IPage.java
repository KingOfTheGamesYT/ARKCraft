package com.uberverse.arkcraft.client.book.lib;

import com.uberverse.arkcraft.client.book.GuiInfoBook;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * 
 * @author Archiving
 * Rendering Interface
 * 
 */
public interface IPage {

	@SideOnly(Side.CLIENT)
	void draw(int guiLeft, int guiRight, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiInfoBook guiScreen);
	
	@SideOnly(Side.CLIENT)
	boolean equals(Object o);
	
}
