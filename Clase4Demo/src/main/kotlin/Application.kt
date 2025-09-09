package com.example

import io.ktor.server.application.*
import com.example.controllers.*
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repo = SQLiteTaskRepository("jdbc:sqlite:tasks.db")
    val service = TaskService(repo)
    TaskController(service).registerRoutes(routing())
    //configureRouting()

}
