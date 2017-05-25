package net.jmdev;

import net.jmdev.command.CommandCTW;
import net.jmdev.core.BungeeMode;
import net.jmdev.core.GameState;
import net.jmdev.database.CoarseDirtDatabase;
import net.jmdev.listener.FoodLevelChangeListener;
import net.jmdev.listener.InventoryClickListener;
import net.jmdev.listener.PlayerDamageListener;
import net.jmdev.listener.PlayerInteractListener;
import net.jmdev.listener.PlayerJoinListener;
import net.jmdev.listener.PlayerMoveListener;
import net.jmdev.util.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
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

    @Override
    public void onEnable() {

        File f = new File("plugins/CaptureTheWool/config.yml");

        if (!f.exists()) {

            saveDefaultConfig();

        } else {

            reloadConfig();

        }

        CoarseDirtDatabase database = new CoarseDirtDatabase();
        database.load();

        GameState.setGameState(GameState.LOBBY);
        BungeeMode.setMode(getConfig().getBoolean("bungeeMode") ? BungeeMode.ON : BungeeMode.OFF);

        if (Bukkit.getWorld(getConfig().getString("game.worldName")) == null)
            Bukkit.createWorld(new WorldCreator(getConfig().getString("game.worldName")));

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this, getConfig()), this), 20 * 5);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        getServer().getPluginCommand("ctw").setExecutor(new CommandCTW(this));

        if (BungeeMode.getMode() == BungeeMode.ON)
            System.out.print(TextUtils.formatText("BungeeMode is ON. Please restart your server and set BungeeMode to 'false' in the config when you are finished making changes."));

    }

}
