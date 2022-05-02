package me.rfmineguy.spigot_hammers.inventories;

import me.rfmineguy.spigot_hammers.SpigotTools;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public abstract class ToolModifierInventory {
    public static final NamespacedKey permanentItemKey = new NamespacedKey(SpigotTools.getPlugin(), "permanentItem");
    public static ItemStack blankItem;
    /*
        UpgradeItem:
            The upgrade type is stored in upgrade item's PDC as "upgradeType" (String)
            The upgrade level is stored in upgrade item's PDC as "upgradeLevel" (byte)
            dataContainer.set(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING, type);
            dataContainer.set(new NamespacedKey(SpigotTools.getPlugin(), "upgradeLevel"), PersistentDataType.BYTE, (byte)level);
     */
    public static class CustomInventory implements InventoryHolder {
        private final Inventory inventory;
        private final ItemStack itemStack;      //modify this when player presses confirm
        private final HashMap<ItemStack, Integer> itemToSlotMapping = new HashMap<>();
        private final HashMap<ItemStack, Integer> hintItemToSlotMapping = new HashMap<>();

        public CustomInventory(ItemStack itemStack) {
            this.inventory = Bukkit.createInventory(this, 18, "Item Modification (WIP)");
            this.inventory.setMaxStackSize(1);

            this.itemStack = itemStack;

            itemToSlotMapping.put(ItemManager.speedUpgradeItemLvl1, 0);
            itemToSlotMapping.put(ItemManager.speedUpgradeItemLvl2, 1);
            itemToSlotMapping.put(ItemManager.speedUpgradeItemLvl3, 2);

            itemToSlotMapping.put(ItemManager.fortuneUpgradeItemLvl1, 4);
            itemToSlotMapping.put(ItemManager.fortuneUpgradeItemLvl2, 5);

            itemToSlotMapping.put(ItemManager.silkTouchUpgradeItem, 7);

            initialize();
        }

        /*
            Color coding
             * Upgraded    - GREEN
             * Available   - YELLOW
             * Unavailable - RED
         */
        private void initialize() {

            ItemStack stack;
            stack = createHintItem("Speed Upgrade", Collections.singletonList("Speed Upgrade I"));
            hintItemToSlotMapping.put(stack, 0);
            stack = createHintItem("Speed Upgrade", Collections.singletonList("Speed Upgrade II"));
            hintItemToSlotMapping.put(stack, 1);
            stack = createHintItem("Speed Upgrade", Collections.singletonList("Speed Upgrade III"));
            hintItemToSlotMapping.put(stack, 2);

            stack = createHintItem("Fortune Upgrade", Collections.singletonList("Fortune Upgrade I"));
            hintItemToSlotMapping.put(stack, 4);
            stack = createHintItem("Fortune Upgrade", Collections.singletonList("Fortune Upgrade II"));
            hintItemToSlotMapping.put(stack, 5);

            stack = createHintItem("Silk Touch Upgrade", Collections.singletonList("Silk Touch Upgrade I"));
            hintItemToSlotMapping.put(stack, 7);

            blankItem = createBlankItem();

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, blankItem);
            }
            for (Map.Entry<ItemStack, Integer> entry : hintItemToSlotMapping.entrySet()) {
                inventory.setItem(entry.getValue(), entry.getKey());
            }

            // now fill in the upgrades that the player has
            PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
            byte speedLevel = dataContainer.getOrDefault(ItemManager.speedUpgradeLvl, PersistentDataType.BYTE, (byte)0); //zero means un-upgraded
            byte fortuneLevel = dataContainer.getOrDefault(ItemManager.fortuneUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            byte silkTouchLevel = dataContainer.getOrDefault(ItemManager.silkTouchUpgradeLvl, PersistentDataType.BYTE, (byte)0);

            // actually fill in the slots
            switch (speedLevel) {
                case 3:
                    inventory.setItem(2, ItemManager.speedUpgradeItemLvl3);
                case 2:
                    inventory.setItem(1, ItemManager.speedUpgradeItemLvl2);
                case 1:
                    inventory.setItem(0, ItemManager.speedUpgradeItemLvl1);
                    break;
                default:
            }
            switch (fortuneLevel) {
                case 2:
                    inventory.setItem(5, ItemManager.fortuneUpgradeItemLvl2);
                case 1:
                    inventory.setItem(4, ItemManager.fortuneUpgradeItemLvl1);
                    break;
                default:
            }
            switch (silkTouchLevel) {
                case 1:
                    inventory.setItem(7, ItemManager.silkTouchUpgradeItem);
                    break;
                default:
            }
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
        public int getSlotForItem(ItemStack itemStack) {
            if (itemStack == null)
                return -1;
            if (itemToSlotMapping.containsValue(itemStack)) {
                return -1;
            }
            return itemToSlotMapping.get(itemStack);
        }
        public ItemStack getItemForSlot(int slot) {
            for (Map.Entry<ItemStack, Integer> entry : hintItemToSlotMapping.entrySet()) {
                if (entry.getValue() == slot)
                    return entry.getKey();
            }
            return null;
        }
        public Inventory getInventory() {
            return inventory;
        }

        //used for making sure the tool in question isn't moved in the player's inventory
        public ItemStack getToolItemStack() {
            return itemStack;
        }
    }

    public static class InventoryListener implements Listener {
        @EventHandler
        public void InventoryInteraction(InventoryClickEvent event) {
            if (event.getClickedInventory() == null) {
                return;
            }
            Player player = (Player) event.getWhoClicked();
            /*
                make the interaction single click
                  - click on upgrade in player inventory to move it into the upgrade slot
                  - click on installed upgrade in tool inventory to move it to the player's slot
             */
            if (!(event.getView().getTopInventory().getHolder() instanceof CustomInventory)) {
                return;
            }

            InventoryHolder clickedInventory = event.getClickedInventory().getHolder();

            CustomInventory upgradeInventory = ((CustomInventory) event.getView().getTopInventory().getHolder());
            InventoryHolder holder = event.getView().getBottomInventory().getHolder();

            //Player Inventory => Upgrade Slots
            if (clickedInventory instanceof Player) {
                ItemStack upgradeItemStack = event.getCurrentItem();
                if (ItemManager.isUpgrade(upgradeItemStack)) {
                    int slot = upgradeInventory.getSlotForItem(upgradeItemStack);                 //the item that the clicked upgrade fits into

                    //ensure the player doesnt try to put an existing upgrade in
                    if (upgradeInventory.getInventory().getItem(slot).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING)) {
                        event.setCancelled(true);
                        return;
                    }

                    //cancel if the desired upgrade is already installed
                    if (upgradeInventory.getInventory().getItem(slot).isSimilar(upgradeItemStack)) {
                        event.setCancelled(true);
                        return;
                    }

                    upgradeInventory.getInventory().setItem(slot, upgradeItemStack);

                    ItemStack currentSlot = player.getInventory().getItem(event.getSlot());
                    player.getInventory().getItem(event.getSlot()).setAmount(currentSlot.getAmount() - 1);
                }
            }
            //Upgrade Slots => Player Inventory
            else if (holder instanceof Player && clickedInventory instanceof CustomInventory) {
                ItemStack upgradeItemStack = event.getCurrentItem();
                if (ItemManager.isUpgrade(upgradeItemStack)) {
                    HashMap<Integer, ItemStack> notAdded = holder.getInventory().addItem(upgradeItemStack);
                    event.setCancelled(true);
                    ItemStack itemStack = ((CustomInventory) clickedInventory).getItemForSlot(event.getSlot());
                    clickedInventory.getInventory().setItem(event.getSlot(), itemStack);

                    SpigotTools.LOGGER.info("Clicked on upgrade item in modifier inventory");
                }
                PersistentDataContainer dataContainer = upgradeItemStack.getItemMeta().getPersistentDataContainer();
                if (dataContainer.getOrDefault(ToolModifierInventory.permanentItemKey, PersistentDataType.BYTE, (byte)0) == 1) {
                    event.setCancelled(true);
                }
            }
        }

        @EventHandler
        public void InventoryExit(InventoryCloseEvent event) {
            SpigotTools.LOGGER.info(event.getView().getTopInventory().getHolder() + "");
            if (event.getView().getTopInventory().getHolder() instanceof CustomInventory) {
                Player player = (Player) event.getPlayer();
                CustomInventory inventory = (CustomInventory) event.getView().getTopInventory().getHolder();
                int speedLevel = 0;
                int fortuneLevel = 0;
                int silkTouchLevel = 0;
                for (int i = 0; i < 9; i ++) {
                    PersistentDataContainer dataContainer = inventory.getInventory().getItem(i).getItemMeta().getPersistentDataContainer();
                    String upgradeType = dataContainer.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING, "N/A");
                    int upgradeLevel = dataContainer.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeLevel"), PersistentDataType.BYTE, (byte)-1);
                    SpigotTools.LOGGER.info("" + upgradeLevel);
                    switch (upgradeType) {
                        case "speed": speedLevel = upgradeLevel;
                        break;
                        case "fortune": fortuneLevel = upgradeLevel;
                        break;
                        case "silk": silkTouchLevel = upgradeLevel;
                        break;
                    }
                }
                player.sendMessage(ChatColor.GREEN + "Saved modifiers to your tool");
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                ItemMeta meta = itemInHand.getItemMeta();
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                dataContainer.set(ItemManager.speedUpgradeLvl, PersistentDataType.BYTE, (byte) speedLevel);
                dataContainer.set(ItemManager.fortuneUpgradeLvl, PersistentDataType.BYTE, (byte) fortuneLevel);
                dataContainer.set(ItemManager.silkTouchUpgradeLvl, PersistentDataType.BYTE, (byte) silkTouchLevel);
                itemInHand.setItemMeta(meta);
                ItemManager.updateItemStackLore(itemInHand);

                enchantItemStack(itemInHand, speedLevel, fortuneLevel, silkTouchLevel);
            }
        }

        /*
            speedLevel -> efficiencyEnchantLevel
            [0, 1, 2, 3] -> [0, 1, 2, 4]

            fortuneLevel -> fortuneEnchantLevel
            [0, 1, 2] -> [0, 1, 3]

            silkLevel -> silkEnchantLevel
            [0, 1] -> [0, 1]
         */
        private void enchantItemStack(ItemStack itemStack, int speedLevel, int fortuneLevel, int silkLevel) {
            itemStack.removeEnchantment(Enchantment.DIG_SPEED);
            itemStack.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
            itemStack.removeEnchantment(Enchantment.SILK_TOUCH);
            int efficiencyEnchantLevel = 0;
            int fortuneEnchantLevel = 0;
            int silkEnchantLevel = 0;
            switch (speedLevel) {
                case 0:break;
                case 1:efficiencyEnchantLevel = 1; break;
                case 2:efficiencyEnchantLevel = 2; break;
                case 3:efficiencyEnchantLevel = 4; break;
            }
            switch (fortuneLevel) {
                case 0:break;
                case 1:fortuneEnchantLevel = 1; break;
                case 2:fortuneEnchantLevel = 3; break;
            }
            switch (silkLevel) {
                case 0: break;
                case 1:silkEnchantLevel = 1; break;
            }
            if (efficiencyEnchantLevel != 0)
                itemStack.addEnchantment(Enchantment.DIG_SPEED, efficiencyEnchantLevel);
            if (fortuneEnchantLevel != 0)
                itemStack.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, fortuneEnchantLevel);
            if (silkEnchantLevel != 0) {
                itemStack.addEnchantment(Enchantment.SILK_TOUCH, silkEnchantLevel);
            }
        }
    }
}
