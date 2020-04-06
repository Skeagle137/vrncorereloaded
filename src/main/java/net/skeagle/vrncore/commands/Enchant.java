package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.enchants.AllEnchants;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.SimpleEnchant;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Enchant extends SimpleCommand {

    public Enchant() {
        super("enchant|ench");
        setDescription("Enchants the item held in your main hand.");
        setPermission("vrn.enchant");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        if (args.length < 1) {
            say(getPlayer(), "&cYou must provide an enchant name.");
            return;
        }
        final ItemStack i = getPlayer().getInventory().getItemInMainHand();
        final Map<Enchantment, Integer> map = i.getEnchantments();
        if (args[0].equalsIgnoreCase("all")) {
            for (final Enchantment ench : Enchantment.values()) {
                i.addUnsafeEnchantment(ench, (args.length < 2 ? 1 : findNumber(1, "&cPlease specify a valid enchant level.")));
            }
        }
        final AllEnchants enchant = checkArgs();
        if (enchant == null) {
            say(getPlayer(), "That is not an available enchant.");
            return;
        }
        if (enchant.isCustom()) {
            getPlayer().getInventory().setItemInMainHand(
                    ItemCreator.of(CompMaterial.fromMaterial(i.getType()))
                            .enchant(new SimpleEnchant(enchant.getEnchant(), (args.length < 2 ? 1 : findNumber(1, "&cPlease specify a valid enchant level."))))
                            .build().makeSurvival()
            );
            for (final Enchantment e : map.keySet()) {
                i.addUnsafeEnchantment(e, map.get(e));
            }
        } else {
            i.addUnsafeEnchantment(enchant.getEnchant(), (args.length < 2 ? 1 : findNumber(1, "&cPlease specify a valid enchant level.")));
        }
    }

    private AllEnchants checkArgs() {
        for (final AllEnchants enchants : AllEnchants.values()) {
            if (args[0].equalsIgnoreCase(enchants.getName())) {
                return enchants;
            }
        }
        return null;
    }

    @Override
    protected List<String> tabComplete() {
        switch (args.length) {
            case 1:
                final ArrayList<String> names = new ArrayList<>();
                for (final AllEnchants enchants : AllEnchants.values()) {
                    names.add(enchants.getName());
                }
                return completeLastWord(names);
            case 2:
                return completeLastWord(new ArrayList<>());
        }
        return new ArrayList<>();
    }
}