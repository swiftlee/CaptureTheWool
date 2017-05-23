package net.jmdev.core;

import net.jmdev.CaptureTheWool;
import net.jmdev.database.CoarseDirtDatabase;
import net.jmdev.util.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/03/2017 | 21:41
 * __________________
 *
 *  [2016] J&M Plugin Development 
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of J&M Plugin Development and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to J&M Plugin Development
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from J&M Plugin Development.
 */
public class GameManager {

    public static List<Player> blueTeam;
    public static List<Player> redTeam;
    public static boolean teamsSet;
    public static boolean woolSet = false;
    public static int redCaptured = 0;
    public static int blueCaptured = 0;
    static CoarseDirtDatabase database = new CoarseDirtDatabase();
    static int totalTimeLeft;
    static short intervalCounter;

    /**
     * @param gameStart - functional interface used to call the start method using lambda
     *                  expressions.
     * @method start - used to start the game.
     */

    public static void start(IGameStart gameStart) {

        gameStart.start();

    }

    /**
     * @method createTeams - creates the teams with random players.
     */

    public static void createTeams() {

        ArrayList<Player> blueTeam = new ArrayList<>(), redTeam = new ArrayList<>();
        ArrayList<Player> onlinePlayers = new ArrayList<>();
        onlinePlayers.addAll(Bukkit.getOnlinePlayers());

        for (int i = 0; i < Math.ceil((double) onlinePlayers.size() / 2); i++) {

            blueTeam.add(onlinePlayers.get(i));

        }

        int counter = 0;

        for (Player p : Bukkit.getOnlinePlayers()) {

            try {

                if (p.getUniqueId() == blueTeam.get(counter).getUniqueId()) {

                    onlinePlayers.remove(counter);

                }

            } catch (IndexOutOfBoundsException e) {

                if (onlinePlayers.size() == Math.floor(Bukkit.getOnlinePlayers().size() / 2)) {

                    redTeam.addAll(onlinePlayers);
                    break;

                }

            }

            counter++;

        }

        GameManager.blueTeam = blueTeam;
        GameManager.redTeam = redTeam;
        teamsSet = true;

    }

    /**
     * @param gameWorld         - the world the players will be playing the game on.
     * @param blueTeamLocations - the locations in gameWorld where blueTeam will spawn
     * @param redTeamLocations  - the locations in the gameWorld where redTeam will spawn
     * @method sendTeamsToSpawn - collectively sends redTeam and blueTeam to their respective spawn
     * points.
     */

    public static void sendTeamsToSpawn(World gameWorld, List<String> blueTeamLocations, List<String> redTeamLocations) {

        if (teamsSet) {

            String blueTeamLocation;
            String redTeamLocation;

            if (blueTeamLocations.size() == 1)
                blueTeamLocation = blueTeamLocations.get(0);
            else
                blueTeamLocation = blueTeamLocations.get(new Random().nextInt(blueTeamLocations.size() - 1));

            if (redTeamLocations.size() == 1)
                redTeamLocation = redTeamLocations.get(0);
            else
                redTeamLocation = redTeamLocations.get(new Random().nextInt(redTeamLocations.size() - 1));

            double x;
            double y;
            double z;
            float yaw;
            float pit;
            Location loc;

            for (Player p : blueTeam) {

                x = Double.valueOf(blueTeamLocation.split(",")[1]);
                y = Double.valueOf(blueTeamLocation.split(",")[2]);
                z = Double.valueOf(blueTeamLocation.split(",")[3]);
                yaw = Float.valueOf(blueTeamLocation.split(",")[4]);
                pit = Float.valueOf(blueTeamLocation.split(",")[5]);

                loc = new Location(gameWorld, x, y, z, yaw, pit);
                p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);

            }

            for (Player p : redTeam) {

                x = Double.valueOf(redTeamLocation.split(",")[1]);
                y = Double.valueOf(redTeamLocation.split(",")[2]);
                z = Double.valueOf(redTeamLocation.split(",")[3]);
                yaw = Float.valueOf(redTeamLocation.split(",")[4]);
                pit = Float.valueOf(redTeamLocation.split(",")[5]);

                loc = new Location(gameWorld, x, y, z, yaw, pit);
                p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);

            }

        }

    }

    public static void startGameCountdown(CaptureTheWool plugin) {

        final int maxGameTime = plugin.getConfig().getInt("maxGameTime");
        totalTimeLeft = plugin.getConfig().getInt("maxGameTime");

        final int interval = plugin.getConfig().getInt("timeLeftInterval");
        intervalCounter = 1;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            totalTimeLeft--;

            if (maxGameTime - (interval * intervalCounter) == totalTimeLeft) {

                Bukkit.broadcastMessage(TextUtils.formatText(plugin.getConfig().getString("messages.timeLeft").replace("{time}", TextUtils.formatTime(totalTimeLeft)).replace("{timeUnit}", TextUtils.getTimeUnit(totalTimeLeft))));
                intervalCounter++;

            }

        }, 0L, 20L);

    }

    public static void saveCoarseBlocksAndReplace(CaptureTheWool plugin) {

        FileConfiguration config = plugin.getConfig();

        String gameWorld = config.getString("game.worldName");

        Location loc1 = TextUtils.parseLocation(config.getStringList("region.coarseDirt").get(0));
        Location loc2 = TextUtils.parseLocation(config.getStringList("region.coarseDirt").get(1));

        Vector max = Vector.getMaximum(loc1.toVector(), loc2.toVector());
        Vector min = Vector.getMinimum(loc1.toVector(), loc2.toVector());

        boolean dbLoaded;

        try {

            database.getYML().load("plugins/CaptureTheWool/dirtDatabase.yml");
            dbLoaded = true;

        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            dbLoaded = false;
        } catch (FileNotFoundException e) {
            dbLoaded = false;
        } catch (IOException e) {
            e.printStackTrace();
            dbLoaded = false;
        }

        ArrayList<String> locList = database.getYML().getStringList("locations") == null ? new ArrayList<>() : (ArrayList<String>) database.getYML().getStringList("locations");

        if (dbLoaded) {

            locList.stream().forEach(location -> {

                Block b = TextUtils.parseLocation(location).getBlock();
                BlockState state = b.getState();
                state.setType(Material.DIRT);
                state.setData(new MaterialData(Material.DIRT, (byte) 1));
                state.update(true, false);

            });


        } else {

            for (int i = min.getBlockX(); i <= max.getBlockX(); i++) { //loop through x
                for (int j = min.getBlockY(); j <= max.getBlockY(); j++) { //loop through y
                    for (int k = min.getBlockZ(); k <= max.getBlockZ(); k++) { //loop through z
                        if (new Random().nextInt(100) <= config.getInt("coarseDirtChance")) {

                            Block block = Bukkit.getServer().getWorld(gameWorld).getBlockAt(i, j, k);
                            database.addNewLocation(TextUtils.locationToString(block.getLocation(), false), locList, max.getBlockX() + max.getBlockY() + max.getBlockZ(), i + j + k);

                            BlockState state = block.getState();
                            state.setType(Material.DIRT);
                            state.setData(new MaterialData(Material.DIRT, (byte) 1));
                            state.update(true, false);

                        }

                    }

                }

            }

        }

    }
}