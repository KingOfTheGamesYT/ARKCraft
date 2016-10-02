package com.uberverse.arkcraft;

import org.apache.logging.log4j.Logger;

import com.uberverse.arkcraft.common.creativetabs.ARKBlueprintTab;
import com.uberverse.arkcraft.common.creativetabs.ARKCreativeTab;
import com.uberverse.arkcraft.common.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = ARKCraft.MODID, updateJSON = ARKCraft.UPDATE_JSON, useMetadata = true)
public class ARKCraft
{
	public static final String MODID = "arkcraft";

	public static final String descriptionPacketChannel = MODID + ":descPacket";
	protected static final String UPDATE_JSON =
			"https://raw.githubusercontent.com/BubbleTrouble14/ARKCraft/master/version-check.json";

	@Instance(ARKCraft.MODID)
	private static ARKCraft instance;

	@SidedProxy(clientSide = "com.uberverse.arkcraft.client.proxy.ClientProxy",
			serverSide = "com.uberverse.arkcraft.server.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static CreativeTabs tabARK = new ARKCreativeTab();
	public static CreativeTabs tabARKBlueprints = new ARKBlueprintTab();
	public static SimpleNetworkWrapper modChannel;
	public static Logger logger;
	public final static EventBus EVENT_BUS = new EventBus();
	public static CheckResult versionCheckResult;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = new com.uberverse.lib.Logger(event.getModLog());
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
		updateCheckResult();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		modContainer.setEnabledState(false);
		proxy.postInit(event);
		updateCheckResult();
	}

	private static ModContainer modContainer = Loader.instance().activeModContainer();

	public static void updateCheckResult()
	{
		CheckResult r = ForgeVersion.getResult(modContainer);
		if (r != null && r.status != Status.PENDING) versionCheckResult = r;
	}

	public static ARKCraft instance()
	{
		return instance;
	}

	public boolean isDebugger()
	{
		return "${version}".equals(version());
	}

	public String version()
	{
		return modContainer.getVersion();
	}

	public String modid()
	{
		return modContainer.getModId();
	}

	public String name()
	{
		return modContainer.getName();
	}
}