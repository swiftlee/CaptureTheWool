package net.jmdev.core;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

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
    public static boolean woolSet;

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
        ArrayList<Player> onlinePlayers = (ArrayList<Player>) Bukkit.getOnlinePlayers();

        for (int i = 0; i < Math.ceil((double) onlinePlayers.size() / 2); i++) {

            blueTeam.add(onlinePlayers.get(i));

        }

        int counter = 0;

        for (Player p : onlinePlayers) {

            if (p.getUniqueId() != blueTeam.get(counter).getUniqueId()) {

                redTeam.add(blueTeam.get(counter));

                if (redTeam.size() == Math.floor(onlinePlayers.size() / 2))
                    break;

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

            String blueTeamLocation = blueTeamLocations.get(new Random().nextInt(blueTeamLocations.size() - 1));
            String redTeamLocation = redTeamLocations.get(new Random().nextInt(redTeamLocations.size() - 1));

            double x;
            double y;
            double z;
            float yaw;
            float pit;
            Location loc;

            for (Player p : blueTeam) {

                x = Double.valueOf(blueTeamLocation.split(",")[0]);
                y = Double.valueOf(blueTeamLocation.split(",")[1]);
                z = Double.valueOf(blueTeamLocation.split(",")[2]);
                yaw = Float.valueOf(blueTeamLocation.split(",")[3]);
                pit = Float.valueOf(blueTeamLocation.split(",")[4]);

                loc = new Location(gameWorld, x, y, z, yaw, pit);
                p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);

            }

            for (Player p : redTeam) {

                x = Double.valueOf(redTeamLocation.split(",")[0]);
                y = Double.valueOf(redTeamLocation.split(",")[1]);
                z = Double.valueOf(redTeamLocation.split(",")[2]);
                yaw = Float.valueOf(redTeamLocation.split(",")[3]);
                pit = Float.valueOf(redTeamLocation.split(",")[4]);

                loc = new Location(gameWorld, x, y, z, yaw, pit);
                p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);

            }

        }

    }

}
