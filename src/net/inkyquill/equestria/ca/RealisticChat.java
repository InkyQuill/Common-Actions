package me.exphc.RealisticChat;

import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class RealisticChat extends JavaPlugin {

    public Logger log;
    private RealisticChatListener listener;
    public int walkieItemId;
    public int bullhornItemId;
    public int smartphoneItemId;
    public ChatColor spokenPlayerColor;
    public ChatColor heardPlayerColor;
    public ChatColor messageColor;
    public ChatColor dimMessageColor;
    public ChatColor generalChannelColor;
    public ChatColor generalMessagesColor;
    private static final Enchantment EFFICIENCY;

    static {
        EFFICIENCY = Enchantment.DIG_SPEED;
    }

    public RealisticChat() {
        log = Logger.getLogger("Minecraft");
    }

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        if(getConfig().getBoolean("earTrumpetEnable", true) && getConfig().getBoolean("earTrumpetEnableCrafting", true)) {
            loadRecipes();
        }
        listener = new RealisticChatListener(this);
        SmartphoneCall.plugin = this;
    }
    

    private void loadRecipes() {
        ItemStack itemstack = new ItemStack(Material.GOLD_HELMET, 1);
        ItemStack itemstack1 = new ItemStack(Material.GOLD_HELMET, 1);
        ItemStack itemstack2 = new ItemStack(Material.GOLD_HELMET, 1);
        itemstack.addUnsafeEnchantment(EFFICIENCY, 1);
        itemstack1.addUnsafeEnchantment(EFFICIENCY, 2);
        itemstack2.addUnsafeEnchantment(EFFICIENCY, 3);
        ShapedRecipe shapedrecipe = new ShapedRecipe(itemstack);
        ShapedRecipe shapedrecipe1 = new ShapedRecipe(itemstack1);
        ShapedRecipe shapedrecipe2 = new ShapedRecipe(itemstack2);
        shapedrecipe.shape("WWW", "WDW");
        shapedrecipe.setIngredient('W', Material.WOOD);
        shapedrecipe.setIngredient('D', Material.DIAMOND);
        Bukkit.addRecipe(shapedrecipe);
        shapedrecipe1.shape("LLL", "LDL");
        shapedrecipe1.setIngredient('L', Material.LEATHER);
        shapedrecipe1.setIngredient('D', Material.DIAMOND);
        Bukkit.addRecipe(shapedrecipe1);
        shapedrecipe2.shape("III", "IDI");
        shapedrecipe2.setIngredient('I', Material.IRON_INGOT);
        shapedrecipe2.setIngredient('D', Material.DIAMOND);
        Bukkit.addRecipe(shapedrecipe2);
    }
}