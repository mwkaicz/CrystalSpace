// Package Declaration
package com.crystalcraftmc.crystalspace.wgen.populators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Populates a world with end portal blocks around bedrock.
 * 
 * @author iffa
 */
public class SpaceEffectPopulator extends BlockPopulator {

    /**
     * Populates a chunk with end portal blocks around bedrock.
     * Gives a cool effect and is totally harmless because the
     * portal frame is not present.
     *
     * @param world World
     * @param random Random
     * @param source Source chunk
     */
    @Override
    public void populate(World world, Random random, Chunk source) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                source.getBlock(x, 1, z).setType(Material.ENDER_PORTAL);
            }
        }
    }
    
}
