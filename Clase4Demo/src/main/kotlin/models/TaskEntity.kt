package com.example.models

data class TaskEntity(
    val id: Int? = null,
    val title: String,
    val done: Boolean = false
)