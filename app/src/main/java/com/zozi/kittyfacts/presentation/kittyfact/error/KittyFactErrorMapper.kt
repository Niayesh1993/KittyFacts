package com.zozi.kittyfacts.presentation.kittyfact.error

import androidx.annotation.StringRes
import com.zozi.kittyfacts.R
import java.io.IOException
import java.net.SocketTimeoutException
import retrofit2.HttpException

object KittyFactErrorMapper {

    @StringRes
    fun toMessageResId(throwable: Throwable): Int {
        return when (throwable) {
            is SocketTimeoutException -> R.string.error_timeout
            is IOException -> R.string.error_no_internet
            is HttpException -> R.string.error_server
            else -> R.string.error_unknown
        }
    }
}

