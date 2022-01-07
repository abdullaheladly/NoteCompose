package com.abdullah996.notecompose.feature_note.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abdullah996.notecompose.core.util.TestTags
import com.abdullah996.notecompose.di.AppModule
import com.abdullah996.notecompose.feature_note.presentation.add_edit_note.component.AddEditNoteScreen
import com.abdullah996.notecompose.feature_note.presentation.notes.component.NotesScreen
import com.abdullah996.notecompose.feature_note.presentation.util.Screen
import com.abdullah996.notecompose.ui.theme.NoteComposeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Before
    fun setup() {
        hiltRule.inject()
        composeRule.setContent {
            NoteComposeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                ) {
                    composable(route = Screen.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                    composable(
                        route = Screen.AddEditNoteScreen.route
                                + "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "noteColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                        )
                    ) {
                        val color = it.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(navController = navController, noteColor = color)
                    }
                }
            }

        }
    }


    @Test
    fun saveNewNote_EditAfterWards(){
        //click on fab to save new note
        composeRule.onNodeWithContentDescription("Add note").performClick()

        //enter text in title and content text filed
        composeRule.
        onNodeWithTag(TestTags.TITLE_TEXT_FILED).
        performTextInput("test-Title")
        composeRule.
        onNodeWithTag(TestTags.CONTENT_TEXT_FILED).
        performTextInput("test-content")
        //save the note
        composeRule.onNodeWithContentDescription("Save note").performClick()

        //make sure there is a note in a list with our title and content
        composeRule.onNodeWithText("test-Title").assertIsDisplayed()

        // click on note to edit it
        composeRule.onNodeWithText("test-Title").performClick()

        //nake sure title and content text filed contain note title and content
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FILED).assertTextEquals("test-Title")

       // composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FILED).assertTextEquals("test-Content")
        composeRule
            .onNodeWithTag(TestTags.TITLE_TEXT_FILED)
            .performTextInput("2")
        // Update the note
        composeRule.onNodeWithContentDescription("Save note").performClick()

        // Make sure the update was applied to the list
        composeRule.onNodeWithText("test-Title2").assertIsDisplayed()


    }

    @Test
    fun saveNewNotes_orderByTitleDescending() {
        for(i in 1..3) {
            // Click on FAB to get to add note screen
            composeRule.onNodeWithContentDescription("Add note").performClick()

            // Enter texts in title and content text fields
            composeRule
                .onNodeWithTag(TestTags.TITLE_TEXT_FILED)
                .performTextInput(i.toString())
            composeRule
                .onNodeWithTag(TestTags.CONTENT_TEXT_FILED)
                .performTextInput(i.toString())
            // Save the new
            composeRule.onNodeWithContentDescription("Save note").performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("Sort")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Title")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Descending")
            .performClick()

        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[0]
            .assertTextContains("3")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[1]
            .assertTextContains("2")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[2]
            .assertTextContains("1")
    }
}