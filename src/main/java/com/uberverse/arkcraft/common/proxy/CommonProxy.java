package com.uberverse.arkcraft.common.proxy;

import net.minecraft.entity.player.EntityPlayer;

public class CommonProxy
{
	 // @SidedProxy(clientSide = "com.arkcraft.module.core.client.gui.book.proxy.DClient", serverSide = "com.arkcraft.module.core.client.gui.book.proxy.DCommon")
	//    public static DCommon dossierProxy;

	    public CommonProxy() {}
	    
	    public void init() {}

	    public void registerPreRenderers() {}

	    public void registerRenderers() {}

	    public void registerEventHandlers(){}

	    public EntityPlayer getPlayer()
	    {
	        return null;
	    }
}
