/*
 */
package me.iffa.bananaspace.wgen.planets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import me.iffa.bananaspace.api.SpaceMessageHandler;
import me.iffa.bananaspace.config.SpacePlanetConfig;
import me.iffa.bananaspace.wgen.populators.SpaceGlowstonePopulator;
import me.iffa.bananaspace.wgen.populators.SpaceSatellitePopulator;
import me.iffa.bananaspace.wgen.populators.SpaceStonePopulator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

/**
 *
 * @author Jack
 */
public class PlanetsChunkGenerator extends ChunkGenerator {
    // Variables
    private Map<Material, Float> allowedShells;
    private Map<Material, Float> allowedCores;
    private long seed = (long) SpacePlanetConfig.getConfig().getDouble("seed", 0.0);; // Seed for generating planetoids
    private int density = SpacePlanetConfig.getConfig().getInt("density", 15000); // Number of planetoids it will try to create per
    private int minSize = SpacePlanetConfig.getConfig().getInt("minSize", 4); // Minimum radius
    private int maxSize = SpacePlanetConfig.getConfig().getInt("maxSize", 20); // Maximum radius
    private int minDistance = SpacePlanetConfig.getConfig().getInt("minDistance", 10); // Minimum distance between planets, in blocks
    private int floorHeight = SpacePlanetConfig.getConfig().getInt("floorHeight", 0); // Floor height
    private int maxShellSize = SpacePlanetConfig.getConfig().getInt("maxShellSize", 5); // Maximum shell thickness
    private int minShellSize = SpacePlanetConfig.getConfig().getInt("minShellSize", 3); // Minimum shell thickness, should be at least 3
    private Material floorBlock = Material.matchMaterial(SpacePlanetConfig.getConfig().getString("floorBlock", "STATIONARY_WATER"));// BlockID for the floor 
    private static HashMap<World,List<Planetoid>> planets = new HashMap<World,List<Planetoid>>();

    public PlanetsChunkGenerator() {
        loadAllowedBlocks();
    }
    
    @Override
    public byte[] generate(World world, Random random, int x, int z) {
        if(!planets.containsKey(world)) planets.put(world, new ArrayList<Planetoid>());
        byte[] retVal = new byte[32768];
        Arrays.fill(retVal, (byte) 0);
        generatePlanetoids(world,x,z);
        // Go through the current system's planetoids and fill in this chunk as
        // needed.
        for (Planetoid curPl : planets.get(world)) {
            // Find planet's center point relative to this chunk.
            int relCenterX = curPl.xPos - x*16;
            int relCenterZ = curPl.zPos - z*16;
            
            for (int curX = -curPl.radius; curX <= curPl.radius; curX++) {//Iterate across every x block
                boolean xShell = false;//Is part of the x shell
                int chunkX = curX + relCenterX;
                if (chunkX >= 0 && chunkX < 16) {
                    int worldX = curX + curPl.xPos;//get the x block number in the world
                    // Figure out radius of this circle
                    int distFromCenter = Math.abs(curX);//Distance from center in the x 
                    if (curPl.radius - distFromCenter < curPl.shellThickness) {//Check if part of xShell
                        xShell = true;
                    }
                    int zHalfLength = (int) Math.ceil(Math.sqrt((curPl.radius * curPl.radius)
                            - (distFromCenter * distFromCenter)));//Half the amount of blocks in the z direction
                    for (int curZ = -zHalfLength; curZ <= zHalfLength; curZ++) {//Iterate over all z blocks 
                        int chunkZ = curZ + relCenterZ;
                        if (chunkZ >= 0 && chunkZ < 16) {
                            int worldZ = curZ + curPl.zPos;//get the z block number in the world
                            boolean zShell = false;//Is part of z shell
                            int zDistFromCenter = Math.abs(curZ);//Distance from center in the z
                            if (zHalfLength - zDistFromCenter < curPl.shellThickness) {//Check if part of zShell
                                zShell = true;
                            }
                            int yHalfLength = (int) Math.ceil(Math.sqrt((zHalfLength * zHalfLength)
                                    - (zDistFromCenter * zDistFromCenter)));
                            for (int curY = -yHalfLength; curY <= yHalfLength; curY++) {
                                int worldY = curY + curPl.yPos;
                                boolean yShell = false;
                                if (yHalfLength - Math.abs(curY) < curPl.shellThickness) {
                                    yShell = true;
                                }
                                if (xShell || zShell || yShell) {
                                    //world.getBlockAt(worldX, worldY, worldZ).setType(curPl.shellBlk);
                                    retVal[(chunkX * 16 + chunkZ) * 128 + worldY] = (byte) curPl.shellBlk.getId();
                                } else {
                                    //world.getBlockAt(worldX, worldY, worldZ).setType(curPl.coreBlk);
                                    retVal[(chunkX * 16 + chunkZ) * 128 + worldY] = (byte) curPl.coreBlk.getId();
                                }
                            }   
                        }
                    }
                }
            }
        }
    
        // Fill in the floor
        for (int floorY = 0; floorY < floorHeight; floorY++) {
            for (int floorX = 0; floorX < 16; floorX++) {
                for (int floorZ = 0; floorZ < 16; floorZ++) {
                    if (floorY == 0) {
                        retVal[floorX * 2048 + floorZ * 128 + floorY] = (byte) Material.BEDROCK.getId();
                    } else {
                        retVal[floorX * 2048 + floorZ * 128 + floorY] = (byte) floorBlock.getId();
                    }
                }
            }
        }
        return retVal;
    }
    private void generatePlanetoids(World world, int x, int z){
        List<Planetoid> planetoids = new ArrayList<Planetoid>();
        int chunkDistance = minDistance%16==0?minDistance/16:1+(minDistance/16);
        //Seed shift;
        // if X is negative, left shift seed by one
        if (x < 0) {
            seed <<= 1;
        } // if Z is negative, change sign on seed.
        if (z < 0) {
            seed = -seed;
        }

        
        
        // If x and Z are zero, generate a log/leaf planet close to 0,0
        if (x == 0 && z == 0) {
            Planetoid spawnPl = new Planetoid();
            spawnPl.xPos = 7;
            spawnPl.yPos = 70;
            spawnPl.zPos = 7;
            spawnPl.coreBlk = Material.LOG;
            spawnPl.shellBlk = Material.LEAVES;
            spawnPl.shellThickness = 3;
            spawnPl.radius = 6;
            planets.get(world).add(spawnPl);
        }
        
        //x = (x*16) - minDistance;
        //z = (z*16) - minDistance;
        
        Random rand = new Random(seed);
        for (int i = 0; i < Math.abs(x) + Math.abs(z); i++) {
            // cycle generator
            rand.nextInt();
            rand.nextInt();
            rand.nextInt();
            //rand.nextInt();
            //rand.nextInt();
            //rand.nextInt();
            //rand.nextInt();
        }

        for (int i = 0; i < density; i++) {
            // Try to make a planet
            Planetoid curPl = new Planetoid();
            curPl.shellBlk = getBlockType(rand, false, true);
            switch (curPl.shellBlk) {
                case LEAVES:
                    curPl.coreBlk = Material.LOG;
                    break;
                case ICE:
                case WOOL:
                    curPl.coreBlk = getBlockType(rand, true, true);
                default:
                    curPl.coreBlk = getBlockType(rand, true, false);
                    break;
            }

            curPl.shellThickness = rand.nextInt(maxShellSize - minShellSize)
                    + minShellSize;
            curPl.radius = rand.nextInt(maxSize - minSize) + minSize;

            // Set position
            curPl.xPos = (x*16) - minDistance + rand.nextInt(minDistance+16+minDistance);
            curPl.yPos = rand.nextInt(128 - curPl.radius * 2 - floorHeight)
                    + curPl.radius;
            curPl.zPos = (z*16) - minDistance + rand.nextInt(minDistance+16+minDistance);

            // Created a planet, check for collisions with existing planets
            // If any collision, discard planet
            boolean discard = false;
            for (Planetoid pl : planetoids) {
                // each planetoid has to be at least pl1.radius + pl2.radius +
                // min distance apart
                int distMin = pl.radius + curPl.radius + minDistance;
                if (distanceSquared(pl, curPl) < distMin * distMin) {
                    discard = true;
                    break;
                }
            }
            if(!planets.isEmpty()){
            if(!planets.get(world).isEmpty()){
                List<Planetoid> tempPlanets = planets.get(world);
                for (Planetoid pl : tempPlanets) {
                    // each planetoid has to be at least pl1.radius + pl2.radius +
                    // min distance apart
                    int distMin = pl.radius + curPl.radius + minDistance;
                    if (distanceSquared(pl, curPl) < distMin * distMin) {
                        discard = true;
                        break;
                    }
                }
            }
            }
            if (!discard) {
                planetoids.add(curPl);
            }
        }
        planets.get(world).addAll(planetoids);

    }
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator) 
                new SpaceGlowstonePopulator(), 
                new SpaceStonePopulator(), 
                new SpaceSatellitePopulator());
    }
    /**
     * Loads allowed blocks
     */
    private void loadAllowedBlocks() {
        allowedCores = new EnumMap<Material, Float>(Material.class);
        allowedShells = new EnumMap<Material, Float>(Material.class);
        for (String s : (List<String>)SpacePlanetConfig.getConfig().getList(
                "blocks.cores", null)) {
            String[] sSplit = s.split("-");
            Material newMat = Material.matchMaterial(sSplit[0]);
            if (newMat.isBlock()) {
                if (sSplit.length == 2) {
                    allowedCores.put(newMat, Float.valueOf(sSplit[1]));
                } else {
                    allowedCores.put(newMat, 1.0f);
                }
            }
        }
        for (String s : (List<String>)SpacePlanetConfig.getConfig().getList(
                "blocks.shells", null)) {
            String[] sSplit = s.split("-");
            Material newMat = Material.matchMaterial(sSplit[0]);
            if (newMat.isBlock()) {
                if (sSplit.length == 2) {
                    allowedShells.put(newMat, Float.valueOf(sSplit[1]));
                } else {
                    allowedShells.put(newMat, 1.0f);
                }
            }
        }
    }
    
    /**
     * Gets the squared distance.
     * @param pl1 Planetoid 1
     * @param pl2 Planetoid 2
     * 
     * @return Distance
     */
    private int distanceSquared(Planetoid pl1, Planetoid pl2) {
        int xDist = pl2.xPos - pl1.xPos;
        int yDist = pl2.yPos - pl1.yPos;
        int zDist = pl2.zPos - pl1.zPos;

        return xDist * xDist + yDist * yDist + zDist * zDist;
    }

    /**
     * Returns a valid block type.
     * 
     * @param randrandom generator to use
     * @param core if true, searching through allowed cores, otherwise allowed shells
     * @param heated if true, will not return a block that gives off heat
     * 
     * @return Material
     */
    private Material getBlockType(Random rand, boolean core, boolean noHeat) {
        Material retVal = null;
        Map<Material, Float> refMap;
        if (core) {
            refMap = allowedCores;
        } else {
            refMap = allowedShells;
        }
        while (retVal == null) {
            int arrayPos = rand.nextInt(refMap.size());
            Material blkID = (Material) refMap.keySet().toArray()[arrayPos];
            float testVal = rand.nextFloat();
            if (refMap.get(blkID) >= testVal) {
                if (noHeat) {
                    switch (blkID) {
                        case BURNING_FURNACE:
                        case FIRE:
                        case GLOWSTONE:
                        case JACK_O_LANTERN:
                        case STATIONARY_LAVA:
                            break;
                        default:
                            retVal = blkID;
                            break;
                    }
                } else {
                    retVal = blkID;
                }
            }
        }
        return retVal;
    }
}
