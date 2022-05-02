package me.rfmineguy.spigot_hammers.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtil {
    final static float NUMBER_OF_PARTICLES_PER_LINE = 20;
    public static void drawLine(Player player, Vector v1, Vector v2, float dustSize) {
        World world = player.getWorld();
        Vector direction = v2.clone().subtract(v1).normalize();
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(75, 75, 75), dustSize);

        for (int i = 0; i < NUMBER_OF_PARTICLES_PER_LINE; i++) {
            Vector point = new Vector();
            point.setX(v1.getX() + (i/NUMBER_OF_PARTICLES_PER_LINE)*direction.getX());
            point.setY(v1.getY() + (i/NUMBER_OF_PARTICLES_PER_LINE)*direction.getY());
            point.setZ(v1.getZ() + (i/NUMBER_OF_PARTICLES_PER_LINE)*direction.getZ());
            player.spawnParticle(Particle.REDSTONE, point.toLocation(world), 1, dustOptions);
        }
    }

    public static void drawLine(Player player, Vector v1, Vector v2) {
        drawLine(player, v1, v2, 0.25f);
    }

    // Unused anywhere in codebase
    public static void drawCube(Player player, Block block) {
        //FULL FACE 1
        drawLine(player, block.getLocation().clone().add(0, 0, 0).toVector(), block.getLocation().clone().add(1, 0, 0).toVector()); //bottom
        drawLine(player, block.getLocation().clone().add(0, 0, 0).toVector(), block.getLocation().clone().add(0, 1, 0).toVector()); //right
        drawLine(player, block.getLocation().clone().add(0, 1, 0).toVector(), block.getLocation().clone().add(1, 1, 0).toVector()); //top
        drawLine(player, block.getLocation().clone().add(1, 1, 0).toVector(), block.getLocation().clone().add(1, 0, 0).toVector()); //left

        //FULL FACE 2
        drawLine(player, block.getLocation().clone().add(0, 0, 1).toVector(), block.getLocation().clone().add(1, 0, 1).toVector()); //bottom
        drawLine(player, block.getLocation().clone().add(0, 0, 1).toVector(), block.getLocation().clone().add(0, 1, 1).toVector()); //right
        drawLine(player, block.getLocation().clone().add(0, 1, 1).toVector(), block.getLocation().clone().add(1, 1, 1).toVector()); //top
        drawLine(player, block.getLocation().clone().add(1, 1, 1).toVector(), block.getLocation().clone().add(1, 0, 1).toVector()); //left

        //TOP
        drawLine(player, block.getLocation().clone().add(0, 1, 0).toVector(), block.getLocation().clone().add(0, 1, 1).toVector());
        drawLine(player, block.getLocation().clone().add(1, 1, 0).toVector(), block.getLocation().clone().add(1, 1, 1).toVector());

        //BOTTOM
        drawLine(player, block.getLocation().clone().add(0, 0, 0).toVector(), block.getLocation().clone().add(0, 0, 1).toVector());
        drawLine(player, block.getLocation().clone().add(1, 0, 0).toVector(), block.getLocation().clone().add(1, 0, 1).toVector());
    }

    public static void drawFace(Player player, Block block, BlockFace blockFace) {
        Location blockLoc = block.getLocation(); // when using this for .add clone it first
        switch (blockFace){
            case NORTH: {
                drawLine(player, blockLoc.clone().add(0, 0, -0.01).toVector(), blockLoc.clone().add(1, 0, -0.01).toVector());
                drawLine(player, blockLoc.clone().add(0, 0, -0.01).toVector(), blockLoc.clone().add(0, 1, -0.01).toVector());
                drawLine(player, blockLoc.clone().add(0, 1, -0.01).toVector(), blockLoc.clone().add(1, 1, -0.01).toVector());
                drawLine(player, blockLoc.clone().add(1, 1, -0.01).toVector(), blockLoc.clone().add(1, 0, -0.01).toVector());
                break;
            }
            case SOUTH: {
                drawLine(player, blockLoc.clone().add(0, 0, 1).toVector(), blockLoc.clone().add(1, 0, 1).toVector());
                drawLine(player, blockLoc.clone().add(0, 0, 1).toVector(), blockLoc.clone().add(0, 1, 1).toVector());
                drawLine(player, blockLoc.clone().add(0, 1, 1).toVector(), blockLoc.clone().add(1, 1, 1).toVector());
                drawLine(player, blockLoc.clone().add(1, 1, 1).toVector(), blockLoc.clone().add(1, 0, 1).toVector());
                break;
            }
            case UP: {
                drawLine(player, blockLoc.clone().add(0, 1, 0).toVector(), blockLoc.clone().add(1, 1, 0).toVector());
                drawLine(player, blockLoc.clone().add(0, 1, 1).toVector(), blockLoc.clone().add(1, 1, 1).toVector());
                drawLine(player, blockLoc.clone().add(0, 1, 0).toVector(), blockLoc.clone().add(0, 1, 1).toVector());
                drawLine(player, blockLoc.clone().add(1, 1, 0).toVector(), blockLoc.clone().add(1, 1, 1).toVector());
                break;
            }
            case DOWN: {
                drawLine(player, blockLoc.clone().add(0, 0, 0).toVector(), blockLoc.clone().add(1, 0, 0).toVector());
                drawLine(player, blockLoc.clone().add(0, 0, 1).toVector(), blockLoc.clone().add(1, 0, 1).toVector());
                drawLine(player, blockLoc.clone().add(0, 0, 0).toVector(), blockLoc.clone().add(0, 0, 1).toVector());
                drawLine(player, blockLoc.clone().add(1, 0, 0).toVector(), blockLoc.clone().add(1, 0, 1).toVector());
                break;
            }
            case EAST: {
                drawLine(player, blockLoc.clone().add(1.01, 0, 0).toVector(), blockLoc.clone().add(1.01, 0, 1).toVector());
                drawLine(player, blockLoc.clone().add(1.01, 1, 0).toVector(), blockLoc.clone().add(1.01, 1, 1).toVector());
                drawLine(player, blockLoc.clone().add(1.01, 1, 0).toVector(), blockLoc.clone().add(1.01, 0, 0).toVector());
                drawLine(player, blockLoc.clone().add(1.01, 1, 1).toVector(), blockLoc.clone().add(1.01, 0, 1).toVector());
                break;
            }
            case WEST: {
                drawLine(player, blockLoc.clone().add(-0.01, 0, 0).toVector(), blockLoc.clone().add(-0.01, 0, 1).toVector());
                drawLine(player, blockLoc.clone().add(-0.01, 1, 0).toVector(), blockLoc.clone().add(-0.01, 1, 1).toVector());
                drawLine(player, blockLoc.clone().add(-0.01, 1, 0).toVector(), blockLoc.clone().add(-0.01, 0, 0).toVector());
                drawLine(player, blockLoc.clone().add(-0.01, 1, 1).toVector(), blockLoc.clone().add(-0.01, 0, 1).toVector());
                break;
            }
        }
    }
}