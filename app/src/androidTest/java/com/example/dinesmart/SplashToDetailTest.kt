package com.example.dinesmart

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.*

@RunWith(AndroidJUnit4::class)
class SplashToDetailTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun splash_clickFeaturedOpen_navigatesToDetails() {
        // Wait for featured list
        rule.onAllNodesWithText("Open")[0].assertIsDisplayed()
        rule.onAllNodesWithText("Open")[0].performClick()

        // After navigation, the Details heading or 'Restaurant not found' may appear; check for either restaurant name or 'Restaurant Details'
        rule.onNodeWithText("Restaurant Details").assertIsDisplayed()
    }
}
