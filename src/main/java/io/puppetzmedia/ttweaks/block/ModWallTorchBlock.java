package io.puppetzmedia.ttweaks.block;

import io.puppetzmedia.ttweaks.tileentity.TorchTileEntity;
import io.puppetzmedia.ttweaks.tileentity.TorchLitTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ModWallTorchBlock extends WallTorchBlock implements ITileEntityOwner {

	public ModWallTorchBlock(int lightValue) {
		super(Block.Properties.from(Blocks.WALL_TORCH).lightValue(lightValue));
	}
	public ModWallTorchBlock() {
		super(Block.Properties.from(Blocks.WALL_TORCH));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {

		// Disable spawning flame and smoke particles for unlit torches
		if (lightValue != 0) {
			super.animateTick(stateIn, worldIn, pos, rand);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return lightValue != 0 ? new TorchLitTileEntity() : new TorchTileEntity();
	}
}
