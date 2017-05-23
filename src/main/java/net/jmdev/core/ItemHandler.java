package net.jmdev.core;

import net.minecraft.server.v1_8_R2.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/13/2017 | 21:07
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
public class ItemHandler {

    public static ItemStack createUnbreakable(Material material) {

        net.minecraft.server.v1_8_R2.ItemStack itemStack = CraftItemStack.asNMSCopy(new ItemStack(material));

        NBTTagCompound tag = itemStack.hasTag() ? itemStack.getTag() : new NBTTagCompound();

        tag.setInt("Unbreakable", 1);

        itemStack.setTag(tag);

        return CraftItemStack.asBukkitCopy(itemStack);

    }

    public static ItemStack addNBTStringTag(ItemStack stack, String dataName, String value) {

        net.minecraft.server.v1_8_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        tag.setString(dataName, value);

        nmsStack.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static ItemStack addNBTIntTag(ItemStack stack, String dataName, int value) {

        net.minecraft.server.v1_8_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        tag.setInt(dataName, value);

        nmsStack.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

}
