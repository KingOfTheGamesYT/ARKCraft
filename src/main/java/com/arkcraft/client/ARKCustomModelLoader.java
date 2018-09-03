package com.arkcraft.client;

import com.arkcraft.ARKCraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.util.HashMap;
import java.util.Map;

public class ARKCustomModelLoader implements ICustomModelLoader {
	public static ARKCustomModelLoader instance = new ARKCustomModelLoader();
	public Map<ResourceLocation, IModel> modelMap = new HashMap<>();
	protected IResourceManager manager;

	public static void init() {
		ARKCraft.logger.info("Loading Custom Model Loader");
		ModelLoaderRegistry.registerLoader(instance);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		manager = resourceManager;
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelMap.containsKey(modelLocation) || modelMap.containsKey(new ResourceLocation(modelLocation.getNamespace(), modelLocation.getPath()));
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		IModel model = modelMap.get(modelLocation);
		if (model == null)
			model = modelMap.get(new ResourceLocation(modelLocation.getNamespace(), modelLocation.getPath()));
		if (model == null) model = ModelLoaderRegistry.getMissingModel();
		return model;
	}
}
