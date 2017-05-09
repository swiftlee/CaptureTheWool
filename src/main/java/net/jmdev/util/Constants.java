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
    public final String gameWorldFolderPath;
    public final String redWoolLocation;
    public final String blueWoolLocation;

    //LOBBY
    public final String lobbyWorldFolderPath;
    public final int lobbyCountDown;

    //TEAMS
    public final List<String> redSpawnLocations;
    public final List<String> blueSpawnLocations;

    //PLAYER REQUIREMENTS
    public final int playersToStart;
    public final int playerLimit;

    //COINS
    public final int coinsPerKill;
    public final int coinsPerCoarseDirt;

    //CHANCES
    public final int coinsPercentCoarseDirt;

    //PRICES
    public final List<String> shopItems;

    //GAME RULES
    public final int woolLimit;
    public final int deathFreezeTime;

    //MESSAGES
    public final String woolTaken;
    public final String woolDropped;
    public final String woolCaptured;

    //SCOREBOARD
    public List<String> scoreboard;

    private CaptureTheWool plugin;

    public Constants(CaptureTheWool plugin) {

        this.plugin = plugin;
        this.config = (YamlConfiguration) plugin.getConfig();

        //WORLD INFO
        gameWorldFolderPath = config.getString("gameWorldFolderPath");
        redWoolLocation = config.getString("redWoolLocation");
        blueWoolLocation = config.getString("blueWoolLocation");

        //LOBBY
        lobbyWorldFolderPath = config.getString("lobbyWorldFolderPath");
        lobbyCountDown = config.getInt("lobbyCountDown");

        //TEAMS
        redSpawnLocations = config.getStringList("redSpawnLocations");
        blueSpawnLocations = config.getStringList("blueSpawnLocations");

        //PLAYER REQUIREMENTS
        playersToStart = config.getInt("playersToStart");
        playerLimit = config.getInt("playerLimit");

        //COINS
        coinsPerKill = config.getInt("coinsPerKill");
        coinsPerCoarseDirt = config.getInt("coinsPerCoarseDirt");

        //CHANCES
        coinsPercentCoarseDirt = config.getInt("coinsPercentCoarseDirt");

        //PRICES
        shopItems = config.getStringList("shopItems");

        //GAME RULES
        woolLimit = config.getInt("woolLimit");
        deathFreezeTime = config.getInt("deathFreezeTime");

        //MESSAGES
        woolTaken = config.getString("woolTaken");
        woolDropped = config.getString("woolDropped");
        woolCaptured = config.getString("woolCaptured");

    }

}
