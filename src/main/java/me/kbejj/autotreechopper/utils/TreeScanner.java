package me.kbejj.autotreechopper.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;

public class TreeScanner {

    public static Block findTargetTree(Block start, int radius) {
        if(radius < 0 || radius > 50) {
            return null;
        }
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block target = start.getRelative(x, y, z);
                    if (target.getType().name().endsWith("_LOG") && blockIsTree(target)) {
                        return target;
                    }
                }
            }
        }
        return findTargetTree(start, radius + 5);
    }

    private static boolean blockIsTree(Block target) {
        Block blockBelow = target.getLocation().clone().subtract(0, 1, 0).getBlock();
        if(blockBelow.getType() != target.getType() && blockBelow.getType().isSolid() && !blockBelow.getType().name().equals("_LEAVES")){
            return isTreeHeight(target);
        }
        return false;
    }

    private static boolean isTreeHeight(Block target) {
        int height = 1;
        Location location = target.getLocation().clone();
        while(true) {
            if(location.add(0, height, 0).getBlock().getType() == target.getType()){
                height++;
            }else{
                break;
            }
        }
        return height >= 3;
    }

    public static List<Block> getTree(Block start) {
        List<Block> tree = new ArrayList<>(getNearbyBlocks(start, new HashSet<>()));
        tree.sort((Comparator.comparingDouble(Block::getY)));
        return tree;
    }

    private static Set<Block> getNearbyBlocks(Block start, Set<Block> blocks) {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    Block block = start.getLocation().clone().add(x, y, z).getBlock();
                    if (!blocks.contains(block) && start.getType() == block.getType()) {
                        blocks.add(block);
                        blocks.addAll(getNearbyBlocks(block, blocks));
                    }
                }
            }
        }
        return blocks;
    }
}
