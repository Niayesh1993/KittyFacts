package com.zozi.kittyfacts.data.mapper

import com.zozi.kittyfacts.data.remote.dto.KittyFactDto
import com.zozi.kittyfacts.domain.model.KittyFact

fun KittyFactDto.toDomain(): KittyFact {
    return KittyFact(
        text = fact
    )
}