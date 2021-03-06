package refinedstorage.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import refinedstorage.RefinedStorage;
import refinedstorage.item.ItemBlockBase;
import refinedstorage.tile.TileBase;

public abstract class BlockBase extends Block {
    private static final PropertyDirection DIRECTION = PropertyDirection.create("direction");

    private String name;

    public BlockBase(String name) {
        super(Material.ROCK);

        this.name = name;

        setHardness(1.9F);
        setRegistryName(RefinedStorage.ID, name);
        setCreativeTab(RefinedStorage.INSTANCE.tab);
    }

    @Override
    public String getUnlocalizedName() {
        return "block." + RefinedStorage.ID + ":" + name;
    }

    protected BlockStateContainer.Builder createBlockStateBuilder() {
        BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);

        if (getPlacementType() != null) {
            builder.add(DIRECTION);
        }

        return builder;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return createBlockStateBuilder().build();
    }

    public Item createItem() {
        return new ItemBlockBase(this, false);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (getPlacementType() != null) {
            return state.withProperty(DIRECTION, ((TileBase) world.getTileEntity(pos)).getDirection());
        }

        return state;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (!world.isRemote && getPlacementType() != null) {
            TileBase tile = (TileBase) world.getTileEntity(pos);

            tile.setDirection(getPlacementType().getNext(tile.getDirection()));

            tile.updateBlock();

            return true;
        }

        return false;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase entity) {
        IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, entity);

        if (getPlacementType() != null) {
            return state.withProperty(DIRECTION, getPlacementType().getFrom(facing, pos, entity));
        }

        return state;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, player, stack);

        if (getPlacementType() != null) {
            ((TileBase) world.getTileEntity(pos)).setDirection(state.getValue(DIRECTION));
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileBase && ((TileBase) tile).getDrops() != null) {
            IItemHandler handler = ((TileBase) tile).getDrops();

            for (int i = 0; i < handler.getSlots(); ++i) {
                if (handler.getStackInSlot(i) != null) {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
                }
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest ? true : super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, tile, stack);

        world.setBlockToAir(pos);
    }

    protected EnumPlacementType getPlacementType() {
        return EnumPlacementType.HORIZONTAL;
    }
}
