package com.arkcraft.client.model;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.block.energy.BlockCable;
import com.arkcraft.common.tileentity.energy.TileEntityCable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ModelCable implements IModel {
	private List<ResourceLocation> textures;

	public ModelCable() {
		textures = new ArrayList<>();
		textures.add(new ResourceLocation("arkcraft:blocks/cable_top"));
		textures.add(new ResourceLocation("arkcraft:blocks/cable_side"));
		textures.add(new ResourceLocation("arkcraft:blocks/cable_top_powered"));
		textures.add(new ResourceLocation("arkcraft:blocks/cable_side_powered"));
	}

	private static IModel getModel(ResourceLocation loc) {
		return ModelLoaderRegistry.getModelOrLogError(loc, "Couldn't load " + loc.toString() + " for arkcraft:cable");
	}

	private static Matrix4f getMatrix(EnumFacing facing) {
		switch (facing) {
			case NORTH:
				return ModelRotation.X0_Y0.getMatrix();
			case SOUTH:
				return ModelRotation.X0_Y180.getMatrix();
			case WEST:
				return ModelRotation.X0_Y270.getMatrix();
			case EAST:
				return ModelRotation.X0_Y90.getMatrix();
			default:
				return new Matrix4f();
		}
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
		ResourceLocation centerP = new ResourceLocation("arkcraft:block/cable_base_powered");
		ResourceLocation connectionP = new ResourceLocation("arkcraft:block/cable_c_powered");
		ResourceLocation verticalP = new ResourceLocation("arkcraft:block/cable_v_powered");
		IBakedModel[] connections = new IBakedModel[4];
		IBakedModel[] connectionsP = new IBakedModel[4];
		IModel modelConnection = getModel(connection);
		IModel modelConnectionP = getModel(connectionP);
		for (EnumFacing f : EnumFacing.HORIZONTALS) {
			connections[f.ordinal() - 2] = modelConnection.bake(new TRSRTransformation(getMatrix(f)), format, bakedTextureGetter);
			connectionsP[f.ordinal() - 2] = modelConnectionP.bake(new TRSRTransformation(getMatrix(f)), format, bakedTextureGetter);
		}
		return new BakedCableModel(getModel(noc).bake(state, format, bakedTextureGetter),
				new IBakedModel[]{getModel(center).bake(state, format, bakedTextureGetter), getModel(centerP).bake(state, format, bakedTextureGetter)},
				new IBakedModel[][]{connections, connectionsP},
				new IBakedModel[]{getModel(vertical).bake(state, format, bakedTextureGetter), getModel(verticalP).bake(state, format, bakedTextureGetter)});
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

	public static class BakedCableModel implements IBakedModel {
		private IBakedModel noc;
		private IBakedModel[] center, vert;
		private IBakedModel[][] connect;

		public BakedCableModel(IBakedModel noc, IBakedModel[] center, IBakedModel[][] connect, IBakedModel[] vert) {
			this.noc = noc;
			this.center = center;
			this.connect = connect;
			this.vert = vert;
			ARKCraft.logger.info("Cable Model Baking Complete");
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
			if (state instanceof IExtendedBlockState && side == null) {
				TileEntityCable te = ((IExtendedBlockState) state).getValue(BlockCable.DATA);
				List<BakedQuad> quads = new ArrayList<>();
				int t = te.isPowered() ? 1 : 0;
				switch (te.type) {
					case NORMAL:
						if (te.connections == 0) quads.addAll(noc.getQuads(state, side, rand));
						else quads.addAll(center[t].getQuads(state, side, rand));
						break;
					case VERTICAL:
						if (te.connects(EnumFacing.UP)) quads.addAll(vert[t].getQuads(state, side, rand));
						else quads.addAll(center[t].getQuads(state, side, rand));
						break;
					default:
						break;

				}
				for (EnumFacing f : EnumFacing.HORIZONTALS) {
					if (te.connects(f)) {
						quads.addAll(connect[t][f.ordinal() - 2].getQuads(state, side, rand));
					}
				}
				return quads;
			} else {
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
