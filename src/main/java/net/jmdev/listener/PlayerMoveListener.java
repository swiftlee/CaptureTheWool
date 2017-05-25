package net.jmdev.listener;

import net.jmdev.CaptureTheWool;
import net.jmdev.core.GameManager;
import net.jmdev.core.GameScoreboard;
import net.jmdev.core.GameState;
import net.jmdev.core.WoolStatus;
import net.jmdev.util.TextUtils;
import net.jmdev.util.Title;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/24/2017 | 17:19
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
public class PlayerMoveListener implements Listener {

    private static Vector maxBlue;
    private static Vector minBlue;
    private static Vector maxRed;
    private static Vector minRed;
    private static ArrayList<Location> blueLocs = new ArrayList<>();
    private static ArrayList<Location> redLocs = new ArrayList<>();
    private static boolean regionsSet = false;
    private CaptureTheWool plugin;
    private FileConfiguration config;

    public PlayerMoveListener(CaptureTheWool plugin, FileConfiguration config) {

        this.plugin = plugin;
        this.config = plugin.getConfig();

    }

    private boolean inBlueRegion(Player p) {

        for (Location loc : blueLocs) {

            if (loc.getBlockX() == p.getLocation().getBlockX() && loc.getBlockY() == p.getLocation().getBlockY() && loc.getBlockZ() == p.getLocation().getBlockZ())
                return true;

        }

        return false;

    }

    private boolean inRedRegion(Player p) {

        for (Location loc : redLocs) {

            if (loc.getBlockX() == p.getLocation().getBlockX() && loc.getBlockY() == p.getLocation().getBlockY() && loc.getBlockZ() == p.getLocation().getBlockZ())
                return true;

        }

        return false;

    }

    private boolean hasWool(Player p) {

        return p.hasMetadata("wool");

    }

    private boolean isBlueTeam(Player p) {

        return p.getScoreboard().getTeam("Blue") != null;

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        if (GameState.getGameState() == GameState.PLAYING) {

            if (!regionsSet) {

                maxBlue = Vector.getMaximum(TextUtils.parseLocation(config.getStringList("region.blueWoolCapture").get(0)).toVector(), TextUtils.parseLocation(config.getStringList("region.blueWoolCapture").get(1)).toVector());
                minBlue = Vector.getMinimum(TextUtils.parseLocation(config.getStringList("region.blueWoolCapture").get(0)).toVector(), TextUtils.parseLocation(config.getStringList("region.blueWoolCapture").get(1)).toVector());
                maxRed = Vector.getMaximum(TextUtils.parseLocation(config.getStringList("region.redWoolCapture").get(0)).toVector(), TextUtils.parseLocation(config.getStringList("region.redWoolCapture").get(1)).toVector());
                minRed = Vector.getMinimum(TextUtils.parseLocation(config.getStringList("region.redWoolCapture").get(0)).toVector(), TextUtils.parseLocation(config.getStringList("region.redWoolCapture").get(1)).toVector());

                String gameWorld = config.getString("game.worldName");

                for (int i = minBlue.getBlockX(); i <= maxBlue.getBlockX(); i++) { //loop through x
                    for (int j = minBlue.getBlockY(); j <= maxBlue.getBlockY(); j++) { //loop through y
                        for (int k = minBlue.getBlockZ(); k <= maxBlue.getBlockZ(); k++) { //loop through z

                            Block block = Bukkit.getServer().getWorld(gameWorld).getBlockAt(i, j, k);
                            blueLocs.add(block.getLocation());

                        }
                    }
                }

                for (int i = minRed.getBlockX(); i <= maxRed.getBlockX(); i++) { //loop through x
                    for (int j = minRed.getBlockY(); j <= maxRed.getBlockY(); j++) { //loop through y
                        for (int k = minRed.getBlockZ(); k <= maxRed.getBlockZ(); k++) { //loop through z

                            Block block = Bukkit.getServer().getWorld(gameWorld).getBlockAt(i, j, k);
                            redLocs.add(block.getLocation());

                        }
                    }
                }

                regionsSet = true;

            }

            if (e.getTo().getX() != e.getFrom().getX() && e.getTo().getZ() != e.getFrom().getZ()) {

                if (e.getPlayer().hasMetadata("frozen")) {

                    e.getPlayer().teleport(e.getFrom());

                } else {

                    if (isBlueTeam(e.getPlayer())) {

                        if (!hasWool(e.getPlayer())) {

                            if (inRedRegion(e.getPlayer())) {

                                //RED HAS BEEN TAKEN!
                                e.getPlayer().getEquipment().setHelmet(new ItemStack(Material.WOOL, 1, (short) 14));
                                WoolStatus.setRedStatus(WoolStatus.TAKEN);
                                GameScoreboard.updateBoard(plugin);
                                e.getPlayer().setMetadata("wool", new FixedMetadataValue(plugin, ""));
                                BlockState state = Bukkit.getWorld(config.getString("game.worldName")).getBlockAt(TextUtils.parseLocation(config.getString("game.redWoolLocation"))).getState();
                                state.setType(Material.AIR);
                                state.setData(new MaterialData(Material.AIR));
                                state.update(true, false);
                                Bukkit.broadcastMessage(TextUtils.formatText(config.getString("messages.woolTaken").replace("{woolColor}", "&cRed")));

                            }

                        } else {

                            if (inBlueRegion(e.getPlayer())) {

                                //RED HAS BEEN CAPTURED!
                                e.getPlayer().getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
                                WoolStatus.setRedStatus(WoolStatus.IN_SPAWN);
                                GameManager.blueCaptured++;
                                GameScoreboard.updateBoard(plugin);
                                e.getPlayer().removeMetadata("wool", plugin);
                                BlockState state = Bukkit.getWorld(config.getString("game.worldName")).getBlockAt(TextUtils.parseLocation(config.getString("game.redWoolLocation"))).getState();
                                state.setType(Material.WOOL);
                                state.setData(new MaterialData(Material.WOOL, (byte) 14));
                                state.update(true, false);

                                if (GameManager.blueCaptured >= config.getInt("gameRules.woolLimit")) {

                                    GameState.setGameState(GameState.FINISHED);

                                    Bukkit.getScheduler().cancelTasks(plugin);
                                    GameManager.redTeam.clear();
                                    GameManager.blueTeam.clear();
                                    GameManager.redCaptured = 0;
                                    GameManager.blueCaptured = 0;
                                    WoolStatus.setBlueStatus(WoolStatus.IN_SPAWN);
                                    WoolStatus.setRedStatus(WoolStatus.IN_SPAWN);

                                    Bukkit.getOnlinePlayers().stream().forEach(p -> {

                                        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                                        p.setLevel(0);

                                    });

                                    Bukkit.broadcastMessage(TextUtils.formatText("&aGame over! &9Blue &ateam has won!"));
                                    new Title(TextUtils.formatText("&aGame Over!"), TextUtils.formatText("&9Blue &ateam has won!"), 1, 2, 1).broadcast();

                                    for (int i = 0; i < 10; i++) {

                                        final int j = i;

                                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

                                            Bukkit.broadcastMessage(TextUtils.formatText("&7Restarting game server in &9" + (10 - j) + " seconds."));

                                            if (j == 9) {

                                                Bukkit.getOnlinePlayers().stream().forEach(p -> p.teleport(TextUtils.parseLocation(config.getString("lobby.spawnLocation"))));
                                                GameState.setGameState(GameState.LOBBY);
                                                Bukkit.getServer().shutdown();

                                            }

                                        }, i * 20);

                                    }

                                    return;

                                } else {

                                    e.getPlayer().setLevel(e.getPlayer().getLevel() + config.getInt("coins.capture"));
                                    Bukkit.broadcastMessage(TextUtils.formatText(config.getString("messages.woolCaptured").replace("{woolColor}", "&cRed")));

                                }

                            }

                        }

                    } else {

                        if (!hasWool(e.getPlayer())) {

                            if (inBlueRegion(e.getPlayer())) {

                                //BLUE HAS BEEN TAKEN!
                                e.getPlayer().getEquipment().setHelmet(new ItemStack(Material.WOOL, 1, (short) 11));
                                WoolStatus.setBlueStatus(WoolStatus.TAKEN);
                                GameScoreboard.updateBoard(plugin);
                                e.getPlayer().setMetadata("wool", new FixedMetadataValue(plugin, ""));
                                BlockState state = Bukkit.getWorld(config.getString("game.worldName")).getBlockAt(TextUtils.parseLocation(config.getString("game.blueWoolLocation"))).getState();
                                state.setType(Material.AIR);
                                state.setData(new MaterialData(Material.AIR));
                                state.update(true, false);
                                Bukkit.broadcastMessage(TextUtils.formatText(config.getString("messages.woolTaken").replace("{woolColor}", "&9Blue")));

                            }

                        } else {

                            if (inRedRegion(e.getPlayer())) {

                                //BLUE HAS BEEN CAPTURED!
                                e.getPlayer().getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
                                WoolStatus.setBlueStatus(WoolStatus.IN_SPAWN);
                                GameManager.redCaptured++;
                                GameScoreboard.updateBoard(plugin);
                                e.getPlayer().removeMetadata("wool", plugin);
                                BlockState state = Bukkit.getWorld(config.getString("game.worldName")).getBlockAt(TextUtils.parseLocation(config.getString("game.blueWoolLocation"))).getState();
                                state.setType(Material.WOOL);
                                state.setData(new MaterialData(Material.WOOL, (byte) 11));
                                state.update(true, false);

                                if (GameManager.redCaptured >= config.getInt("gameRules.woolLimit")) {

                                    GameState.setGameState(GameState.FINISHED);

                                    Bukkit.getScheduler().cancelTasks(plugin);
                                    GameManager.redTeam.clear();
                                    GameManager.blueTeam.clear();
                                    GameManager.redCaptured = 0;
                                    GameManager.blueCaptured = 0;
                                    WoolStatus.setBlueStatus(WoolStatus.IN_SPAWN);
                                    WoolStatus.setRedStatus(WoolStatus.IN_SPAWN);

                                    Bukkit.getOnlinePlayers().stream().forEach(p -> {

                                        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                                        p.setLevel(0);

                                    });

                                    Bukkit.broadcastMessage(TextUtils.formatText("&aGame over! &cRed &ateam has won!"));
                                    new Title(TextUtils.formatText("&aGame Over!"), TextUtils.formatText("&cRed &ateam has won!"), 1, 2, 1).broadcast();

                                    for (int i = 0; i < 10; i++) {

                                        final int j = i;

                                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

                                            Bukkit.broadcastMessage(TextUtils.formatText("&7Restarting game server in &9" + (10 - j) + " seconds."));

                                            if (j == 9) {

                                                Bukkit.getOnlinePlayers().stream().forEach(p -> p.teleport(TextUtils.parseLocation(config.getString("lobby.spawnLocation"))));
                                                GameState.setGameState(GameState.LOBBY);
                                                Bukkit.getServer().shutdown();

                                            }

                                        }, i * 20);

                                    }

                                    return;

                                } else {

                                    e.getPlayer().setLevel(e.getPlayer().getLevel() + config.getInt("coins.capture"));
                                    Bukkit.broadcastMessage(TextUtils.formatText(config.getString("messages.woolCaptured").replace("{woolColor}", "&9Blue")));

                                }

                            }

                        }

                    }

                }

            }

        }

    }

}
