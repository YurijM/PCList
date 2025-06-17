package com.mu.pclist.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS pc_temp (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "inventory_number TEXT NOT NULL," +
                "user_id INTEGER DEFAULT NULL," +
                "FOREIGN KEY(user_id) REFERENCES table_users(id))")
        db.execSQL("INSERT INTO pc_temp (id, inventory_number) " +
                "SELECT id, inventory_number " +
                "FROM table_pc")
        db.execSQL("DROP TABLE table_pc")
        db.execSQL("ALTER TABLE pc_temp RENAME TO table_pc")
        db.execSQL("CREATE UNIQUE INDEX index_table_pc_inventory_number ON table_pc(inventory_number)")
        db.execSQL("CREATE INDEX index_table_pc_user_id ON table_pc(user_id)")
        db.execSQL("UPDATE table_pc SET user_id = (SELECT id FROM table_users WHERE pc_id = table_pc.id)")
    }
}

val migration_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        val cursor = db.query(
            "SELECT * FROM table_users"
        )
        if (cursor.columnNames.contains("pc_id")) {
            db.execSQL("ALTER TABLE table_users DROP COLUMN pc_id")
        }
    }
}
