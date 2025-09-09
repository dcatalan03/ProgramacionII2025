package com.example.repositories

import com.example.models.TaskEntity
import java.sql.DriverManager

class SQLiteTaskRepository(dbUrl: String) : TaskRepository {
    private val conn = DriverManager.getConnection(dbUrl)

    init {
        conn.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS tasks (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, done INTEGER)"
        )
    }

    override fun findAll(): List<TaskEntity> {
        val rs = conn.createStatement().executeQuery("SELECT * FROM tasks")
        val list = mutableListOf<TaskEntity>()
        while (rs.next()) {
            list += TaskEntity(rs.getInt("id"), rs.getString("title"), rs.getInt("done") == 1)
        }
        return list
    }

}