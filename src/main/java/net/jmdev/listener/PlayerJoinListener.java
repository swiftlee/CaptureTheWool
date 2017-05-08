package net.jmdev.listener;

import net.jmdev.CaptureTheWool;
import net.jmdev.core.GameManager;
import net.jmdev.util.Constants;
import net.jmdev.util.TextUtils;
import net.jmdev.util.Title;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
    private Constants constants = new Constants(plugin);

    public PlayerJoinListener(CaptureTheWool plugin) {

        this.plugin = plugin;

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        if (constants.playersToStart >= Bukkit.getOnlinePlayers().size()) {

            if (constants.lobbyCountDown > 0) {

                GameManager.start(() -> {

                    for (int i = 0; i < constants.lobbyCountDown; i++) {

                        final int j = i;

                        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {

                            for (Player p : Bukkit.getOnlinePlayers())
                                new Title(TextUtils.formatText("&0"), TextUtils.formatText("&aGame starting in: " + (constants.lobbyCountDown - j)), 0, 1, 0).send(p);

                        }, i * 20);

                    }

                });

            } else {

                //just start the game already!

            }

        }

    }

}
