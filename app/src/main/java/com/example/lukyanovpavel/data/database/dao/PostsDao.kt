package com.example.lukyanovpavel.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lukyanovpavel.data.database.entity.PostEntity
import io.reactivex.Observable

@Dao
interface PostsDao {

    @Query("SELECT * FROM posts WHERE page = :page AND count = :count AND category = :category")
    fun getPost(
        category: String,
        page: Int,
        count: Int
    ): Observable<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: PostEntity)
}