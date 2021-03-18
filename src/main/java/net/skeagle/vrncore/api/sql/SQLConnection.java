package net.skeagle.vrncore.api.sql;

import net.skeagle.vrncore.utils.storage.homes.HomeManager;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import net.skeagle.vrncore.utils.storage.warps.WarpManager;
import org.bukkit.util.Consumer;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrncore.api.util.VRNUtil.sneakyThrow;

public class SQLConnection implements Closeable {

    private static Connection conn;

    public SQLConnection(String table) {
        try {
            Class.forName("org.sqlite.JDBC");
            File datafolder = SimplePlugin.getInstance().getDataFolder();
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
        String sql = "CREATE TABLE IF NOT EXISTS " + name + " (" + columns.toString() + ");";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM " + name);
            ResultSet rs = ps2.executeQuery();
            if (fields.length > rs.getMetaData().getColumnCount() - (clazz.isAnnotationPresent(SkipPrimaryID.class) ? 0 : 1)) {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet rs2;
                for (Field f : fields) {
                    rs2 = meta.getColumns(null, null, name, f.getName().toLowerCase());
                    if (rs2.next())
                        continue;
                    PreparedStatement ps3 = conn.prepareStatement("ALTER TABLE " + name + " ADD COLUMN " + f.getName().toLowerCase());
                    ps3.executeUpdate();
                    Common.log(name + " database updated new column " + f.getName().toLowerCase());
                }
            }
            Common.log(name + " database loaded.");

        } catch (SQLException e) {
            Common.log("Could not load " + name + " database.");
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

        private final String typeString;
        private final Class<?> serializedDataType;

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

    public PreparedStatement prepareStatement(String s, Object... keys) {
        try {
            PreparedStatement statement = conn.prepareStatement(s);
            for (int i = 1; i < keys.length; i++)
                statement.setObject(i, keys[i - 1]);
            return statement;
        } catch (SQLException e) {
            sneakyThrow(e);
            return null;
        }
    }

    public void execute(String command, Object... fields) {
        try {
            PreparedStatement statement = prepareStatement(command, fields);
            statement.execute();
        } catch (SQLException e) {
            sneakyThrow(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T queryResult(String s, Object... keys) {
        try {
            PreparedStatement statement = prepareStatement(s, keys);
            ResultSet results = statement.executeQuery();
            if (!results.next())
                return null;
            T obj = (T) results.getObject(1);
            results.close();
            return obj;
        } catch (SQLException e) {
            sneakyThrow(e);
            return null;
        }
    }

    public String queryResultString(String s, Object... keys) {
        try {
            PreparedStatement statement = prepareStatement(s, keys);
            ResultSet results = statement.executeQuery();
            if (!results.next())
                return null;
            return results.getString(1);
        } catch (SQLException e) {
            sneakyThrow(e);
            return null;
        }
    }

    public Long queryResultLong(String s, Object... keys) {
        try {
            PreparedStatement statement = prepareStatement(s, keys);
            ResultSet results = statement.executeQuery();
            if (!results.next())
                return null;
            return results.getLong(1);
        } catch (SQLException e) {
            sneakyThrow(e);
            return null;
        }
    }

    public Long queryResultBoolean(String s, Object... keys) {
        try {
            PreparedStatement statement = prepareStatement(s, keys);
            ResultSet results = statement.executeQuery();
            if (!results.next())
                return null;
            return results.getLong(1);
        } catch (SQLException e) {
            sneakyThrow(e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryResultList(String s, Object... keys) {
        List<T> list = new ArrayList<>();
        try {
            PreparedStatement statement = prepareStatement(s, keys);
            ResultSet results = statement.executeQuery();
            while (results.next())
                list.add((T) results.getObject(1));
            results.close();
        } catch (SQLException e) {
            sneakyThrow(e);
        }
        return list;
    }

    public List<String> queryResultListString(String s, Object... keys) {
        List<String> list = new ArrayList<>();
        try {
            PreparedStatement statement = prepareStatement(s, keys);
            ResultSet results = statement.executeQuery();
            while (results.next())
                list.add(results.getString(1));
            results.close();
        } catch (SQLException e) {
            sneakyThrow(e);
        }
        return list;
    }

    public Results queryResults(String s, Object... keys) {
        try {
            ResultSet results = prepareStatement(s, keys).executeQuery();
            return new Results(results);
        } catch (SQLException e) {
            sneakyThrow(e);
            return null;
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            sneakyThrow(e);
        }
    }

    public static class Results implements Closeable {

        private final ResultSet results;
        private boolean empty;

        private Results(ResultSet results) {
            this.results = results;
            try {
                empty = !results.next();
            } catch (SQLException e) {
                sneakyThrow(e);
            }
        }

        public boolean isEmpty() {
            return empty;
        }

        public boolean next() {
            try {
                return results.next();
            } catch (SQLException e) {
                sneakyThrow(e);
                return false;
            }
        }

        public void forEach(Consumer<Results> action) {
            if (isEmpty())
                return;
            action.accept(this);
            while (next())
                action.accept(this);
            close();
        }

        @SuppressWarnings("unchecked")
        public <T> T get(int column) {
            try {
                return (T) results.getObject(column);
            } catch (SQLException e) {
                sneakyThrow(e);
                return null;
            }
        }

        public String getString(int column) {
            try {
                return results.getString(column);
            } catch (SQLException e) {
                sneakyThrow(e);
                return null;
            }
        }

        public Long getLong(int column) {
            try {
                return results.getLong(column);
            } catch (SQLException e) {
                sneakyThrow(e);
                return null;
            }
        }

        public Boolean getBoolean(int column) {
            try {
                return results.getBoolean(column);
            } catch (SQLException e) {
                sneakyThrow(e);
                return null;
            }
        }

        public int getColumnCount() {
            try {
                return results.getMetaData().getColumnCount();
            } catch (SQLException e) {
                sneakyThrow(e);
                return 0;
            }
        }

        @Override
        public void close() {
            try {
                results.close();
            } catch (SQLException e) {
                sneakyThrow(e);
            }
        }

    }
}