package com.abdullah996.notecompose.di

import android.app.Application
import androidx.room.Room
import com.abdullah996.notecompose.feature_note.data.data_source.NoteDatabase
import com.abdullah996.notecompose.feature_note.data.repository.NoteRepositoryImpl
import com.abdullah996.notecompose.feature_note.domain.repository.NoteRepository
import com.abdullah996.notecompose.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(application: Application):NoteDatabase{
     return   Room.inMemoryDatabaseBuilder(
            application,
            NoteDatabase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db:NoteDatabase):NoteRepository{
        return NoteRepositoryImpl(db.noteDao
        )
    }

    @Provides
    @Singleton
    fun provideNotesUseCases(repository: NoteRepository):NoteUseCases{
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(repository = repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository = repository),
            addNoteUseCase = AddNoteUseCase(repository = repository),
            getNoteUseCase = GetNoteUseCase(repository = repository)
        )
    }
}