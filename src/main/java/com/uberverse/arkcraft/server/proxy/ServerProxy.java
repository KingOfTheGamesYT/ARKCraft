package com.uberverse.arkcraft.server.proxy;

import com.uberverse.arkcraft.common.proxy.CommonProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy extends CommonProxy
{
	@Override
	public EntityPlayer getPlayerFromContext(MessageContext ctx)
	{
		return ctx.getServerHandler().playerEntity;
	}
}
