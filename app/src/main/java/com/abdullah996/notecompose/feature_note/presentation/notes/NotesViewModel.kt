package com.abdullah996.notecompose.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullah996.notecompose.feature_note.domain.model.Note
import com.abdullah996.notecompose.feature_note.domain.use_case.NoteUseCases
import com.abdullah996.notecompose.feature_note.domain.util.NoteOrder
import com.abdullah996.notecompose.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
):ViewModel() {

    private val _state= mutableStateOf<NotesStates>(NotesStates())
    val state:State<NotesStates> = _state


    private var recentlyDeletedNote:Note?=null


    private var getNotesJob:Job?=null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.Order->{
                /***
                 * compere the note order and the asc or desc
                 */
                if (state.value.noteOrder::class.java==event.noteOrder::class&&
                        state.value.noteOrder.orderType==event.noteOrder.orderType){
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote->{
                viewModelScope.launch {
                    noteUseCases.deleteNoteUseCase(event.note)
                    recentlyDeletedNote=event.note
                }
            }
            is NotesEvent.RestoreNote->{
                viewModelScope.launch {
                    noteUseCases.addNoteUseCase(recentlyDeletedNote?:return@launch)
                    recentlyDeletedNote=null
                }

            }
            is NotesEvent.ToggleOrderSection->{
                _state.value=state.value.copy(
                    inOrderSectionVisible = !state.value.inOrderSectionVisible
                )
            }
        }
    }
    private fun getNotes(noteOrder: NoteOrder){
        getNotesJob?.cancel()

        getNotesJob=noteUseCases.getNotesUseCase(noteOrder)
            .onEach {notes->
                _state.value=state.value.copy(
                    notes,
                    noteOrder
                )

            }.launchIn(viewModelScope)
    }
}