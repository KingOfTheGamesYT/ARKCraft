package com.arkcraft.common.item;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemWaterContainer extends ARKCraftItem {
	public int maxWaterValue;

	public ItemWaterContainer(int maxWaterValue) {
		this.maxWaterValue = maxWaterValue;
		setMaxStackSize(1);
	}

	public static int getWaterValueLeft(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().getInteger("water") : 0;
	}

	public static void setWaterValueLeft(ItemStack stack, int f) {
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("water", f);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getWaterValueLeft(stack) < maxWaterValue;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1d - (double) getWaterValueLeft(stack) / (double) maxWaterValue;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		ItemStack s = new ItemStack(this);
		setWaterValueLeft(s, maxWaterValue);
		items.add(s);
	}

	public final boolean isRaining(EntityPlayer player, World world) {
		return world.isRaining() && world.canSeeSky(player.getPosition());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);

		RayTraceResult movingobjectposition = this.rayTrace(worldIn, playerIn, true);

		if (movingobjectposition == null) {
			return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
		} else {
			if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
				ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(playerIn, worldIn, itemStackIn,
						movingobjectposition);
				if (ret != null) return ret;

				BlockPos blockpos = movingobjectposition.getBlockPos();
				IBlockState blockState = worldIn.getBlockState(blockpos);
				Material material = blockState.getMaterial();

				if (material == Material.WATER && blockState.getValue(BlockLiquid.LEVEL).intValue() == 0)
					setWaterValueLeft(itemStackIn, maxWaterValue);
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (worldIn.isRemote) return;

		if (isRaining((EntityPlayer) entityIn, worldIn) && worldIn.getTotalWorldTime() % 20 == 0) setWaterValueLeft(
				stack, MathHelper.clamp(getWaterValueLeft(stack) + 1, 0, maxWaterValue));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return oldStack != null && newStack != null && oldStack.getItem() != newStack.getItem() && slotChanged;
	}

	private boolean canDrink(ItemStack stack) {
		return getWaterValueLeft(stack) / 100 > 0;
	}
}
