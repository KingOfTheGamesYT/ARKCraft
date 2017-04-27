package com.uberverse.arkcraft.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.ModelProcessingHelper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.BlockCable;
import com.uberverse.arkcraft.common.tileentity.TileEntityCable;

import com.google.common.base.Function;

public class ModelCable implements IModel {
	private List<ResourceLocation> textures;
	public ModelCable() {
		textures = new ArrayList<>();
		textures.add(new ResourceLocation("arkcraft:blocks/cable_top"));
		textures.add(new ResourceLocation("arkcraft:blocks/cable_side"));
	}
	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return textures;
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		ARKCraft.logger.info("Baking Cable Model");
		ResourceLocation noc = new ResourceLocation("arkcraft:block/cable_noc");
		ResourceLocation center = new ResourceLocation("arkcraft:block/cable_base");
		ResourceLocation connection = new ResourceLocation("arkcraft:block/cable_c");
		ResourceLocation vertical = new ResourceLocation("arkcraft:block/cable_v");
		return new BakedCableModel(getAndBakeModel(noc, state, format, bakedTextureGetter),
				getAndBakeModel(center, state, format, bakedTextureGetter),
				getAndBakeModel(connection, state, format, bakedTextureGetter),
				getAndBakeModel(vertical, state, format, bakedTextureGetter));
	}
	private static IBakedModel getAndBakeModel(ResourceLocation loc, IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		return ModelProcessingHelper.uvlock(ModelLoaderRegistry.getModelOrLogError(loc, "Couldn't load " + loc.toString() + " for arkcraft:cable"), true).bake(state, format, bakedTextureGetter);
	}
	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}
	public static class BakedCableModel implements IBakedModel {
		private IBakedModel noc, center, connect, vert;
		public BakedCableModel(IBakedModel noc, IBakedModel center, IBakedModel connect, IBakedModel vert) {
			this.noc = noc;
			this.center = center;
			this.connect = connect;
			this.vert = vert;
			ARKCraft.logger.info("Cable Model Baking Complete");
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
			if(state instanceof IExtendedBlockState && side == null){
				TileEntityCable te = ((IExtendedBlockState)state).getValue(BlockCable.DATA);
				List<BakedQuad> quads = new ArrayList<>();
				return quads;
			}else{
				return Collections.emptyList();
			}
		}

		@Override
		public boolean isAmbientOcclusion() {
			return noc.isAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return noc.isGui3d();
		}

		@Override
		public boolean isBuiltInRenderer() {
			return noc.isBuiltInRenderer();
		}

		@Override
		public TextureAtlasSprite getParticleTexture() {
			return noc.getParticleTexture();
		}

		@SuppressWarnings("deprecation")
		@Override
		public ItemCameraTransforms getItemCameraTransforms() {
			return noc.getItemCameraTransforms();
		}

		@Override
		public ItemOverrideList getOverrides() {
			return noc.getOverrides();
		}

	}
}
