package io.puppetzmedia.ttweaks.block;

import io.puppetzmedia.ttweaks.tileentity.LiquidVoidTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class LiquidVoidBlock extends Block implements ITileEntityOwner {

    public LiquidVoidBlock() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL)
                .hardnessAndResistance(2.0F, 10.0F)
                .harvestTool(ToolType.PICKAXE).harvestLevel(2));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LiquidVoidTileEntity();
    }
}