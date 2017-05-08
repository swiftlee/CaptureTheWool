package net.jmdev.util;

import net.jmdev.CaptureTheWool;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/07/2017 | 23:18
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
public class Constants {

    private static YamlConfiguration config;
    //WORLD INFO
    public final String gameWorldFolderPath = config.getString("gameWorldFolderPath");
    public final String redWoolLocation = config.getString("redWoolLocation");
    public final String blueWoolLocation = config.getString("blueWoolLocation");

    //LOBBY
    public final String lobbyWorldFolderPath = config.getString("lobbyWorldFolderPath");
    public final int lobbyCountDown = config.getInt("lobbyCountDown");

    //TEAMS
    public final List<String> redSpawnLocations = config.getStringList("redSpawnLocations");
    public final List<String> blueSpawnLocations = config.getStringList("blueSpawnLocations");

    //PLAYER REQUIREMENTS
    public final int playersToStart = config.getInt("playersToStart");
    public final int playerLimit = config.getInt("playerLimit");

    //COINS
    public final int coinsPerKill = config.getInt("coinsPerKill");
    public final int coinsPerCoarseDirt = config.getInt("coinsPerCoarseDirt");

    //CHANCES
    public final int coinsPercentCoarseDirt = config.getInt("coinsPercentCoarseDirt");

    //PRICES
    public final List<String> shopItems = config.getStringList("shopItems");

    //GAME RULES
    public final int woolLimit = config.getInt("woolLimit");
    public final int deathFreezeTime = config.getInt("deathFreezeTime");

    //MESSAGES
    public final String woolTaken = config.getString("woolTaken");
    public final String woolDropped = config.getString("woolDropped");
    public final String woolCaptured = config.getString("woolCaptured");

    //SCOREBOARD
    public List<String> scoreboard = config.getStringList("scoreboard");

    private CaptureTheWool plugin;

    public Constants(CaptureTheWool plugin) {

        this.plugin = plugin;
        this.config = (YamlConfiguration) plugin.getConfig();

    }

}
