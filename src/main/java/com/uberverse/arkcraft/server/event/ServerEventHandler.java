package com.uberverse.arkcraft.server.event;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class ServerEventHandler
{
	public static void init()
	{
		ServerEventHandler s = new ServerEventHandler();
		FMLCommonHandler.instance().bus().register(s);
	}

	// TODO remove when release
	@SubscribeEvent
	public void onClientConnected(ServerConnectionFromClientEvent event)
	{
		if (!ARKCraft.instance().isDebugger() && !event.isLocal && MinecraftServer.getServer().isDedicatedServer())
		{
			MinecraftServer.getServer().stopServer();
			event.handler.onDisconnect(new ChatComponentText("Nazi Spock does not approve of your shenanigans."));
		}
	}
}
