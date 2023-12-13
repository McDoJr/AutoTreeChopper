package me.kbejj.autotreechopper.utils;

import me.kbejj.autotreechopper.model.TreeChopperType;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;

public class PluginUtil {

    public static int getSpeed(TreeChopperType chopperType){
        if(chopperType == TreeChopperType.DIAMOND){
            return 10;
        }
        if(chopperType == TreeChopperType.EMERALD){
            return 20;
        }
        return 30;
    }

    public static void applyChoppingEffects(TreeChopperType chopperType, Block machineBlock) {
        Location location = machineBlock.getLocation().clone().add(.5, 2.5, .5);
        if(chopperType == TreeChopperType.DIAMOND){
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromBGR(6, 228, 226), 10.0F);
            machineBlock.getWorld().spawnParticle(Particle.REDSTONE, location, 50, dustOptions);
            machineBlock.getWorld().playSound(machineBlock.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);
        }
        if(chopperType == TreeChopperType.EMERALD){
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromBGR(46, 253, 9), 10.0F);
            machineBlock.getWorld().spawnParticle(Particle.REDSTONE, location, 50, dustOptions);
            machineBlock.getWorld().playSound(machineBlock.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);
        }
        if(chopperType == TreeChopperType.GOLD){
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromBGR(228, 227, 6), 10.0F);
            machineBlock.getWorld().spawnParticle(Particle.REDSTONE, location, 50, dustOptions);
            machineBlock.getWorld().playSound(machineBlock.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);
        }
    }

    public static void applyContactEffect(Block machineBlock) {
        Location location = machineBlock.getLocation().clone().add(.5, 1.5, .5);
        location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromBGR(255, 59, 8), 10.0F);
        machineBlock.getWorld().spawnParticle(Particle.REDSTONE, location, 50, dustOptions);
    }
}
