package com.arkcraft;

import com.arkcraft.common.creativetabs.ARKBlueprintTab;
import com.arkcraft.common.creativetabs.ARKCreativeTab;
import com.arkcraft.common.proxy.CommonProxy;
import com.arkcraft.lib.Logger;
import com.tom.soundregistry.ARKSoundRegistry;
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
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = ARKCraft.MODID, updateJSON = ARKCraft.UPDATE_JSON, useMetadata = true)
public class ARKCraft {
	public static final String MODID = "arkcraft";
	protected static final String UPDATE_JSON =
			"https://raw.githubusercontent.com/BubbleTrouble14/ARKCraft/master/version-check.json";
	public static CreativeTabs tabARK = new ARKCreativeTab();
	public static CreativeTabs tabARKBlueprints = new ARKBlueprintTab();
	public static SimpleNetworkWrapper modChannel;
	public static Logger logger;
	public static CheckResult versionCheckResult;
	@SidedProxy(clientSide = "com.arkcraft.client.ClientProxy",
			serverSide = "com.arkcraft.server.proxy.ServerProxy")
	private static CommonProxy proxy;
	@Instance(ARKCraft.MODID)
	private static ARKCraft instance;
	private static ModContainer modContainer = Loader.instance().activeModContainer();

	public static void updateCheckResult() {
		CheckResult r = ForgeVersion.getResult(modContainer);
		if (r != null && r.status != Status.PENDING) versionCheckResult = r;
	}

	public static ARKCraft instance() {
		return instance;
	}

	public static CommonProxy proxy() {
		return proxy;
	}

	public static String version() {
		return modContainer.getVersion();
	}

	public static String modid() {
		return MODID;
	}

	public static String name() {
		return modContainer.getName();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = new com.arkcraft.lib.Logger(event.getModLog());
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ARKSoundRegistry.init();
		proxy.init(event);
		updateCheckResult();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		modContainer.setEnabledState(false);
		proxy.postInit(event);
		updateCheckResult();
	}
}