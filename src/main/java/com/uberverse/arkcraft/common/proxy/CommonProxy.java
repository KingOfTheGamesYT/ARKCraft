package com.uberverse.arkcraft.common.proxy;

import com.uberverse.arkcraft.client.book.proxy.BookCommon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy {
	@SidedProxy(clientSide = "com.uberverse.arkcraft.client.book.proxy.BookClient", serverSide = "com.uberverse.arkcraft.client.book.proxy.BookCommon")
	public static BookCommon dossierProxy;

	public CommonProxy() {}
	
	public void init() {}
	public void registerPreRenderers() {}
	public void registerRenderers() {}
	public void registerEventHandlers() {}

	public EntityPlayer getPlayer() {
		return null;
	}
}
