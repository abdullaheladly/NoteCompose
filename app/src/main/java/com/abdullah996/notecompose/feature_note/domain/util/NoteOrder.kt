package com.abdullah996.notecompose.feature_note.domain.util

sealed class NoteOrder(val orderType: OrderType){
    class Title(orderType: OrderType):NoteOrder(orderType)
    class Date(orderType: OrderType):NoteOrder(orderType)
    class Color(orderType: OrderType):NoteOrder(orderType)

    /**
     * create copy fun to hold the note order and only change order type*/

    fun copy(orderType: OrderType):NoteOrder{
        return when(this){
            is Title->Title(orderType)
            is Date->Date(orderType)
            is Color->Color(orderType)
        }
    }

}
