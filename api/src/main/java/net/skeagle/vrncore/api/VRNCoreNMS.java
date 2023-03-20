package net.skeagle.vrncore.api;

import com.mojang.authlib.properties.Property;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface VRNCoreNMS {

    void showDemoMenu(Player player);

    void showHallucination(Player player);

    Property getTexturesProperty(Player player);

    void replaceProperty(Player player, String name, Property oldProperty, Property newProperty);

    void reloadSkin(Player player);

    Npc createNpc(String name, Location location);
}
