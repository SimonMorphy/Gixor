package com.cpy3f2.gixor_mobile.dao

import GitHubUser
import androidx.room.Dao
import androidx.room.Insert


@Dao
interface GitHubUserDao {
    @Insert
    suspend fun insert(gitHubUser: GitHubUser)
}