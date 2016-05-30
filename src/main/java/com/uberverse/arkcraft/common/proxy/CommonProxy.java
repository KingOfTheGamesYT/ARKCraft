package com.uberverse.arkcraft.common.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.uberverse.arkcraft.common.event.CommonEventHandler;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftEntities;
import com.uberverse.arkcraft.init.ARKCraftItems;

public abstract class CommonProxy
{
	public void preInit()
	{
		ARKCraftItems.init();
		ARKCraftBlocks.init();
		ARKCraftEntities.init();
	}

	public void init()
	{
		CommonEventHandler coreEventHandler = new CommonEventHandler();
		MinecraftForge.EVENT_BUS.register(coreEventHandler);
		FMLCommonHandler.instance().bus().register(coreEventHandler);
	}

	public void postInit()
	{

	}

}
