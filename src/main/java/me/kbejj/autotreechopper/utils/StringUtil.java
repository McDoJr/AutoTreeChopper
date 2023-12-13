package me.kbejj.autotreechopper.utils;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

public class StringUtil {

    public static String translate(String paramString) {
        return ChatColor.translateAlternateColorCodes('&', paramString);
    }

    public static void message(CommandSender sender, String message) {
        sender.sendMessage(translate(message));
    }

    public static String sliceBlockName(Block block, String pattern) {
        return sliceBlockName(block.getType().name(), pattern);
    }

    public static String sliceBlockName(String blockNme, String pattern) {
        return blockNme.replace(pattern, "");
    }

    public static String renameBlock(Block block){
        String[] data = block.getType().name().split("_");
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i <data.length; i++){
            String name = data[i].toUpperCase().charAt(0) + data[i].substring(1).toLowerCase();
            builder.append(i == 0 ? name : " " + name);
        }
        return builder.toString();
    }



    public static String renameBlock(String string){
        String[] data = string.split("_");
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i <data.length; i++){
            String name = data[i].charAt(0) + data[i].substring(1).toLowerCase();
            builder.append(i == 0 ? name : " " + name);
        }
        return builder.toString();
    }
}
