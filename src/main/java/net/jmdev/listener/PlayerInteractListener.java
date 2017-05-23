package net.jmdev.listener;

import net.jmdev.CaptureTheWool;
import net.jmdev.core.BungeeMode;
import net.jmdev.gui.ShopGUI;
import net.jmdev.util.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/15/2017 | 19:49
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
public class PlayerInteractListener implements Listener {

    private CaptureTheWool plugin;
    private Inventory inventory = Bukkit.createInventory(null, 9, TextUtils.formatText("&6&lShop"));

    public PlayerInteractListener(CaptureTheWool plugin) {

        this.plugin = plugin;

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (e.getItem() != null && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(TextUtils.formatText("&e&lShop"))) {

                new ShopGUI(plugin, e.getPlayer(), inventory);

            }

        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        if (BungeeMode.getMode() == BungeeMode.OFF)
        e.setCancelled(true);

    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {

        if (BungeeMode.getMode() == BungeeMode.OFF)
        e.setCancelled(true);

    }

}
