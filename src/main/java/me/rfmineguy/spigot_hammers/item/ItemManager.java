package me.rfmineguy.spigot_hammers.item;

import com.jeff_media.morepersistentdatatypes.DataType;
import me.rfmineguy.spigot_hammers.SpigotTools;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private enum ToolType {
        EXCAVATOR, HAMMER
    }

    static int totalRecipes = 0;

    public static class ToolItem {
        public static final NamespacedKey hammerKey = new NamespacedKey(SpigotTools.getPlugin(), "hammer");
        public static final NamespacedKey excavatorKey = new NamespacedKey(SpigotTools.getPlugin(), "excavator");
        public static final NamespacedKey isBrokenKey = new NamespacedKey(SpigotTools.getPlugin(), "isBroken");
        public static final NamespacedKey efficiencyFuelKey = new NamespacedKey(SpigotTools.getPlugin(), "efficiencyFuel");
        public static final NamespacedKey maxEfficiencyFuelKey = new NamespacedKey(SpigotTools.getPlugin(), "maxEfficiencyFuel");
        public static final NamespacedKey fortuneFuelKey = new NamespacedKey(SpigotTools.getPlugin(), "fortuneFuel");
        public static final NamespacedKey maxFortuneFuelKey = new NamespacedKey(SpigotTools.getPlugin(), "maxFortuneFuel");
        public static final NamespacedKey silkTouchFuelKey = new NamespacedKey(SpigotTools.getPlugin(), "silkTouchFuel");
        public static final NamespacedKey maxSilkTouchFuelKey = new NamespacedKey(SpigotTools.getPlugin(), "maxEfficiencyFuel");

        public static ItemStack create(ToolType type, Material material, String name, int itemMeta) {
            ItemStack stack = new ItemStack(material, 1);
            ItemMeta meta = stack.getItemMeta();
            if (meta == null) {
                SpigotTools.LOGGER.info("Something went wrong with creating the [" + name + "] tool.");
                return new ItemStack(Material.AIR);
            }
            meta.setCustomModelData(itemMeta);
            PersistentDataContainer data = meta.getPersistentDataContainer();
            meta.setDisplayName(name);
            meta.addEnchant(Enchantment.DURABILITY, 1, false);

            initializeToolUpgradeData(data);

            switch (type) {
                case HAMMER: {
                    data.set(new NamespacedKey(SpigotTools.getPlugin(), "hammer"), PersistentDataType.STRING, "");
                }
                break;
                case EXCAVATOR: {
                    data.set(new NamespacedKey(SpigotTools.getPlugin(), "excavator"), PersistentDataType.STRING, "");
                }
                break;
            }
            data.set(isBrokenKey, PersistentDataType.BYTE, (byte)0);
            data.set(efficiencyFuelKey, PersistentDataType.SHORT, (short)0);
            data.set(maxEfficiencyFuelKey, PersistentDataType.SHORT, (short)255);
            data.set(fortuneFuelKey, PersistentDataType.SHORT, (short)0);
            data.set(maxFortuneFuelKey, PersistentDataType.SHORT, (short)255);
            data.set(silkTouchFuelKey, PersistentDataType.SHORT, (short)0);
            data.set(maxSilkTouchFuelKey, PersistentDataType.SHORT, (short)255);

            stack.setItemMeta(meta);
            updateLore(stack);

            return stack;
        }
        public static boolean isHammer(ItemStack itemStack){
            if (itemStack == null)
                return false;
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null)
                return false;
            PersistentDataContainer container = meta.getPersistentDataContainer();
            return container.has(hammerKey, PersistentDataType.STRING);
        }
        public static boolean isExcavator(ItemStack itemStack) {
            if (itemStack == null)
                return false;
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null)
                return false;
            PersistentDataContainer container = meta.getPersistentDataContainer();
            return container.has(excavatorKey, PersistentDataType.STRING);
        }
        public static boolean isTool(ItemStack itemStack) {
            return isHammer(itemStack) || isExcavator(itemStack);
        }
        public static boolean isBroken(ItemStack itemStack) {
            if (itemStack == null)
                return false;
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null)
                return false;
            PersistentDataContainer container = meta.getPersistentDataContainer();
            return container.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "isBroken"), PersistentDataType.BYTE, (byte)0) == 1;
        }
        public static void updateLore(ItemStack itemStack) {
            ItemMeta meta = itemStack.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            byte speedLvl = data.getOrDefault(speedUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            byte fortuneLvl = data.getOrDefault(fortuneUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            byte silkTouchLvl = data.getOrDefault(silkTouchUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            byte isBroken = data.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "isBroken"), PersistentDataType.BYTE, (byte)0);
            List<String> lore = new ArrayList<>();
            lore.add("Mines in a 3x3 area");
            lore.add("Shift+RightClick for modifiers");
            lore.add("");
            lore.add("Modifiers");
            lore.add(" - Efficiency Level   " + speedLvl);
            lore.add(" - Fortune Level      " + fortuneLvl);
            lore.add(" - Silk Touch Level   " + silkTouchLvl);
            if (isBroken == (byte)1) {
                lore.add(ChatColor.ITALIC + "" + ChatColor.RED + "BROKEN");
            }
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
        }
        private static void initializeToolUpgradeData(PersistentDataContainer dataContainer) {
            dataContainer.set(speedUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            dataContainer.set(fortuneUpgradeLvl, PersistentDataType.BYTE, (byte)0);
            dataContainer.set(silkTouchUpgradeLvl, PersistentDataType.BYTE, (byte)0);
        }
    }

    public static class ConversionKitItem {
        public static ItemStack create(Material material, String name, int itemMeta) {
            ItemStack itemStack = new ItemStack(material, 1);
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) {
                SpigotTools.LOGGER.info("Something went wrong with creating the [" + name + "] conversion kit.");
                return new ItemStack(Material.AIR);
            }
            meta.setCustomModelData(itemMeta);
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(new NamespacedKey(SpigotTools.getPlugin(), "conversionkit"), PersistentDataType.STRING, "");
            meta.setDisplayName(name);

            List<String> lore = new ArrayList<>();
            lore.add("Used to convert a pickaxe or shovel into is hammer equivalent.");
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            return itemStack;
        }
    }

    public static class UpgradeItem {
        public static ItemStack create(String name, String type, int level, int itemMeta) {
            // create itemstack with modified 'book' Material
            ItemStack itemStack = new ItemStack(Material.BOOK);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setCustomModelData(itemMeta);
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING, type);
            dataContainer.set(new NamespacedKey(SpigotTools.getPlugin(), "upgradeLevel"), PersistentDataType.BYTE, (byte)level);
            meta.setDisplayName(name);

            List<String> lore = new ArrayList<>();
            lore.add("Upgrade : " + name);
            lore.add("Level : " + level);
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            return itemStack;
        }

        public static boolean isUpgrade(ItemStack itemStack) {
            if (itemStack == null)
                return false;
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null)
                return false;
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            return dataContainer.has(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING) &&
                    dataContainer.has(new NamespacedKey(SpigotTools.getPlugin(), "upgradeLevel"), PersistentDataType.BYTE);
        }
        
        public static boolean isEqual(ItemStack itemStack1, ItemStack itemStack2) {
            if (!isUpgrade(itemStack1) || !isUpgrade(itemStack2))
                return false;
            PersistentDataContainer item1 = itemStack1.getItemMeta().getPersistentDataContainer();
            PersistentDataContainer item2 = itemStack2.getItemMeta().getPersistentDataContainer();
            String item1Type = item1.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING, "");
            String item2Type = item2.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeType"), PersistentDataType.STRING, "");
            byte item1Level = item1.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeLevel"), PersistentDataType.BYTE, (byte)-1);
            byte item2Level = item2.getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "upgradeLevel"), PersistentDataType.BYTE, (byte)-1);
            if (item1Type.equals("") || item2Type.equals("") || item1Level == (byte)-1 || item2Level == (byte)-1)
                return false;
            return item1Type.equals(item2Type) && item1Level == item2Level;
        }

        public static void makeUnique(ItemStack stack) {
            ItemMeta meta = stack.getItemMeta();
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            int random = SpigotTools.RANDOM.nextInt();
            dataContainer.set(new NamespacedKey(SpigotTools.getPlugin(), "uniqueId"), PersistentDataType.INTEGER, random);
            stack.setItemMeta(meta);
        }
    }

    public static class BackpackItem {
        public static ItemStack create(String name, int itemMeta) {
            ItemStack itemStack = new ItemStack(Material.SHULKER_SHELL);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setCustomModelData(itemMeta);
            meta.setDisplayName(name);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(new NamespacedKey(SpigotTools.getPlugin(), "minerBackpack"), PersistentDataType.BYTE, (byte)1);
            dataContainer.set(new NamespacedKey(SpigotTools.getPlugin(), "contents"), DataType.ITEM_STACK_ARRAY, new ItemStack[]{});
            List<String> lore = new ArrayList<>();
            lore.add("Miner's backpack lore");
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            itemStack.addUnsafeEnchantment(Enchantment.PIERCING, 1);
            return itemStack;
        }
    }

    //Hammers
    public static ItemStack stoneHammer, ironHammer, diamondHammer, netheriteHammer;

    //Excavators
    public static ItemStack stoneExcavator, ironExcavator, diamondExcavator, netheriteExcavator;

    //Upgrades
    public static final NamespacedKey speedUpgradeLvl = new NamespacedKey(SpigotTools.getPlugin(), "speedUpgrade"); //type byte
    public static final NamespacedKey fortuneUpgradeLvl = new NamespacedKey(SpigotTools.getPlugin(), "fortuneUpgrade"); //type byte
    public static final NamespacedKey silkTouchUpgradeLvl = new NamespacedKey(SpigotTools.getPlugin(), "silkTouchUpgrade"); //type byte

    //Conversion Kits
    public static ItemStack ironConversionKit, diamondConversionKit, netheriteConversionKit;

    //Modifier items
    public static ItemStack speedUpgradeItemLvl1, speedUpgradeItemLvl2, speedUpgradeItemLvl3;
    public static ItemStack fortuneUpgradeItemLvl1, fortuneUpgradeItemLvl2; //all upgrade items will be books
    public static ItemStack silkTouchUpgradeItem;
    public static ItemStack minersBackpack; //shulker shell

    public static void init() {
        stoneHammer             = ToolItem.create(ToolType.HAMMER, Material.STONE_PICKAXE    , "Stone Hammer"    , 100001);
        ironHammer              = ToolItem.create(ToolType.HAMMER, Material.IRON_PICKAXE     , "Iron Hammer"     , 100002);
        diamondHammer           = ToolItem.create(ToolType.HAMMER, Material.DIAMOND_PICKAXE  , "Diamond Hammer"  , 100004);
        netheriteHammer         = ToolItem.create(ToolType.HAMMER, Material.NETHERITE_PICKAXE, "Netherite Hammer", 100005);

        stoneExcavator          = ToolItem.create(ToolType.EXCAVATOR, Material.STONE_SHOVEL, "Stone Excavator", 100007);
        ironExcavator           = ToolItem.create(ToolType.EXCAVATOR, Material.IRON_SHOVEL, "Iron Excavator", 100008);
        diamondExcavator        = ToolItem.create(ToolType.EXCAVATOR, Material.DIAMOND_SHOVEL, "Diamond Excavator", 1000010);
        netheriteExcavator      = ToolItem.create(ToolType.EXCAVATOR, Material.NETHERITE_SHOVEL, "Netherite Excavator", 100012);

        ironConversionKit       = ConversionKitItem.create(Material.IRON_INGOT, "Redstone Infused Iron Ingot", 100020);
        diamondConversionKit    = ConversionKitItem.create(Material.DIAMOND, "Redstone Infused Diamond", 100021);
        netheriteConversionKit  = ConversionKitItem.create(Material.NETHERITE_INGOT, "Redstone Infused Netherite Ingot", 100022);

        speedUpgradeItemLvl1    = UpgradeItem.create("Speed Upgrade 1", "speed", 1, 100030);  // efficiency 1
        speedUpgradeItemLvl2    = UpgradeItem.create("Speed Upgrade 2", "speed", 2, 100031);  // efficiency 2
        speedUpgradeItemLvl3    = UpgradeItem.create("Speed Upgrade 3", "speed", 3, 100032);  // efficiency 4

        fortuneUpgradeItemLvl1  = UpgradeItem.create("Fortune Upgrade 1", "fortune", 1, 100035);  //fortune 1
        fortuneUpgradeItemLvl2  = UpgradeItem.create("Fortune Upgrade 2", "fortune", 2, 100036);  //fortune 3

        silkTouchUpgradeItem    = UpgradeItem.create("Silk Touch Upgrade", "silk", 1, 100039);  //silk touch

        minersBackpack          = BackpackItem.create("Miner's Backpack (WIP)", 100060);
    }

    //RECIPE MANAGEMENT FUNCTIONS
    public static void initRecipes() {

        //Crafting table recipes
        addStoneToolRecipe(Material.STONE_PICKAXE, stoneHammer, "stone_hammer");
        addStoneToolRecipe(Material.STONE_SHOVEL, stoneExcavator, "stone_excavator");

        addConversionKitRecipe(Material.IRON_INGOT, "rs_infused_iron_ingot", ironConversionKit);
        addConversionKitRecipe(Material.DIAMOND, "rs_infused_diamond", diamondConversionKit);
        addConversionKitRecipe(Material.NETHERITE_INGOT, "rs_infused_netherite_ingot", netheriteConversionKit);

        //Smithing table recipes
        addHammerRecipe(Material.IRON_PICKAXE, ironConversionKit, "iron_hammer", ironHammer);
        addHammerRecipe(Material.DIAMOND_PICKAXE, diamondConversionKit, "diamond_hammer", diamondHammer);
        addHammerRecipe(Material.NETHERITE_PICKAXE, netheriteConversionKit, "netherite_hammer", netheriteHammer);

        addExcavatorRecipe(Material.IRON_SHOVEL, ironConversionKit, "iron_excavator", ironExcavator);
        addExcavatorRecipe(Material.DIAMOND_SHOVEL, diamondConversionKit, "diamond_excavator", diamondExcavator);
        addExcavatorRecipe(Material.NETHERITE_SHOVEL, netheriteConversionKit, "netherite_excavator", netheriteExcavator);

        SpigotTools.LOGGER.info("Initialized recipes (" + totalRecipes + ")");
    }
    /*
        Recipes :
        [redstone, null, redstone],
        [null,  iron_ingot, null ],
        [redstone, null, redstone]
     */
    private static void addConversionKitRecipe(Material baseMaterial, String name, ItemStack result) {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(SpigotTools.getPlugin(), name), result);
        recipe.shape("r r", " b ", "r r");
        recipe.setIngredient('r', Material.REDSTONE);
        recipe.setIngredient('b', baseMaterial);
        Bukkit.getServer().addRecipe(recipe);
        totalRecipes++;
    }
    /*
     * Recipe
     * [WOOD, WOOD , WOOD],
     * [    , TOOL ,     ],
     * [    , STICK,     ],
     */
    private static void addHammerRecipe(Material baseTool, ItemStack conversionKit, String name, ItemStack result) {
        //Doesn't work because there is already smithing recipes that involve the tool and a material
        SmithingRecipe smithingRecipe = new SmithingRecipe(NamespacedKey.minecraft(name), result, new RecipeChoice.MaterialChoice(baseTool), new RecipeChoice.ExactChoice(conversionKit));
        Bukkit.getServer().addRecipe(smithingRecipe);
        totalRecipes ++;
    }
    /*
     * Recipe
     * [    , WOOD , WOOD],
     * [    , TOOL ,     ],
     * [    , STICK,     ]
     */
    private static void addExcavatorRecipe(Material baseTool, ItemStack conversionKit, String name, ItemStack result) {
        SmithingRecipe smithingRecipe = new SmithingRecipe(NamespacedKey.minecraft(name), result, new RecipeChoice.MaterialChoice(baseTool), new RecipeChoice.ExactChoice(conversionKit));
        Bukkit.getServer().addRecipe(smithingRecipe);
        totalRecipes ++;
    }
    private static void addStoneToolRecipe(Material baseTool, ItemStack result, String name) {
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft(name), result);
        recipe.shape("x", "b", "s"); //x = ingot, b = baseTool, s = stick
        recipe.setIngredient('x', Material.COBBLESTONE);
        recipe.setIngredient('b', baseTool);
        recipe.setIngredient('s', Material.STICK);
        Bukkit.getServer().addRecipe(recipe);
        totalRecipes ++;
    }

    //LOGIC UTILITY FUNCTIONS
    public static boolean isBackpack(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(new NamespacedKey(SpigotTools.getPlugin(), "minerBackpack"), PersistentDataType.BYTE);
    }

}