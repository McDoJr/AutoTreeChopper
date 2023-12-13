package me.kbejj.autotreechopper.managers;

import me.kbejj.autotreechopper.model.TreeChopper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

public class TreeChopperManager {

    private final List<TreeChopper> treeChoppers;

    public TreeChopperManager() {
        this.treeChoppers = new ArrayList<>();
    }

    public void createTreeChopper(TreeChopper treeChopper) {
        this.treeChoppers.add(treeChopper);
    }

    public void removeTreeChopper(Location location) {
        if(this.treeChoppers.removeIf(treeChopper -> treeChopper.getMachineBlock().getLocation().equals(location))){
            Bukkit.broadcastMessage("Removed!");
        }
    }

    public void removeTreeChopper(TreeChopper treeChopper){
        this.treeChoppers.remove(treeChopper);
    }

    public TreeChopper getTreeChopper(Location location) {
        return this.treeChoppers.stream().filter(treeChopper -> treeChopper.getMachineBlock().getLocation().equals(location)).findFirst().orElse(null);
    }

    public TreeChopper getTreeChopperFromChest(Location location) {
        return this.treeChoppers.stream().filter(treeChopper -> treeChopper.getChest().getLocation().equals(location)).findFirst().orElse(null);
    }

    public List<TreeChopper> getTreeChoppers() {
        return treeChoppers;
    }
}
