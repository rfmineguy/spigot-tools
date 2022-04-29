package me.rfmineguy.spigot_hammers.inventories;

import me.rfmineguy.spigot_hammers.SpigotTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ToolModifierInventory {
    public static final NamespacedKey permanentItemKey = new NamespacedKey(SpigotTools.getPlugin(), "permanentItem");
    public static final NamespacedKey menuItemType = new NamespacedKey(SpigotTools.getPlugin(), "itemType"); //0 = discard, 1 = confirm

    public static class CustomInventory implements InventoryHolder {
        private final Inventory inventory;

        public CustomInventory() {
            inventory = Bukkit.createInventory(this, 18, "Item Modification (WIP)");
            inventory.setMaxStackSize(1);
            initialize();
        }

        private void initialize() {
            ItemStack speedUpgradeLvl1 = createHintItem("Speed Upgrade", Collections.singletonList("WIP Speed Upgrade I"));
            ItemStack speedUpgradeLvl2 = createHintItem("Speed Upgrade", Collections.singletonList("WIP Speed Upgrade II"));
            ItemStack speedUpgradeLvl3 = createHintItem("Speed Upgrade", Collections.singletonList("WIP Speed Upgrade III"));

            ItemStack fortuneUpgradeLvl1 = createHintItem("Fortune Upgrade", Collections.singletonList("WIP Fortune Upgrade I"));
            ItemStack fortuneUpgradeLvl2 = createHintItem("Fortune Upgrade", Collections.singletonList("WIP Fortune Upgrade II"));

            ItemStack silkTouchUpgrade = createHintItem("Silk Touch Upgrade", Collections.singletonList("WIP Silk Touch Upgrade I"));

            ItemStack blankItem = createBlankItem();
            ItemStack discardItem = createDiscardChangesItem();
            ItemStack saveItem = createSaveChangesItem();

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, blankItem);
            }
            inventory.setItem(0, speedUpgradeLvl1);
            inventory.setItem(1, speedUpgradeLvl2);
            inventory.setItem(2, speedUpgradeLvl3);
            inventory.setItem(4, fortuneUpgradeLvl1);
            inventory.setItem(5, fortuneUpgradeLvl2);
            inventory.setItem(7, silkTouchUpgrade);
            inventory.setItem(9, discardItem);
            inventory.setItem(17, saveItem);
        }

        private ItemStack createHintItem(String name, List<String> lore) {
            ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(ToolModifierInventory.permanentItemKey, PersistentDataType.BYTE, (byte)1);
            itemStack.setItemMeta(meta);
            return itemStack;
        }

        private ItemStack createBlankItem() {
            ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(" ");
            meta.getPersistentDataContainer().set(ToolModifierInventory.permanentItemKey, PersistentDataType.BYTE, (byte)1);
            itemStack.setItemMeta(meta);
            return itemStack;
        }

        private ItemStack createDiscardChangesItem() {
            ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_RED + "Discard");
            meta.getPersistentDataContainer().set(ToolModifierInventory.permanentItemKey, PersistentDataType.BYTE, (byte)1);
            meta.getPersistentDataContainer().set(ToolModifierInventory.menuItemType, PersistentDataType.BYTE, (byte)0);
            List<String> lore = new ArrayList<>();
            lore.add("Discard your current changes (WIP)");
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            return itemStack;
        }

        private ItemStack createSaveChangesItem() {
            ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GREEN + "Save");
            meta.getPersistentDataContainer().set(ToolModifierInventory.permanentItemKey, PersistentDataType.BYTE, (byte)1);
            meta.getPersistentDataContainer().set(ToolModifierInventory.menuItemType, PersistentDataType.BYTE, (byte)1);
            List<String> lore = new ArrayList<>();
            lore.add("Save your current changes (WIP)");
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            return itemStack;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }

    public static class InventoryListener implements Listener {
        @EventHandler
        public void InventoryInteraction(InventoryClickEvent event) {
            SpigotTools.LOGGER.info("Inventory clicked");
            if (event.getClickedInventory() == null) {
                return;
            }
            SpigotTools.LOGGER.info("Inventory not null");
            if (event.getClickedInventory().getHolder() instanceof CustomInventory) {
                SpigotTools.LOGGER.info("Inventory is correct");
                ItemStack currentItem = event.getCurrentItem();
                if (currentItem != null) {
                    ItemMeta meta = currentItem.getItemMeta();
                    PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                    byte menuItemType = dataContainer.getOrDefault(ToolModifierInventory.menuItemType, PersistentDataType.BYTE, (byte)-1);
                    switch (menuItemType){
                        case 0: {
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().sendMessage(ChatColor.RED + "Did not save changes to tool.");
                            break;
                        }
                        case 1: {
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().sendMessage(ChatColor.GREEN + "Saved modifiers to your tool.");
                            break;
                        }
                    }
                    byte isPermanentItem = dataContainer.getOrDefault(ToolModifierInventory.permanentItemKey, PersistentDataType.BYTE, (byte) 0);
                    if (isPermanentItem == 1) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
