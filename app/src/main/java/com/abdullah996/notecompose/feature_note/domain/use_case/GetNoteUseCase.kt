package com.abdullah996.notecompose.feature_note.domain.use_case

import com.abdullah996.notecompose.feature_note.domain.model.Note
import com.abdullah996.notecompose.feature_note.domain.repository.NoteRepository

class GetNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id:Int): Note? {
        return repository.getNoteById(id)
    }

}