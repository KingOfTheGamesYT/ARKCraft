package com.uberverse.arkcraft.wip.blueprint;

import com.uberverse.arkcraft.client.util.SmartReplacingItemModel;
import com.uberverse.arkcraft.common.engram.EngramManager;
import com.uberverse.arkcraft.common.engram.EngramManager.Engram;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class SmartBlueprintModel extends SmartReplacingItemModel
{
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("arkcraft:items/blueprint", "inventory");
	public static final TextureAtlasSprite texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("arkcraft:items/blueprint");

	private static final float delta = 0.001f;
	private static final float thickness = 1 / 32.0F;
	private static final float zCentre = 0.25F;
	private static final float zMax = zCentre + thickness / 2.0F;
	private static final float zMin = zCentre - thickness / 2.0F;
	private static final float southFacePos = 1.0F;
	private static final float northFacePos = 0.0F;
	private static final float distBehindSouth = southFacePos - zMax;
	private static final float distBehindNorth = zMin - northFacePos;

	public static final BakedQuad bpNorth =
			BakedModelUtil.createBakedQuadForFace(0.5f, 1f, 0.5f, 1f, -distBehindSouth + delta, 0, texture, EnumFacing.SOUTH);
	public static final BakedQuad bpSouth =
			BakedModelUtil.createBakedQuadForFace(0.5f, 1f, 0.5f, 1f, -distBehindNorth + delta, 0, texture, EnumFacing.NORTH);

	@Override
	public IBakedModel getStackModel(ItemStack stack)
	{
		Engram e = EngramManager.instance().getEngram(stack.getTagCompound().getShort("engram"));
		if (e != null)
		{
			toRender = e.getItem();
			return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(e.getItem()));
		}
		return null;
	}
}
