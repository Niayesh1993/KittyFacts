package com.zozi.kittyfacts.presentation.theme

import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.zozi.kittyfacts.R

@Stable
object KittyDimens {
    val space4: Dp @Composable get() = dimen(R.dimen.space_4)
    val space8: Dp @Composable get() = dimen(R.dimen.space_8)
    val space10: Dp @Composable get() = dimen(R.dimen.space_10)
    val space12: Dp @Composable get() = dimen(R.dimen.space_12)
    val space16: Dp @Composable get() = dimen(R.dimen.space_16)

    val icon18: Dp @Composable get() = dimen(R.dimen.icon_18)

    @Composable
    private fun dimen(@DimenRes id: Int): Dp = dimensionResource(id)
}

