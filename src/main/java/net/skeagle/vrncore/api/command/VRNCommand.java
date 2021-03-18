package net.skeagle.vrncore.api.command;

import net.skeagle.vrncore.api.VRNPlugin;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.*;

import static org.bukkit.Bukkit.getServer;

@SuppressWarnings("unchecked")
public abstract class VRNCommand extends Command {

    private final VRNPlugin plugin;
    private static Map<String, Command> knownCommands;
    private static SimpleCommandMap commandMap;
    private final Map<UUID, Long> cooldownMap;
    private String name;
    private int requiredArgs;
    private long cooldown;
    protected CommandSender sender;
    protected String[] args;

    static {
        try {
            final Field f = getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (SimpleCommandMap) f.get(Bukkit.getPluginManager());
            Class<?> clazz = commandMap.getClass();
            while (!clazz.getSimpleName().equals("SimpleCommandMap"))
                clazz = clazz.getSuperclass();
            Field f2 = clazz.getDeclaredField("knownCommands");
            f2.setAccessible(true);
            knownCommands = (Map<String, Command>) f2.get(commandMap);
        }
        catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        return false;
    }

    public VRNCommand(final String label, final VRNPlugin plugin) {
        super(label);
        this.plugin = plugin;
        this.cooldownMap = new HashMap<>();
    }

    public void unregister() {
        try {
            final Field f = getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            final CommandMap commandMap = (CommandMap) f.get(getServer());
            final Field f2 = Arrays.stream(ArrayUtils.addAll(commandMap.getClass().getDeclaredFields(), commandMap.getClass().getSuperclass().getDeclaredFields())).filter(f3 -> f3.getName().equals("knownCommands")).findAny().get();
            f2.setAccessible(true);
            final Map<?, ?> map = (Map<?, ?>) f2.get(commandMap);
            map.remove(this.getName());
            f2.set(commandMap, map);
        }
        catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }


}
