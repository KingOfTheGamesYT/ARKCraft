package com.arkcraft.common.item.tools;

import com.arkcraft.ARKCraft;
import com.arkcraft.client.proxy.ClientProxy;
import com.arkcraft.common.arkplayer.ARKPlayer;
import com.arkcraft.init.ARKCraftItems;
import com.arkcraft.util.Utils;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public abstract class ARKCraftTool extends ItemTool {
	private static final String DAMAGE_NBT_NAME = "damage";
	private static final Predicate<IBlockState> WOOD_PREDICATE = new Predicate<IBlockState>() {

		@Override
		public boolean apply(IBlockState blockState) {
			return blockState.getBlock() == Blocks.LOG || blockState.getBlock() == Blocks.LOG2;
		}
	};
	private static final Predicate<IBlockState> IRON_ORE_PREDICATE = new Predicate<IBlockState>() {

		@Override
		public boolean apply(IBlockState blockState) {
			return blockState.getBlock() == Blocks.IRON_ORE;
		}
	};
	public static int count = 0;
	public final ToolType toolType;
	boolean arkMode;

	public ARKCraftTool(float attackDamage, ToolMaterial material, Set<Block> effectiveBlocks, ToolType toolType) {
		super(attackDamage, attackDamage, material, effectiveBlocks);
		this.setCreativeTab(ARKCraft.tabARK);
		setHasSubtypes(true);
		this.toolType = toolType;
		initToolClasses();
	}

	public ARKCraftTool(String name, float attackDamage, ToolMaterial material, Set<Block> effectiveBlocks, ToolType toolType) {
		this(attackDamage, material, effectiveBlocks, toolType);
		this.setCreativeTab(ARKCraft.tabARK);
		this.setTranslationKey(name);
	}

	private static int calcOutput(int count, float toolModifier, double materialModifier) {
		double ret = 0;
		for (int i = 0; i < count; i++) {
			double r = (10 + itemRand.nextInt(100)) / 30D;
			r *= materialModifier;
			r *= toolModifier;
			ret += r;
		}
		return MathHelper.floor(ret);
	}

	private void initToolClasses() {
		Set<String> c = getToolClasses(new ItemStack(this));
		for (String o : c) {
			if (o != null) {
				setHarvestLevel(o, toolMaterial.getHarvestLevel());
			}
		}
	}

	// For the ItemDrop
	private void entityDropItem(World worldIn, BlockPos pos, Block block, EntityPlayer playerIn, ItemStack itemStackIn) {
		if (itemStackIn.getCount() != 0 && itemStackIn.getItem() != null) {
			Float offset = worldIn.rand.nextFloat();
			EntityItem entityitem = new EntityItem(worldIn, pos.getX() + offset, pos.getY() + 0.5, pos.getZ() + offset, itemStackIn);
			entityitem.setDefaultPickupDelay();
			if (playerIn.captureDrops) {
				playerIn.capturedDrops.add(entityitem);
			} else {
				worldIn.spawnEntity(entityitem);
			}
		}
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
									EntityLivingBase entityLiving) {
		if (!(entityLiving instanceof EntityPlayer)) return false;
		EntityPlayer playerIn = (EntityPlayer) entityLiving;
		Block blockIn = state.getBlock();
		if (playerIn instanceof EntityPlayer && ARKPlayer.isARKMode(playerIn)) {
			EntityPlayer player = playerIn;
			IBlockState blockState = worldIn.getBlockState(pos);
			if (WOOD_PREDICATE.apply(blockState)) {

				this.destroyBlocks(worldIn, pos, player, stack, WOOD_PREDICATE, EnumHand.MAIN_HAND);
				int wood = calcOutput(count, toolType.getPickaxeModifier(), 1);
				int thatch = calcOutput(count, toolType.getPickaxeModifier(), 1);
				entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.wood, wood));// (int)
				// (10
				// +
				// itemRand.nextInt(100)/20.0*count*toolType.getHatchetModifier())
				entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.thatch, thatch));// (int)
				// (10
				// +
				// itemRand.nextInt(100)/20.0*count*toolType.getPickaxeModifier())

				// thatch);
				count = 0;

				// this.destroyBlocks(worldIn, pos, player, stack);

				// entityDropItem(worldIn, pos, blockIn, player, new
				// ItemStack(ARKCraftItems.wood, (int) (10 +
				// itemRand.nextInt(100)/20.0*count*toolType.getPrimaryModifier())));
				// entityDropItem(worldIn, pos, blockIn, player, new
				// ItemStack(ARKCraftItems.thatch, (int) (10 +
				// itemRand.nextInt(100)/20.0*count*toolType.getPrimaryModifier())));

				// thatch);
				count = 0;

			} else if (blockState.getBlock() == Blocks.STONE) {
				damageTool(stack, playerIn, EnumHand.MAIN_HAND);
				int multiplier = 0;
				{
					IBlockState blockState2 = worldIn.getBlockState(pos.up());
					if (blockState2.getBlock() == Blocks.STONE) {
						multiplier++;
						worldIn.destroyBlock(pos.up(), false);
					}
				}
				{
					IBlockState blockState2 = worldIn.getBlockState(pos);
					if (blockState2.getBlock() == Blocks.STONE) {
						multiplier++;
						worldIn.destroyBlock(pos, false);
					}
				}
				{
					IBlockState blockState2 = worldIn.getBlockState(pos.down());
					if (blockState2.getBlock() == Blocks.STONE) {
						multiplier++;
						worldIn.destroyBlock(pos.down(), false);
					}
				}
				EnumFacing f = Utils.getDirectionFacing(playerIn, false);
				{
					BlockPos pos2 = pos.offset(f.rotateY());
					{
						IBlockState blockState2 = worldIn.getBlockState(pos2.up());
						if (blockState2.getBlock() == Blocks.STONE) {
							multiplier++;
							worldIn.destroyBlock(pos2.up(), false);
						}
					}
					{
						IBlockState blockState2 = worldIn.getBlockState(pos2);
						if (blockState2.getBlock() == Blocks.STONE) {
							multiplier++;
							worldIn.destroyBlock(pos2, false);
						}
					}
					{
						IBlockState blockState2 = worldIn.getBlockState(pos2.down());
						if (blockState2.getBlock() == Blocks.STONE) {
							multiplier++;
							worldIn.destroyBlock(pos2.down(), false);
						}
					}
				}
				{
					BlockPos pos2 = pos.offset(f.rotateYCCW());
					{
						IBlockState blockState2 = worldIn.getBlockState(pos2.up());
						if (blockState2.getBlock() == Blocks.STONE) {
							multiplier++;
							worldIn.destroyBlock(pos2.up(), false);
						}
					}
					{
						IBlockState blockState2 = worldIn.getBlockState(pos2);
						if (blockState2.getBlock() == Blocks.STONE) {
							multiplier++;
							worldIn.destroyBlock(pos2, false);
						}
					}
					{
						IBlockState blockState2 = worldIn.getBlockState(pos2.down());
						if (blockState2.getBlock() == Blocks.STONE) {
							multiplier++;
							worldIn.destroyBlock(pos2.down(), false);
						}
					}
				}

				int stone = calcOutput(multiplier, toolType.getPickaxeModifier(), 1);
				int flint = calcOutput(multiplier, toolType.getHatchetModifier(), 1);
				int metal = calcOutput(multiplier, toolType.getPickaxeModifier(), 0.1D);
				entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.stone, stone));// (int)
				// (10
				// +
				// itemRand.nextInt(100)/20.0*multiplier*toolType.getPickaxeModifier())
				entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.flint, flint));// (int)
				// (10
				// +
				// itemRand.nextInt(100)/20.0*multiplier*toolType.getHatchetModifier())
				entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.metal, metal));// (int)
				// (1
				// +
				// itemRand.nextInt(100)/20.0*multiplier*toolType.getPickaxeModifier())
			} else if (IRON_ORE_PREDICATE.apply(blockState)) {
				this.destroyBlocks(worldIn, pos, player, stack, IRON_ORE_PREDICATE, EnumHand.MAIN_HAND);
				int stone = calcOutput(count, toolType.getPickaxeModifier(), 0.8D);
				int metal = calcOutput(count, toolType.getPickaxeModifier(), 1);
				entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.metal, metal));// (int)
				// (10
				// +
				// itemRand.nextInt(100)/20.0*count*toolType.getPickaxeModifier())
				entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.stone, stone));// (int)
				// (10
				// +
				// itemRand.nextInt(100)/20.0*count*toolType.getHatchetModifier())

				// thatch);
				count = 0;

				// entityDropItem(worldIn, pos, blockIn, player, new
				// ItemStack(ARKCraftItems.stone, (int) (10 +
				// itemRand.nextInt(100)/20.0*multiplier*toolType.getPrimaryModifier())));
				// entityDropItem(worldIn, pos, blockIn, player, new
				// ItemStack(ARKCraftItems.flint, (int) (10 +
				// itemRand.nextInt(100)/20.0*multiplier*toolType.getPrimaryModifier())));
				// entityDropItem(worldIn, pos, blockIn, player, new
				// ItemStack(ARKCraftItems.metal, (int) (1 +
				// itemRand.nextInt(100)/20.0*multiplier*toolType.getPrimaryModifier())));
			}
		} else {
			damageTool(stack, playerIn, EnumHand.MAIN_HAND);
		}
		return true;
	}

	public void destroyBlocks(World world, BlockPos pos, EntityPlayer player, ItemStack stack,
							  Predicate<IBlockState> blockChecker, EnumHand hand) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		for (int i = x - 1; i <= x + 1; i++) {
			for (int k = z - 1; k <= z + 1; k++) {
				for (int j = y - 1; j <= y + 1; j++) {
					IBlockState blockState = world.getBlockState(new BlockPos(i, j, k));
					if (blockChecker.apply(blockState)) {
						world.destroyBlock(new BlockPos(i, j, k), false);
						++count;
						if (damageTool(stack, player, hand)) {
							this.destroyBlocks(world, new BlockPos(i, j, k), player, stack, blockChecker, hand);
						} else {
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, ToolLevel.VALUES.length - 1));
	}

	@SideOnly(Side.CLIENT)
	public void registerModels() {
		// TODO: Call this from the Client proxy for each tool item.
		ClientProxy p = ((ClientProxy) ARKCraft.proxy);
		NonNullList<ItemStack> list = NonNullList.create();
		getSubItems(getCreativeTab(), list);
		for (int i = 0; i < list.size(); i++) {
			p.registerItemTexture(this, i, "tool_" + getTranslationKey().substring(5) + "_"
					+ ToolLevel.VALUES[i].name);
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(I18n.format("arkcraft.tooltip.toolLevel", ToolLevel.VALUES[stack.getMetadata()
				% ToolLevel.VALUES.length].getTranslatedName()));
		if (flag.isAdvanced()) {
			int max = ToolLevel.VALUES[stack.getMetadata() % ToolLevel.VALUES.length].getDurrability(toolMaterial
					.getMaxUses());
			tooltip.add("Durability: " + max + "/" + (max - getToolDamage(stack)));
		}
	}

	public int getDurability(ItemStack stack) {
		return ToolLevel.VALUES[stack.getMetadata() % ToolLevel.VALUES.length].getDurrability(toolMaterial
				.getMaxUses());
	}

	public int getToolDamage(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return stack.getTagCompound().getInteger(DAMAGE_NBT_NAME);
		}
		return 0;
	}

	public void setToolDamage(ItemStack stack, int newValue) {
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger(DAMAGE_NBT_NAME, newValue);
	}

	public boolean damageTool(ItemStack toolStack, EntityLivingBase entityIn, EnumHand hand) {
		int newValue = getToolDamage(toolStack) + 1;
		setToolDamage(toolStack, newValue);
		if (newValue >= getDurability(toolStack)) {
			entityIn.renderBrokenItemStack(toolStack);
			toolStack.shrink(1);

			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityIn;
				entityplayer.addStat(StatList.getObjectBreakStats(toolStack.getItem()));
				if (toolStack.getCount() < 1) {
					entityplayer.setHeldItem(hand, null);
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getToolDamage(stack) > 0;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return ((double) getToolDamage(stack)) / getDurability(stack);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		damageTool(stack, target, EnumHand.MAIN_HAND);
		damageTool(stack, target, EnumHand.MAIN_HAND);
		return true;
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		return false;
	}
}
