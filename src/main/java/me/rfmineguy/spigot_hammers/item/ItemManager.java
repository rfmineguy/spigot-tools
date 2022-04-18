package me.rfmineguy.spigot_hammers.item;

import me.rfmineguy.spigot_hammers.SpigotTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private enum ToolType {
        EXCAVATOR, HAMMER
    }

    static int totalRecipes = 0;

    public static ItemStack stoneHammer;
    public static ItemStack ironHammer;
    public static ItemStack diamondHammer;
    public static ItemStack netheriteHammer;

    public static ItemStack stoneExcavator;
    public static ItemStack ironExcavator;
    public static ItemStack diamondExcavator;
    public static ItemStack netheriteExcavator;

    public static ItemStack ironConversionKit;
    public static ItemStack diamondConversionKit;
    public static ItemStack netheriteConversionKit;

    public static void init() {
        //woodHammer          = createTool(ToolType.HAMMER, Material.WOODEN_PICKAXE, "Wood Hammer", 100000);
        stoneHammer         = createTool(ToolType.HAMMER, Material.STONE_PICKAXE    , "Stone Hammer"    , 100001);
        ironHammer          = createTool(ToolType.HAMMER, Material.IRON_PICKAXE     , "Iron Hammer"     , 100002);
        //goldHammer          = createTool(ToolType.HAMMER, Material.GOLDEN_PICKAXE , "Gold Hammer"           , 100003);
        diamondHammer       = createTool(ToolType.HAMMER, Material.DIAMOND_PICKAXE  , "Diamond Hammer"  , 100004);
        netheriteHammer     = createTool(ToolType.HAMMER, Material.NETHERITE_PICKAXE, "Netherite Hammer", 100005);

        //woodExcavator       = createTool(ToolType.EXCAVATOR, Material.WOODEN_SHOVEL, "Wood Excavator", 100006);
        stoneExcavator      = createTool(ToolType.EXCAVATOR, Material.STONE_SHOVEL, "Stone Excavator", 100007);
        ironExcavator       = createTool(ToolType.EXCAVATOR, Material.IRON_SHOVEL, "Iron Excavator", 100008);
        //goldExcavator       = createTool(ToolType.EXCAVATOR, Material.GOLDEN_SHOVEL, "Gold Excavator", 100009);
        diamondExcavator    = createTool(ToolType.EXCAVATOR, Material.DIAMOND_SHOVEL, "Diamond Excavator", 1000010);
        netheriteExcavator  = createTool(ToolType.EXCAVATOR, Material.NETHERITE_SHOVEL, "Netherite Excavator", 100012);

        ironConversionKit       = createConversionKit(Material.IRON_INGOT, "Redstone Infused Iron Ingot", 100020);
        diamondConversionKit    = createConversionKit(Material.DIAMOND, "Redstone Infused Diamond", 100021);
        netheriteConversionKit  = createConversionKit(Material.NETHERITE_INGOT, "Redstone Infused Netherite Ingot", 100022);
    }

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
    private static ItemStack createTool(ToolType type, Material material, String name, int itemMeta) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            SpigotTools.LOGGER.info("Something went wrong with creating the [" + name + "] tool.");
            return new ItemStack(Material.AIR);
        }
        meta.setCustomModelData(itemMeta);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        meta.setDisplayName(name);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);

        List<String> lore = new ArrayList<>();
        lore.add("Mines in a 3x3 area");
        meta.setLore(lore);

        switch (type) {
            case HAMMER: data.set(new NamespacedKey(SpigotTools.getPlugin(), "hammer"), PersistentDataType.STRING, "");
            break;
            case EXCAVATOR: data.set(new NamespacedKey(SpigotTools.getPlugin(), "excavator"), PersistentDataType.STRING, "");
            break;
        }
        itemStack.setItemMeta(meta);

        return itemStack;
    }
    public static ItemStack createConversionKit(Material material, String name, int itemMeta) {
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
    public static boolean isHammer(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(new NamespacedKey(SpigotTools.getPlugin(), "hammer"), PersistentDataType.STRING);
    }
    public static boolean isExcavator(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(new NamespacedKey(SpigotTools.getPlugin(), "excavator"), PersistentDataType.STRING);
    }

    public static boolean isConversionKit(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(new NamespacedKey(SpigotTools.getPlugin(), "conversionkit"), PersistentDataType.STRING);
    }
    public static boolean isPickaxe(ItemStack itemStack) {
        return itemStack.getType() == Material.IRON_PICKAXE ||
                itemStack.getType() == Material.DIAMOND_PICKAXE ||
                itemStack.getType() == Material.NETHERITE_PICKAXE;
    }
    public static boolean isShovel(ItemStack itemStack) {
        return itemStack.getType() == Material.IRON_SHOVEL ||
                itemStack.getType() == Material.DIAMOND_SHOVEL ||
                itemStack.getType() == Material.NETHERITE_SHOVEL;
    }
}
