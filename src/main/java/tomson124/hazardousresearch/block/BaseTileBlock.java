package tomson124.hazardousresearch.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseTileBlock extends Block implements ITileEntityProvider
{
    protected BaseTileBlock(Material materialIn)
    {
        super(materialIn);
        setHardness(1F);
    }

    public int getRenderType()
    {
        return 3;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
}
