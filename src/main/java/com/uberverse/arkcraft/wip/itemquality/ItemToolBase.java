package com.uberverse.arkcraft.wip.itemquality;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import com.google.common.collect.Multimap;
import com.uberverse.arkcraft.common.block.ARKMark;
import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class ItemToolBase extends ItemQualitable
{
	private final double baseBreakSpeed;
	private final double baseDamage;
	private final ToolType toolType;
	private final Block[] effectiveBlocks;

	public ItemToolBase(int baseDurability, double baseBreakSpeed, double baseDamage, ItemType type, ToolType toolType, Block... effectiveBlocks)
	{
		super(baseDurability, type);
		this.baseBreakSpeed = baseBreakSpeed;
		this.baseDamage = baseDamage;
		this.toolType = toolType;
		this.effectiveBlocks = effectiveBlocks;
	}

	@Override
	public float getDigSpeed(ItemStack itemstack, IBlockState state)
	{
		float base = 10f * (float) getBreakSpeed(itemstack);
		return MathHelper.clamp_float(base * getSpeedDivider(itemstack), 0.1f, base);
	}

	private float getSpeedDivider(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			setSpeedDivider(stack, -1);
		}
		return stack.getTagCompound().getFloat("divider");
	}

	private void setSpeedDivider(ItemStack stack, float value)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setFloat("divider", value);
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			// MovingObjectPosition mop = getMovingObjectPositionFromPlayer(entityLiving.worldObj, (EntityPlayer) entityLiving, false);
			MovingObjectPosition mop = ItemRangedWeapon.rayTrace(entityLiving, 5, 0);
			if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				IBlockState bs = entityLiving.worldObj.getBlockState(mop.getBlockPos());
				Block target = null;
				for (Block b : effectiveBlocks)
				{
					if (b == bs.getBlock())
					{
						target = bs.getBlock();
						break;
					}
				}
				if (target != null)
				{
					World w = entityLiving.worldObj;
					BlockPos pos = mop.getBlockPos();
					int size = 0;
					if (target instanceof BlockLog) size = countTree(w, pos, target);
					else size = countOre(w, pos, target);
					setSpeedDivider(stack, (float) 1 / (float) size);
				}
				return false;
			}
		}
		// TODO else do harvesting (give stuff based on stack and block)
		return super.onEntitySwing(entityLiving, stack);
	}

	/**
	 * This method can provide a general idea of the size of a uniform block formation being of variable height but limited width, e.g. a tree.
	 * For more accuracy use countOre();
	 * 
	 * @param world
	 * @param start
	 * @param target
	 * @return
	 */
	private int countTree(World world, final BlockPos start, Block target)
	{
		BlockPos pos = start;

		int height = 0;
		while (world.getBlockState(pos).getBlock() == target)
		{
			height++;
			pos = pos.up();
		}

		pos = start.down();

		while (world.getBlockState(pos).getBlock() == target)
		{
			pos = pos.down();
			height++;
		}

		pos = start;
		int width = 1;
		if (world.getBlockState(pos.north()).getBlock() == target || world.getBlockState(pos.south()).getBlock() == target
				|| world.getBlockState(pos.east()).getBlock() == target || world.getBlockState(pos.west()).getBlock() == target)
			width++;

		return width * width * height;
	}

	private int countOre(World world, final BlockPos start, Block target)
	{
		return countOre(world, start, target, false);
	}

	private int countOre(World world, final BlockPos start, Block target, boolean shouldBreak)
	{
		BlockPos pos = start;
		Collection<BlockPos> done = new HashSet<>();
		Queue<BlockPos> queue = new LinkedList<>();

		queue.add(pos);

		while (!queue.isEmpty())
		{
			pos = queue.remove();
			int i = pos.getX();
			int j = pos.getY();
			int k = pos.getZ();

			for (int x = i - 1; x <= i + 1; x++)
			{
				for (int z = k - 1; z <= k + 1; z++)
				{
					for (int y = j - 1; y <= j + 1; y++)
					{
						if (!(x == i && y == j && k == z))
						{
							BlockPos n = new BlockPos(x, y, z);
							IBlockState blockState = world.getBlockState(n);
							if (blockState.getBlock() == target)
							{
								if (!done.contains(n)) queue.add(n);
							}
						}
					}
				}
			}
			done.add(pos);
		}

		if (shouldBreak) for (BlockPos p : done)
			world.destroyBlock(p, false);

		return done.size();
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player)
	{
		boolean found = false;
		for (Block b : effectiveBlocks)
		{
			if (b == block)
			{
				found = true;
				break;
			}
		}
		if (found)
		{
			countOre(world, pos, block, true);
			return true;
		}
		return super.onBlockDestroyed(stack, world, block, pos, player);
	}

	@Override
	public boolean canHarvestBlock(Block block)
	{
		return block instanceof ARKMark;
	}

	@Override
	public boolean canHarvestBlock(Block block, ItemStack itemStack)
	{
		return canHarvestBlock(block);
	}

	public double getAttackDamage(ItemStack stack)
	{
		return Qualitable.get(stack).multiplierTreshold * baseDamage;
	}

	public double getBreakSpeed(ItemStack stack)
	{
		return Qualitable.get(stack).multiplierTreshold * baseBreakSpeed;
	}

	protected double getThatchModifier()
	{
		return toolType.thatchModifier;
	}

	protected double getWoodModifier()
	{
		return toolType.woodModifier;
	}

	protected double getCrystalModifier()
	{
		return toolType.crystalModifier;
	}

	protected double getFlintModifier()
	{
		return toolType.flintModifier;
	}

	protected double getHideModifier()
	{
		return toolType.hideModifier;
	}

	protected double getMeatModifier()
	{
		return toolType.meatModifier;
	}

	protected double getStoneModifier()
	{
		return toolType.stoneModifier;
	}

	protected double getMetalModifier()
	{
		return toolType.metalModifier;
	}

	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
		Multimap map = super.getAttributeModifiers(stack);
		map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
				new AttributeModifier(itemModifierUUID, "attack_damage", getAttackDamage(stack), 0));
		return map;
	}

	private static final double change = 0.2, c = 1, cm = c - change, cmm = cm - change, cmmm = cmm - change, cp = c + change, cpp = cp + change,
			cppp = cpp + change;

	public enum ToolType
	{
		PICK(c, cmm, cmm, cmmm, cm, cm, c, cmm), HATCHET(cmm, c, cmmm, cm, cmm, cmm, cmm, c);
		private final double thatchModifier, woodModifier, metalModifier, stoneModifier, crystalModifier, flintModifier, meatModifier, hideModifier;

		private ToolType(double thatchModifier, double woodModifier, double metalModifier, double stoneModifier, double crystalModifier, double flintModifier, double meatModifier, double hideModifier)
		{
			this.thatchModifier = thatchModifier;
			this.woodModifier = woodModifier;
			this.metalModifier = metalModifier;
			this.stoneModifier = stoneModifier;
			this.crystalModifier = crystalModifier;
			this.flintModifier = flintModifier;
			this.meatModifier = meatModifier;
			this.hideModifier = hideModifier;
		}
	}
}
