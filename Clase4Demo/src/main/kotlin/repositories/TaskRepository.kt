package com.example.repositories

import com.example.TaskEntity

interface TaskRepository {
    fun findAll(): List<TaskEntity>
    fun findById(id: Int): TaskEntity?
    fun save(task: TaskEntity): TaskEntity
    fun delete(id: Int): Boolean
}