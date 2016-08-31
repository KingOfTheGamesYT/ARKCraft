package com.uberverse.arkcraft.rework.arkplayer.network;

import com.uberverse.arkcraft.rework.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.rework.arkplayer.ARKPlayer.Variable;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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
		System.out.println("reply");
		this.player = player;
		this.all = all;
		NBTTagCompound nbt = new NBTTagCompound();
		if (all) player.saveNBTData(nbt);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		all = buf.readBoolean();
		player = ARKPlayer.getDefault();
		if (all)
		{
			System.out.println("read");
			nbt = ByteBufUtils.readTag(buf);
		}
		else
		{
			for (Variable<?> var : player.getStats().values())
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
			System.out.println("write");
			ByteBufUtils.writeTag(buf, nbt);
		}
		else
		{
			for (Variable<?> var : player.getStats().values())
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
				System.out.println("arrived");
				message.store(ARKPlayer.get(Minecraft.getMinecraft().thePlayer));
			}
			return null;
		}
	}
}
