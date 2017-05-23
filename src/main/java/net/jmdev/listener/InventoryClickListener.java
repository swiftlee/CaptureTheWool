package net.jmdev.listener;

import net.jmdev.CaptureTheWool;
import net.jmdev.util.TextUtils;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/15/2017 | 16:47
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
public class InventoryClickListener implements Listener {

    private CaptureTheWool plugin;

    public InventoryClickListener(CaptureTheWool plugin) {

        this.plugin = plugin;

    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {

        if (e.getClickedInventory() == null || e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;

        Player p = (Player) e.getWhoClicked();

        if (e.getInventory().getName().equalsIgnoreCase(TextUtils.formatText("&6&lShop"))) {

            ItemStack bukkitStack = e.getCurrentItem();
            net.minecraft.server.v1_8_R2.ItemStack stack = CraftItemStack.asNMSCopy(bukkitStack);

            if (stack.hasTag()) {

                if (stack.getTag().get("price") != null) {

                    if ((int) Math.floor(p.getExp()) >= stack.getTag().getInt("price")) {

                        p.setExp(p.getExp() - stack.getTag().getInt("price"));

                        p.closeInventory();
                        p.getInventory().addItem(new ItemStack(bukkitStack.getType()));
                        p.sendMessage(TextUtils.formatText("&aYour coin balance is now: " + (int) p.getExp()));

                    } else {

                        p.sendMessage(TextUtils.formatText("&cYou do not have enough coins to purchase that item!"));

                    }

                }

            } else {

                p.closeInventory();

            }

        }

        e.setCancelled(true);

    }

}
