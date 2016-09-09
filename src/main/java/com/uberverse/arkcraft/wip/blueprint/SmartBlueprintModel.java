package com.uberverse.arkcraft.wip.blueprint;

import com.uberverse.arkcraft.client.util.SmartReplacingItemModel;
import com.uberverse.arkcraft.common.engram.EngramManager;
import com.uberverse.arkcraft.common.engram.EngramManager.Engram;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;

public class SmartBlueprintModel extends SmartReplacingItemModel
{
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
