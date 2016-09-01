package com.uberverse.arkcraft.common.proxy;

import com.uberverse.arkcraft.client.book.proxy.BookCommon;
import com.uberverse.arkcraft.rework.arkplayer.event.PlayerCommonEventHandler;
import com.uberverse.arkcraft.rework.engram.EngramManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide = "com.uberverse.arkcraft.client.book.proxy.BookClient",
			serverSide = "com.uberverse.arkcraft.client.book.proxy.BookCommon")
	public static BookCommon dossierProxy;

	public CommonProxy()
	{}

	public void preInit()
	{
		EngramManager.init();
	}

	public void init()
	{}

	public void postInit()
	{}

	public void registerPreRenderers()
	{}

	public void registerRenderers()
	{}

	public void registerEventHandlers()
	{
		PlayerCommonEventHandler p = new PlayerCommonEventHandler();
		FMLCommonHandler.instance().bus().register(p);
		MinecraftForge.EVENT_BUS.register(p);
	}

	public EntityPlayer getPlayer()
	{
		return null;
	}
}
