package com.uberverse.arkcraft.wip.blueprint;

import java.util.List;

import com.uberverse.arkcraft.common.engram.EngramManager;
import com.uberverse.arkcraft.common.engram.EngramManager.Engram;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;

public class SmartBlueprintModel implements ISmartItemModel
{
	private IBakedModel bakedModel;

	@Override
	public List getFaceQuads(EnumFacing facing)
	{
		return bakedModel.getFaceQuads(facing);
	}

	@Override
	public List getGeneralQuads()
	{
		return bakedModel.getGeneralQuads();
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return bakedModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return bakedModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return bakedModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getTexture()
	{
		return bakedModel.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return bakedModel.getItemCameraTransforms();
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack)
	{
		Engram e = EngramManager.instance().getEngram(stack.getTagCompound().getShort("engram"));
		if (e != null)
		{
			bakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(e.getItem()));
		}
		return bakedModel;
	}
}
