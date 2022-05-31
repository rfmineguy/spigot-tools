package me.rfmineguy.spigot_hammers.inventories;

import me.rfmineguy.spigot_hammers.SpigotTools;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public abstract class ToolModifierInventoryV2 {

    private static class ToolModificationUtil {
        private enum FuelType {
            EFFICIENCY, FORTUNE, SILK_TOUCH
        }
        public static NamespacedKey permanent = new NamespacedKey(SpigotTools.getPlugin(), "permanent");
        public static final ItemStack BLANK_ITEM = createBlank();

        public static ItemStack[] slotToUpgradeMapping;// = new ItemStack[];
        public static ItemStack[] slotToHintMapping;

        // executes when the class is loaded
        static {
            slotToUpgradeMapping = new ItemStack[18];// {BLANK_ITEM};
            for (int i = 0; i < 18; i++) {
                slotToUpgradeMapping[i] = BLANK_ITEM;
            }
            slotToUpgradeMapping[0] = ItemManager.speedUpgradeItemLvl1;
            slotToUpgradeMapping[1] = ItemManager.speedUpgradeItemLvl2;
            slotToUpgradeMapping[2] = ItemManager.speedUpgradeItemLvl3;
            slotToUpgradeMapping[4] = ItemManager.fortuneUpgradeItemLvl1;
            slotToUpgradeMapping[5] = ItemManager.fortuneUpgradeItemLvl2;
            slotToUpgradeMapping[7] = ItemManager.silkTouchUpgradeItem;

            slotToHintMapping = new ItemStack[18];
            for (int i = 0; i < 18; i++) {
                slotToHintMapping[i] = BLANK_ITEM;
            }
            slotToHintMapping[0] = createHint("Speed Upgrade", Collections.singletonList("Speed Upgrade I"));
            slotToHintMapping[1] = createHint("Speed Upgrade", Collections.singletonList("Speed Upgrade II"));
            slotToHintMapping[2] = createHint("Speed Upgrade", Collections.singletonList("Speed Upgrade III"));
            slotToHintMapping[4] = createHint("Fortune Upgrade", Collections.singletonList("Fortune Upgrade I"));
            slotToHintMapping[5] = createHint("Fortune Upgrade", Collections.singletonList("Fortune Upgrade II"));
            slotToHintMapping[7] = createHint("Silk Touch Upgrade", Collections.singletonList("Silk Touch Upgrade I"));
        }

        /**
         * <p>This function determines whether an item can be removed from the given slot. For instance if you tried to remove the level 1 upgrade while having a level 3 upgrade installed, this function returns false</p>
         * @param tool The tool currently being used (hammer or excavator)
         * @param upgradeStack The itemStack for the upgrade trying to be extracted
         * @return Whether the [upgradeStack] is able to be removed from the inventory (depends on the specific inventory supplied by ToolModifierInventoryV2.Holder
         *
         * @see ToolModifierInventoryV2.Holder
         */
        public static boolean canUpgradeBeExtracted(ItemStack tool, ItemStack upgradeStack) {
            if (tool == null || upgradeStack == null)
                return false;
            ItemMeta upgradeStackMeta = upgradeStack.getItemMeta();
            ItemMeta toolStackMeta = tool.getItemMeta();
            assert upgradeStackMeta != null && toolStackMeta != null;

            PersistentDataContainer upgradeContainer = upgradeStackMeta.getPersistentDataContainer();
            PersistentDataContainer toolContainer = toolStackMeta.getPersistentDataContainer();

            String upgradeType = upgradeContainer.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING, "");
            byte upgradeItemLevel = upgradeContainer.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeLevel"), PersistentDataType.BYTE, (byte) 0);

            int toolCurrentUpgradeLevel;
            switch (upgradeType) {
                case "speed": toolCurrentUpgradeLevel = toolContainer.getOrDefault(ItemManager.speedUpgradeLvl, PersistentDataType.BYTE, (byte)0);
                break;
                case "fortune": toolCurrentUpgradeLevel = toolContainer.getOrDefault(ItemManager.fortuneUpgradeLvl, PersistentDataType.BYTE, (byte)0);
                break;
                case "silk": toolCurrentUpgradeLevel = toolContainer.getOrDefault(ItemManager.silkTouchUpgradeLvl, PersistentDataType.BYTE, (byte)0);
                break;
                default: toolCurrentUpgradeLevel = -1;
            }
            return upgradeItemLevel == toolCurrentUpgradeLevel;      //if the level of the upgrade item in question is equal to the current tool's level then it can be removed
        }

        /**
         * <p>This function determines whether the slot specified is the correct slot for the supplied upgrade item</p>
         * @param tool The tool currently being used (hammer or excavator)
         * @param upgradeStack The itemStack for the upgrade trying to be inserted
         * @return Whether the slot specified is suitable for the upgrade itemStack supplied
         *
         * @see ToolModifierInventoryV2.Holder
         */
        public static boolean canUpgradeBeInserted(ItemStack tool, ItemStack upgradeStack) {
            if (tool == null || !ItemManager.UpgradeItem.isUpgrade(upgradeStack))
                return false;
            ItemMeta upgradeStackMeta = upgradeStack.getItemMeta();
            ItemMeta toolStackMeta = tool.getItemMeta();
            assert upgradeStackMeta != null && toolStackMeta != null;

            PersistentDataContainer upgradeContainer = upgradeStackMeta.getPersistentDataContainer();
            PersistentDataContainer toolContainer = toolStackMeta.getPersistentDataContainer();

            String upgradeType = upgradeContainer.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING, "");
            byte upgradeItemLevel = upgradeContainer.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeLevel"), PersistentDataType.BYTE, (byte) 0);

            int toolCurrentUpgradeLevel;
            switch (upgradeType) {
                case "speed": toolCurrentUpgradeLevel = toolContainer.getOrDefault(ItemManager.speedUpgradeLvl, PersistentDataType.BYTE, (byte)0);
                    break;
                case "fortune": toolCurrentUpgradeLevel = toolContainer.getOrDefault(ItemManager.fortuneUpgradeLvl, PersistentDataType.BYTE, (byte)0);
                    break;
                case "silk": toolCurrentUpgradeLevel = toolContainer.getOrDefault(ItemManager.silkTouchUpgradeLvl, PersistentDataType.BYTE, (byte)0);
                    break;
                default: toolCurrentUpgradeLevel = -1;
            }
            return upgradeItemLevel == toolCurrentUpgradeLevel + 1;
        }

        public static boolean canFuelBeInserted(ItemStack tool, ItemStack item) {
            if (tool == null || item == null || !isFuel(item))
                return false;
            ItemMeta fuelStackMeta = item.getItemMeta();
            ItemMeta toolStackMeta = tool.getItemMeta();
            assert fuelStackMeta != null && toolStackMeta != null;
            PersistentDataContainer fuelContainer = fuelStackMeta.getPersistentDataContainer();
            PersistentDataContainer toolContainer = toolStackMeta.getPersistentDataContainer();

            switch (item.getType()) {
                case REDSTONE: {
                    short fuelCount = toolContainer.getOrDefault(ItemManager.ToolItem.efficiencyFuelKey, PersistentDataType.SHORT, (short)0);
                    short maxFuelCount = toolContainer.getOrDefault(ItemManager.ToolItem.maxEfficiencyFuelKey, PersistentDataType.SHORT, (short)0);
                    if (fuelCount + 1 < maxFuelCount)
                        return true;
                    break;
                }
                case LAPIS_LAZULI: {
                    short fuelCount = toolContainer.getOrDefault(ItemManager.ToolItem.fortuneFuelKey, PersistentDataType.SHORT, (short)0);
                    short maxFuelCount = toolContainer.getOrDefault(ItemManager.ToolItem.maxFortuneFuelKey, PersistentDataType.SHORT, (short)0);
                    if (fuelCount + 1 < maxFuelCount)
                        return true;
                    break;
                }
                case SLIME_BALL: {
                    short fuelCount = toolContainer.getOrDefault(ItemManager.ToolItem.silkTouchFuelKey, PersistentDataType.SHORT, (short)0);
                    short maxFuelCount = toolContainer.getOrDefault(ItemManager.ToolItem.maxSilkTouchFuelKey, PersistentDataType.SHORT, (short)0);
                    if (fuelCount + 1 < maxFuelCount)
                        return true;
                    break;
                }
            }
            return false;
        }
        public static ItemStack hintForSlot(int slot) {
            return slotToHintMapping[slot];
        }

        // isSimilar wont work because we add random nbt to each one. we need an ItemManager method
        public static int slotForUpgradeItem(ItemStack itemStack) {
            for (int i = 0; i < 18; i++) {
                if (ItemManager.UpgradeItem.isEqual(slotToUpgradeMapping[i], itemStack)) {
                    return i;
                }
            }
            return -1;
        }
        private static ItemStack createBlank() {
            ItemStack blank = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = blank.getItemMeta();
            assert meta != null;
            meta.setDisplayName(" ");
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(permanent, PersistentDataType.BYTE, (byte)1);
            blank.setItemMeta(meta);
            return blank;
        }
        private static ItemStack createHint(String name, List<String> lore) {
            ItemStack hint = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta meta = hint.getItemMeta();
            assert meta != null;
            meta.setDisplayName(name);
            meta.setLore(lore);
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(permanent, PersistentDataType.BYTE, (byte)1);
            hint.setItemMeta(meta);
            return hint;
        }
        private static ItemStack createFuelIndicatorItem(ItemStack tool, String name, Material material, FuelType fuelType) {
            ItemStack itemStack = new ItemStack(material);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(name);

            PersistentDataContainer container = tool.getItemMeta().getPersistentDataContainer();

            List<String> lore = new ArrayList<>();
            switch (fuelType) {
                case EFFICIENCY: {
                    int fuelCount = container.getOrDefault(ItemManager.ToolItem.efficiencyFuelKey, PersistentDataType.SHORT, (short)0);
                    int fuelMax = container.getOrDefault(ItemManager.ToolItem.maxEfficiencyFuelKey, PersistentDataType.SHORT, (short)255);
                    lore.add("" + fuelCount + "/" + fuelMax);
                    break;
                }
                case FORTUNE: {
                    int fuelCount = container.getOrDefault(ItemManager.ToolItem.fortuneFuelKey, PersistentDataType.SHORT, (short)0);
                    int fuelMax = container.getOrDefault(ItemManager.ToolItem.maxFortuneFuelKey, PersistentDataType.SHORT, (short)255);
                    lore.add("" + fuelCount + "/" + fuelMax);
                    break;
                }
                case SILK_TOUCH: {
                    int fuelCount = container.getOrDefault(ItemManager.ToolItem.silkTouchFuelKey, PersistentDataType.SHORT, (short)0);
                    int fuelMax = container.getOrDefault(ItemManager.ToolItem.maxSilkTouchFuelKey, PersistentDataType.SHORT, (short)255);
                    lore.add("" + fuelCount + "/" + fuelMax);
                    break;
                }
            }
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            return itemStack;
        }
        private static boolean isFuel(ItemStack itemStack) {
            Material type = itemStack.getType();
            return type == Material.REDSTONE || type == Material.LAPIS_LAZULI || type == Material.SLIME_BALL;
        }
    }

    public static class Holder implements InventoryHolder {
        Inventory inventory;

        public Holder(ItemStack heldItem) {
            inventory = Bukkit.createInventory(this, 18, "Tool Modification");

            initialize(heldItem);
        }

        private void initialize(ItemStack itemStack) {
            //fill the inventory with the blank item
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, ToolModificationUtil.BLANK_ITEM);
            }

            //selectively fill in the hint items
            for (int i = 0; i < inventory.getSize(); i++) {
                if (!ToolModificationUtil.slotToHintMapping[i].isSimilar(ToolModificationUtil.BLANK_ITEM))
                    inventory.setItem(i, ToolModificationUtil.slotToHintMapping[i]);
            }

            inventory.setItem(9, ToolModificationUtil.createFuelIndicatorItem(itemStack, "Efficiency Fuel", Material.REDSTONE, ToolModificationUtil.FuelType.EFFICIENCY));
            inventory.setItem(13, ToolModificationUtil.createFuelIndicatorItem(itemStack, "Fortune Fuel", Material.LAPIS_LAZULI, ToolModificationUtil.FuelType.FORTUNE));
            inventory.setItem(16, ToolModificationUtil.createFuelIndicatorItem(itemStack, "Silk Touch Fuel", Material.SLIME_BALL, ToolModificationUtil.FuelType.SILK_TOUCH));

            //fill in the upgrades that are currently installed on the tool
            PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
            byte speedLevel = container.getOrDefault(ItemManager.speedUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            byte fortuneLevel = container.getOrDefault(ItemManager.fortuneUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            byte silkLevel = container.getOrDefault(ItemManager.silkTouchUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            switch (speedLevel) {
                case 3: inventory.setItem(2, ItemManager.speedUpgradeItemLvl3);
                case 2: inventory.setItem(1, ItemManager.speedUpgradeItemLvl2);
                case 1: inventory.setItem(0, ItemManager.speedUpgradeItemLvl1);
                case 0:
                break;
            }
            switch (fortuneLevel) {
                case 2: inventory.setItem(5, ItemManager.fortuneUpgradeItemLvl2);
                case 1: inventory.setItem(4, ItemManager.fortuneUpgradeItemLvl1);
                case 0:
                break;
            }
            switch (silkLevel) {
                case 1: inventory.setItem(7, ItemManager.silkTouchUpgradeItem);
                case 0:
                break;
            }
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }

    /*
        Deals with the main logic of the tool modification system
     */
    public static class InvListener implements Listener {
        @EventHandler
        public void interaction(InventoryClickEvent event) {
            if (event.getClickedInventory() == null) {
                return;
            }
            if (!(event.getView().getTopInventory().getHolder() instanceof ToolModifierInventoryV2.Holder)) {
                return;
            }

            // only continue if the top inventory is indeed an instance of our ToolModifierInventory
            Player player = (Player) event.getWhoClicked();
            ToolModifierInventoryV2.Holder toolModifierInventory = (Holder) event.getView().getTopInventory().getHolder();
            InventoryHolder playerInventory = event.getView().getBottomInventory().getHolder();
            InventoryHolder clickedHolder = event.getClickedInventory().getHolder();

            ItemStack toolInMainHand = player.getInventory().getItemInMainHand();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedHolder instanceof ToolModifierInventoryV2.Holder) {
                // move items OUT of the tool inventory and IN to the player inventory
                if (!ToolModificationUtil.canUpgradeBeExtracted(toolInMainHand, event.getCurrentItem())) {
                    event.setCancelled(true);
                    return;
                }
                else {
                    int clickedSlot = event.getSlot();
                    ItemStack hint = ToolModificationUtil.hintForSlot(clickedSlot);

                    PersistentDataContainer container = event.getCurrentItem().getItemMeta().getPersistentDataContainer();
                    String upgradeType = container.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING, "N/A");
                    byte upgradeLevel = container.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeLevel"), PersistentDataType.BYTE, (byte)0);
                    applyChange(toolInMainHand, upgradeType, upgradeLevel - 1);
                    toolModifierInventory.initialize(toolInMainHand);
                    event.setCancelled(true);
                    playerInventory.getInventory().addItem(clickedItem);
                }
            }
            if (clickedHolder instanceof Player) {
                // move items OUT of the player inventory and IN to the tool inventory
                if (ToolModificationUtil.canUpgradeBeInserted(player.getInventory().getItemInMainHand(), event.getCurrentItem())) {

                    insertUpgrade(toolModifierInventory, playerInventory, event);
                }
                else if (ToolModificationUtil.canFuelBeInserted(player.getInventory().getItemInMainHand(), event.getCurrentItem())) {
                    SpigotTools.LOGGER.info("Fuel can be inserted");
                    insertFuel(toolModifierInventory, playerInventory, event);
                }
                else {
                    SpigotTools.LOGGER.info("That item can't be inserted into the modification table");
                    event.setCancelled(true);
                }
            }

            player.updateInventory();
        }

        private void insertUpgrade(ToolModifierInventoryV2.Holder modificationInventory, InventoryHolder playerInventory, InventoryClickEvent event) {
            ItemStack currentItem = event.getCurrentItem();
            ItemStack itemInMainHand = event.getWhoClicked().getInventory().getItemInMainHand();
            assert currentItem != null;
            int slotForUpgrade = ToolModificationUtil.slotForUpgradeItem(currentItem);
            modificationInventory.getInventory().setItem(slotForUpgrade, currentItem);
            ItemStack playerSlotStack = playerInventory.getInventory().getItem(event.getSlot());
            playerInventory.getInventory().getItem(event.getSlot()).setAmount(playerSlotStack.getAmount() - 1);

            PersistentDataContainer container = currentItem.getItemMeta().getPersistentDataContainer();
            String upgradeType = container.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING, "N/A");
            byte upgradeLevel = container.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeLevel"), PersistentDataType.BYTE, (byte)0);
            applyChange(itemInMainHand, upgradeType, upgradeLevel);
            modificationInventory.initialize(itemInMainHand);
            event.setCancelled(true);
        }
        private void insertFuel(ToolModifierInventoryV2.Holder modificationInventory, InventoryHolder playerInventory, InventoryClickEvent event) {
            ItemStack currentItem = event.getCurrentItem();
            ItemStack itemInMainHand = event.getWhoClicked().getInventory().getItemInMainHand();
            assert currentItem != null;
            event.setCancelled(true);
        }
        private void applyChange(ItemStack tool, String upgradeType, int upgradeLevel) {
            ItemMeta meta = tool.getItemMeta();
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

            switch (upgradeType) {
                case "speed":
                    dataContainer.set(ItemManager.speedUpgradeLvl, PersistentDataType.BYTE, (byte)upgradeLevel);
                    break;
                case "fortune":
                    dataContainer.set(ItemManager.fortuneUpgradeLvl, PersistentDataType.BYTE, (byte)upgradeLevel);
                    break;
                case "silk":
                    dataContainer.set(ItemManager.silkTouchUpgradeLvl, PersistentDataType.BYTE, (byte)upgradeLevel);
                    break;
            }
            tool.setItemMeta(meta);
            ItemManager.ToolItem.updateLore(tool);
            applyEnchants(tool);
        }

        private void applyEnchants(ItemStack tool) {
            tool.removeEnchantment(Enchantment.DIG_SPEED);
            tool.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
            tool.removeEnchantment(Enchantment.SILK_TOUCH);

            // i need to take advantage of the new system im using. using the data container i can
            //  retrieve the current levels of SPEED, FORTUNE, and SILK

            PersistentDataContainer dataContainer = tool.getItemMeta().getPersistentDataContainer();
            byte speedLevel = dataContainer.getOrDefault(ItemManager.speedUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            byte fortuneLevel = dataContainer.getOrDefault(ItemManager.fortuneUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            byte silkLevel = dataContainer.getOrDefault(ItemManager.silkTouchUpgradeLvl, PersistentDataType.BYTE, (byte)0);

            switch (speedLevel) {
                case 0: break;
                case 1: tool.addEnchantment(Enchantment.DIG_SPEED, 1); break;
                case 2: tool.addEnchantment(Enchantment.DIG_SPEED, 2); break;
                case 3: tool.addEnchantment(Enchantment.DIG_SPEED, 4); break;
            }
            switch (fortuneLevel) {
                case 0: break;
                case 1: tool.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1); break;
                case 2: tool.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3); break;
            }
            switch (silkLevel) {
                case 0: break;
                case 1: tool.addEnchantment(Enchantment.SILK_TOUCH, 1); break;
            }
        }
    }
}