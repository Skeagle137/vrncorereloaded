package net.skeagle.vrncore.db;

import lombok.Getter;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.storage.api.DBObject;
import net.skeagle.vrncore.utils.storage.api.SkipPrimaryID;
import net.skeagle.vrncore.utils.storage.homes.HomeManager;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import net.skeagle.vrncore.utils.storage.warps.WarpManager;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBConnect {

    private final List<DBObject<?>> tablesList = new ArrayList<>();
    @Getter
    private static Connection conn;
    @Getter
    private final static DBConnect instance = new DBConnect();

    public void load() {
        try {
            Class.forName("org.sqlite.JDBC");
            File datafolder = SimplePlugin.getInstance().getDataFolder();
            if (!datafolder.exists()) datafolder.mkdirs();
            File f = new File(datafolder, "vrn_data.db");
            conn = DriverManager.getConnection("jdbc:sqlite:" + f);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing database: " + e.getMessage());
        }
        registerTable(new WarpManager());
        registerTable(new HomeManager());
        registerTable(new PlayerManager());
    }

    private <T extends DBObject<?>> void registerTable(T table) {
        if (tablesList.contains(table))
            throw new RuntimeException("Database already registered:" + table.getName());
        tablesList.add(table);
        try {
            initializeTable(table.getName(), table.getObjectClass());
            table.onFinishLoad();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeTable(String name, Class<?> clazz) {
        StringBuilder columns = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (Modifier.isPrivate(fields[i].getModifiers())) {
                if (!fields[i].getType().isPrimitive()) {
                    if (!clazz.isAnnotationPresent(SkipPrimaryID.class)) {
                        columns.append(i != 0 ? "" : "id INTEGER PRIMARY KEY AUTOINCREMENT,")
                                .append(fields[i].getName().toLowerCase())
                                .append(" ")
                                .append(Conversions.getTypeStringFromClass(fields[i].getType()))
                                .append(i != fields.length - 1 ? "," : "");
                    }
                    else {
                        columns.append(fields[i].getName().toLowerCase())
                                .append(" ")
                                .append(Conversions.getTypeStringFromClass(fields[i].getType()))
                                .append(i != 0 ? "" : " PRIMARY KEY")
                                .append(i != fields.length - 1 ? "," : "");
                    }
                }
                else
                    throw new RuntimeException("The field " + fields[i].getName() + " in " + clazz.getName() + " cannot be primitive.");
            }
        }
        //language=SQLite
        String sql = "CREATE TABLE IF NOT EXISTS " + name + " (" + columns.toString() + ");";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            System.out.println(name + " database loaded.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private enum Conversions {
        STRING("VARCHAR(255)", String.class),
        INTEGER("INTEGER", Integer.class),
        DOUBLE("INTEGER", Double.class),
        LONG("INTEGER", Long.class),
        FLOAT("INTEGER", Float.class),
        BOOLEAN("BOOL", Boolean.class);

        private String typeString;
        private Class<?> serializedDataType;

        Conversions(final String typeString, final Class<?> serializedDataType) {
            this.typeString = typeString;
            this.serializedDataType = serializedDataType;
        }

        static String getTypeStringFromClass(Class<?> clazz) {
            for (Conversions c : Conversions.values())
                if (c.serializedDataType == clazz)
                    return c.typeString;
            return STRING.typeString;
        }
    }
}