package me.kbejj.autotreechopper.utils;

import me.kbejj.autotreechopper.model.TreeChopper;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BlockUtil {

    private static final BlockFace[] blockFaces = {
            BlockFace.DOWN,
            BlockFace.UP,
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.NORTH,
            BlockFace.SOUTH
    };

    private static final List<String> dangerousBlockMaterials = Arrays.asList(
            "mushroom", "fern", "dandelion", "poppy", "blue_orchid", "allium", "azure_bluet", "tulip", "daisy", "flower", "lily", "rose"
    );

    private static final List<String> interactionBlocks = Arrays.asList("sponge", "ice");

    public static void applyEffectUponContact(Block machineBlock) {
        for(BlockFace blockFace : blockFaces){
            Block relativeBlock = machineBlock.getRelative(blockFace);
            if(interactionBlocks.stream().anyMatch(name -> relativeBlock.getType().name().toLowerCase().contains(name))){
                PluginUtil.applyContactEffect(machineBlock);
                break;
            }
        }
    }

    public static void destroyIfDangerousBlock(TreeChopper treeChopper) {
        Player player = treeChopper.getPlayer();
        Block machineBlock = treeChopper.getMachineBlock();
        for(BlockFace blockFace : blockFaces){
            Block relativeBlock = machineBlock.getRelative(blockFace);
            if(dangerousBlockMaterials.stream().anyMatch(name -> relativeBlock.getType().name().toLowerCase().contains(name))){
                machineBlock.breakNaturally();
                StringUtil.message(player, "&cMachine has been destroyed upon contact with " + StringUtil.renameBlock(relativeBlock));
                SoundUtil.pling(player);
                treeChopper.setDestroyed(true);
                treeChopper.setMachineBlock(null);
                break;
            }
        }
    }


}
