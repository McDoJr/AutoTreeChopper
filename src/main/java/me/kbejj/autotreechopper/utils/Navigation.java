package me.kbejj.autotreechopper.utils;

import me.kbejj.autotreechopper.model.TreeChopper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

public class Navigation {

    private static final BlockFace[] blockFaces = {
            BlockFace.UP,
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.NORTH,
            BlockFace.SOUTH,
    };

    public static Block getBlockDestination(Block machineBlock, Block targetTree, TreeChopper treeChopper, Material material, Location previousLocation) {
        Location machineLocation = machineBlock.getLocation().clone();
        Vector vector = getSteps(machineBlock, targetTree, previousLocation);
        machineLocation.add(vector);
        treeChopper.setChest(getChestDestination(machineLocation.getBlock(), treeChopper.getChest()));
        Block newMachineBlock = machineLocation.getBlock();
        BlockData blockData = newMachineBlock.getBlockData();
        int stepY = (int) vector.getY();
        if(stepY != 0) { // If the Y axis has been modified
            newMachineBlock.setType(material);
            if(!machineBlock.getState().equals(treeChopper.getChest())){ // Checks if machine's old position has been replaced by its own chest. If not then set the block to AIR
                machineBlock.setType(Material.AIR);
            }
        }else{
            newMachineBlock.setBlockData(machineBlock.getBlockData());
            machineBlock.setBlockData(blockData);
        }
        return newMachineBlock;
    }

    public static Chest getChestDestination(Block machineBlock, Chest chest) {
        Location location = machineBlock.getLocation().clone().add(0, 1, 0);
        Block block = location.getBlock();
        block.setBlockData(chest.getBlockData());
        Chest newChest = (Chest) block.getState();
        newChest.getInventory().setContents(chest.getInventory().getContents());
        chest.getBlock().setType(Material.AIR);
        return newChest;
    }

    private static Vector getSteps(Block machineBlock, Block targetTree, Location previousLocation) {
        Location location = machineBlock.getLocation();
        int machineX = machineBlock.getX();
        int targetX = targetTree.getX();
        int machineY = machineBlock.getY();
        int targetY = targetTree.getY();
        int machineZ = machineBlock.getZ();
        int targetZ = targetTree.getZ();
        int stepX = 0;
        int stepY = 0;
        int stepZ = 0;
        boolean isClimbing = false;
        // Unfinished. Needs time to make the navigation perfect
//        stepX = targetX == machineX ? getSideX(location, stepY, stepZ) : getX(location, targetX, machineX, stepY, stepZ);
//        stepZ = targetZ == machineZ ? getSideZ(location, stepX, stepY) : getZ(location, targetZ, machineZ, stepX, stepY);
        if(targetX != machineX){
            stepX = getX(location, targetX, machineX, stepY, stepZ);
        }
        if(targetZ != machineZ){
            stepZ = getZ(location, targetZ, machineZ, stepX, stepY);
        }
        if(targetY == machineY){ // if targetY and machineY is the same, then climb 1 block if the block ahead is solid
            Block current = location.clone().add(stepX, 0, stepZ).getBlock();
            if(current.getType().isSolid()) {
                stepY = 1;
                isClimbing = true;
            }
        }else {
            stepY = getY(location, targetY, machineY, stepX, stepZ);
        }
        // Check if the current location's ground is air, then reset the steps and go down 1 block
        Block ground = location.clone().subtract(0, 1, 0).getBlock();
        if(ground.getType().isAir() && !isClimbing && !ground.getLocation().equals(previousLocation)){
            stepY = -1;
            stepX = 0;
            stepZ = 0;
        }
        Vector vector = new Vector(stepX, stepY, stepZ);
        Block block = location.clone().add(vector).getBlock();
        Vector alternative = findEmptySpot(machineBlock);
        return vector;
    }

    private static Vector findEmptySpot(Block currentBlock) {
        for(BlockFace blockFace : blockFaces){
            Block relative = currentBlock.getRelative(blockFace);
            if(relative.getType().isAir()){
                return relative.getLocation().toVector();
            }
        }
        return null;
    }

    private static int getSideX(Location location, int stepY, int stepZ) {
        Block current = location.clone().add(0, stepY, stepZ).getBlock();
        if(current.getType().isSolid()){
            Block left = location.clone().add(1, stepY, stepZ).getBlock();
            Block right = location.clone().add(-1, stepY, stepZ).getBlock();
            if(left.getType().isAir()) return 1;
            if(right.getType().isAir()) return -1;
        }
        return 0;
    }

    private static int getSideZ(Location location, int stepX, int stepY) {
        Block current = location.clone().add(stepX, stepY, 0).getBlock();
        if(current.getType().isSolid()){
            Block left = location.clone().add(stepX, stepY, 1).getBlock();
            Block right = location.clone().add(stepX, stepY, -1).getBlock();
            if(left.getType().isAir()) return 1;
            if(right.getType().isAir()) return -1;
        }
        return 0;
    }

    private static int getX(Location location, int targetX, int machineX, int stepY, int stepZ) {
        int stepX = targetX > machineX ? 1 : -1;
        Block current = location.clone().add(stepX, stepY, stepZ).getBlock();
        return current.getType().isSolid() && !isLeaves(current) ? 0 : stepX;
    }

    private static int getY(Location location, int targetY, int machineY, int stepX, int stepZ) {
        Block current = location.clone().add(stepX, 0, stepZ).getBlock();
        return current.getType().isSolid() ? targetY > machineY ? 1 : -1 : 0;
    }

    private static int getZ(Location location, int targetZ, int machineZ, int stepX, int stepY) {
        int stepZ = targetZ > machineZ ? 1 : -1;
        Block current = location.clone().add(stepX, stepY, stepZ).getBlock();
        return current.getType().isSolid() && !isLeaves(current) ? 0 : stepZ;
    }

    private static boolean isLeaves(Block block){
        return block.getType().name().endsWith("_LEAVES");
    }
}
