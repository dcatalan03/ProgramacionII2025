package com.example

import com.example.dto.TaskDTO
import com.example.repository.TaskRepository

class TaskService(private val repo: TaskRepository) {
    fun getAllTasks(): List<TaskDTO> =
        repo.findAll().map { TaskDTO(it.id, it.title, it.done) }

    fun createTask(dto: TaskDTO): TaskDTO {
        val saved = repo.save(dto.toEntity())
        return TaskDTO(saved.id, saved.title, saved.done)
    }
}