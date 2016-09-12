package com.uberverse.arkcraft.wip.itemquality;

import com.google.common.collect.Multimap;
import com.uberverse.arkcraft.common.block.ARKMark;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

public abstract class ItemToolBase extends ItemQualitable
{
	private final double baseBreakSpeed;
	private final double baseDamage;
	private final ToolType toolType;
	protected float divider = 1;

	public ItemToolBase(int baseDurability, double baseBreakSpeed, double baseDamage, ItemType type, ToolType toolType)
	{
		super(baseDurability, type);
		this.baseBreakSpeed = baseBreakSpeed;
		this.baseDamage = baseDamage;
		this.toolType = toolType;
	}

	@Override
	public float getDigSpeed(ItemStack itemstack, IBlockState state)
	{
		float base = 1F * (float) getBreakSpeed(itemstack);
		return MathHelper.clamp_float(base * getSpeedDivider(itemstack), 0.1f, base);
	}

	protected float getSpeedDivider(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			setSpeedDivider(stack, 1);
		}
		return stack.getTagCompound().getFloat("divider");
	}

	protected void setSpeedDivider(ItemStack stack, float value)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setFloat("divider", value);
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
