package com.uberverse.arkcraft.client.event;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.event.CommonEventHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ARKModeMessage implements IMessage {
	
	private static boolean toggleArkMode = false;
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{

	}

	@Override
	public void toBytes(ByteBuf buf) 
	{

	}
	
	public static class Handler implements IMessageHandler<ARKModeMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final ARKModeMessage message, MessageContext ctx)
		{
			if (ctx.side != Side.SERVER)
			{
				System.err.println("MPUpdateDoReloadStarted received on wrong side:" + ctx.side);
				return null;
			}
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			ARKPlayer p = ARKPlayer.get(ARKCraft.proxy.getPlayerFromContext(ctx));
			player.getServerForPlayer().addScheduledTask(new Runnable()
			{
				public void run()
				{
					processMessage(message, p);
				}
			});
			return null;
		}
	}

	static void processMessage(ARKModeMessage message, ARKPlayer p)
	{
		if (p != null)
		{
			if(ARKPlayer.getARKMode() == true)
			{
				p.setARKMode(false);
			}
			else
			{
				p.setARKMode(true);
			}
			System.out.println("ARKMode " + p.getARKMode());
		}
	}

}



