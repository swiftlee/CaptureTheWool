package net.jmdev.listener;

import net.jmdev.CaptureTheWool;
import net.jmdev.core.BungeeMode;
import net.jmdev.core.GameManager;
import net.jmdev.core.GameScoreboard;
import net.jmdev.core.GameState;
import net.jmdev.core.ItemHandler;
import net.jmdev.core.WoolStatus;
import net.jmdev.database.CoarseDirtDatabase;
import net.jmdev.util.TextUtils;
import net.jmdev.util.Title;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

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

    static CoarseDirtDatabase database;
    private static boolean hasGenerated = false;
    private CaptureTheWool plugin;
    private FileConfiguration config;
    private boolean isEnoughPlayers = true;
    private BukkitTask task;
    private int playersNeeded;
    private Material helmet = Material.IRON_HELMET;
    private Material chestplate = Material.IRON_CHESTPLATE;
    private Material leggings = Material.IRON_LEGGINGS;
    private Material boots = Material.IRON_BOOTS;
    private Material sword = Material.IRON_SWORD;
    private Material shovel = Material.WOOD_SPADE;
    private Material bow = Material.BOW;
    private Material arrow = Material.ARROW;
    private Material chest = Material.CHEST;

    public PlayerJoinListener(CaptureTheWool plugin) {

        this.plugin = plugin;
        config = plugin.getConfig();
        database = new CoarseDirtDatabase();

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        if (GameState.getGameState() != GameState.PLAYING && GameState.getGameState() != GameState.FINISHED) {

            if (BungeeMode.getMode() == BungeeMode.OFF) {

                e.getPlayer().teleport(TextUtils.parseLocation(config.getString("lobby.spawnLocation")), PlayerTeleportEvent.TeleportCause.PLUGIN);
                e.getPlayer().getInventory().clear();

                if (config.getString("lobby.worldName").equalsIgnoreCase(e.getPlayer().getWorld().getName())) { //if is lobby world

                    if (config.getInt("players.limit") >= Bukkit.getOnlinePlayers().size()) { //if the player limit has not been reached yet

                        e.setJoinMessage(TextUtils.formatText("&a" + e.getPlayer().getDisplayName() + " &ahas joined! (" + Bukkit.getOnlinePlayers().size() + "/" + config.getInt("players.limit") + ")"));
                        e.getPlayer().teleport(TextUtils.parseLocation(config.getString("lobby.spawnLocation")), PlayerTeleportEvent.TeleportCause.PLUGIN);

                        if (config.getInt("players.toStart") <= Bukkit.getOnlinePlayers().size()) { //if enough players to start

                            if (config.getInt("lobby.countdown") > 0) { //if there is a countdown

                                //starts the game with countdown

                                if (GameState.getGameState() == GameState.LOBBY) {

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

                                                    GameManager.createTeams();
                                                    WoolStatus.setBlueStatus(WoolStatus.IN_SPAWN);
                                                    WoolStatus.setRedStatus(WoolStatus.IN_SPAWN);
                                                    GameManager.sendTeamsToSpawn(config.getStringList("spawnLocations.blueTeam"), config.getStringList("spawnLocations.redTeam"));

                                                } else {

                                                    new Title(TextUtils.formatText("&0"), TextUtils.formatText("&aGame starting in &a&l" + (config.getInt("lobby.countdown") - j)), 1, 1, 1).broadcast();
                                                    GameState.setGameState(GameState.STARTING);

                                                }

                                            }, i * 20);

                                        }

                                    });

                                }

                            } else {

                                //starts the game with no countdown
                                GameManager.createTeams();
                                WoolStatus.setBlueStatus(WoolStatus.IN_SPAWN);
                                WoolStatus.setRedStatus(WoolStatus.IN_SPAWN);
                                GameManager.sendTeamsToSpawn(config.getStringList("spawnLocations.blueTeam"), config.getStringList("spawnLocations.redTeam"));

                            }

                        }

                    } else {

                        //put player back to where they came from
                        e.getPlayer().kickPlayer(TextUtils.formatText("&cThis lobby is currently full!"));

                    }

                }

            } else { //server is in bungeemode

                if (e.getPlayer().hasPermission("ctw.bungeemode"))
                    Bukkit.broadcastMessage(TextUtils.formatText("&aServer running in &7BungeeMode&a, please change make your changes and set 'bungeemode' to off in the config when finished."));
                else
                    e.getPlayer().kickPlayer(TextUtils.formatText("&cThis mini-game is currently under maintenance."));

            }

        } else { //game is currently finishing or in progress

            e.getPlayer().kickPlayer(TextUtils.formatText("&cThe game is currently in progress!"));

        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        if (config.getString("lobby.worldName").equalsIgnoreCase(e.getPlayer().getWorld().getName())) {

            if (GameState.getGameState() != GameState.PLAYING)
                e.setQuitMessage(TextUtils.formatText("&c" + e.getPlayer().getDisplayName() + " &ahas left! (" + (Bukkit.getOnlinePlayers().size() - 1) + "/" + config.getInt("players.limit") + ")"));
            if (e.getPlayer().getScoreboard().getTeam("Blue") != null) {

                e.setQuitMessage(TextUtils.formatText("&9" + e.getPlayer().getDisplayName() + " &7has left!"));
                e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                GameManager.blueTeam.remove(e.getPlayer().getUniqueId());

            } else if (e.getPlayer().getScoreboard().getTeam("Red") != null) {

                e.setQuitMessage(TextUtils.formatText("&c" + e.getPlayer().getDisplayName() + " &7has left!"));
                e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                GameManager.redTeam.remove(e.getPlayer().getUniqueId());

            }

        }

    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {

        if (BungeeMode.getMode() == BungeeMode.OFF) {

            if (config.getString("lobby.worldName").equalsIgnoreCase(e.getFrom().getName())) {

                GameState.setGameState(GameState.PLAYING);

                if (!GameManager.woolSet) { //if wool has not been placed

                    GameManager.woolSet = true;
                    Location blueWoolLoc = TextUtils.parseLocation(config.getString("game.blueWoolLocation"));
                    Location redWoolLoc = TextUtils.parseLocation(config.getString("game.redWoolLocation"));

                    BlockState blueState = blueWoolLoc.getBlock().getState();
                    BlockState redState = redWoolLoc.getBlock().getState();

                    if (hasGenerated == false) {

                        GameScoreboard.setupGameScoreboard(plugin); //sets up scoreboard for all team players
                        GameManager.startGameCountdown(plugin); //start the game countdown in chat

                        blueState.setType(Material.WOOL);
                        redState.setType(Material.WOOL);
                        blueState.setData(new MaterialData(Material.WOOL, (byte) 11));
                        redState.setData(new MaterialData(Material.WOOL, (byte) 14));
                        blueState.update(true, false);
                        redState.update(true, false);

                        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

                            GameManager.saveCoarseBlocksAndReplace(plugin);
                            Bukkit.broadcastMessage(TextUtils.formatText("&aCoarse dirt has generated!"));

                        }, 0, config.getInt("time.dirtRegen") * 20);

                        ItemStack chestStack = new ItemStack(chest);
                        ItemMeta meta = chestStack.getItemMeta();
                        meta.setDisplayName(TextUtils.formatText("&e&lShop"));

                        List<String> lore = new ArrayList<>();
                        lore.add(TextUtils.formatText("&eBuy all of your shovels here!"));

                        meta.setLore(lore);
                        chestStack.setItemMeta(meta);

                        for (Player p : Bukkit.getOnlinePlayers()) {
                            ItemStack bowItemStack = ItemHandler.createUnbreakable(bow);
                            bowItemStack.addEnchantment(Enchantment.ARROW_INFINITE, 1);

                            p.getInventory().clear();
                            p.getInventory().setHelmet(ItemHandler.createUnbreakable(helmet));
                            p.getInventory().setChestplate(ItemHandler.createUnbreakable(chestplate));
                            p.getInventory().setLeggings(ItemHandler.createUnbreakable(leggings));
                            p.getInventory().setBoots(ItemHandler.createUnbreakable(boots));
                            p.getInventory().setItem(0, ItemHandler.createUnbreakable(sword));
                            p.getInventory().setItem(1, ItemHandler.createUnbreakable(shovel));
                            p.getInventory().setItem(2, bowItemStack);
                            p.getInventory().setItem(3, new ItemStack(arrow));
                            p.getInventory().setItem(8, chestStack);

                        }

                        hasGenerated = true;
                    }


                    e.getPlayer().setLevel(0);
                    e.getPlayer().setExp(0);

                }

            }

        }

    }

}
