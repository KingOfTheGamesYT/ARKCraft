package com.uberverse.arkcraft.server.net;

import com.uberverse.arkcraft.common.network.ReloadFinished;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerReloadFinishedHandler implements IMessageHandler<ReloadFinished, IMessage>
{
	@Override
	public IMessage onMessage(final ReloadFinished message, MessageContext ctx)
	{
		return null;
	}
}
