package com.uberverse.arkcraft.server.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class ServerEventHandler
{
	public static void init()
	{
		ServerEventHandler s = new ServerEventHandler();
		MinecraftForge.EVENT_BUS.register(s);
	}

}
