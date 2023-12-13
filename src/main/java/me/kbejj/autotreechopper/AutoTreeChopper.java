package me.kbejj.autotreechopper;

import me.kbejj.autotreechopper.listeners.PlayerListener;
import me.kbejj.autotreechopper.managers.TreeChopperManager;
import me.kbejj.autotreechopper.model.TreeChopper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;

public final class AutoTreeChopper extends JavaPlugin {

    private static AutoTreeChopper plugin;
    private BukkitTask searchingTask;
    private TreeChopperManager treeChopperManager;

    @Override
    public void onEnable() {
        plugin = this;
        this.treeChopperManager = new TreeChopperManager();
        new PlayerListener(this);
        startSearchingTask();
    }

    @Override
    public void onDisable() {
        searchingTask.cancel();
    }

    public static AutoTreeChopper getPlugin() {
        return plugin;
    }

    public void startSearchingTask() {
        this.searchingTask = Bukkit.getScheduler().runTaskTimer(plugin, this::handleTreeSearching, 0, 20);
    }

    private void handleTreeSearching() {
        // Converts to iterator to avoid error when removing inside this loop
        for(Iterator<TreeChopper> iterator = treeChopperManager.getTreeChoppers().iterator(); iterator.hasNext();){
            TreeChopper treeChopper = iterator.next();
            if(treeChopper.isDestroyed()){
                iterator.remove();
            }else{
                treeChopper.search();
            }
        }
    }

    public TreeChopperManager getTreeChopperManager() {
        return treeChopperManager;
    }

    public BukkitTask getSearchingTask() {
        return searchingTask;
    }
}
