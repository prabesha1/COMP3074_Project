package com.example.dinesmart

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.*

@RunWith(AndroidJUnit4::class)
class SplashToListTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun splash_clickExplore_navigatesToList() {
        // Wait for splash to be visible
        rule.onNodeWithText("Explore").assertIsDisplayed()
        rule.onNodeWithText("Explore").performClick()

        // After navigation, the Restaurants title should be visible
        rule.onNodeWithText("Restaurants").assertIsDisplayed()
    }
}
