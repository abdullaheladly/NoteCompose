package com.abdullah996.notecompose.feature_note.domain.use_case


import com.abdullah996.notecompose.feature_note.data.repository.FakeNoteRepository
import com.abdullah996.notecompose.feature_note.domain.model.InvalidNotesException
import com.abdullah996.notecompose.feature_note.domain.model.Note
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test
import java.lang.IndexOutOfBoundsException

class AddNoteUseCaseTest{
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setup(){
        fakeNoteRepository= FakeNoteRepository()
        addNoteUseCase= AddNoteUseCase(fakeNoteRepository)

    }

    @Test
    fun `add note with blank title`()= runBlocking {
        val notes=fakeNoteRepository.getNotes().first()
       val note= Note("","ahmed",1,1)

        var thrown = false
        try {
            runBlocking { addNoteUseCase(note) }
        } catch (e: InvalidNotesException) {
            thrown = true
        }
        assertThat(thrown).isTrue()
    }

    @Test
    fun `add note with blank content`()= runBlocking {
        val notes=fakeNoteRepository.getNotes().first()
        val note= Note("ahmed","",1,1)

        var thrown = false
        try {
            runBlocking { addNoteUseCase(note) }
        } catch (e: InvalidNotesException) {
            thrown = true
        }
        assertThat(thrown).isTrue()
    }

}