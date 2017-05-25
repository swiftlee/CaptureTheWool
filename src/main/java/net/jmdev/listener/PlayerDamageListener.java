package net.jmdev.listener;

import net.jmdev.CaptureTheWool;
import net.jmdev.core.GameScoreboard;
import net.jmdev.core.GameState;
import net.jmdev.core.WoolStatus;
import net.jmdev.util.TextUtils;
import net.jmdev.util.Title;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;

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
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {

        if (GameState.getGameState() == GameState.PLAYING)
            e.setCancelled(true);

    }

    private boolean hasWool(Player p) {

        return p.hasMetadata("wool");

    }


    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {

        if (GameState.getGameState() == GameState.PLAYING) {

            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player || e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {

                Player killed = (Player) e.getEntity();
                Player killer;

                if (killed.hasMetadata("frozen")) {

                    e.setCancelled(true);
                    return;

                }

                if (e.getDamager() instanceof Arrow) {

                    Arrow a = (Arrow) e.getDamager();
                    killer = (Player) a.getShooter();

                } else {

                    killer = (Player) e.getDamager();

                }

                if (e.getDamage() >= killed.getHealth()) {

                    killer.setLevel(killer.getLevel() + config.getInt("coins.perKill"));

                    for (int i = 0; i < config.getInt("gameRules.deathFreezeTime"); i++) {

                        final int j = i;

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> new Title(TextUtils.formatText("&0"), TextUtils.formatText("&aUnfrozen in &7&l" + (config.getInt("gameRules.deathFreezeTime") - j)), 1, 1, 1).send(killed), i * 20);

                    }

                    new Title(TextUtils.formatText("&0"), TextUtils.formatText(config.getString("messages.death").replace("{time}", config.getInt("gameRules.deathFreezeTime") + "")), 1, 5, 1).send(killed);

                    if (killed.getScoreboard().getTeam("Red") != null) {

                        if (hasWool(killed)) {

                            //BLUE WOOL DROPPED
                            BlockState state = Bukkit.getWorld(config.getString("game.worldName")).getBlockAt(TextUtils.parseLocation(config.getString("game.blueWoolLocation"))).getState();
                            state.setType(Material.WOOL);
                            state.setData(new MaterialData(Material.WOOL, (byte) 11));
                            state.update(true, false);
                            killed.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
                            Bukkit.broadcastMessage(TextUtils.formatText(config.getString("messages.woolDropped").replace("{woolColor}", "&9Blue")));
                            WoolStatus.setBlueStatus(WoolStatus.IN_SPAWN);
                            GameScoreboard.updateBoard(plugin);

                        }

                        List<String> redTeamLocations = config.getStringList("spawnLocations.redTeam");
                        String redTeamLocation;

                        if (redTeamLocations.size() == 1)
                            redTeamLocation = redTeamLocations.get(0);
                        else
                            redTeamLocation = redTeamLocations.get(new Random().nextInt(redTeamLocations.size() - 1));

                        Location loc = TextUtils.parseLocation(redTeamLocation);
                        killed.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);

                    } else if (killed.getScoreboard().getTeam("Blue") != null) {

                        if (hasWool(killed)) {

                            //RED WOOL DROPPED
                            BlockState state = Bukkit.getWorld(config.getString("game.worldName")).getBlockAt(TextUtils.parseLocation(config.getString("game.redWoolLocation"))).getState();
                            state.setType(Material.WOOL);
                            state.setData(new MaterialData(Material.WOOL, (byte) 14));
                            state.update(true, false);
                            killed.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
                            Bukkit.broadcastMessage(TextUtils.formatText(config.getString("messages.woolDropped").replace("{woolColor}", "&cRed")));
                            WoolStatus.setRedStatus(WoolStatus.IN_SPAWN);
                            GameScoreboard.updateBoard(plugin);

                        }

                        List<String> blueTeamLocations = config.getStringList("spawnLocations.blueTeam");
                        String blueTeamLocation;

                        if (blueTeamLocations.size() == 1)
                            blueTeamLocation = blueTeamLocations.get(0);
                        else
                            blueTeamLocation = blueTeamLocations.get(new Random().nextInt(blueTeamLocations.size() - 1));

                        Location loc = TextUtils.parseLocation(blueTeamLocation);
                        killed.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);

                    }

                    killed.setHealth(killed.getMaxHealth());
                    killed.setWalkSpeed(0);
                    killed.setMetadata("frozen", new FixedMetadataValue(plugin, "frozen"));

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

                        killed.setWalkSpeed(0.2F);
                        killed.removeMetadata("frozen", plugin);

                    }, plugin.getConfig().getInt("gameRules.deathFreezeTime") * 20);

                    e.setCancelled(true);

                }

            }
        } else {

            e.setCancelled(true);

        }

    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {

        if (GameState.getGameState() == GameState.PLAYING) {

            if (e.getBlock().getType() == Material.DIRT && e.getBlock().getData() == 1) {

                if (new Random().nextInt(100) <= plugin.getConfig().getInt("chances.coinsCoarseDirt")) {

                    e.getPlayer().setLevel(e.getPlayer().getLevel() + plugin.getConfig().getInt("coins.perCoarseDirt"));

                }

            } else {

                e.setCancelled(true);

            }

        } else {

            e.setCancelled(true);

        }

    }

    @EventHandler
    public void onExpSpawn(EntitySpawnEvent e) {

        if (e.getEntity() instanceof ExperienceOrb) {

            e.setCancelled(true);

        }

    }

}
