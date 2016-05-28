package com.uberverse.arkcraft.common.proxy;

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
	
	}

	public void postInit()
	{

	}
}
