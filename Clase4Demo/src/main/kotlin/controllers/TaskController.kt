package com.example.controllers

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.service.TaskService
import com.example.dto.TaskDTO

class TaskController(private val service: TaskService) {
    fun registerRoutes(routing: Routing) {
        routing.route("/tasks") {
            get {
                call.respond(service.getAllTasks())
            }
            post {
                val dto = call.receive<TaskDTO>()
                call.respond(service.createTask(dto))
            }
        }
    }
}