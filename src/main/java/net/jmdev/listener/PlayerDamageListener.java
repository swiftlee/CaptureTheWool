package net.jmdev.listener;

import net.jmdev.CaptureTheWool;
import net.jmdev.core.GameState;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/20/2017 | 11:11
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
public class PlayerDamageListener implements Listener {

    private CaptureTheWool plugin;
    private FileConfiguration config;

    public PlayerDamageListener(CaptureTheWool plugin) {

        this.plugin = plugin;
        this.config = plugin.getConfig();

    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {

        if (GameState.getGameState() == GameState.PLAYING) {

            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {

                Player killed = (Player) e.getEntity();
                Player killer = (Player) e.getDamager();

                killer.setExp(killer.getExp() + config.getInt("coins.perKill"));

                if (e.getDamage() >= killed.getHealth()) {

                    if (killed.getScoreboard().getTeam("Red") != null) {

                        List<String> redTeamLocations = config.getStringList("spawnLocations.blueTeam");
                        String redTeamLocation;

                        if (redTeamLocations.size() == 1)
                            redTeamLocation = redTeamLocations.get(0);
                        else
                            redTeamLocation = redTeamLocations.get(new Random().nextInt(redTeamLocations.size() - 1));

                        double x = Double.valueOf(redTeamLocation.split(",")[1]);
                        double y = Double.valueOf(redTeamLocation.split(",")[2]);
                        double z = Double.valueOf(redTeamLocation.split(",")[3]);
                        float yaw = Float.valueOf(redTeamLocation.split(",")[4]);
                        float pit = Float.valueOf(redTeamLocation.split(",")[5]);

                        Location loc = new Location(Bukkit.getWorld(config.getString("game.worldName")), x, y, z, yaw, pit);
                        killed.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        //freeze player on teleport

                    } else if (killed.getScoreboard().getTeam("Blue") != null) {

                        List<String> blueTeamLocations = config.getStringList("spawnLocations.blueTeam");
                        String blueTeamLocation;

                        if (blueTeamLocations.size() == 1)
                            blueTeamLocation = blueTeamLocations.get(0);
                        else
                            blueTeamLocation = blueTeamLocations.get(new Random().nextInt(blueTeamLocations.size() - 1));

                        double x = Double.valueOf(blueTeamLocation.split(",")[1]);
                        double y = Double.valueOf(blueTeamLocation.split(",")[2]);
                        double z = Double.valueOf(blueTeamLocation.split(",")[3]);
                        float yaw = Float.valueOf(blueTeamLocation.split(",")[4]);
                        float pit = Float.valueOf(blueTeamLocation.split(",")[5]);

                        Location loc = new Location(Bukkit.getWorld(config.getString("game.worldName")), x, y, z, yaw, pit);
                        killed.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        //freeze player on teleport

                    }

                    killed.setHealth(killed.getMaxHealth());
                    e.setCancelled(true);

                }

            }
        }

    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {

        if (e.getBlock().getState().getData().toItemStack() == new ItemStack(Material.DIRT, (byte) 1)) {

            if (new Random().nextInt(100) <= plugin.getConfig().getInt("chances.coinsCoarseDirt")) {

                e.getPlayer().setExp(e.getPlayer().getExp() + plugin.getConfig().getInt("coins.perCoarseDirt"));

            }

        }

    }

    @EventHandler
    public void onExpSpawn(EntitySpawnEvent e) {

        if (e.getEntity() instanceof ExperienceOrb) {

            e.setCancelled(true);

        }

    }

}
