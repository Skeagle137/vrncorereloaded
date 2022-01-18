package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncommands.misc.FormatUtils;
import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailColors;
import net.skeagle.vrnlib.inventorygui.*;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class TrailsGUI {

    public TrailsGUI(final Player player, final Player target) {
        InventoryGUI gui = new InventoryGUI(45, "Trail Selection for " + target.getName());
        PlayerData data = PlayerManager.getData(target.getUniqueId());
        Particles playerParticle = Particles.getFromParticle(data.getPlayerTrailData().getParticle());
        Particles arrowParticle = Particles.getFromParticle(data.getArrowTrailData().getParticle());

        gui.addButton(ItemButton.create(new ItemBuilder(Material.BLAZE_POWDER).setName("&b&lSet Player Trail")
                .setLore("", "&7Click to open the player", "&7trail selection menu.",
                        "", "&7Currently selected trail: &a" + (playerParticle != null ? playerParticle.getParticleName() : "&cNone")),
                e -> new TrailSelection(player, target, data.getPlayerTrailData())), 19);

        gui.addButton(ItemButton.create(new ItemBuilder(Material.BARRIER).setName("&cReset trails")
                .setLore("", "&7Left click to reset player trail.", "&7Right click to reset arrow trail."), e -> {
            if (e.isLeftClick()) {
                data.getPlayerTrailData().setParticle(null);
            } else if (e.isRightClick()) {
                data.getArrowTrailData().setParticle(null);
            }
        }), 13);

        gui.addButton(itemOfPermission(player, "vrn.trails.options", new ItemBuilder(Material.REDSTONE).setName("&6&lTrail Options")
                .setLore("", "&7Left click to edit player trail.", "&7Right click to edit arrow trail."), (e, button) -> {
            TrailData trailData = e.isLeftClick() ? data.getPlayerTrailData() : data.getArrowTrailData();
            Particles particle = Particles.getFromParticle(trailData.getParticle());
            if (particle != null) {
                new TrailOptions(player, target, particle, trailData);
            }
        }, "", "&cYou cannot use", "&cthis feature."), 31);

        gui.addButton(ItemButton.create(new ItemBuilder(Material.SPECTRAL_ARROW).setName("&d&lSet Arrow Trail")
                .setLore("", "&7Click to open the arrow", "&7trail selection menu.",
                        "", "&7Currently selected trail: &a" + (arrowParticle != null ? arrowParticle.getParticleName() : "&cNone")),
                e -> new TrailSelection(player, target, data.getArrowTrailData())), 25);

        gui.open(player);
    }

    private static class TrailSelection {

        private TrailSelection(final Player player, final Player target, TrailData data) {
            BorderedGUI gui = new BorderedGUI("&9" + data.getType().getName() + "Trails");
            PaginationPanel panel = gui.paginate(
                    ItemButton.create(new ItemBuilder(Material.CRIMSON_DOOR).setName("&eBack to Main Menu"), e ->
                        new TrailsGUI(player, target)),
                    ItemButton.create(new ItemBuilder(Material.MAP).setName("&e&lInfo")
                        .setLore("", "&bLeft Click to apply an available trail.", "&bRight click to see the trail", "&boptions customization menu."), e -> {})
            );

            for (Particles particle : Particles.values()) {
                final boolean same = data.getParticle() != null && data.getParticle() == particle.getParticle();
                panel.addPagedButton(itemOfPermission(player, particle.getPermission(data.getType()), new ItemBuilder(particle.getMaterial()).setName("&6" + particle.getParticleName())
                        .setLore("", "&7Click to select", "&7this " + data.getType().name().toLowerCase() + " trail.").glint(same), (e, button) -> {
                    if (!Arrays.asList(particle.getProperties()).contains(Particles.ParticleProperties.DIRECTIONAL)) {
                        if (data.getStyle().get() == Style.SPREAD) {
                            data.setStyle(Style.DEFAULT);
                        }
                    }
                    player.closeInventory();
                    data.setParticle(particle.getParticle());
                    say(player, (target != player ? "&a" + target.getName() + "'s&7 " : "&7Your ") +
                            "trail has been changed to &a" + particle.getParticleName() + "&7.");
                }, "", "&cYou cannot use", "&cthis " + data.getType().name().toLowerCase() + " trail."));
            }
            gui.open(player);
        }
    }

    private static class TrailOptions {

        private TrailOptions(final Player player, final Player target, Particles particle, TrailData data) {
            InventoryGUI gui = new InventoryGUI(45, "&9" + data.getType().getName() + "Trail Options for " + target.getName());
            TrailColors color = TrailColors.getFromColor(data.getColor());
            TrailColors fade = TrailColors.getFromColor(data.getFade());
            if (color == null) {
                data.setColor(Color.RED);
                color = TrailColors.RED;
            }
            if (fade == null) {
                data.setFade(Color.WHITE);
                fade = TrailColors.WHITE;
            }
            List<Particles.ParticleProperties> props = Arrays.asList(particle.getProperties());

            String[] no = {"", "&cYou cannot use", "&cthis trail option."};

            gui.addButton(itemOfPermission(player, "vrn.trails.options.style", new ItemBuilder(Material.ENDER_PEARL).setName("&eEdit Trail Style"), (e, button) -> {
                new TrailOptionSelection<>(player, "&cTrail Styles", List.of(Style.values()),
                        t -> itemOfPermission(player, "vrn.trails.styles." + t.name().toLowerCase(), new ItemBuilder(switch (t) {
                            case DEFAULT -> Material.BLAZE_POWDER;
                            case SPREAD -> Material.FIREWORK_STAR;
                            case ORBIT -> Material.ENDER_EYE;
                            case CIRCLE -> Material.FIREWORK_ROCKET;
                        }).setName("&e&l" + FormatUtils.toTitleCase(t.name())), (ev, button2) -> {
                            player.closeInventory();
                            data.setStyle(t);
                            say(player, (target != player ? "&a" + target.getName() + "'s&7 " : "&7Your ") +
                                    "trail style has been changed to &a" + FormatUtils.toTitleCase(t.name()) + "&7.");
                        }, isStyleCompatable(t, props), "", "&cYou cannot use", "&cthis trail style."));
            }, no), 28);

            gui.addButton(itemOfPermission(player, "vrn.trails.options.size", new ItemBuilder(Material.MAGMA_CREAM).setName("&eEdit Trail Size")
                    .setLore(getSizeLore(data)), (e, button) -> {
                if (e.isLeftClick()) {
                    if (data.getSize() >= 2.0) return;
                    data.setSize(data.getSize() + 0.25);
                } else if (e.isRightClick()) {
                    if (data.getSize() <= 0.5) return;
                    data.setSize(data.getSize() - 0.25);
                }
                button.setItem(new ItemBuilder(button.getItem()).setLore(getSizeLore(data)));
                gui.update();
            }, props.contains(Particles.ParticleProperties.COLOR) || props.contains(Particles.ParticleProperties.COLOR_TRANSITION), no), 11);

            gui.addButton(itemOfPermission(player, "vrn.trails.options.fadecolor", new ItemBuilder(fade.getDye()).setName("&eEdit Trail Fade Color")
                    .setLore("", "&7Currently selected fade color: ", fade.getDisplayName()), (e, button) -> {
                new TrailOptionSelection<>(player, "&cTrail Fade Colors", Arrays.stream(TrailColors.values())
                        .filter(t -> particle.getParticle() != Particle.NOTE || t != TrailColors.BLACK && t != TrailColors.WHITE).collect(Collectors.toList()),
                        t -> itemOfPermission(player, "vrn.trails.fadecolors." + t.name().toLowerCase(), new ItemBuilder(t.getWool()).setName(t.getDisplayName()), (ev, button2) -> {
                            player.closeInventory();
                            data.setFade(t.getColor());
                            say(player, (target != player ? "&a" + target.getName() + "'s&7 " : "&7Your ") + "trail fade color has been changed to " + t.getDisplayName() + "&7.");
                        }, "", "&cYou cannot use", "&cthis trail fade color."));
            }, props.contains(Particles.ParticleProperties.COLOR_TRANSITION), no), 15);

            gui.addButton(itemOfPermission(player, "vrn.trails.options.color", new ItemBuilder(color.getDye()).setName("&eEdit Trail Color")
                    .setLore("", "&7Currently selected color: ", color.getDisplayName()), (e, button) -> {
                new TrailOptionSelection<>(player, "&cTrail Colors", Arrays.stream(TrailColors.values())
                        .filter(t -> particle.getParticle() != Particle.NOTE || t != TrailColors.BLACK && t != TrailColors.WHITE).collect(Collectors.toList()),
                        t -> itemOfPermission(player, "vrn.trails.colors." + t.name().toLowerCase(), new ItemBuilder(t.getWool()).setName(t.getDisplayName()), (ev, button2) -> {
                            player.closeInventory();
                            data.setColor(t.getColor());
                            say(player, (target != player ? "&a" + target.getName() + "'s&7 " : "&7Your ") + "trail color has been changed to " + t.getDisplayName() + "&7.");
                        }, "", "&cYou cannot use", "&cthis trail color."));
            }, props.contains(Particles.ParticleProperties.COLOR) || props.contains(Particles.ParticleProperties.COLOR_TRANSITION), no), 34);

            gui.open(player);
        }

        private boolean isStyleCompatable(Style style, List<Particles.ParticleProperties> props) {
            return style != Style.SPREAD || props.contains(Particles.ParticleProperties.DIRECTIONAL);
        }

        private String[] getSizeLore(TrailData data) {
            return new String[]{"", "&7Left click to increase trail size.", "&7Right click to decrease trail size.",
                    "", "&7Current trail size: &a", "&b" + data.getSize() + "x"};
        }
    }

    private static class TrailOptionSelection<T> {

        private TrailOptionSelection(Player player, String title, List<T> options, Function<T, ItemButton> optionFunc) {
            InventoryGUI gui = new InventoryGUI((int) Math.ceil(options.size() / 9D) * 9, title);
            PaginationPanel panel = new PaginationPanel(gui);
            panel.addSlots(0, options.size());
            for (T option : options) {
                panel.addPagedButton(optionFunc.apply(option));
            }
            gui.open(player);
        }
    }

    private static class ItemBlockSelection<T> {

        private ItemBlockSelection(Player player, String title, List<T> list, Function<T, ItemButton> func) {
            BorderedGUI gui = new BorderedGUI(title);
            PaginationPanel panel = gui.paginate();
            for (T itemBlock : list) {
                panel.addPagedButton(func.apply(itemBlock));
            }
            gui.open(player);
        }
    }

    private static ItemButton itemOfPermission(Player player, String permission, ItemStack stack, BiConsumer<InventoryClickEvent, ItemButton> consumer, boolean compatible, String... message) {
        return ItemButton.create(player.hasPermission(permission) ? (compatible ? stack : new ItemBuilder(stack).setLore("", "&cThis option cannot", "&capply to your", "&ccurrent trail.")) :
                new ItemBuilder(Material.GRAY_DYE).setName("&4???").setLore(message), (e, button) -> {
            if (player.hasPermission(permission) && compatible) {
                consumer.accept(e, button);
            }
        });
    }

    private static ItemButton itemOfPermission(Player player, String permission, ItemStack stack, BiConsumer<InventoryClickEvent, ItemButton> consumer, String... message) {
        return itemOfPermission(player, permission, stack, consumer, true, message);
    }
}
