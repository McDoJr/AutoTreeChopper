package me.kbejj.autotreechopper.listeners;

import me.kbejj.autotreechopper.AutoTreeChopper;
import me.kbejj.autotreechopper.model.TreeChopper;
import me.kbejj.autotreechopper.model.TreeChopperType;
import me.kbejj.autotreechopper.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;
import java.util.List;

public class PlayerListener implements Listener {

    private final AutoTreeChopper plugin;

    public PlayerListener(AutoTreeChopper plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        if(!allowedBlocks.contains(block.getType())){
            handleChestPlace(player, block);
            return;
        }
        String name = StringUtil.sliceBlockName(block, "_BLOCK");
        TreeChopper treeChopper = new TreeChopper(player, TreeChopperType.valueOf(name), block);
        plugin.getTreeChopperManager().createTreeChopper(treeChopper);
        StringUtil.message(player, "&eYou have place a " + name + " Tree Chopper");
    }

    private void handleChestPlace(Player player, Block block) {
        BlockState blockState = block.getState();
        if(!(blockState instanceof Chest)){
            return;
        }
        TreeChopper treeChopper = plugin.getTreeChopperManager().getTreeChopper(block.getLocation().clone().subtract(0, 1, 0));
        if(treeChopper == null){
            return;
        }
        treeChopper.setChest((Chest) blockState);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        if(!allowedBlocks.contains(block.getType())){
            if(block.getState() instanceof Chest){
                TreeChopper treeChopper = plugin.getTreeChopperManager().getTreeChopperFromChest(block.getLocation());
                if(treeChopper != null){
                    treeChopper.setChest(null);
                }
            }
            return;
        }
        plugin.getTreeChopperManager().removeTreeChopper(block.getLocation());
    }

    private final List<Material> allowedBlocks = Arrays.asList(Material.DIAMOND_BLOCK, Material.EMERALD_BLOCK, Material.GOLD_BLOCK);
}
