package com.uberverse.arkcraft.client.proxy;

import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftItems;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void init()
	{
		registerRenderers();
	}

	private void registerRenderers()
	{
		ARKCraftItems.registerRenderers();
	//	ARKCraftBlocks.registerRenderers();	
	}
}