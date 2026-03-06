package com.example.mybookshelf

data class Book(
    val cover: Int,
    val name: String,
    val author: String,
    val description: String,
    val releaseYear: Int,
    val status: String,
    val id: Int = 0
)

