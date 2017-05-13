package net.jmdev.listener;

import net.jmdev.CaptureTheWool;
import net.jmdev.core.BungeeMode;
import net.jmdev.core.GameManager;
import net.jmdev.core.GameState;
import net.jmdev.util.TextUtils;
import net.jmdev.util.Title;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitTask;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/07/2017 | 23:15
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
public class PlayerJoinListener implements Listener {

    private CaptureTheWool plugin;
    private FileConfiguration config;
    private boolean isEnoughPlayers = true;
    private BukkitTask task;
    private int playersNeeded;

    public PlayerJoinListener(CaptureTheWool plugin) {

        this.plugin = plugin;
        config = plugin.getConfig();

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        if (BungeeMode.getMode() == BungeeMode.OFF) {

            if (config.getString("lobby.worldName").equalsIgnoreCase(e.getPlayer().getWorld().getName())) { //if is lobby world

                if (config.getInt("players.limit") >= Bukkit.getOnlinePlayers().size()) { //if the player limit has not been reached yet

                    e.setJoinMessage(TextUtils.formatText("&a" + e.getPlayer().getDisplayName() + " &ahas joined! (" + Bukkit.getOnlinePlayers().size() + "/" + config.getInt("players.limit") + ")"));

                    if (config.getInt("players.toStart") <= Bukkit.getOnlinePlayers().size()) { //if enough players to start

                        if (config.getInt("lobby.countdown") > 0) { //if there is a countdown

                            //starts the game with countdown

                            GameManager.start(() -> {

                                for (int i = 0; i < config.getInt("lobby.countdown"); i++) {

                                    final int j = i;

                                    task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {

                                        playersNeeded = config.getInt("players.toStart") - Bukkit.getOnlinePlayers().size();
                                        isEnoughPlayers = playersNeeded <= 0;

                                        if (!isEnoughPlayers && task != null) { //if not enough players and task is null

                                            Bukkit.broadcastMessage(TextUtils.formatText("&cCountdown cancelled! Waiting for &7" + ((playersNeeded == 1 ? "1 &cmore player." : playersNeeded + " &cmore players."))));
                                            GameState.setGameState(GameState.LOBBY);
                                            Bukkit.getScheduler().cancelTasks(plugin);

                                        } else if ((config.getInt("lobby.countdown") - j) == 1) {

                                            Bukkit.broadcastMessage(TextUtils.formatText("&aStarting..."));
                                            GameState.setGameState(GameState.PLAYING);

                                            GameManager.createTeams();
                                            GameManager.sendTeamsToSpawn(Bukkit.getWorld(config.getString("game.worldName")), config.getStringList("spawnLocations.blueTeam"), config.getStringList("spawnLocations.redTeam"));

                                        } else {

                                            new Title(TextUtils.formatText("&0"), TextUtils.formatText("&aGame starting in &a&l" + (config.getInt("lobby.countdown") - j)), 1, 1, 1).broadcast();
                                            GameState.setGameState(GameState.STARTING);

                                        }

                                    }, i * 20);

                                }

                            });

                        } else {

                            //starts the game with no countdown
                            GameState.setGameState(GameState.PLAYING);

                            GameManager.createTeams();
                            GameManager.sendTeamsToSpawn(Bukkit.getWorld(config.getString("game.worldName")), config.getStringList("spawnLocations.blueTeam"), config.getStringList("spawnLocations.redTeam"));

                        }

                    }

                } else {

                    //put player back to where they came from
                    e.getPlayer().kickPlayer(TextUtils.formatText("&cThis lobby is currently full!"));

                }

            } else if (config.getString("game.worldName").equalsIgnoreCase(e.getPlayer().getWorld().getName())) {

                if (!GameManager.woolSet) { //if wool has not been placed

                    GameManager.woolSet = true;
                    Location blueWoolLoc = TextUtils.parseLocation(config.getString("game.blueWoolLocation"));
                    Location redWoolLoc = TextUtils.parseLocation(config.getString("game.redWoolLocation"));

                    blueWoolLoc.getBlock().getState().setData(new MaterialData(Material.WOOL, (byte) 11));
                    redWoolLoc.getBlock().getState().setData(new MaterialData(Material.WOOL, (byte) 14));

                }

            }

        } else {

            if (e.getPlayer().hasPermission("ctw.bungeemode"))
                Bukkit.broadcastMessage(TextUtils.formatText("&aServer running in &7BungeeMode&a, please change make your changes and set 'bungeemode' to off in the config when finished."));
            else
                e.getPlayer().kickPlayer(TextUtils.formatText("&cThis mini-game is currently under maintenance."));

        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        if (config.getString("lobby.worldName").equalsIgnoreCase(e.getPlayer().getWorld().getName())) {

            e.setQuitMessage(TextUtils.formatText("&c" + e.getPlayer().getDisplayName() + " &ahas left! (" + (Bukkit.getOnlinePlayers().size() - 1) + "/" + config.getInt("players.limit") + ")"));

        }

    }

}
