package me.kbejj.autotreechopper.utils;

import me.kbejj.autotreechopper.AutoTreeChopper;
import org.bukkit.Bukkit;

public class TimeUtil {

    private static final AutoTreeChopper plugin = AutoTreeChopper.getPlugin();

    public static void delay(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }
}
