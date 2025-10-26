package com.example.dinesmart

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.*

@RunWith(AndroidJUnit4::class)
class DetailScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun list_clickCard_showsDetailWithAddressPhoneAndMap() {
        // Click Explore on splash
        rule.onNodeWithText("Explore").assertIsDisplayed().performClick()

        // Tap the seeded restaurant 'Sushi Place'
        rule.onNodeWithText("Sushi Place").assertIsDisplayed().performClick()

        // Now assert address and phone labels are present
        rule.onNodeWithText("Address").assertIsDisplayed()
        rule.onNodeWithText("Phone").assertIsDisplayed()

        // Assert map card is present (contentDescription)
        rule.onNodeWithContentDescription("Restaurant location map").assertIsDisplayed()
    }
}
