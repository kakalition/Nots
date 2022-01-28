package com.daggery.nots.home.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.daggery.nots.data.Note
import com.daggery.nots.data.NoteDao
import com.daggery.nots.data.NotsDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    private lateinit var database: NotsDatabase
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var notes: LiveData<List<Note>>

    private val expected1 = Note(
        uuid = "0",
        priority = 0,
        noteOrder = 0,
        noteTitle = "TestTitle 2",
        noteBody = "Test Body 2",
        noteDate = "01-01-01"
    )

    private val expected2 = Note(
        uuid = "1",
        priority = 0,
        noteOrder = 1,
        noteTitle = "TestTitle 2",
        noteBody = "Test Body 2",
        noteDate = "02-02-02"
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            NotsDatabase::class.java
        ).allowMainThreadQueries().build()
        homeViewModel = HomeViewModel(database)
        notes = homeViewModel.notes
        notes.observeForever {  }
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        runBlocking {
            database.clearAllTables()
            database.close()
        }
    }

    @Test
    fun should_GetCorrectNote_When_CallGetNote() {
        runBlocking {
            homeViewModel.addNote(expected1)
            val actualLiveData = homeViewModel.getNote("0")
            actualLiveData.observeForever {  }
            assertEquals(expected1, actualLiveData.value)
            actualLiveData.removeObserver {  }
        }
    }

    @Test
    fun should_ReturnNotesLiveData_When_AccessNotes() {
        assertEquals("Size should be 0, no data added", 0, notes.value?.size)
        homeViewModel.addNote(expected1)
        assertEquals("Size should be 1", 1, notes.value?.size)
    }

    @Test
    fun should_SwapUuidOfCorrespondingNotes_When_RearrangeNotes() {
        homeViewModel.addNote(expected1)
        homeViewModel.addNote(expected2)
        // Test for normal order
        assertEquals(
            "notes[0] should be with a note uuid of 0",
            expected1,
            notes.value?.get(0))
        assertEquals(
            "notes[1] should be a note with uuid of 1",
            expected2,
            notes.value?.get(1)
        )
        // Call rearrangeNoteOrder()
        homeViewModel.rearrangeNoteOrder(
            notes.value?.get(0)!!,
            notes.value?.get(1)!!
        )
        // Test for swapped order
        assertEquals(
            "notes[0] should be a note with uuid of 1",
            expected2.copy(noteOrder = 0),
            notes.value?.get(0)
        )
        assertEquals(
            "notes[0] should be a note with uuid of 1",
            expected1.copy(noteOrder = 1),
            notes.value?.get(1)
        )
    }

    @Test
    fun should_UpdateNotePriority_When_CallPrioritizeAndUnprioritize() {
        homeViewModel.addNote(expected1)
        assertTrue(
            "Priority should be 0",
            notes.value?.get(0)?.priority == 0
        )
        homeViewModel.prioritize(notes.value?.get(0)!!)
        assertTrue(
            "Priority should be 1",
            notes.value?.get(0)?.priority == 1
        )
        homeViewModel.unprioritize(notes.value?.get(0)!!)
        assertTrue(
            "Priority should be 0",
            notes.value?.get(0)?.priority == 0
        )
    }

    @Test
    fun should_UpdateNote_When_CallUpdateNote() {
        homeViewModel.addNote(expected1)
        assertEquals(
            "Should equal to expected 1",
            expected1,
            notes.value?.get(0)
        )
        val updatedNote = Note(
            uuid = expected1.uuid,
            priority = 1,
            noteOrder = 0,
            noteTitle = "UpdatedTitle",
            noteBody = "UpdatedNote",
            noteDate = "10-10-10"
        )
        homeViewModel.updateNote(updatedNote)
        assertEquals(
            "Should equal to updatedNote",
            updatedNote,
            notes.value?.get(0)
        )
    }

    @Test
    fun should_DeleteNote_When_CallDelete() {
        homeViewModel.addNote(expected1)
        assertEquals(
            "notes[] size should be 1",
            1,
            notes.value?.size
        )
        homeViewModel.deleteNote(expected1)
        assertEquals(
            "notes[] size should be 0",
            0,
            notes.value?.size
        )
    }

    @Test
    fun should_DeleteAllNote_When_CallDeleteAll() {
        homeViewModel.addNote(expected1)
        homeViewModel.addNote(expected2)
        assertEquals(
            "notes[] size should be 2",
            2,
            notes.value?.size
        )
        homeViewModel.deleteAllNotes()
        assertEquals(
            "notes[] size should be 0",
            0,
            notes.value?.size
        )
    }
}