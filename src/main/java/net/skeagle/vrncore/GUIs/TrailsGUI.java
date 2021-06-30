package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.trail.Trail;
import net.skeagle.vrncore.utils.VRNPlayer;
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
        final VRNPlayer vrnPlayer = new VRNPlayer(target);
        addButton(ItemButton.create(new ItemBuilder(Material.BLAZE_POWDER).setName("&b&lPlayer trail selection menu")
                .setLore("", "&7Click to open the player", "&7trail selection menu."), e -> new TrailSelection(target, TrailType.PLAYER).open(player)), 10);

        addButton(new ItemButton() {
            @Override
            public ItemStack getItem() {
                return new ItemBuilder(Material.REDSTONE).setName("&6&lReset trails")
                        .setLore("", "&7Player trail: " + (vrnPlayer.getPlayerTrail() != null ? "&a" + vrnPlayer.getPlayerTrail() : "&cnone"),
                                "&7Arrow trail: " + (vrnPlayer.getArrowTrail() != null ? "&a" + vrnPlayer.getArrowTrail() : "&cnone"),
                                "&bLeft click &7to reset player trail.",
                                "&aShift left click &7to edit player trail.",
                                "&dRight click &7to reset arrow trail.",
                                "&eShift right click &7to edit arrow trail.")
                        .glint(vrnPlayer.getArrowTrail() != null || vrnPlayer.getPlayerTrail() != null);
            }

            @Override
            public void onClick(final InventoryClickEvent e) {
                if (e.getClick().isShiftClick() && e.getClick().isLeftClick())
                    vrnPlayer.setPlayerTrail(null);
                if (e.getClick().isShiftClick() && e.getClick().isRightClick()) {
                    vrnPlayer.setArrowTrail(null);
                }
                if (!e.getClick().isShiftClick() && e.getClick().isLeftClick()) {

                }
                if (!e.getClick().isShiftClick() && e.getClick().isRightClick()) {

                }
                update();
            }
        }, 13);

        addButton(ItemButton.create(new ItemBuilder(Material.SPECTRAL_ARROW).setName("&d&lArrow trail selection menu")
                .setLore("", "&7Click to open the arrow", "&7trail selection menu."), e -> new TrailSelection(target, TrailType.ARROW).open(player)), 16);

        open(player);
    }

    private static class TrailSelection extends PageableGUI<Trail> {

        private final VRNPlayer target;
        private final TrailType type;

        private TrailSelection(final Player target, final TrailType type) {
            super(type.title);
            this.target = new VRNPlayer(target);
            this.type = type;
        }

        @Override
        protected List<Trail> getContents() {
            return Arrays.stream(Trail.values()).collect(Collectors.toList());
        }

        @Override
        protected ItemStack convertToItem(final Trail particle) {
            if (getViewer().hasPermission(type.permission + particle.toString().toLowerCase())) {
                final boolean b = (type == TrailType.ARROW ? target.getArrowTrail() : target.getPlayerTrail()) != null &&
                        (type == TrailType.ARROW ? target.getArrowTrail() : target.getPlayerTrail()) == particle.getParticle();
                return new ItemBuilder(particle.getMaterial()).setName("&6" + particle.getParticleName())
                        .setLore("", "&7Click to select", "&r&7this " + type.name + " trail.").glint(b);
            }
            return new ItemBuilder(Material.GRAY_DYE).setName("&4???")
                    .setLore("", "&cYou cannot use", "&cthis " + type.name + " trail.");
        }

        @Override
        protected void onClickItem(final Trail particle, final InventoryClickEvent inventoryClickEvent) {
            if (getViewer().hasPermission(type.permission + particle.toString().toLowerCase())) {
                if (type == TrailType.ARROW)
                    target.setArrowTrail(particle.getParticle());
                else
                    target.setPlayerTrail(particle.getParticle());
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

    private enum TrailType {
        PLAYER("vrn.playertrails.", "&dPlayer Trails", "player"),
        ARROW("vrn.arrowtrails.", "&9Arrow Trails", "arrow");

        private final String permission;
        private final String title;
        private final String name;

        TrailType(final String permission, final String title, final String name) {
            this.permission = permission;
            this.title = title;
            this.name = name;
        }
    }
}
