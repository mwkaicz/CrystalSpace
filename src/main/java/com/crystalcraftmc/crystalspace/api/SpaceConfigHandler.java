// Package Declaration
package com.crystalcraftmc.crystalspace.api;

import com.crystalcraftmc.crystalspace.config.SpaceConfig;
import com.crystalcraftmc.crystalspace.config.SpaceConfig.ConfigFile;
import com.crystalcraftmc.crystalspace.handlers.MessageHandler;
import com.crystalcraftmc.crystalspace.handlers.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Static methods handle configuration.
 * External use only
 * TODO: Put defaults back to use! (currently commented out... needs some refactoring for it to work)
 *
 * @author iffa
 * @author Jack
 * @author jflory7
 */
public class SpaceConfigHandler {

    /**
     * @see WorldHandler#getID(org.bukkit.World)
     *
     * @param world World
     *
     * @return ID of world
     *
     * @see(WorldHandler.java)
     */
    public static String getID(World world) {
        return WorldHandler.getID(world);
    }
    //Global

    /**
     * Checks if debugging mode is enabled.
     *
     * @return true if debugging mode is enabled
     */
    public static boolean getDebugging() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("debug");
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("debug", (Boolean) DEBUGGING.getDefault());
    }

    /**
     * Gets the helmet given-state of a world.
     *
     * @return true if a helmet is given when teleporting to this world
     */
    public static boolean isHelmetGiven() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.givehelmet");
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.givehelmet", (Boolean) HELMET_GIVEN.getDefault());
    }

    /**
     * Gets the suit given-state of a world.
     *
     * @return true if a suit is given when teleporting to this world
     */
    public static boolean isSuitGiven() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.givesuit");
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.givesuit", (Boolean) SUIT_GIVEN.getDefault());
    }

    /**
     * Gets the helmet
     *
     * @return string of id or name
     */
    public static String getHelmet() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.helmet");
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.helmet", (String) HELMET.getDefault());
    }

    /**
     * Gets the chestplate
     *
     * @return string of id or name
     */
    public static String getChestPlate() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.chestplate");
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.chestplate", (String) CHESTPLATE.getDefault());
    }

    /**
     * Gets the leggings
     *
     * @return string of id or name
     */
    public static String getLeggings() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.leggings");
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.leggings", (String) LEGGINGS.getDefault());
    }

    /**
     * Gets the boots
     *
     * @return string of id or name
     */
    public static String getBoots() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.boots");
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.boots", (String) BOOTS.getDefault());
    }

    /**
     * Gets the suit armortype of a world.
     *
     * @return armortype string
     */
    public static String getArmorType() {
        String armorType = SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype");
        //String armorType = SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype", (String) ARMOR_TYPE.getDefault());
        if (!"id".equals(armorType) && Material.matchMaterial(armorType + "_HELMET") == null) {
            MessageHandler.print(Level.SEVERE, "Invalid armortype '" + SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype") + "' in config!");
            return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype");
            //return (String) ARMOR_TYPE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype");
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype", (String) ARMOR_TYPE.getDefault());
    }

    /**
     * Gets the gravity value.
     *
     * @return True if gravity enabled
     */
    public static boolean getStopDrowning() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.drowning.stopdrowning");
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.drowning.stopdrowning", (Boolean) STOPDROWNING.getDefault());
    }

    /**
     * Gets the gravity value.
     *
     * @return True if gravity enabled
     */
    public static List<World> getStopDrowningWorlds() {
        @SuppressWarnings("unchecked")
        List<String> strings = SpaceConfig.getConfig(ConfigFile.CONFIG).getStringList("global.drowning.worlds");
        List<World> worlds = new ArrayList<World>();
        for (String string : strings) {
            worlds.add(Bukkit.getWorld(string));
        }
        return worlds;
    }

    //ID-specific
    /**
     * Gets the name of the planets file. Not checked if it exists.
     *
     * @param id Id
     *
     * @return name of planets file
     */
    public static String getPlanetsFile(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return "planets.yml";//Never going to change; unless of course someone demands it
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getString("ids." + id + ".generation.planets-file", "planets.yml");
    }

    /**
     * Gets the required helmet-state of a world.
     *
     * @param id Id
     *
     * @return true if a helmet is required
     */
    public static boolean getRequireHelmet(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".helmet.required");
            //return (Boolean) REQUIRE_HELMET.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".helmet.required");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".helmet.required", (Boolean) REQUIRE_HELMET.getDefault());
    }

    /**
     * Gets the required suit-state of a world.
     *
     * @param id ID
     *
     * @return true if a suit is required
     */
    public static boolean getRequireSuit(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".suit.required");
            //return (Boolean) REQUIRE_SUIT.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".suit.required");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".suit.required", (Boolean) REQUIRE_SUIT.getDefault());
    }

    /**
     * Gets the force night-state of a world.
     *
     * @param id ID
     *
     * @return true if night is forced
     */
    public static boolean forceNight(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".alwaysnight");
            //return (Boolean) FORCE_NIGHT.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".alwaysnight");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".alwaysnight", (Boolean) FORCE_NIGHT.getDefault());
    }

    /**
     * Gets the maximum room height of a world.
     *
     * @param id Id
     *
     * @return room height int
     */
    public static int getRoomHeight(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".maxroomheight");
            //return (Integer) ROOM_HEIGHT.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".maxroomheight");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".maxroomheight", (Integer) ROOM_HEIGHT.getDefault());
    }

    /**
     * Gets the glowstone chance of a world.
     *
     * @param id Id
     *
     * @return glowstone chance int
     */
    public static int getGlowstoneChance(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.glowstonechance");
            //return (Integer) GLOWSTONE_CHANCE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.glowstonechance");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.glowstonechance", (Integer) GLOWSTONE_CHANCE.getDefault());
    }

    /**
     * Gets the stone chance of a world.
     *
     * @param id ID
     *
     * @return asteroid chance int
     */
    public static int getStoneChance(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.stonechance");
            //return (Integer) STONE_CHANCE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.stonechance");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.stonechance", (Integer) STONE_CHANCE.getDefault());
    }

    /**
     * Checks if asteroid generation is enabled for a world.
     *
     * @param id
     *
     * @return true if asteroid generation is enabled
     */
    public static boolean getAsteroidsEnabled(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateasteroids");
            //return (Boolean) ASTEROIDS_ENABLED.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateasteroids");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateasteroids", (Boolean) ASTEROIDS_ENABLED.getDefault());
    }

    /**
     * Checks if satellites are enabled.
     *
     * @param id ID
     *
     * @return True if satellites are enabled
     */
    public static boolean getSatellitesEnabled(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generatesatellites");
            //return (Boolean) SATELLITES_ENABLED.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generatesatellites");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generatesatellites", (Boolean) SATELLITES_ENABLED.getDefault());
    }

    /**
     * Gets the satellite spawn chance.
     *
     * @param id ID
     *
     * @return Spawn chance
     */
    public static int getSatelliteChance(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.satellitechance");
            //return (Integer) SATELLITE_CHANCE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.satellitechance");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.satellitechance", (Integer) SATELLITE_CHANCE.getDefault());
    }

    /**
     * Gets generate planets value.
     *
     * @param id ID
     *
     * @return True if generateplantes=true
     */
    public static boolean getGeneratePlanets(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateplanets");
            //return (Boolean) GENERATE_PLANETS.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateplanets");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateplanets", (Boolean) GENERATE_PLANETS.getDefault());
    }

    /**
     * Gets generate schematics value.
     *
     * @param id ID
     *
     * @return True if generateschematics=true
     */
    public static boolean getGenerateSchematics(String id) {
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateschematics");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateschematics", (Boolean) GENERATE_SCHEMATICS.getDefault());
    }

    /**
     * Gets the schematic-chance.
     *
     * @param id ID
     *
     * @return Schematic chance
     */
    public static int getSchematicChance(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.schematicchance");
            //return (Integer) SCHEMATIC_CHANCE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.schematicchance");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.schematicchance", (Integer) SCHEMATIC_CHANCE.getDefault());
    }
    

     /**
     * Gets the generate black holes without spout value.
     *
     * @param id ID
     *
     * @return True if generating black holes without spout
     */
    public static boolean getGenerateBlackHoles(String id) {
        // TODO: clean up any leftover Spout-related naming etc.
        if (id.equalsIgnoreCase("planets")  ) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateblackholes");
            //return (Boolean) NONSPOUT_BLACKHOLES.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateblackholes");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.nonspoutblackholes", (Boolean) SPOUT_BLACKHOLES.getDefault());
    }

    /**
     * Gets the black hole chance.
     *
     * @param id ID
     *
     * @return Black hole chance
     */
    public static int getBlackHoleChance(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.blackholechance");
            //return (Integer) BLACKHOLE_CHANCE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.blackholechance");
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.blackholechance", (Integer) BLACKHOLE_CHANCE.getDefault());
    }

    /**
     * Constructor of SpaceConfigHandler.
     */
    protected SpaceConfigHandler() {
    }
}
