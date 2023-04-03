package net.skeagle.vrncore.configurable;

import net.skeagle.vrnlib.config.annotations.Comment;
import net.skeagle.vrnlib.config.annotations.ConfigName;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class GuiConfig {

    //exp trade
    @Comment("The materials that can be traded in the exp trade GUI. Values should be an integer representing the exp points gained from trading.")
    @ConfigName("exp-trade-materials")
    public static Map<Material, Integer> expTradeMap = new HashMap<>() {{
        put(Material.NETHERITE_INGOT, 120);
        put(Material.ANCIENT_DEBRIS, 80);
        put(Material.DIAMOND, 50);
        put(Material.GOLD_INGOT, 5);
        put(Material.QUARTZ, 4);
        put(Material.REDSTONE, 4);
        put(Material.EMERALD, 4);
        put(Material.LAPIS_LAZULI, 4);
        put(Material.COPPER_INGOT, 3);
        put(Material.IRON_INGOT, 3);
        put(Material.COAL, 3);
    }};
}
