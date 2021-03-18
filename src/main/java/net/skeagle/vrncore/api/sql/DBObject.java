package net.skeagle.vrncore.api.sql;

import lombok.Getter;

import java.sql.Connection;

public abstract class DBObject<T extends StoreableObject<T>> {

    @Getter
    private final String name;
    @Getter
    private final Class<T> objectClass;
    @Getter
    private final Connection conn = SQLConnection.getConnection();

    public DBObject(String name, Class<T> object) {
        this.name = name;
        this.objectClass = object;
    }

    public void onFinishLoad() {
    }


}
