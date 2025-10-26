package com.example.dinesmart

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.*

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun splash_showsWelcomeAndExploreButton() {
        rule.onNodeWithText("Welcome to DineSmart").assertIsDisplayed()
        rule.onNodeWithText("Explore").assertIsDisplayed().assertIsEnabled()
    }
}
