package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrnlib.inventorygui.InventoryGUI;
import net.skeagle.vrnlib.inventorygui.ItemButton;
import net.skeagle.vrnlib.inventorygui.PageableGUI;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TrailsGUI extends InventoryGUI {

    public TrailsGUI(final Player player, final Player target) {
        super(27, "Trail Selection for " + target.getName());
        final PlayerData data = PlayerManager.getData(target.getUniqueId());
        addButton(ItemButton.create(new ItemBuilder(Material.BLAZE_POWDER).setName("&b&lPlayer trail selection menu")
                .setLore("", "&7Click to open the player", "&7trail selection menu."), e -> new TrailSelection(target, TrailType.PLAYER).open(player)), 10);

        addButton(ItemButton.create(new ItemBuilder(Material.REDSTONE).setName("&6&lReset trails")
                .setLore("", "&7Player trail: " + (data.getPlayerTrail() != null ? "&a" + data.getPlayerTrail() : "&cnone"),
                        "&7Arrow trail: " + (data.getArrowTrail() != null ? "&a" + data.getArrowTrail() : "&cnone"),
                        "&bLeft click &7to reset player trail.",
                        "&aShift left click &7to edit player trail.",
                        "&dRight click &7to reset arrow trail.",
                        "&eShift right click &7to edit arrow trail.")
                .glint(data.getArrowTrail() != null || data.getPlayerTrail() != null), (e, button) -> {
            if (e.getClick().isShiftClick() && e.getClick().isLeftClick())
                data.setPlayerTrail(null);
            if (e.getClick().isShiftClick() && e.getClick().isRightClick()) {
                data.setArrowTrail(null);
            }
            if (!e.getClick().isShiftClick() && e.getClick().isLeftClick()) {

            }
            if (!e.getClick().isShiftClick() && e.getClick().isRightClick()) {

            }
            button.setItem(new ItemBuilder(button.getItem()).glint(data.getArrowTrail() != null || data.getPlayerTrail() != null));
            update();
        }), 13);

        /*addButton(new ItemButton(new ItemBuilder(Material.REDSTONE).setName("&6&lReset trails")
                .setLore("", "&7Player trail: " + (data.getPlayerTrail() != null ? "&a" + data.getPlayerTrail() : "&cnone"),
                        "&7Arrow trail: " + (data.getArrowTrail() != null ? "&a" + data.getArrowTrail() : "&cnone"),
                        "&bLeft click &7to reset player trail.",
                        "&aShift left click &7to edit player trail.",
                        "&dRight click &7to reset arrow trail.",
                        "&eShift right click &7to edit arrow trail.")
                .glint(data.getArrowTrail() != null || data.getPlayerTrail() != null)) {

            @Override
            public void onClick(final InventoryClickEvent e) {
                if (e.getClick().isShiftClick() && e.getClick().isLeftClick())
                    data.setPlayerTrail(null);
                if (e.getClick().isShiftClick() && e.getClick().isRightClick()) {
                    data.setArrowTrail(null);
                }
                if (!e.getClick().isShiftClick() && e.getClick().isLeftClick()) {

                }
                if (!e.getClick().isShiftClick() && e.getClick().isRightClick()) {

                }
                update();
            }
        }, 13);*/

        addButton(ItemButton.create(new ItemBuilder(Material.SPECTRAL_ARROW).setName("&d&lArrow trail selection menu")
                .setLore("", "&7Click to open the arrow", "&7trail selection menu."), e -> new TrailSelection(target, TrailType.ARROW).open(player)), 16);

        open(player);
    }

    private static class TrailSelection extends PageableGUI<Particles> {

        private final PlayerData data;
        private final TrailType type;

        private TrailSelection(final Player target, final TrailType type) {
            super(getTitle(type));
            this.data = PlayerManager.getData(target.getUniqueId());
            this.type = type;
        }

        @Override
        protected List<Particles> getContents() {
            return Arrays.stream(Particles.values()).collect(Collectors.toList());
        }

        @Override
        protected ItemStack convertToItem(final Particles particle) {
            if (getViewer().hasPermission(particle.getPermission(type))) {
                final boolean b = (type == TrailType.ARROW ? data.getArrowTrail() : data.getPlayerTrail()) != null &&
                        (type == TrailType.ARROW ? data.getArrowTrail() : data.getPlayerTrail()) == particle.getParticle();
                return new ItemBuilder(particle.getMaterial()).setName("&6" + particle.getParticleName())
                        .setLore("", "&7Click to select", "&7this " + type.getName() + " trail.").glint(b);
            }
            return new ItemBuilder(Material.GRAY_DYE).setName("&4???")
                    .setLore("", "&cYou cannot use", "&cthis " + type.getName() + " trail.");
        }

        @Override
        protected void onClickItem(final Particles particle, final InventoryClickEvent inventoryClickEvent) {
            if (getViewer().hasPermission(particle.getPermission(type))) {
                if (type == TrailType.ARROW)
                    data.setArrowTrail(particle.getParticle());
                else
                    data.setPlayerTrail(particle.getParticle());
            }
            //else animateTitle("&4You cannot use this trail.");
            updateGui();
        }

        @Override
        protected ItemButton getInfoItem() {
            return ItemButton.create(new ItemBuilder(Material.MAP).setName("&e&lInfo")
                    .setLore("", "&bLeft Click to apply an available trail.", "&bRight click to see the trail", "&boptions customization menu."), e -> {
            });
        }
    }

    private static String getTitle(TrailType type) {
        return (type == TrailType.ARROW ? "&9Arrow " : "&dPlayer ") + "Trails";
    }
}
