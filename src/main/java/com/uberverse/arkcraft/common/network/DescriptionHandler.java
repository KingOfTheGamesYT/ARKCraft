package com.uberverse.arkcraft.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityArkCraft;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class DescriptionHandler extends SimpleChannelInboundHandler<FMLProxyPacket>
{
	static
	{
		NetworkRegistry.INSTANCE.newChannel(ARKCraft.descriptionPacketChannel,
				new DescriptionHandler());
	}

	public static void init()
	{
		System.out.println("Init the DescriptionHandler");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception
	{
		final ByteBuf buf = msg.payload();
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{

			@Override
			public void run()
			{
				int x = buf.readInt();
				int y = buf.readInt();
				int z = buf.readInt();
				TileEntity te = ARKCraft.proxy.getPlayer().worldObj
						.getTileEntity(new BlockPos(x, y, z));
				if (te instanceof TileEntityArkCraft)
				{
					((TileEntityArkCraft) te).readFromPacket(ByteBufUtils.readTag(buf));
				}
			}
		});
	}

}
