package com.uberverse.arkcraft.common.item.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.client.proxy.ClientProxy;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.lib.Utils;

public abstract class ARKCraftTool extends ItemTool{
	private static final String DAMAGE_NBT_NAME = "damage";
	boolean arkMode;
	public static  int count = 0;
	public final ToolType toolType;

	@SuppressWarnings("rawtypes")
	public ARKCraftTool(float attackDamage, ToolMaterial material, Set effectiveBlocks, ToolType toolType) {
		super(attackDamage, material, effectiveBlocks);
		setHasSubtypes(true);
		this.toolType = toolType;
	}

	@SuppressWarnings("rawtypes")
	public ARKCraftTool(String name, float attackDamage, ToolMaterial material, Set effectiveBlocks, ToolType toolType){
		super(attackDamage, material, effectiveBlocks);
		this.setUnlocalizedName(name);
		setHasSubtypes(true);
		this.toolType = toolType;
	}

	 //For the ItemDrop
	 private void entityDropItem(World worldIn, BlockPos pos, Block block, EntityPlayer playerIn, ItemStack itemStackIn) {
			if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
				Float offset = worldIn.rand.nextFloat();
				EntityItem entityitem = new EntityItem(worldIn, pos.getX() + offset, pos.getY() + block.getBlockBoundsMaxY(),
						pos.getZ() + offset, itemStackIn);
				entityitem.setDefaultPickupDelay();
				if (playerIn.captureDrops) {
					playerIn.capturedDrops.add(entityitem);
				} else {
					worldIn.spawnEntityInWorld(entityitem);
				}
			}
		} 

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
	{
		if (arkMode != ClientEventHandler.arkMode())
		{
			if(playerIn instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) playerIn;
				IBlockState blockState = worldIn.getBlockState(pos);
				if (blockState.getBlock() == Blocks.log || blockState.getBlock() == Blocks.log2)
				{
					this.destroyBlocks(worldIn, pos, player, stack);
					System.out.println("How many wood blocks ? " + count);
					
					entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.wood, (int) (10 + itemRand.nextInt(100)/20.0*count*toolType.getPrimaryModifier())));	
					entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.thatch, (int) (10 + itemRand.nextInt(100)/20.0*count*toolType.getPrimaryModifier())));	
					
				//	System.out.println(" Wood: " + wood + " Thatch: " + thatch);
					count = 0;
					
				}else if (blockState.getBlock() == Blocks.stone)
				{
					damageTool(stack, playerIn);
					int multiplier = 1;
					{
						IBlockState blockState2 = worldIn.getBlockState(pos.up());
						if (blockState2.getBlock() == Blocks.stone){
							multiplier++;
							worldIn.destroyBlock(pos.up(), false);
						}
					}
					{
						IBlockState blockState2 = worldIn.getBlockState(pos.down());
						if (blockState2.getBlock() == Blocks.stone){
							multiplier++;
							worldIn.destroyBlock(pos.down(), false);
						}
					}
					EnumFacing f = Utils.getDirectionFacing(playerIn, false);
					{
						BlockPos pos2 = pos.offset(f.rotateY());
						{
							IBlockState blockState2 = worldIn.getBlockState(pos2.up());
							if (blockState2.getBlock() == Blocks.stone){
								multiplier++;
								worldIn.destroyBlock(pos2.up(), false);
							}
						}
						{
							IBlockState blockState2 = worldIn.getBlockState(pos2);
							if (blockState2.getBlock() == Blocks.stone){
								multiplier++;
								worldIn.destroyBlock(pos2, false);
							}
						}
						{
							IBlockState blockState2 = worldIn.getBlockState(pos2.down());
							if (blockState2.getBlock() == Blocks.stone){
								multiplier++;
								worldIn.destroyBlock(pos2.down(), false);
							}
						}
					}
					{
						BlockPos pos2 = pos.offset(f.rotateYCCW());
						{
							IBlockState blockState2 = worldIn.getBlockState(pos2.up());
							if (blockState2.getBlock() == Blocks.stone){
								multiplier++;
								worldIn.destroyBlock(pos2.up(), false);
							}
						}
						{
							IBlockState blockState2 = worldIn.getBlockState(pos2);
							if (blockState2.getBlock() == Blocks.stone){
								multiplier++;
								worldIn.destroyBlock(pos2, false);
							}
						}
						{
							IBlockState blockState2 = worldIn.getBlockState(pos2.down());
							if (blockState2.getBlock() == Blocks.stone){
								multiplier++;
								worldIn.destroyBlock(pos2.down(), false);
							}
						}
					}
					entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.stone, (int) (10 + itemRand.nextInt(100)/20.0*multiplier*toolType.getPrimaryModifier())));		
					entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.flint, (int) (10 + itemRand.nextInt(100)/20.0*multiplier*toolType.getPrimaryModifier())));	
					entityDropItem(worldIn, pos, blockIn, player, new ItemStack(ARKCraftItems.metal, (int) (1 + itemRand.nextInt(100)/20.0*multiplier*toolType.getPrimaryModifier())));	
				}
			}
		}else{
			damageTool(stack, playerIn);
		}
		return true;
	}

	public void destroyBlocks(World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		for (int i = x - 1; i <= x + 1; i++) {
			for (int k = z - 1; k <= z + 1; k++) {
				for (int j = y - 1; j <= y + 1; j++) {
					IBlockState blockState = world.getBlockState(new BlockPos(i, j, k));
					if (blockState.getBlock() == Blocks.log || blockState.getBlock() == Blocks.log2)
					{
						world.destroyBlock(new BlockPos(i, j, k), false);
						++count;
						if(damageTool(stack, player)){
							this.destroyBlocks(world, new BlockPos(i, j, k), player, stack);
						}else{
							return;
						}
					}
				}
			}
		}

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		for(ToolLevel level : ToolLevel.VALUES){
			subItems.add(new ItemStack(itemIn, 1, level.ordinal()));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerModels(){//TODO: Call this from the Client proxy for each tool item.
		ClientProxy p = ((ClientProxy)ARKCraft.proxy);
		List<ItemStack> list = new ArrayList<ItemStack>();
		getSubItems(this, getCreativeTab(), list);
		for(int i = 0;i<list.size();i++){
			p.registerItemTexture(this, i, "tool_" + getUnlocalizedName().substring(5) + "_" + ToolLevel.VALUES[i].name);
		}
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add(I18n.format("arkcraft.tooltip.toolLevel", ToolLevel.VALUES[stack.getMetadata()].getTranslatedName()));
	}
	
	public int getDurrability(ItemStack stack){
		return ToolLevel.VALUES[stack.getMetadata()].getDurrability(toolMaterial.getMaxUses());
	}
	public int getToolDamage(ItemStack stack){
		if(stack.hasTagCompound()){
			return stack.getTagCompound().getInteger(DAMAGE_NBT_NAME);
		}
		return 0;
	}
	public void setToolDamage(ItemStack stack, int newValue){
		if(!stack.hasTagCompound())stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger(DAMAGE_NBT_NAME, newValue);
	}
	public boolean damageTool(ItemStack toolStack, EntityLivingBase entityIn){
		int newValue = getToolDamage(toolStack) + 1;
		setToolDamage(toolStack, newValue);
		if(newValue >= getDurrability(toolStack)){
			entityIn.renderBrokenItemStack(toolStack);
			--toolStack.stackSize;

			if (entityIn instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)entityIn;
				entityplayer.triggerAchievement(StatList.objectBreakStats[Item.getIdFromItem(toolStack.getItem())]);
				if (toolStack.stackSize < 1)
				{
					entityplayer.destroyCurrentEquippedItem();
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
		return ((double)getToolDamage(stack)) / getDurrability(stack);
	}
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + "_" + ToolLevel.VALUES[stack.getMetadata()].name;
	}
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		damageTool(stack, target);
		damageTool(stack, target);
		return true;
	}
	@Override
	public boolean isDamaged(ItemStack stack) {
		return false;
	}
}
