package com.zozi.kittyfacts.repository

import com.zozi.kittyfacts.data.local.dao.KittyFactDao
import com.zozi.kittyfacts.data.local.entity.KittyFactEntity
import com.zozi.kittyfacts.data.remote.api.KittyFactApi
import com.zozi.kittyfacts.data.remote.dto.KittyFactDto
import com.zozi.kittyfacts.data.repository.KittyFactRepositoryImpl
import com.zozi.kittyfacts.domain.model.KittyFact
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KittyFactRepositoryImplTest {

    private val api: KittyFactApi = mockk()
    private val dao: KittyFactDao = mockk()

    private val repository = KittyFactRepositoryImpl(api, dao)

    @Test
    fun `getRandomFact returns mapped domain model when api succeeds`() = runTest {
        // given
        coEvery { api.getRandomFact() } returns KittyFactDto(fact = "Hello", length = 5)

        // when
        val result = repository.getRandomFact()

        // then
        assertTrue(result.isSuccess)
        assertEquals("Hello", result.getOrNull()?.text)
        coVerify(exactly = 1) { api.getRandomFact() }
    }

    @Test
    fun `getRandomFact returns failure when api throws`() = runTest {
        // given
        val ex = RuntimeException("boom")
        coEvery { api.getRandomFact() } throws ex

        // when
        val result = repository.getRandomFact()

        // then
        assertTrue(result.isFailure)
        assertEquals("boom", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { api.getRandomFact() }
    }

    @Test
    fun `saveFact maps to entity and inserts via dao`() = runTest {
        // given
        val fact = KittyFact(id = 10, text = "Saved")
        coEvery { dao.insertFact(any()) } returns Unit

        // when
        repository.saveFact(fact)

        // then
        coVerify(exactly = 1) {
            dao.insertFact(
                match { it.id == 10L && it.text == "Saved" }
            )
        }
    }

    @Test
    fun `getSavedFacts maps entities to domain models`() = runTest {
        // given
        val entities = listOf(
            KittyFactEntity(id = 2, text = "B"),
            KittyFactEntity(id = 1, text = "A")
        )
        every { dao.getAllFacts() } returns flowOf(entities)

        // when
        val emitted = repository.getSavedFacts().first()

        // then
        assertEquals(listOf("B", "A"), emitted.map { it.text })
        assertEquals(listOf(2L, 1L), emitted.map { it.id })
    }
}

