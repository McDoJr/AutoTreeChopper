package me.kbejj.autotreechopper.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtil {

    public static void pling(Player player) {
        player.getWorld().playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }

    public static void woodBreak(Player player){
        player.getWorld().playSound(player, Sound.BLOCK_WOOD_BREAK, 1, 0);
    }

    public static void exp(Player player) {
        player.getWorld().playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
    }
}
