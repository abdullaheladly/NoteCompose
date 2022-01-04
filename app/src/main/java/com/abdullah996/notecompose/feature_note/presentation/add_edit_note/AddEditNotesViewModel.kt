package com.abdullah996.notecompose.feature_note.presentation.add_edit_note


import androidx.lifecycle.ViewModel
import com.abdullah996.notecompose.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.abdullah996.notecompose.feature_note.domain.model.InvalidNotesException
import com.abdullah996.notecompose.feature_note.domain.model.Note
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AddEditNotesViewModel @Inject constructor(
    private val notesUseCase: NoteUseCases,
    savedStateHandle: SavedStateHandle
) :ViewModel(){

    private var currentNoteId:Int?=null
    /**
     * state will be repeated  for example when we rotate the device but
     * shared flow for events that only happens one time
     */
    private val _noteTitle= mutableStateOf(NoteTextFieldState(
        hint = "Enter title"
    ))
    val noteTitle:State <NoteTextFieldState> = _noteTitle

    private val _noteContent= mutableStateOf(NoteTextFieldState(
        hint = "Enter some content"
    ))
    val noteContent:State <NoteTextFieldState> = _noteContent


    private val _noteColor= mutableStateOf<Int>(Note.noteColors.random().toArgb())
    val noteColor:State<Int> = _noteColor


    private val _eventFlow=MutableSharedFlow<UiEvent>()
    val eventFlow=_eventFlow.asSharedFlow()


    init {
        savedStateHandle.get<Int>("noteId")?.let { noteid->
            if (noteid!=-1){
                viewModelScope.launch {
                    notesUseCase.getNoteUseCase(noteid)?.also {
                        currentNoteId=it.id
                        _noteTitle.value=noteTitle.value.copy(
                            text = it.title,
                            isHintVisible = false
                        )
                        _noteContent.value=noteContent.value.copy(
                            text = it.content,
                            isHintVisible = false
                        )
                        _noteColor.value=it.color
                    }
                }
            }
        }
    }


    fun onEvent(event: AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.EnteredTitle->{
                _noteTitle.value=noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus->{
                _noteTitle.value=noteTitle.value.copy(
                    isHintVisible = !event.focusState.hasFocus &&
                            noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent->{
                _noteContent.value=noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus->{
                _noteContent.value=noteContent.value.copy(
                    isHintVisible = !event.focusState.hasFocus &&
                            noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor->{
                _noteColor.value=event.color
            }
            is AddEditNoteEvent.SaveNote->{
                viewModelScope.launch {
                    try {
                        notesUseCase.addNoteUseCase(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timeStamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    }catch (e:InvalidNotesException){
                        _eventFlow.emit(
                            UiEvent.ShowSnaKBar(
                                message = e.message?:"unknown error"
                            )
                        )
                    }
                }
            }
        }

    }

    sealed class UiEvent{
        data class ShowSnaKBar(val message:String):UiEvent()
        object SaveNote:UiEvent()
    }
}