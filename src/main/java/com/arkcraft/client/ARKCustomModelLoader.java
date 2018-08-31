package com.arkcraft.client;

import java.util.HashMap;
import java.util.Map;

import com.arkcraft.ARKCraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ARKCustomModelLoader implements ICustomModelLoader{
	protected IResourceManager manager;
	public static ARKCustomModelLoader instance = new ARKCustomModelLoader();
	public Map<ResourceLocation, IModel> modelMap = new HashMap<>();
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		manager = resourceManager;
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelMap.containsKey(modelLocation) || modelMap.containsKey(new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath()));
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		IModel model = modelMap.get(modelLocation);
		if(model == null)model = modelMap.get(new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath()));
		if(model == null)model = ModelLoaderRegistry.getMissingModel();
		return model;
	}
	public static void init(){
		ARKCraft.logger.info("Loading Custom Model Loader");
		ModelLoaderRegistry.registerLoader(instance);
	}
}
