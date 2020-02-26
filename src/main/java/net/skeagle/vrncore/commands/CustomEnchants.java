package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.enchants.enchantment.ExecuteEnchant;
import net.skeagle.vrncore.enchants.enchantment.ExplosiveEnchant;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.SimpleEnchant;
import org.mineacademy.fo.remain.CompMaterial;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class CustomEnchants extends SimpleCommand {

    public CustomEnchants() {
        super("vcustomenchants|vce");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        getPlayer().getInventory().addItem(
                ItemCreator.of(CompMaterial.BOW,
                        "test item 1")
                        .enchant(new SimpleEnchant(ExplosiveEnchant.getInstance(), Integer.parseInt(args[0])))
                        .build().makeSurvival(),
                ItemCreator.of(CompMaterial.DIAMOND_SWORD,
                        "test item 2")
                        .enchant(new SimpleEnchant(ExecuteEnchant.getInstance(), Integer.parseInt(args[1])))
                        .build().makeSurvival()
        );

        say(getPlayer(), "given items.");
    }
}