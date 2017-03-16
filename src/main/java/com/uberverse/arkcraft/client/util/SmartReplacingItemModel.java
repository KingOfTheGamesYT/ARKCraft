package com.uberverse.arkcraft.client.util;

import java.util.List;

import javax.vecmath.Vector3f;

import com.uberverse.arkcraft.common.item.ranged.ItemShotgun;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public abstract class SmartReplacingItemModel implements ISmartItemModel
{
	private IBakedModel bakedModel;
	protected Item toRender;

	public IBakedModel getBakedModel()
	{
		return bakedModel;
	}

	@Override
	public List getFaceQuads(EnumFacing side)
	{
		return bakedModel.getFaceQuads(side);
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
		ItemCameraTransforms ict = bakedModel.getItemCameraTransforms();
		ItemTransformVec3f gui = ItemCameraTransforms.DEFAULT.gui;
		ItemTransformVec3f head = ItemCameraTransforms.DEFAULT.head;
		ItemTransformVec3f thirdPerson = new ItemTransformVec3f(new Vector3f(-90f, 0f, 0f), new Vector3f(0f, 0.0625f,
				-0.188f), new Vector3f(0.55f, 0.55f, 0.55f));
		ItemTransformVec3f firstPerson = new ItemTransformVec3f(new Vector3f(0, -135, 25), new Vector3f(0f, 0.25f,
				0.125f), new Vector3f(1.7f, 1.7f, 1.7f));

		if (toRender instanceof ItemShotgun)
		{
			gui = new ItemTransformVec3f(new Vector3f(-26, 16, 40), new Vector3f(0, 0.30f, -0.20f), new Vector3f(1.8f,
					1.8f, 1.8f));
		}
		ItemCameraTransforms ictN = new ItemCameraTransforms(thirdPerson, firstPerson, head, gui);

		return ictN;
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack)
	{
		bakedModel = getStackModel(stack);
		return this;
	}

	public abstract IBakedModel getStackModel(ItemStack stack);
}
