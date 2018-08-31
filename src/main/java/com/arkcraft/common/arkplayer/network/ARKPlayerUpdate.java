package com.arkcraft.common.arkplayer.network;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.arkplayer.ARKPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ARKPlayerUpdate implements IMessage
{
	public ARKPlayerUpdate()
	{}

	private ARKPlayer player;
	private boolean all;
	private NBTTagCompound nbt;

	public ARKPlayerUpdate(ARKPlayer player, boolean all)
	{
		this.player = player;
		this.all = all;
		nbt = new NBTTagCompound();
		if (all) player.saveNBTData(nbt);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		all = buf.readBoolean();
		player = ARKPlayer.getDefault();
		if (all)
		{
			nbt = ByteBufUtils.readTag(buf);
		}
		else
		{
			for (ARKPlayer.Variable<?> var : player.getStats().values())
			{
				var.readConditionally(buf);
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(all);
		if (all)
		{
			ByteBufUtils.writeTag(buf, nbt);
		}
		else
		{
			for (ARKPlayer.Variable<?> var : player.getStats().values())
			{
				var.writeConditionally(buf);
			}
		}
	}

	private void store(ARKPlayer player)
	{
		if (all)
		{
			player.loadNBTData(nbt);
		}
		else player.copyConditionally(this.player);
	}

	public static class Handler implements IMessageHandler<ARKPlayerUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(ARKPlayerUpdate message, MessageContext ctx)
		{
			if (ctx.side.isClient())
			{
				message.store(ARKPlayer.get(ARKCraft.proxy.getPlayerFromContext(ctx)));
			}
			return null;
		}
	}
}
