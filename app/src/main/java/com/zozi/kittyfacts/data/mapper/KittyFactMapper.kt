package com.zozi.kittyfacts.data.mapper

import com.zozi.kittyfacts.data.local.entity.KittyFactEntity
import com.zozi.kittyfacts.data.remote.dto.KittyFactDto
import com.zozi.kittyfacts.domain.model.KittyFact

fun KittyFactDto.toDomain(): KittyFact {
    return KittyFact(
        text = fact
    )
}

fun KittyFact.toEntity(): KittyFactEntity {
    return KittyFactEntity(
        id = id,
        text = text
    )
}

fun KittyFactEntity.toDomain(): KittyFact {
    return KittyFact(
        id = id,
        text = text
    )
}