package net.skeagle.vrncore.api;

import net.skeagle.vrncore.api.task.TaskManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public abstract class VRNPlugin extends JavaPlugin {

    private List<Runnable> disableHooks;
    private String version;

    public void addDisableHook(final Runnable runnable) {
        this.disableHooks.add(runnable);
    }

    @Override
    public void onEnable() {
        version = this.getDescription().getVersion();
        new TaskManager(this);
    }

    @Override
    public void onDisable() {
        //disableHooks.forEach();
    }

    public static VRNPlugin getInstance() {
        return VRNPlugin.getPlugin(VRNPlugin.class);
    }

    @SuppressWarnings("unchecked")
    private <T> List<Class<? extends T>> getExtendedClasses(VRNPlugin plugin, Class<T> theClass) {
        List<Class<? extends T>> commands = new ArrayList<>();
        try {
            ClassLoader loader = plugin.getClass().getClassLoader();
            JarFile file = new JarFile(new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()));

            while (file.entries().hasMoreElements()) {
                if (file.entries().nextElement().isDirectory() || file.entries().nextElement().getName().endsWith(".class"))
                    continue;
                String name = file.entries().nextElement().getName();
                name = name.substring(0, name.length() - 6).replace("/", ".");
                Class<?> clazz;
                try {
                    clazz = Class.forName(name, true, loader);
                } catch (ClassNotFoundException ex) {
                    continue;
                }
                if (!theClass.isAssignableFrom(clazz) || Modifier.isAbstract(theClass.getModifiers()) || theClass.isInterface())
                    continue;
                commands.add((Class<? extends T>) clazz);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return commands;
    }


}
