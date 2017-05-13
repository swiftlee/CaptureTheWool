package net.jmdev;

import net.jmdev.core.BungeeMode;
import net.jmdev.database.CoinsDatabase;
import net.jmdev.listener.PlayerJoinListener;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/06/2017 | 18:22
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
public class CaptureTheWool extends JavaPlugin {

    public static CoinsDatabase coinsDatabase;

    @Override
    public void onEnable() {

        File f = new File("plugins/CaptureTheWool/config.yml");

        if (!f.exists()) {
            saveDefaultConfig();
            reloadConfig();
        } else {
            reloadConfig();
        }

        coinsDatabase = new CoinsDatabase();
        coinsDatabase.reload();

        BungeeMode.setMode(getConfig().getBoolean("bungeemode") ? BungeeMode.ON : BungeeMode.OFF);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

    }

    @Override
    public void onDisable() {


    }

}
