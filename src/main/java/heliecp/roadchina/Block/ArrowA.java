package heliecp.roadchina.Block;

import heliecp.roadchina.Item.Wrench;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ArrowA extends Block
{
    public static final Block block = null;
    public ArrowA()
    {
        super(Block.Properties.of(Material.STONE).harvestLevel(10));
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return Block.box(0.0D,0.0D,0.0D,0.0D,0.0D,0.0D);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader source, BlockPos pos, ISelectionContext iSelectionContext) {
        switch (state.getValue(BlockStateProperties.FACING)) {
            case SOUTH:
            case NORTH:
            default:
                return Block.box(4.0D, 0.0D, -16.0D, 12.0D, 0.0D, 32.0D);
            case EAST:
            case WEST:
                return Block.box(-16.0D, 0.0D, 4.0D, 32.0D, 0.0D, 12.0D);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void clientLoad(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(block, RenderType.cutout());
    }


    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return this.defaultBlockState().setValue(BlockStateProperties.FACING, ctx.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        return p_185499_1_.setValue(BlockStateProperties.FACING, p_185499_2_.rotate(p_185499_1_.getValue(BlockStateProperties.FACING)));
    }

    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
        return p_185471_1_.rotate(p_185471_2_.getRotation(p_185471_1_.getValue(BlockStateProperties.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockItemUseContext context) {
        ItemStack itemstack = context.getItemInHand();
        if (itemstack.getItem() == this.asItem()) {
            if (context.replacingClickedOnBlock()) {
                Direction direction = context.getClickedFace();
                if (blockState.getBlock() == BlockRegistry.whiteLine1.get()) {
                    return direction == Direction.UP || direction.getAxis().isHorizontal();
                }
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
        return false;
    }


    @Override
    public ActionResultType use(BlockState blockState, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult blockRayTraceResult)
    {
        if(worldIn.isClientSide)
        {
            return ActionResultType.SUCCESS;
        }

        if(playerIn.getMainHandItem().getItem() instanceof Wrench)
        {
            Direction direction = blockState.getValue(BlockStateProperties.FACING);
            worldIn.setBlockAndUpdate(pos, blockState.setValue(BlockStateProperties.FACING, direction.getClockWise()));
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;
    }

}
