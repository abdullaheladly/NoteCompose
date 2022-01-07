package com.abdullah996.notecompose.feature_note.presentation.notes.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abdullah996.notecompose.core.util.TestTags
import com.abdullah996.notecompose.di.AppModule
import com.abdullah996.notecompose.feature_note.presentation.MainActivity
import com.abdullah996.notecompose.feature_note.presentation.util.Screen
import com.abdullah996.notecompose.ui.theme.NoteComposeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesScreenTest{

    @get:Rule(order = 0)
    val hiltRule=HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule= createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Before
    fun setup(){
        hiltRule.inject()
        composeRule.setContent {
            val navController= rememberNavController()
            NoteComposeTheme {
                NavHost(navController = navController,
                startDestination = Screen.NotesScreen.route
                ){
                    composable(route = Screen.NotesScreen.route){
                        NotesScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun clickToggleOrderSection_IsVisible(){
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertIsDisplayed()
    }
}