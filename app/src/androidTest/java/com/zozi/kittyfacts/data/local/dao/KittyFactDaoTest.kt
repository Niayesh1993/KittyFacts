package com.zozi.kittyfacts.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zozi.kittyfacts.data.local.database.AppDatabase
import com.zozi.kittyfacts.data.local.entity.KittyFactEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KittyFactDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: KittyFactDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // OK for tests; avoids needing background dispatchers.
            .allowMainThreadQueries()
            .build()
        dao = db.kittyFactDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_getAllFacts_returns_in_desc_id_order() = runTest {
        dao.insertFact(KittyFactEntity(text = "first"))
        dao.insertFact(KittyFactEntity(text = "second"))

        val all = dao.getAllFacts().first()

        assertEquals(2, all.size)
        assertEquals("second", all[0].text)
        assertEquals("first", all[1].text)
    }

    @Test
    fun searchFacts_matches_substring_case_insensitive() = runTest {
        dao.insertFact(KittyFactEntity(text = "Cats purr"))
        dao.insertFact(KittyFactEntity(text = "Cats sleep"))
        dao.insertFact(KittyFactEntity(text = "Dogs bark"))

        val result = dao.searchFacts("PUR").first()

        assertEquals(1, result.size)
        assertEquals("Cats purr", result.first().text)
    }

    @Test
    fun deleteFactById_removes_item() = runTest {
        dao.insertFact(KittyFactEntity(text = "to delete"))
        val inserted = dao.getAllFacts().first().first()

        dao.deleteFactById(inserted.id)

        val all = dao.getAllFacts().first()
        assertTrue(all.isEmpty())
    }
}

