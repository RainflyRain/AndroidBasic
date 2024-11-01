package com.example.kotlin

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by zpf on 2023/1/28.
 */
class KotlinRepositoryTest {

    @Test
    fun login() = runBlocking {

        val expectResponse = "login success"

        val kotlinRepository = KotlinRepository()

        val loginResponse = kotlinRepository.loginRequest()

        assertEquals(loginResponse, expectResponse)
    }

}