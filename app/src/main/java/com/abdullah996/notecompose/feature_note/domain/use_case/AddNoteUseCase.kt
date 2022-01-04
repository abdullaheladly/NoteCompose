package com.abdullah996.notecompose.feature_note.domain.use_case

import com.abdullah996.notecompose.feature_note.domain.model.InvalidNotesException
import com.abdullah996.notecompose.feature_note.domain.model.Note
import com.abdullah996.notecompose.feature_note.domain.repository.NoteRepository

class AddNoteUseCase(
    private val repository: NoteRepository
) {

    @Throws(InvalidNotesException::class)
    suspend operator fun invoke(note:Note){
        if (note.title.isBlank()){
            throw InvalidNotesException("The title of the cote can't be empty")
        }
        if (note.content.isBlank()){
            throw InvalidNotesException("the content of the note can't be null")
        }
        repository.insertNote(note)
    }
}