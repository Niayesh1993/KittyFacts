package com.zozi.kittyfacts.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kitty_facts")
data class KittyFactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String
)
