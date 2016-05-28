package com.uberverse.arkcraft.client.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.uberverse.arkcraft.common.proxy.CommonProxy;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void init()
	{
		registerRenderers();
	}

	private void registerRenderers()
	{

	}
}