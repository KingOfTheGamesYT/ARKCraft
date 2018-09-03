package com.arkcraft.common.network;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.container.scrollable.IContainerScrollable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Lewis_McReu
 */
public class ScrollGui implements IMessage {
	private int scrollInt = 0;
	private float scrollFloat = 0;
	private boolean bool;

	public ScrollGui(float scrollAmount) {
		this.scrollFloat = scrollAmount;
	}

	public ScrollGui(int scrollAmount) {
		this.scrollInt = scrollAmount;
	}

	public ScrollGui() {
	}

	static void processMessage(ScrollGui message, EntityPlayer player) {
		if (player != null) {
			Container c = player.openContainer;
			if (c instanceof IContainerScrollable) {
				if (message.bool) ((IContainerScrollable) c).scroll(message.scrollInt);
				else ((IContainerScrollable) c).scroll(message.scrollFloat);
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		bool = buf.readBoolean();
		if (bool) scrollInt = buf.readInt();
		else scrollFloat = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(scrollInt != 0);
		if (scrollInt != 0) buf.writeInt(scrollInt);
		else buf.writeFloat(scrollFloat);
	}

	public static class Handler implements IMessageHandler<ScrollGui, IMessage> {
		@Override
		public IMessage onMessage(final ScrollGui message, MessageContext ctx) {
			if (ctx.side != Side.SERVER) {
				ARKCraft.logger.error("MPUpdateScroll received on wrong side:" + ctx.side);
				return null;
			}
			processMessage(message, ctx.getServerHandler().player);
			return null;
		}
	}

}