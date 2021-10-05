package com.example.lukyanovpavel.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lukyanovpavel.domain.posts.Post

@Entity(tableName = "posts")
class PostEntity(
    @PrimaryKey val id: Int,
    val page: Int,
    val count: Int,
    val category: String,
    val description: String,
    val gifURL: String
)

fun PostEntity.toDomain(): Post =
    Post(
        description,
        gifURL
    )