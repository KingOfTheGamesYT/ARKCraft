package com.uberverse.arkcraft.common.entity;

import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Lewis_McReu
 */
public interface ITranquilizer
{
	public default void applyTorpor(Entity entityHit)
	{
		if (entityHit instanceof ITranquilizable)
		{
			((ITranquilizable) entityHit).applyTorpor(getTorpor());
		}
		else if (entityHit instanceof EntityPlayer)
		{
			ARKPlayer.get((EntityPlayer) entityHit).applyTorpor(getTorpor());
		}
	}

	public int getTorpor();
}
