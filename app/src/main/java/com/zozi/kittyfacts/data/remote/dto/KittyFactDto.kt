package com.zozi.kittyfacts.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class KittyFactDto(
    val fact: String,
    val length: Int
)
