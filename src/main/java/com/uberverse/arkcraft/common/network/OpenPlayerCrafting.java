package com.uberverse.arkcraft.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.uberverse.arkcraft.ARKCraft;

/**
 * Used so the player can open the Crafting GUI
 *
 * @author William
 * @author Lewis_McReu
 */
public class OpenPlayerCrafting implements IMessage
{
	public OpenPlayerCrafting()
	{}

	@Override
	public void fromBytes(ByteBuf buf)
	{}

	@Override
	public void toBytes(ByteBuf buf)
	{
	}

	public static class Handler implements IMessageHandler<OpenPlayerCrafting, IMessage>
	{
		@Override
		public IMessage onMessage(final OpenPlayerCrafting message, MessageContext ctx)
		{
			if (ctx.side != Side.SERVER)
			{
				System.err.println("MPUpdateDoCraft received on wrong side:" + ctx.side);
				return null;
			}
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			player.getServerForPlayer().addScheduledTask(new Runnable()
			{
				public void run()
				{
					processMessage(message, player);
				}
			});
			return null;
		}
	}

	// On Server
	static void processMessage(OpenPlayerCrafting message, EntityPlayerMP player)
	{
		if (player != null)
		{
			player.openGui(ARKCraft.instance(), ARKCraft.GUI.PLAYER.getID(), player.worldObj, 0, 0, 0);
		}
	}
}
