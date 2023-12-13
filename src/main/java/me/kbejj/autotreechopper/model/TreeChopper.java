package me.kbejj.autotreechopper.model;

import me.kbejj.autotreechopper.AutoTreeChopper;
import me.kbejj.autotreechopper.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TreeChopper {

    private final AutoTreeChopper plugin = AutoTreeChopper.getPlugin();
    private final Player player;
    private final TreeChopperType treeChopperType;
    private Block machineBlock;
    private final Material material;
    private Chest chest;
    private boolean isActivated;
    private Block targetTree;
    private boolean chopping;
    private Location previousLocation;
    private List<Block> treeBlocks;
    private boolean destroyed;
    private final int speed;

    public TreeChopper(Player player, TreeChopperType treeChopperType, Block machineBlock) {
        this.player = player;
        this.treeChopperType = treeChopperType;
        this.machineBlock = machineBlock;
        this.material = machineBlock.getType();
        this.chest = null;
        this.isActivated = false;
        this.chopping = false;
        this.treeBlocks = new ArrayList<>();
        this.destroyed = false;
        this.speed = PluginUtil.getSpeed(treeChopperType);
    }

    public Player getPlayer() {
        return player;
    }

    public Block getMachineBlock() {
        return machineBlock;
    }

    public void setMachineBlock(Block machineBlock) {
        this.machineBlock = machineBlock;
    }

    public Chest getChest() {
        return chest;
    }

    public void setChest(Chest chest) {
        this.chest = chest;
        setActivated(chest != null);
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public void setTargetTree(Block targetTree) {
        this.targetTree = targetTree;
    }

    public boolean isChopping() {
        return chopping;
    }

    public void setChopping(boolean chopping) {
        this.chopping = chopping;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    private void alertPlayer() {
        if(player == null || !player.isOnline()) return;
        StringUtil.message(player, "&cYour chest is full! Your machine is now deactivated.");
        SoundUtil.pling(player);
        setActivated(false);
    }

    public void search() {
        if(destroyed || machineBlock == null){
            return;
        }
//        if(!isActivated && chest != null && chest.getInventory().firstEmpty() != -1){
//            setActivated(true);
//        }
        if (!isActivated() || chest == null) {
            return;
        }
        if (isActivated && chest.getInventory().firstEmpty() == -1) {
            alertPlayer();
            return;
        }
        if (targetTree == null || (targetTree.getType().isAir() && treeBlocks.isEmpty())) {
            // Get scanned target tree
            Block target = TreeScanner.findTargetTree(machineBlock, 5);
            if(target == null){
                // Deactivate the machine if there's no trees nearby
                StringUtil.message(player, "&cNo tree's has been detected within 50 radius. Please find a spot with tree's nearby");
                SoundUtil.exp(player);
                setActivated(false);
                return;
            }
            setTargetTree(target);
            StringUtil.message(player, "&a" + StringUtil.renameBlock(StringUtil.sliceBlockName(target, "_LOG")) + " Tree &7has been found!");
            return;
        }
        navigateToTarget();
    }

    private void navigateToTarget() {
        Location machineLocation = machineBlock.getLocation().clone();
        if(machineLocation.distance(targetTree.getLocation()) > 1) {
            setMachineBlock(Navigation.getBlockDestination(machineBlock, targetTree, this, material, previousLocation));
            previousLocation = machineLocation;
            BlockUtil.applyEffectUponContact(machineBlock);
            BlockUtil.destroyIfDangerousBlock(this);
            return;
        }

        if(!isChopping()){
            setChopping(true);
            startChoppingTask();
        }
    }

    private void startChoppingTask() {

        treeBlocks = treeBlocks.isEmpty() ? TreeScanner.getTree(targetTree) : treeBlocks;
        Inventory inventory = chest.getInventory();

        new BukkitRunnable() {

            @Override
            public void run() {
                if(machineBlock == null || machineBlock.getType().isAir() || chest == null || chest.getType().isAir()) {
                    StringUtil.message(player, "Cancelled");
                    this.cancel();
                    return;
                }
                if(treeBlocks.isEmpty()){ // Stop chopping if empty
                    setChopping(false);
                    setTargetTree(null);
                    StringUtil.message(player, "Done Chopping!");
                    this.cancel();
                    return;
                }
                Block block = treeBlocks.get(0);
                if(inventory.firstEmpty() != -1){
                    chest.getInventory().addItem(new ItemStack(block.getType()));
                    block.setType(Material.AIR);
                    PluginUtil.applyChoppingEffects(treeChopperType, machineBlock);
                }else{
                    this.cancel();
                    setChopping(false);
                    return;
                }
                treeBlocks.remove(0);
            }
        }.runTaskTimer(plugin, 0, speed);

    }
    

}
