package com.uberverse.arkcraft.common.tileentity;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

/**
 * 
 * @author Lewis_McReu
 * 
 *         Credit to Jabelar for a slightly more performant implementation
 * 
 */
public class TileFlashlight extends TileEntity implements IUpdatePlayerListBox
{
	public int ticks;

	public TileFlashlight()
	{
		super();
		ticks = 0;
	}

	@Override
	public void update()
	{
		if (++ticks > 2)
		{
			this.getWorld().setBlockToAir(getPos());
			return;
		}
	}
}