package com.zozi.kittyfacts.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zozi.kittyfacts.data.local.entity.KittyFactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KittyFactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact(fact: KittyFactEntity)

    @Query("SELECT * FROM kitty_facts ORDER BY id DESC")
    fun getAllFacts(): Flow<List<KittyFactEntity>>

    @Query(
        """
        SELECT * FROM kitty_facts
        WHERE text LIKE '%' || :query || '%' COLLATE NOCASE
        ORDER BY id DESC
        """
    )
    fun searchFacts(query: String): Flow<List<KittyFactEntity>>

    @Query("DELETE FROM kitty_facts WHERE id = :id")
    suspend fun deleteFactById(id: Long)
}