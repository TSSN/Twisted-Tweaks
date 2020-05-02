package io.puppetzmedia.ttweaks.tileentity;

import io.puppetzmedia.ttweaks.TTLogger;
import io.puppetzmedia.ttweaks.TwistedTweaks;
import io.puppetzmedia.ttweaks.block.ModBlocks;
import io.puppetzmedia.ttweaks.config.TorchConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(TwistedTweaks.MODID)
public class TorchLitTileEntity extends TorchTileEntity implements ITickableTileEntity {

	@ObjectHolder("torch_lit_te")
	public static final TileEntityType<TorchLitTileEntity> ENTITY_TYPE = null;

	public TorchLitTileEntity() {
		super(ENTITY_TYPE);
	}

	@Override
	public void tick() {

		if (!TorchConfig.isEnableTorchBurnout() || world == null) {
			return;
		}
		final int litTime = getLitTime();

		final boolean canSeeSky = (TorchConfig.isRainExtinguish() &&
				(litTime % 200) == 0) && world.canSeeSky(pos.up(1));

		increaseLitTime(1);
 		boolean isTimeOverMax = litTime >= TorchConfig.getMaxLitTime();

		if (isTimeOverMax || (TorchConfig.isRainExtinguish() && world.isRaining() && canSeeSky))
		{
			final double destroyChance = TorchConfig.getBurnoutDestroyChance();

			// Roll dice to see if torch should be destroyed
			if (destroyChance > 0 && world.rand.nextFloat() < destroyChance) {
				destroyTorch(world, pos);
			}
			else extinguishTorch(world, pos);
		}
	}

	/**
	 * Remove torch tile entity and block at given {@code BlockPos}.
	 */
	public void destroyTorch(World world, BlockPos pos) {
		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
	}

	/**
	 * Replace {@code TorchLitTileEntity} in given world at
	 * given {@code BlockPos} with unlit version.
	 *
	 * @see #extinguishTorch(TorchLitTileEntity)
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static ActionResultType extinguishTorch(World world, BlockPos pos) {
		return extinguishTorch((TorchLitTileEntity)world.getTileEntity(pos));
	}

	/**
	 * Replace given {@code TorchLitTileEntity} and block with unlit version.
	 *
	 * @param torchEntity torch {@code TileEntity} to extinguish.
	 * @return {@link ActionResultType#SUCCESS} if torch was successfully extinguished
	 * 			or {@link ActionResultType#FAIL} if something went wrong.
	 *
	 * @see #copyToAndReset(World, BlockPos, int)
	 */
	public static ActionResultType extinguishTorch(TorchLitTileEntity torchEntity) {

		final World world = torchEntity.getWorld();
		final BlockPos pos = torchEntity.getPos();
		final BlockState state = torchEntity.getBlockState();
		final Block torchBlock = state.getBlock();

		if (torchBlock == ModBlocks.TORCH) {
			world.setBlockState(pos, ModBlocks.TORCH_UNLIT.getDefaultState());
		}
		else if (torchBlock == ModBlocks.WALL_TORCH)
		{
			Direction direction = state.get(WallTorchBlock.HORIZONTAL_FACING);
			world.setBlockState(pos, ModBlocks.WALL_TORCH_UNLIT.getDefaultState()
					.with(WallTorchBlock.HORIZONTAL_FACING, direction));
		}
		else {
			TTLogger.error("Unknown torch block at pos %s, expected %s or %s",
					pos.toString(), ModBlocks.TORCH.toString(), ModBlocks.WALL_TORCH.toString());

			return ActionResultType.FAIL;
		}
		return torchEntity.copyToAndReset(world, pos, 0);
	}
}
