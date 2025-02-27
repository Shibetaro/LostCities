package mcjty.lostcities.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

public class WorldGenerationTools {

    public static int findUpsideDownEmptySpot(World world, int x, int z) {
        for (int y = 90 ; y > 0 ; y--) {
            if (world.isEmptyBlock(new BlockPos(x, y, z)) && world.isEmptyBlock(new BlockPos(x, y+1, z)) && world.isEmptyBlock(new BlockPos(x, y+2, z))
                    && world.isEmptyBlock(new BlockPos(x, y+3, z)) && world.isEmptyBlock(new BlockPos(x, y+4, z))) {
                return y;
            }
        }
        return -1;
    }



    public static int findSuitableEmptySpot(World world, int x, int z) {
        int y = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z)).getY();
        if (y == -1) {
            return -1;
        }

        y--;            // y should now be at a solid or liquid block.

        if (y > world.getMaxBuildHeight() - 5) {
            y = world.getMaxBuildHeight() / 2;
        }


        BlockState state = world.getBlockState(new BlockPos(x, y + 1, z));
        while (state.getMaterial().isLiquid()) {
            y++;
            if (y > world.getMaxBuildHeight()-10) {
                return -1;
            }
            state = world.getBlockState(new BlockPos(x, y + 1, z));
        }

        return y;
    }

    // Return true if this block is solid.
    public static boolean isSolid(World world, int x, int y, int z) {
        if (world.isEmptyBlock(new BlockPos(x, y, z))) {
            return false;
        }
        BlockState state = world.getBlockState(new BlockPos(x, y, z));
        return state.getMaterial().blocksMotion();
    }

    // Return true if this block is solid.
    public static boolean isAir(World world, int x, int y, int z) {
        if (world.isEmptyBlock(new BlockPos(x, y, z))) {
            return true;
        }
        Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
        return block == null;
    }

    // Starting at the current height, go down and fill all air blocks with stone until a
    // non-air block is encountered.
    public static void fillEmptyWithStone(World world, int x, int y, int z) {
        while (y > 0 && !isSolid(world, x, y, z)) {
            world.setBlock(new BlockPos(x, y, z), Blocks.STONE.defaultBlockState(), 2);
            y--;
        }
    }
}
