package com.abdullah996.notecompose.feature_note.domain.use_case

import com.abdullah996.notecompose.feature_note.data.repository.FakeNoteRepository
import com.abdullah996.notecompose.feature_note.domain.model.Note
import com.abdullah996.notecompose.feature_note.domain.util.NoteOrder
import com.abdullah996.notecompose.feature_note.domain.util.OrderType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test

class GetNotesUseCaseTest{
    private lateinit var getNotes:GetNotesUseCase
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setup(){
        fakeNoteRepository=FakeNoteRepository()
        getNotes= GetNotesUseCase(fakeNoteRepository)

        val notesTOInsert= mutableListOf<Note>()
        ('a'..'z').forEachIndexed { index, c ->
            notesTOInsert.add(
                Note(
                    title = c.toString(),
                    content = c.toString(),
                    timeStamp = index.toLong(),
                    color = index
                )
            )
        }
        notesTOInsert.shuffle()
        runBlocking {
            notesTOInsert.forEach { fakeNoteRepository.insertNote(it) }
        }
    }

    @Test
    fun `Order Notes By Title Asc Correct Order`()= runBlocking {
        val notes=getNotes(NoteOrder.Title(OrderType.Ascending)).first()
        for ( i in 0..notes.size-2){
            assertThat(notes[i].title).isLessThan(notes[i+1].title)
        }
    }
    @Test
    fun `Order Notes By Title Dsc Correct Order`()= runBlocking {
        val notes=getNotes(NoteOrder.Date(OrderType.Descending)).first()
        for ( i in 0..notes.size-2){
            assertThat(notes[i].title).isGreaterThan(notes[i+1].title)
        }
    }
    @Test
    fun `Order Notes By Date Asc Correct Order`()= runBlocking {
        val notes=getNotes(NoteOrder.Date(OrderType.Ascending)).first()
        for ( i in 0..notes.size-2){
            assertThat(notes[i].timeStamp).isLessThan(notes[i+1].timeStamp)
        }
    }
    @Test
    fun `Order Notes By Date Dsc Correct Order`()= runBlocking {
        val notes=getNotes(NoteOrder.Color(OrderType.Descending)).first()
        for ( i in 0..notes.size-2){
            assertThat(notes[i].timeStamp).isGreaterThan(notes[i+1].timeStamp)
        }
    }
    @Test
    fun `Order Notes By Color Asc Correct Order`()= runBlocking {
        val notes=getNotes(NoteOrder.Color(OrderType.Ascending)).first()
        for ( i in 0..notes.size-2){
            assertThat(notes[i].color).isLessThan(notes[i+1].color)
        }
    }
    @Test
    fun `Order Notes By Color Dsc Correct Order`()= runBlocking {
        val notes=getNotes(NoteOrder.Title(OrderType.Descending)).first()
        for ( i in 0..notes.size-2){
            assertThat(notes[i].color).isGreaterThan(notes[i+1].color)
        }
    }


}