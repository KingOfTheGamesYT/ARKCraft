package com.uberverse.arkcraft.common.network;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.inventory.InventoryAttachment;
import com.uberverse.arkcraft.common.item.ranged.ItemRangedWeapon;
import com.uberverse.arkcraft.util.SoundUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RecoilDown implements IMessage
{
	public RecoilDown()
	{

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{}

	@Override
	public void toBytes(ByteBuf buf)
	{}

	public static class Handler implements IMessageHandler<RecoilDown, IMessage>
	{
		@Override
		public IMessage onMessage(final RecoilDown message, MessageContext ctx)
		{
			if (ctx.side != Side.SERVER)
			{
				System.err.println("MPUpdateDoReloadStarted received on wrong side:" + ctx.side);
				return null;
			}
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			player.getServer().addScheduledTask(new Runnable()
			{
				public void run()
				{
					processMessage(message, player);
				}
			});
			return null;
		}
	}

	static void processMessage(RecoilDown message, EntityPlayerMP player)
	{
		if (player != null)
		{
			ItemStack stack = player.getHeldItemMainhand();
			if (stack != null && stack.getItem() instanceof ItemRangedWeapon)
			{
				ItemRangedWeapon weapon = (ItemRangedWeapon) stack.getItem();
				weapon.recoilDown((EntityPlayer)player, weapon.getRecoil(), weapon.getRecoilSneaking(), weapon.getShouldRecoil());
				weapon.setFired(stack, player, false);
			}
		}
	}

}
