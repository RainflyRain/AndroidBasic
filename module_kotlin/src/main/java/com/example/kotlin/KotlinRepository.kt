package com.example.kotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by zpf on 2023/1/28.
 */
class KotlinRepository {

    suspend fun loginRequest(): String {
        return withContext(Dispatchers.IO) {
            Thread.sleep(3000)
            "login success"
        }
    }

}