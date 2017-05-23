package net.jmdev.gui;

import net.jmdev.CaptureTheWool;
import net.jmdev.core.ItemHandler;
import net.jmdev.util.TextUtils;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/14/2017 | 22:59
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
public class ShopGUI {

    public ShopGUI(CaptureTheWool plugin, Player p, Inventory inv) {

        FileConfiguration config = plugin.getConfig();
        org.bukkit.inventory.ItemStack stack;

        for (String s : config.getStringList("shopItems")) {

            stack = new org.bukkit.inventory.ItemStack(Material.valueOf(s.split(":")[0].toUpperCase()));

            if (!s.split(":")[1].equalsIgnoreCase("*"))
                stack = ItemHandler.addNBTIntTag(stack, "price", Integer.valueOf(s.split(":")[1]));

            ItemMeta meta = stack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(TextUtils.formatText("&ePrice: &6$" + Integer.valueOf(s.split(":")[1])));
            meta.setLore(lore);
            stack.setItemMeta(meta);

            inv.setItem(Integer.valueOf(s.split(":")[2]), stack);

        }

        p.openInventory(inv);


    }

}
