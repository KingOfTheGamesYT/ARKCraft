package com.uberverse.arkcraft.server.proxy;

import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.server.event.ServerEventHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy
{
	@Override
	protected void registerEventHandlers()
	{
		super.registerEventHandlers();
		ServerEventHandler.init();
	}

	@Override
	public EntityPlayer getPlayerFromContext(MessageContext ctx)
	{
		return ctx.getServerHandler().playerEntity;
	}

	@Override
	public long getTime()
	{
		return MinecraftServer.getCurrentTimeMillis();
	}

	@Override
	public long getWorldTime()
	{
		return MinecraftServer.getServer().getEntityWorld().getTotalWorldTime();
	}
}
