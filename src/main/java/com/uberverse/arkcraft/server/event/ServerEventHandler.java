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

	// // TODO remove when release
	// @SubscribeEvent
	// public void onClientConnected(ServerConnectionFromClientEvent event)
	// {
	// if (!ARKCraft.instance().isDebugger() && !event.isLocal
	// && MinecraftServer.getServer().isDedicatedServer())
	// {
	// MinecraftServer.getServer().stopServer();
	// event.handler.onDisconnect(new ChatComponentText(
	// "Nazi Spock does not approve of your shenanigans."));
	// }
	// }
}
